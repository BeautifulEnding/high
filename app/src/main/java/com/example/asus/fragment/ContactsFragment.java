/*
package com.example.asus.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.example.asus.activity.MainActivity;
import Constant;
import com.example.asus.he.R;
import TagUtil;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactsFragment extends BaseFragment{
//    发布页面
    private int SET_FLAG=2;
    public int IF_NEED_CHANGE_FRAGMENT=0;
    int itemId=-1;
    //定义一个回调接口，该Fragment所在的activity需要实现该接口
    private Callbcaks callbcaks;
    //该Fragment将通过该接口与他所在的activity进行交互
    public interface Callbcaks{
        public void setFlag(int flag);
    }
    //定义Fragment管理器
    FragmentManager manager;
    //定义Fragment事务
    private FragmentTransaction fragmentTransaction = null;
    //定义一个GridView
    GridView gridView;
    //定义一个ImageView
    ImageView cancel;
    //定义显示图片组
    int[] imageIds=new int[]{R.drawable.text,R.drawable.help,
    R.drawable.appointment,R.drawable.one};
    //定义图片下方的文字
    String[] contents;
    View contactsLayout=null;
   //这是Send界面
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 contactsLayout = inflater.inflate(R.layout.contacts_layout,
				container, false);
        //得到GridView
        gridView=(GridView)contactsLayout.findViewById(R.id.format);
        //得到字符串数组资源
        contents=getResources().getStringArray(R.array.send_format_list);
        //创建一个List对象，元素是Map
        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        for (int i=0;i<imageIds.length;i++){
            Map<String,Object> item=new HashMap<String, Object>();
            item.put("image",imageIds[i]);
            item.put("content",contents[i]);
            listItems.add(item);
        }
        //创建一个SimpleAdapter
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),listItems,R.layout.send_format_list,
                new String[]{"image","content"},new int[]{R.id.header,R.id.name});
        //为GridView添加适配器
        gridView.setAdapter(adapter);
		return contactsLayout;
	}
    @Override
    //在该方法中处理Fragment自身的事件，不需要回调到Activity进行处理
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        AlphaAnimation animation=new AlphaAnimation(0.0f,1.0f);
//        TranslateAnimation animation=new TranslateAnimation(0.0f,0.0f,500.0f,1.0f);
//        animation.setDuration(100);
//        gridView.setAnimation(animation);
//        animation.start();
//        加载进入动画资源
        final Animation anim= AnimationUtils.loadAnimation(getActivity(),R.anim.home_weiboitem_arrow_popup_enter);
        contactsLayout.setAnimation(anim);
        anim.start();
        //为GridView设置事件监听器
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent("next.is.send.activity");
                        startActivity(intent);
                        callbcaks.setFlag(SET_FLAG);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        //如果activity没有实现该接口
        if (!(activity instanceof Callbcaks)){
            throw new IllegalStateException("SendFragment所在的activity必须实现Callbacks接口");

        }
        //把该activity当成Callbacks对象
        callbcaks=(Callbcaks)activity;
    }
    //当该Fragment从它所属的activity中被删除时回调该方法
    @Override
    public void onDetach(){
        super.onDetach();
        callbcaks=null;
    }
    //当该Fragment从它所属的activity中被其他的Fragment替换时
    @Override
    public void onPause(){
//        当被替换时，执行关闭动画
//       closeSendFragment(true);
        super.onPause();
    }
    //通过tag得到itemid
 @Override
  public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.currFragTag =Constant.FRAGMENT_FLAG_SEND;
        }
    public void closeSendFragment(boolean yes){
        if (yes){
            final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.cancel_send_anim);
            contactsLayout.setAnimation(animation);
//            用gridView没有效果？？？
//            gridView.setAnimation(animation);
            animation.start();
        }
    }

}
*/
