package com.example.asus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.asus.he.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class IndexActivity extends BaseActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        setContentView(R.layout.index);
//        设置动画
        AlphaAnimation animation=new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(1000);
//        为图片设置动画
        ImageView view=(ImageView)findViewById(R.id.index);
        view.setAnimation(animation);
//        设置监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //获取只能被本程序读写的sharedPreferences对象
                SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
                if (preferences.contains("user_id")){
                    Intent intent=new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(IndexActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                MyApplication.getInstance().removeActivity(IndexActivity.this);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
