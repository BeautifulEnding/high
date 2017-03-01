package com.example.asus.he;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class ClientThread implements Runnable{
    private Socket s;
    //定义向UI线程发送消息的Handler对象
    Handler handler;
    //定义接收UI线程消息的Handler对象
    Handler revHandler;
    //定义接收Socket对象输入流的数据
    BufferedReader br;
    OutputStream os;
    public ClientThread(Handler handler){
        this.handler=handler;
    }
    public void run(){
        try {
            s=new Socket("127.0.0.1",30000);
            br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            os=s.getOutputStream();
            //启动一条子线程来读取服务器响应的数据
            new Thread(){
                @Override
                public void run(){
                    String content=null;
                    //不断读取Socket输入流中的数据
                    try {
                        while ((content=br.readLine())!=null){
                            Message msg=new Message();
                            msg.what=0x123;
                            msg.obj=content;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                }.start();
            //为当前线程初始化Looper对象
            Looper.prepare();
            //创建revHandler对象
            revHandler=new Handler(){
                @Override
                public void handleMessage(Message msg){
                    //接收到UI线程中用户输入的数据
                    if (msg.what==0x345){
                        try {
                            os.write((msg.obj.toString()+"\r\n").getBytes("utf-8"));
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            //启动Looper
            Looper.loop();
        }
        catch (SocketTimeoutException e1){
           System.out.print("网络连接超时");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
