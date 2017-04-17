package com.example.asus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.constant.Constant;
import com.example.asus.he.R;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener{
    //定义输入框
    EditText editText;
    //定义输入框里的内容
    String name=null;
    //定义一个Tag标识输入的内容
    String tag=null;
    char[] text2=null;
    //定义与SettingActivity交互的Intent
    Intent intent=null;
    //定义返回图片
    ImageView name_reverse=null;
    //定义保存按钮
    Button save=null;
    //得到顶部文本框
    TextView text=null;
    //String[] key={"user_photo","user_id","user_name","user_tel","user_self","user_sex","user_birthday"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_name);
        //得到返回图像
         name_reverse=(ImageView)findViewById(R.id.name_reverse);
        //增加监听
        name_reverse.setOnClickListener(MessageActivity.this);
        //得到保存按钮
        save=(Button)findViewById(R.id.save);
        //增加监听
        save.setOnClickListener(MessageActivity.this);
        //得到顶部文本框
        text=(TextView)findViewById(R.id.head_message);
        //得到输入框
        editText=(EditText)findViewById(R.id.name);
        intent=new Intent(MessageActivity.this,SettingActivity.class);
        for (int i=0;i< Constant.key.length;i++){
            String value=getIntent().getStringExtra(Constant.key[i]);
            if (!(value==null)){
                switch (value){
                    case "user_name"://setContentView(R.layout.change_name);
                        text2="更改用户名".toCharArray();
                        text.setText(text2,0,text2.length);
                        getIntent().getExtras().clear();
                        tag=Constant.key[1];
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        //设置弹出键盘
                        //((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
                        break;
                    /*case "user_tel":
                        text2="更改手机号".toCharArray();
                        text.setText(text2,0,text2.length);
                        getIntent().getExtras().clear();
                        tag=Constant.key[2];
                        //设置输入框为号码格式
                        editText.setInputType(InputType.TYPE_CLASS_PHONE);
                        //设置长度为11
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                        //设置弹出键盘
                        //((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
                        break;*/
                    case "user_self":
                        text2="更改个人介绍".toCharArray();
                        text.setText(text2,0,text2.length);
                        getIntent().getExtras().clear();
                        tag=Constant.key[3];
                        //设置数据格式
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        //设置输入框输入的字符串长度最长为20
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        //设置弹出键盘
                        //((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
                        break;
                }
            }
        }
//        Timer timer=new Timer();
        /*//        设置自动弹出键盘
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText,InputMethodManager.RESULT_SHOWN);
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        },300);*/
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editText.getText())){
                    save.setBackgroundResource(R.drawable.button_enable);
                    save.setEnabled(true);
                    save.setTextColor(getResources().getColor(R.color.bg_color));
                }
                else{
                    save.setBackgroundResource(R.drawable.button_unenable);
                    save.setEnabled(false);
                    save.setTextColor(getResources().getColor(R.color.divider_color));
                }
            }
        });

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.name_reverse:
                MyApplication.getInstance().removeActivity(MessageActivity.this);
                finish();
                break;
            case R.id.save:
                name=editText.getText().toString();
                intent.putExtra(tag,name);
                //Toast.makeText(MessageActivity.this,"更换信息成功",Toast.LENGTH_SHORT).show();
                //结束该activity
                MyApplication.getInstance().removeActivity(MessageActivity.this);
                finish();
                break;

        }
    }
}
