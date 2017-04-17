package com.example.asus.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;
import com.example.asus.util.CacUtil;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.DialogUtil;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
//这里没有对与http的通信采取异步方式，
public class LoginActivity extends Activity{
    //定义JSONObject
    JSONObject jsonObj=null;
    //定义布局文件中的EditText和Button
    EditText editText;
    EditText editText1;
    Button login;
    SharedPreferences preferences=null;
    SharedPreferences.Editor editor=null;
    //定义用户的个人信息
    String user_id=null;
    String user_password=null;
    //得到分割条
    View divider1=null;
    View divider2=null;
    private boolean exception=false;
//    登录进度条
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        if (!isNetWorkConnected()) {
//            如果网络连接不可用，则显示在页面最上方
//            Toast.makeText(this,"当前网络不可用，请检查网络设置",Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("设置网络");
            builder.setMessage("当前网络不可用，请检查网络设置");
            builder.setPositiveButton("设置网络",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                   打开系统设置网络页面
//                    android.provider包提供打开系统页面的常量
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            });
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
        editText = (EditText) findViewById(R.id.user_id);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    divider1.setBackgroundColor(getResources().getColor(R.color.button_bg));
                } else {
                    divider1.setBackgroundColor(getResources().getColor(R.color.divider_color));
                }
            }
        });
        editText1 = (EditText) findViewById(R.id.password);
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    divider2.setBackgroundColor(getResources().getColor(R.color.button_bg));
                } else {
                    divider2.setBackgroundColor(getResources().getColor(R.color.divider_color));
                }
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        login = (Button) findViewById(R.id.login);
        divider1 = (View) findViewById(R.id.divider1);
        divider2 = (View) findViewById(R.id.divider2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetWorkConnected()) {
                    Toast.makeText(LoginActivity.this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (validate()){
                        login.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        // 如果登录成功
                        if (loginPro())  // ②
                        {
                            try {
                                Constant.SD_PATH=Constant.SD_PATH+jsonObj.getString("user_id")+"/";
                                User user=new User();
//                                修改存储根目录
                                user.setId(jsonObj.getString("user_id"));
                                user.setProfile_image_url(jsonObj.getString("user_photo"));
                                user.setDescription(jsonObj.getString("user_self"));
                                user.setTelPhone(jsonObj.getString("user_tel"));
                                user.setGender(jsonObj.getString("user_sex"));
                                user.setScreen_name(jsonObj.getString("user_name"));
                                CacheUtil.cacheSave("selfMessage",LoginActivity.this,user);
                            }catch (Exception e){
                                LogUtil.e("保存user异常-------->"+e.getMessage());
                            }
                            //保存登录记录，下次无需登录
                            preferences=getSharedPreferences("login",MODE_PRIVATE);
                            editor=preferences.edit();
                            try{
                                int i=0;
                                while (i< Constant.allKey.length){
                                    if (jsonObj.getString(Constant.allKey[i])!=null) {
                                        editor.putString(Constant.allKey[i], jsonObj.getString(Constant.allKey[i]));
                                        i++;

                                    }else{
                                        i++;
                                        continue;
                                    }

                                }

                            }catch (JSONException e){
                                Log.e("error",e.getMessage());
                            }
                            //提交数据
                            editor.commit();
                            /*int i=0;
                            if (preferences.contains("user_photo")){
                                Log.e("photo","照片存在");
                                //将图片保存到本地中
                                try{
                                    //最后再将Base64编码的字符串转成字节数组，
                                    ByteArrayInputStream user_photo = new ByteArrayInputStream(Base64.decode(preferences.getString("user_photo",null).getBytes(), Base64.DEFAULT));
                                    //得到图片
                                    Bitmap bitmap=BitmapFactory.decodeStream(user_photo);
                                    String f = System.currentTimeMillis() + ".png";
                                    try{
                                        new SavePhoto(bitmap,"/user_photo/"+f);
                                        editor.putString("user_photo",SavePhoto.USER_PHOTO_PATH+"/"+f);
                                        //提交数据
                                        editor.commit();
                                    }catch (NullPointerException e1){
                                        Log.e("NullPointerException",e1.getMessage());
                                    }
                                }catch (IOException e){
                                    Log.e("IOException", e.getMessage());

                                }finally {
                                }*/


//                            }
                            // 启动MainActivity
                            Intent intent = new Intent(LoginActivity.this
                                    , MainActivity.class);
                            startActivity(intent);
                            // 结束该Activity
                            MyApplication.getInstance().removeActivity(LoginActivity.this);

                            finish();
                        }
                        else
                        {
                            if (!exception){
                                DialogUtil.showDialog(LoginActivity.this
                                        , "用户名称或者密码错误，请重新输入！", false);
                            }
                            try{
                                if (jsonObj.getInt("error")!=0){
                                    DialogUtil.showDialog(LoginActivity.this
                                            , "连接服务器失败，请重试！", false);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }
    private boolean loginPro()
    {
        // 获取用户输入的用户名、密码
         user_id = editText.getText().toString();
         user_password = editText1.getText().toString();
        try
        {
            jsonObj = query(user_id, user_password);
            // 如果userId 大于0
            if (jsonObj.getString("user_id")!=null);
            {
                return true;
            }

        }
        catch (Exception e)
        {
            LogUtil.e("服务器响应异常,异常原因："+e.getMessage());
            exception=true;
        }

        return false;
    }
    // 定义发送请求的方法
    private JSONObject query(String username, String password)
            throws Exception
    {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<>();
        map.put("user_id", username);
        map.put("password", password);
        // 定义发送请求的URL
        String url = HttpUtil.BASE_URL + "login.jsp";
//        String url="http://192.168.1.117:8080/highservice/android/"+"login.jsp";
        // 发送请求
        return new JSONObject(HttpUtil.postRequest(url, map));
    }
    // 对用户输入的用户名、密码进行校验
    private boolean validate()
    {
        user_id= editText.getText().toString().trim();
        if (user_id.equals(""))
        {
            DialogUtil.showDialog(this, "用户账户是必填项！", false);
            return false;
        }
        String user_password = editText1.getText().toString().trim();
        if (user_password.equals(""))
        {
            DialogUtil.showDialog(this, "请输入密码！", false);
            return false;
        }
        return true;
    }

//    判断网络状态
    private boolean isNetWorkConnected(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
//        返回联网状态，如果有联网信息并且连了网，则返回true
        return (networkInfo!=null&&networkInfo.isConnected());
//        判断wifi连接情况
        /*WifiManager wifimanager= (WifiManager) getSystemService(WIFI_SERVICE);
        wifimanager.isWifiEnabled();
        wifimanager.getWifiState();
*/
    }
}
