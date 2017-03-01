package com.example.asus.he.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class ServerThread implements Runnable {
    //定义当前线程所处理的Socket
    Socket socket=null;
    //该线程所处理的Socket所对应的输入流
    BufferedReader br=null;
    public ServerThread(Socket socket)throws IOException{
        this.socket=socket;
        //初始化该Socket对应的输入流
        br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
    }
    public void run(){
        try{
            String content=null;
            //采用循环不断从Socket中读取客户端发送过来的数据
            while ((content=readFromClient())!=null){
                //遍历SocketList中的每一个Socket
                //将读到的数据向每个Socket发送一遍
                for (Iterator<Socket> it=MyServer.sockets.iterator();it.hasNext();){
                    Socket s=it.next();
                    try {
                        OutputStream os=s.getOutputStream();
                        os.write((content+"\n").getBytes("utf-8"));
                    }
                    catch (SocketException e){
                        e.printStackTrace();
                        //删除该Socket
                        it.remove();
                        System.out.print(MyServer.sockets);
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    //定义读取客户端数据的方法
    public String readFromClient(){
        try {
            return br.readLine();
        }
        //如果捕获到异常，说明该Socket对应的客户端已经关闭
        catch (IOException e){
            e.printStackTrace();
            MyServer.sockets.remove(socket);
        }
        return null;
    }
}
