package com.example.asus.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/1/22 0022.
 */

public class DownloadUtil {

    public static List<JSONObject> downloadContent(final String url) throws Exception {
        FutureTask<List<JSONObject>> task = new FutureTask<List<JSONObject>>(
                new Callable<List<JSONObject>>() {
                    @Override
                    public List<JSONObject> call() throws Exception {
                        /*URL realUrl=new URL(url);
//		打开和URL之间的连接
                        HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
//		设置通用的请求属性
//		允许输入输出
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(10000);
                       *//* connection.setRequestProperty("Accept-language","zh-CN");
                        connection.setRequestProperty("Charset","utf-8");
                        connection.setRequestProperty("Connection","Keep-Alive");*//*
//		http://localhost:8080/notepad/notepad/index.jsp?filename=123
//		建立实际连接
                        connection.connect();
                        Log.e("contentLength",connection.getContentLength()+"");
//		        得到服务器的回复（响应）
                        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String lString="";
                        int i=1;
                        List<String> result=new ArrayList<>();
                        while ((lString=reader.readLine())!=null) {
                            result.add(lString);
                        }*/
                        try {
//		设置请求参数，在URL后面加上参数名和参数内容，用?表示参数的输入
//		参数在请求中直接发过去，如果需要发送大文件，则使用POST方法在请求体中发过去
//		请求体中的内容才需要使用到输入输出流
                            URL realUrl=new URL(url);
//		打开和URL之间的连接
                            HttpURLConnection connection=(HttpURLConnection)realUrl.openConnection();
//		设置通用的请求属性
//		允许输入输出
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.setRequestMethod("GET");
//		http://localhost:8080/notepad/notepad/index.jsp?filename=123
//		建立实际连接
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(10000);
                            connection.connect();
//		        得到服务器的回复（响应）
                            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String lString="";
                            List<JSONObject> result=new ArrayList<>();
                            while ((lString=reader.readLine())!=null) {
                                LogUtil.e("超時時服務器返回的值:"+lString);
                                JSONObject object=new JSONObject(lString);
                                result.add(object);
                            }
                            return result;
                        }
                        catch (Exception e) {
                            System.out.println("发送GET请求出现异常");
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
        new Thread(task).start();
        return task.get();
    }
}
