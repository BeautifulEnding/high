package com.example.asus.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.File.SavePhoto;
//import Constant;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.ImgUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class SettingActivity extends BaseActivity{
    //定义请求代码
    public static final int GET_CAMERA=0;
    public static final int REQUEST_CODE_PICK_IMAGE=1;
    //定义手机号的TextView内容
    TextView textView_tel=null;
    TextView textView_sex=null;
    TextView textView_birthday=null;
    //定义相机拍摄照片的保存路径
    Uri fileUri=null;
    //定义头像
    ImageView pic=null;
    //public static final String[] key={"user_photo","user_id","user_name","user_tel","user_self","user_sex","user_birthday"};
    //定义一个列表项
    Map<String,Object> listItem=new HashMap<String,Object>();
    //创建一个List集合，List集合的元素是Map，List包含列表的每个列表项
    List<Map<String,Object>> listItems=new ArrayList<>();
    //为ListView定义一个适配器
    SimpleAdapter adapter=null;
    //定义与MessageActivity交换信息的Intent
    Intent intent=null;
    //定义存放个人信息的ListView
    ListView list;
    //定义ListView中Item内容中的个人信息
    String[] messages;
    //定义内容
    private  List<String> contents=new ArrayList<String>();
    //定义保存个人信息的Sharepreferences
    SharedPreferences login=null;
    SharedPreferences.Editor editor=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_activity_layout);
        //定义与MessageActivity交互数据的intent
        intent = new Intent(SettingActivity.this, MessageActivity.class);
        //指定信息值
        messages = this.getResources().getStringArray(R.array.user_setting_list);
        //得到SharePreferences里的账号
        login = getSharedPreferences("login", MODE_PRIVATE);
        editor = login.edit();
        int i = 0;
        //得到头像布局
        RelativeLayout photoLayout = (RelativeLayout) findViewById(R.id.portraitlayout);
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为头像选择注册一个PopupMenu菜单
                PopupMenu popupMenu = new PopupMenu(SettingActivity.this, v);
                //将菜单资源加载到popup菜单中
                getMenuInflater().inflate(R.menu.photo, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.camera:
                                //系统常量， 启动相机的关键
                                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //设置图片的保存名
                                String f = System.currentTimeMillis() + ".png";
                                String path =SavePhoto.USER_PHOTO_PATH;
                                //设置图片的保存路径
                                File file = new File(path);
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                fileUri = Uri.fromFile(new File(path + "/" + f));
                                editor.putString("user_photo",path+"/"+f);
                                editor.putBoolean("photo_changed",true);
                                editor.commit();
                                Log.e("photo_changed","图像已更改-----------------------");
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
                                startActivityForResult(openCameraIntent, GET_CAMERA);
                                // 参数常量为自定义的request code, 在取返回结果时有用
                                break;
                            case R.id.picture:
                                //获得系统的相册
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");//相片类型
                                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        //获得头像
        pic = (ImageView) photoLayout.findViewById(R.id.portrait);
//        pic.setImageDrawable(Drawable.createFromPath(login.getString("user_photo",null)));
        Glide.with(this).load(Constant.USER_PHOTO+login.getString("user_photo",null)).placeholder(R.color.gray).dontAnimate().dontTransform().into(pic);
        onRestart();
        //从用户名开始存入信息
        i = 0;
        while (i < messages.length) {
            //如果user_message中包含个人信息值，则contents显示，否则显示默认值
            if (login.contains(Constant.key[i])) {
                //Log.e("user_message",user_message.getString(key[i],null));
                contents.add(i, login.getString(Constant.key[i], null));
            } else {
                contents.add(i, "未指定，单击以更改");
            }
            i++;
        }
        //获取ListView
        list = (ListView) findViewById(R.id.user_setting_list);
        for (i = 0; i < messages.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("messages", messages[i]);
            listItem.put("contents", contents.get(i));
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter,其中String指定需要添加到列表项的内容，int指定添加到哪个组件
        adapter=new SimpleAdapter(this,listItems,R.layout.user_message_list,
                new String[]{"messages","contents"},new int[]{R.id.message,R.id.content}
       );
        list.setAdapter(adapter);
        //为箭头图像处理事件监听
        ImageView row = (ImageView) findViewById(R.id.reverse);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                //startActivity(intent);
//                上传更改的用户信息到服务器
                Map<String,String> map=new HashMap<String, String>();
                    if (login.contains("changed")){
//只修改了文字信息
                        map.put("user_name",login.getString("user_name",null));
                        map.put("user_tel",login.getString("user_tel",null));
                        map.put("user_self",login.getString("user_self",null));
                        map.put("user_sex",login.getString("user_sex",null));
                        map.put("user_birthday",login.getString("user_birthday",null));
                        map.put("changed","true");
                    }
                    if (login.contains("photo_changed")){
//只修改了图片
                        try{
                            FileInputStream inputStream=new FileInputStream(login.getString("user_photo",null));
                            byte[] bytes=new byte[1024];
                            int len=0;
                            String photo="";
                            while ((len=inputStream.read(bytes))>0){
                                photo=photo+bytes.toString();
                            }
                            map.put("user_photo",photo);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        map.put("photo_changed","true");
                    }
                if (map.size()>0){
                    map.put("user_id",login.getString("user_id",null));
                    String url= HttpUtil.BASE_URL+"saveUserMessage.jsp";
                    try{
                        HttpUtil.postRequest(url,map);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
        //为列表项添加事件监听器
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //view指的就是列表项组件
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //view为ListView中被选中项
                switch (position) {
                    case 0:
                        Toast.makeText(SettingActivity.this, "账号不能更改", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //用户名
                        putExtra(Constant.key[1]);
                        break;
                    case 2:
                        //得到手机号的内容
                       textView_tel=(TextView)view.findViewById(R.id.content);
                        //为手机号选择注册一个PopupMenu菜单
                        PopupMenu popupMenu1=new PopupMenu(SettingActivity.this,view);
                        //将菜单加载到popup菜单中
                        getMenuInflater().inflate(R.menu.show,popupMenu1.getMenu());
                        popupMenu1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.show:
                                                textView_tel.setText("15179139295");
                                                break;
                                    case R.id.notshow:
                                        //更改内容
                                        textView_tel.setText("电话号码已隐藏");
                                        break;
                                }
                                return true;
                            }
                        });
                        popupMenu1.show();
                        break;
                    case 3:
                        //个人介绍
                        putExtra(Constant.key[3]);
                        break;
                    case 4:
                        textView_sex=(TextView)view.findViewById(R.id.content);
                        //为性别选择注册一个PopupMenu菜单
                        PopupMenu popupMenu = new PopupMenu(SettingActivity.this, view);
                        //将菜单资源加载到popup菜单中
                        getMenuInflater().inflate(R.menu.sex, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                /*switch (item.getItemId()) {
                                    case R.id.man:
                                        //改变列表项的内容
                                        //changeListItem("user_sex",4,item.getTitle().toString());
                                        textView_sex.setText(item.getTitle().toString());
                                        changePreferences("user_sex",item.getTitle().toString());
                                    case R.id.woman:
                                        textView_sex.setText(item.getTitle().toString());
                                        changePreferences("user_sex",item.getTitle().toString());
                                    case R.id.unknown:
                                        textView_sex.setText(item.getTitle().toString());
                                        changePreferences("user_sex",item.getTitle().toString());
                                }*/
                                textView_sex.setText(item.getTitle().toString());
                                changePreferences("user_sex",item.getTitle().toString());
                                return true;
                            }
                        });
                        popupMenu.show();
                        break;
                    case 5:
                        //生日
                        textView_birthday=(TextView)view.findViewById(R.id.content);
                        //得到日历
                        Calendar c=Calendar.getInstance();
                        //弹出一个日期选择对话器
                        new DatePickerDialog(SettingActivity.this,new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                                //更改内容
                                textView_birthday.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                                changePreferences("user_birthday",String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                            }
                        },
                        //设置初始日期
                        c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
                        break;
                }
            }
        });
    }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                //requestCode为自定义的常量，用于区分请求
                //resultCode为执行后得到的结果
                switch (requestCode) {
                    //当请求结果为获得系统相机时
                    case GET_CAMERA:
                        if ( resultCode == RESULT_OK) {
                            //获得拍摄照片,这种方式只限定于相机图片保存在系统默认路径
                            //Bundle bundle = data.getExtras();
                            //Bitmap bitmap = (Bitmap) bundle.get("data");
                            //根据路径获得图片
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(fileUri.getPath())));
                                //将图片设为头像
                                pic.setImageBitmap(bitmap);
                            }catch (IOException e){
                                e.getMessage();
                            }
                            //对图片进行处理，得到图片，将其设为头像
                            Toast.makeText(SettingActivity.this,"图片保存成功",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //当请求结果为获得系统相册时
                    //对图片进行处理，得到图片，将其设为头像
                    case REQUEST_CODE_PICK_IMAGE:
                        if (resultCode == RESULT_OK) {
                            Bitmap bitmap = null;
                            //判断手机系统版本号
                            if (Build.VERSION.SDK_INT >= 19) {
                                //4.4及以上系统使用这个方法处理图片
                                bitmap = ImgUtil.handleImageOnKitKat(SettingActivity.this, data);        //ImgUtil是自己实现的一个工具类
                            } else {
                                //4.4以下系统使用这个方法处理图片
                                bitmap = ImgUtil.handleImageBeforeKitKat(SettingActivity.this, data);
                            }
                            pic.setImageBitmap(bitmap);
                            //获取图片的绝对路径
                            Uri photoUri=data.getData();
                            String[] imagepaths={MediaStore.Images.Media.DATA};
                            Cursor cursor=managedQuery(photoUri,imagepaths,null,null,null);
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                            //将光标移至开头 ，这个很重要，不小心很容易引起越界

                            cursor.moveToFirst();

                            //最后根据索引值获取图片路径

                            String path = cursor.getString(column_index);
                            editor.putString("user_photo",path);
                            editor.putBoolean("photo_changed",true);
                            editor.commit();
                        }
                        break;
                    default:
                        break;
                }
            }
@Override
public void onRestart(){
                //onStart方法在onCreate之后执行，并在该页面重新回到用户眼前时执行
                //onRestart方法在没有finish但是退出了该页面时回调
                //在页面退到隐藏再重新开始时，onCreate方法也会被回调
                //Toast.makeText(SettingActivity.this,"成功执行onRestart方法",Toast.LENGTH_SHORT).show();
                Log.e("","成功执行onstart方法");
                super.onRestart();
            for (int i=0;i<Constant.key.length;i++){
                //得到修改后的用户名
                //String value=getIntent().getStringExtra(change[i]);
                boolean value=getIntent().hasExtra(Constant.key[i]);
                if (value){
                    //Log.d("value_number","value的值为true");
                    switch (i){
                        case 1:
                            //用户名
                            changePreferencesByActivity(Constant.key[1]);
                            break;
                        case 3:
                            //个人介绍
                            changePreferencesByActivity(Constant.key[3]);
                            break;
                    }
                }
            }
}
            private void putExtra(String name){
                //与MessageActivity页面交换数据
                intent.putExtra(name,name);
                startActivity(intent);
            }
   private void changePreferencesByActivity(String user_message) {
               //将更改后的信息存入login的SharePreferences中
                 editor.putString(user_message, getIntent().getStringExtra(user_message));
                 editor.putBoolean("changed",true);
                 editor.commit();
                //清空Intent中的数据
                getIntent().getExtras().clear();
            }
    private void changePreferences(String user_message,String content){
        // //将更改后的信息存入login的SharePreferences中
        editor.putString(user_message,content);
        editor.putBoolean("changed",true);
        editor.commit();
    }
}
