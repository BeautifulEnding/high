package com.example.asus.client;

import android.content.Context;
import android.content.Intent;

import com.example.asus.activity.ChatActivity;
import com.example.asus.activity.MyApplication;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.MessageType;
import com.example.asus.constant.Constant;
import com.example.asus.util.CacUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.NotificationUtil;
import com.example.asus.util.SDCardUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
//该类负责接收服务器发送过来的消息
public class ClientThread extends Thread{
    private Socket socket;
    //    private BufferedReader reader=null;
    private Context context;
    private BufferedReader reader;
    private Message message;
    private MessageList messageList;
    private ClientThread clientThread;
    public ClientThread(Socket socket,Context context){
        this.socket=socket;
        this.context=context;
        try{
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        clientThread=this;
    }
    @Override
    public void run(){
        String result;
        try{
            while ((result=reader.readLine())!=null){
                message = new Gson().fromJson(result,Message.class);
                LogUtil.e("从服务器接收到消息");
                    switch (message.getType()){
                        case MessageType.ADD_FRIEND:
                            LogUtil.e("sender_id:"+message.getSender_id());
//                            如果当前不存在该用户的验证信息
                            if (! SDCardUtil.findMessage(message.getSender_id())){
                                message.setType(MessageType.RECEIVE_FRIEND);
                                Intent intent=new Intent("add.friend.message");
                                intent.putExtra("message",message);
                                context.sendBroadcast(intent);
                            }
                            break;
                        case MessageType.AGREE_FRIEND:
                            message.setType(MessageType.ACCEPT_FRIEND);
                            Intent intent=new Intent("add.friend.message");
                            intent.putExtra("message",message);
                            context.sendBroadcast(intent);
                            break;
                        case MessageType.COM_MES:case MessageType.PICTURE_MESSAGE:case MessageType.VOICE_MESSAGE:
                            if (MyApplication.getInstance().containActivity(ChatActivity.class)){
                            Intent intent2=new Intent("chat");
                                message.setMessageType(1);
                            intent2.putExtra("message",message);
                            context.sendBroadcast(intent2);
                                LogUtil.e("直接在聊天页面");
                            }else{
//                                发送通知说有人发了消息
                                Intent intent2=new Intent("add.friend.message");
                                intent2.putExtra("message",message);
                                context.sendBroadcast(intent2);
//                                保存消息到本地，下次直接显示
                                messageList= MessageList.parse(CacUtil.cacheLoad(Constant.LOAD_MESSAGELIST,context,message.getSender_id()));
                                message.setMessageType(1);
                                messageList.getMessage().add(message);
                                messageList.setContent(message.getContent());
                                messageList.setTitle(message.getSender_id());
                                messageList.setLastTime(message.getSendTime());
                                messageList.setType(message.getType());
                                CacUtil.cacheSave(message.getSender_id(),context,messageList);
                            }
                            break;
                        case MessageType.HELP_MESSAGE:case MessageType.TOGETHER_MESSAGE:case MessageType.ONE_MESSAGE:
                            NotificationUtil.showHangNotification(message,context);
                        default:
                            LogUtil.e("该消息类型暂时未定义 消息类型："+message.getType());
                            break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
