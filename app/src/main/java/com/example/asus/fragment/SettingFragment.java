package com.example.asus.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.activity.MainActivity;
import com.example.asus.activity.TrueSettingActivity;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;
import com.example.asus.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragment extends BaseFragment {
    //定义保存个人信息的SharePreferences
    SharedPreferences user_message=null;
    SharedPreferences.Editor editor=null;
    //定义个人信息
    RelativeLayout personMessage=null;
    //定义Fragment中的ListView
    ListView list;
    //设置
    LinearLayout setting=null;
    //定义启动SettingActivity的Action
    String SETTING_ACTION="this.is.setting.activity";
    //定义头像
    private ImageView user;
    //定义用户名
    private TextView user_name;
    private String[] names;
    private int[] iamgesId=new int[]{R.drawable.photo,R.drawable.comments,R.drawable.phraise};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
		View settingLayout = inflater.inflate(R.layout.setting_layout,container, false);
        names=getResources().getStringArray(R.array.setting_list);
        personMessage=(RelativeLayout)settingLayout.findViewById(R.id.personmessage);
        user=(ImageView)personMessage.findViewById(R.id.portraits);
        user_name=(TextView)personMessage.findViewById(R.id.network_name);
        TextView user_id=(TextView)personMessage.findViewById(R.id.id);
        //将sharepreferences中的头像，用户名，用户账号提取出来
        user_message=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor=user_message.edit();
        user_name.setText(user_message.getString("user_name",null));
        user_id.setText("账号:"+user_message.getString("user_id",null));
       /* //获得头像
        Drawable drawable=Drawable.createFromPath(user_message.getString("user_photo",null));
        user.setBackground(drawable);*/
        LogUtil.e(Constant.USER_PHOTO+user_message.getString("user_photo",null));
        Glide.with(this).load(Constant.USER_PHOTO+user_message.getString("user_photo",null)).placeholder(R.color.gray).dontAnimate().dontTransform().into(user);
//        ImageLoader.getInstance().loadImage(Constant.USER_PHOTO+user_message.getString("user_id",null)+".jpg",new ImageSize(80,80),);
        //创建一个List集合，List集合的元素是Map
        List<Map<String,Object>> listItems=new ArrayList<>();
        for (int i=0;i<names.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("header",iamgesId[i]);
            listItem.put("settingName",names[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),listItems,R.layout.setting_list_item,
                new String[]{"header","settingName"},new int[]{R.id.header,R.id.name});
        list=(ListView)settingLayout.findViewById(R.id.my_list);
        //为ListView设置Adapter
        list.setAdapter(adapter);
        setting=(LinearLayout)settingLayout.findViewById(R.id.setting);
		return settingLayout;
	}
    @Override
    //在该方法中处理Fragment自身的事件，不需要回调到Activity进行处理
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        //为个人信息添加点击事件
        personMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"成功点击个人信息",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setAction(SETTING_ACTION);
                startActivity(intent);
            }
        });
        //对ListView的Item添加点击事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //相册
                    case 0:
                        //Toast.makeText(getActivity(),"这是设置Item",Toast.LENGTH_SHORT).show();
                        //Intent intent=new Intent(getActivity(), SettingActivity.class);
                        break;
                    //评论
                    case 1:
                        break;
                    case 2:
                        break;

                }
               // Toast.makeText(getActivity(),names[position],Toast.LENGTH_SHORT).show();
            }
        });
        //处理设置
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TrueSettingActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
    * 重新回到该Fragment会执行该方法，所以在这刷新
    * */
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
        Log.e("SettingFragment","正在执行SettingFragment的onResume方法");
		super.onResume();
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_SETTING;
        if (user_message.getBoolean("changed",false)){
            user_name.setText(user_message.getString("user_name",null));
            editor.remove("changed");
            editor.commit();
        }
        if (user_message.getBoolean("photo_changed",false)){
            user.setBackground(Drawable.createFromPath(user_message.getString("user_photo",null)));
            editor.remove("photo_changed");
            editor.commit();
        }

	}
	

}
