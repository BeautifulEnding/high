package com.example.asus.client;

import android.content.Context;

import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.util.LogUtil;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
//该类负责发送消息到服务器
//    把该类转变成IntentService或者是Service
public class Client  extends  Thread{
    private Context mContext;
    //    定义Socket
    public Socket socket;
    //    定义Socket输出流
    private PrintWriter writer;
    //    定义该客户端的用户
    private User mUser;
    private Client client;
    public Client(User user, Context context){
//        LogUtil.e("client构造方法被调用");
        this.mUser=user;
        this.mContext=context;
        this.setPriority(10);
        client=this;
        Constant.CHAT_PATH=Constant.SD_PATH+"chat";
    }
    @Override
    public void run() {
        try{
            socket = new Socket("192.168.1.138", 10000);
//            LogUtil.e("socket isConnected()："+socket.isConnected()+"   socket isClosed"+socket.isClosed());
            if (mUser!=null && socket !=null && socket.isConnected() && !socket.isClosed()){
//                LogUtil.e("正在向服务器发送消息");
                writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
                JSONObject object=new JSONObject();
                object.put("type", MessageType.USER_MESSAGE);
                object.put("user_id",mUser.getId());
                writer.println(object.toString());
//                writer.write(object.toString()+"\n");
                writer.flush();
                new ClientThread(socket,mContext).start();
            }else{
                LogUtil.e("异常socket isConnected()："+socket.isConnected()+"  socket isClosed"+  socket.isClosed());
            }
        }catch (Exception e){
//            LogUtil.e("client 异常"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void setMessage(String message){
        LogUtil.e("正在发送消息到服务器");
        try{
//            Thread.sleep(5000);
            writer=new PrintWriter(socket.getOutputStream());
            writer.println(message);
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
            writer.close();
        }
    }

}
