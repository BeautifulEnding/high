package com.example.asus.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.client.Client;
import com.example.asus.client.ManageClientThread;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.ToastUtil;

import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class AddFriend extends Activity {
    private ImageView reverse;
    private TextView messageTextView;
    private ImageView portraits;
    private TextView netWorkName;
    private TextView id;
    private TextView settingTag;
    private Button addButton;
    private User user;
    private User selfUser;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.add_friend_layout);
        initView();
        initEvent();
    }
    private void initView(){
        reverse=(ImageView) findViewById(R.id.reverse);
        messageTextView=(TextView)findViewById(R.id.messageTextView);
        portraits=(ImageView)findViewById(R.id.portraits);
        netWorkName=(TextView)findViewById(R.id.network_name);
        id=(TextView)findViewById(R.id.id);
        settingTag=(TextView)findViewById(R.id.settingTag);
        addButton=(Button)findViewById(R.id.addButton);
        selfUser= CacheUtil.cacheLoad("selfMessage",this);
    }

    private void initEvent(){
        user=(User) getIntent().getSerializableExtra("friend");
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().removeActivity(AddFriend.this);
                finish();
            }
        });

        messageTextView.setText("好友资料");
//        Glide.with(this).load(user.getProfile_image_url()).placeholder(R.color.gray).dontAnimate().dontTransform().into(portraits);
        portraits.setBackground(Drawable.createFromPath(Constant.USER_PHOTO+user.getProfile_image_url()));
        netWorkName.setText(user.getScreen_name());
        id.setText(user.getId());
        settingTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client=new Client(selfUser,AddFriend.this);
                Socket socket=client.socket;
                try{
                    if (socket!=null){
                        ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
                        Message message=new Message();
                        message.setContent("addFriend");
                        message.setSender_id(selfUser.getId());
                        message.setReceiver_id(user.getId());
                        message.setType(MessageType.ADD_FRIEND);
                        message.setSendTime(System.currentTimeMillis());
                        outputStream.writeObject(message);
                    }
                }catch (Exception e){
                    LogUtil.e("addFriend"+e.getMessage());
                }
                ToastUtil.show(AddFriend.this,"已向好友发送请求，等待好友验证", Toast.LENGTH_SHORT);
            }
        });
    }
}
