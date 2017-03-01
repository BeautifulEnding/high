package com.example.asus.client;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;
import com.example.asus.util.LogUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.MessageDigestSpi;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
//该类负责接收服务器发送过来的消息
public class ClientThread extends Thread{
    private Socket socket;
//    private BufferedReader reader=null;
    private Context context;
    private ObjectInputStream reader=null;
    public ClientThread(Socket socket,Context context){
        this.socket=socket;
        this.context=context;
        try{
//            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader=new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            Log.e("IOException",e.getMessage());
        }
    }
    @Override
    public void run(){
        while (true){
            try{
                Message message=(Message)reader.readObject();
                LogUtil.e("从服务器接收到消息");
                LogUtil.e("消息内容"+message.getContent());
                switch (message.getType()){
                    case MessageType.ADD_FRIEND:
                        Intent intent=new Intent("add.friend.message");
                        intent.putExtra("message",message);
                        context.sendBroadcast(intent);
                        break;
                    default:
                        break;
                }
            }catch (Exception e){
                LogUtil.e("读取服务器消息异常"+e.getMessage());
            }
        }
    }

}
