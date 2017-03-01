package com.example.asus.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.asus.he.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/13 0013.
 */
public class TrueSettingActivity extends Activity{
//    定义列表
    ListView listView=null;
//    定义Adapter
    SimpleAdapter adapter=null;
//    定义图片数组
    int[] images=new int[]{R.drawable.right,
        R.drawable.right,R.drawable.right,R.drawable.right,
        R.drawable.right,R.drawable.right};
//    定义名称
    String[] names=null;
//    定义退出
    TextView exit=null;
    ImageView reverse=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.true_setting_layout);
        listView=(ListView)findViewById(R.id.setting_list);
        names=getResources().getStringArray(R.array.true_setting_list);
//        创建list集合
        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        for (int i=0;i<images.length;i++){
            Map<String,Object> listItem=new HashMap<>();
            listItem.put("name",names[i]);
            listItem.put("image",images[i]);
            listItems.add(listItem);
        }
        adapter=new SimpleAdapter(this,listItems,R.layout.true_setting_item,
                new String[]{"name","image"},new int[]{R.id.name,R.id.right});
        listView.setAdapter(adapter);
        exit=(TextView)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        为退出设置Popumenu
                PopupMenu menu=new PopupMenu(TrueSettingActivity.this,exit);
                getMenuInflater().inflate(R.menu.exit,menu.getMenu());
//        增加监听
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.exit_one){
//                    退出当前账号
//                    清除登录信息，并退出
                            SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.clear();
                            editor.commit();
                        }
                        MyApplication.getInstance().exit();
                        return true;
                    }
                });
                menu.show();
            }
        });
//        得到返回
        reverse=(ImageView)findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().removeActivity(TrueSettingActivity.this);
                finish();
            }
        });
//        为listView设置监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
//                        设置消息类型是为仅接收好友动态，还是接收所有人的动态，默认接收所有人的
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
        });
    }
}
