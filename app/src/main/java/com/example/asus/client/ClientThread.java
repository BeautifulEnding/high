package com.example.asus.client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;
import com.example.asus.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
    public ClientThread(Socket socket,Context context){
        this.socket=socket;
        this.context=context;
        try{
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
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
                            message.setType(MessageType.RECEIVE_FRIEND);
                            Intent intent=new Intent("add.friend.message");
                            intent.putExtra("message",message);
                            context.sendBroadcast(intent);
                            break;
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
