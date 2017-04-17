package com.example.asus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.asus.he.R;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class SendContentActivity extends Activity{
//    帮助
    private ImageView help;
//    约
    private ImageView appointment;
//    we are one
    private ImageView together;
    private LinearLayout compose;
    private LinearLayout innerCompose;
//    取消
    private ImageView cancel;
//  消失动画
    private Animation animation;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_layout);
        MyApplication.getInstance().addActivity(this);
        initView();
        final Animation ani = AnimationUtils.loadAnimation(this, R.anim.send_anim);
        compose.setAnimation(ani);
        initEvent();
    }
    private void initView(){
        help=(ImageView)findViewById(R.id.help);
        appointment=(ImageView)findViewById(R.id.appointment);
        together=(ImageView)findViewById(R.id.together);
        compose=(LinearLayout)findViewById(R.id.compose);
        innerCompose=(LinearLayout)findViewById(R.id.inner_compose);
        cancel=(ImageView)findViewById(R.id.cancel);
        //        设置动画
        animation=AnimationUtils.loadAnimation(SendContentActivity.this,R.anim.send_out_anim);
        /*handler=new Handler(){
            @Override
            public void handleMessage(Message message){
                if (message.what==0){
                    innerCompose.setAnimation(animation);
                    animation.start();
                }
            }
        };*/
    }

    private void initEvent(){
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("next.is.send.activity");
                intent.putExtra("topic","帮助");
                startActivity(intent);
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("next.is.send.activity");
                intent.putExtra("topic","约");
                startActivity(intent);
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();
            }
        });

        together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("next.is.send.activity");
                intent.putExtra("topic","we are one");
                startActivity(intent);
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* LogUtil.e("取消发布");
                *//*innerCompose.setAnimation(animation);
                animation.start();*//*
                *//*MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();*//*
                handler.sendEmptyMessage(0);*/
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();

             }
        });

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                MyApplication.getInstance().removeActivity(SendContentActivity.this);
                finish();
            }
        });
    }

}
