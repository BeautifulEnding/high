package com.example.asus.client;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.User;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.LogUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
//该类负责发送消息到服务器
//    把该类转变成IntentService或者是Service
public class Client extends Thread{
    private Context mContext;
//    定义Socket
    public Socket socket;
//    定义Socket输出流
    private ObjectOutputStream writer;
//    定义该客户端的用户
    private User user;
    public interface SetHandler{
        public void setHandler(Handler handler);
    }
    public Client(User user,Context context){
        LogUtil.e("client构造方法被调用");
        this.user=user;
        this.mContext=context;
        this.setPriority(10);
        try{
            socket=new Socket();
            socket.connect(new InetSocketAddress(HttpUtil.SERVER_URL,10000),2000);
            if (user!=null && socket!=null){
//                LogUtil.e("Client user不为空");
                writer=new ObjectOutputStream(socket.getOutputStream());
                writer.writeObject(user);
                new ClientThread(socket,mContext).start();
                writer.flush();
                writer.close();
                Log.e("client socket", " " + socket.isClosed() + socket.isConnected());
            }
        }catch (Exception e){
//            Log.e("UnknownHostException",e.getMessage());
            LogUtil.e("client 异常"+e.getMessage());
        }
    }
    @Override
    public void run(){
        try{
            for (;;){
                if (!socket.isClosed() && socket.isConnected()){
                    try{
                        writer=new ObjectOutputStream(socket.getOutputStream());
                        LogUtil.e("正在向服务器发送消息");
                        Message message=new Message();
                        message.setContent("test");
                        writer.writeObject(message);
                        writer.flush();
                        writer.close();
                        Thread.sleep(2000);
                        Log.e(" run socket", " " + socket.isClosed() + socket.isConnected());
                    }catch (Exception e){
                        LogUtil.e("发送消息异常"+e.getMessage());
                    } finally {
                        socket.close();
                    }

                }
            }
        }catch (UnknownHostException e){
//            Log.e("UnknownHostException",e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e1){
//            Log.e("IOException", e1.getMessage());
            e1.printStackTrace();
        }
    }

    public void setMessage(Message message){
        try{
            writer.writeObject(message);
        }catch (Exception e){
            LogUtil.e("发送消息异常"+e.getMessage());
        }
    }

}
