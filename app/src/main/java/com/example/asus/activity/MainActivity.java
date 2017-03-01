package com.example.asus.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.asus.client.Client;
import com.example.asus.client.ManageClientThread;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.fragment.BaseFragment;
import com.example.asus.fragment.NewsFragment;
import com.example.asus.fragment.ShowFragment;
import com.example.asus.he.R;
import com.example.asus.ui.BottomControlPanel;
import com.example.asus.util.AndroidUtil;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.NotificationUtil;
import com.example.asus.util.TagUtil;

public class MainActivity extends BaseActivity implements BottomControlPanel.BottomPanelCallback{
    //定义SengFragment的顶部图片
    ImageView imageView=null;
    //定义一个记号判断是哪个activity使mainActivity隐藏
    int flag=-1;
	BottomControlPanel bottomPanel = null;
    FrameLayout head_panel=null;
	View head=null;
    String[] topics=null;
	private FragmentManager fragmentManager = null;
	private FragmentTransaction fragmentTransaction = null;
	//定义字符串标识当前的Fragment
	public static String currFragTag = "";
    //定义字符串标识的上一个Fragment
    public static String preFragTag="";
    private final int UPDATE_TOPIC=2;
    private Spinner spinner;
//    spinner的适配器
    ArrayAdapter<String> adapter=null;
//    保存话题
    SharedPreferences topSp;
    SharedPreferences.Editor editor;
//    Set<String> topicSet=new HashSet<>();
    private int topicSize;
//    得到showFragment
    public ShowFragment showFragment;
    public NewsFragment newsFragment;
//    public Handler handler;
//    private Message message;
    private Bundle bundle;
    public static final int UPDATE=0X1;
    private MyBroadcastReceiver br;
    public interface UpdateRecyclerView{
        public void updateRecyclerView();
    }
    private UpdateRecyclerView updateRecyclerView;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent();
        intent.setAction("start.bootService");
        User user= CacheUtil.cacheLoad("selfMessage",this);
        /*if (user!=null){
            LogUtil.e("mainActivity      User不为空");
            intent.putExtra("user",user);
        }else{
            LogUtil.e("mainActivity      user为空");
        }
        sendBroadcast(intent);*/
        Client client=new Client(user,this);
        client.start();
        ManageClientThread.addClientConServerThread(user,client);
//        LogUtil.e("mainActivity当前进程名-------------->"+ AndroidUtil.getCurProcessName(this));
        /*//设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        MyApplication.getInstance().addActivity(this);
        topSp=this.getSharedPreferences("topic",MODE_PRIVATE);
        editor=topSp.edit();
        topicSize=topSp.getInt("topicSize",0);
        if (topicSize!=0){
            topics=new String[topicSize];
            for (int i=0;i<topicSize;i++){
                topics[i]=topSp.getString("topic"+i,"");
            }
        }else {
            topics=getResources().getStringArray(R.array.topic);
        }
        //获取顶部容器
        head_panel=(FrameLayout) findViewById(R.id.head_layout);
        //获取顶部容器中填充的相关布局，默认先显示show_head_layout.
        head=View.inflate(MainActivity.this,R.layout.show_head_layout,null);
        /*TextView spinner=(TextView)head.findViewById(R.id.spinner);
        PopupMenu spinnerMenu= new PopupMenu(this,spinner);
        topics=getResources().getStringArray(R.array.topic);*/
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,topics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner=(Spinner)head.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        head_panel.addView(head);
        //初始化页面
		initUI();
		fragmentManager = getFragmentManager();
        //设置默认显示的界面为show页面
		setDefaultFirstFragment(Constant.FRAGMENT_FLAG_SHOW);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==topics.length-1){
                    Intent intent=new Intent(MainActivity.this,AddTopicActivity.class);
                    intent.putExtra("topics",topics);
                    startActivityForResult(intent,UPDATE_TOPIC);
                }else {
//                    更新showFragment中recyclerView的数据，
                   /*if (handler!=null){
                       handler.sendEmptyMessage(UPDATE);

                   }*/
                    showFragment.updateRecyclerView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//为接收到好友添加请求注册监听
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add.friend.message");
        br=new MyBroadcastReceiver();
        registerReceiver(br, myIntentFilter);
	}
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message=(Message) intent.getSerializableExtra("message");
            if (currFragTag==Constant.FRAGMENT_FLAG_SEARCH){
//                如果当前的fragment就是好友页，则直接更新newFragment的布局
                newsFragment.updateRecyclerView(message);
            }else{
                NotificationUtil.showHangNotification(message,context);
            }
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void initUI(){
		bottomPanel = (BottomControlPanel)findViewById(R.id.bottom_layout);
		if(bottomPanel != null){
			bottomPanel.initBottomPanel();
            //设置该Activity必须实现bottomPanel的Callback接口
            //也就是该Activity要实现对底部按钮的监听
			bottomPanel.setBottomCallback(this);
		}

	}
	@Override
	public void onBottomPanelClick(final int itemId) {
        //bottomPanel接口必须实现的方法
        //当点击底部按钮时
        // TODO Auto-generated method stub
        String tag = "";
        if ((itemId & Constant.BTN_FLAG_SHOW) != 0) {
            //将当前Fragemnt保存，成为上一个Fragment
            //tag得到应该显示的Fragment的值
            tag = Constant.FRAGMENT_FLAG_SHOW;
            //更改顶部布局填充文件
            head_panel.removeAllViews();
            head = View.inflate(MainActivity.this, R.layout.show_head_layout, null);
            spinner=(Spinner)head.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);
            head_panel.addView(head);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position==topics.length-1){
                        Intent intent=new Intent(MainActivity.this,AddTopicActivity.class);
                        intent.putExtra("topics",topics);
                        startActivityForResult(intent,UPDATE_TOPIC);
                    }else {
//                    更新showFragment中recyclerView的数据，
//                        handler.sendEmptyMessage(UPDATE);
                        showFragment.updateRecyclerView();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if ((itemId & Constant.BTN_FLAG_MESSAGE) != 0) {
            tag = Constant.FRAGMENT_FLAG_MESSAGE;
            if (currFragTag.equals(Constant.FRAGMENT_FLAG_SHOW)){
                //更改顶部布局填充文件
                head_panel.removeAllViews();
                head = View.inflate(MainActivity.this, R.layout.search_head_layout, null);
                head_panel.addView(head);
            }
        } else if ((itemId & Constant.BTN_FLAG_SEND) != 0) {
            Intent intent=new Intent(MainActivity.this,SendContentActivity.class);
            startActivity(intent);
            intent.setClassName(this,"com.example.asus.activity.SendContentActivity");//打开一个activity
            this.startActivity(intent);
            this.overridePendingTransition(0,R.anim.send_out_anim);
        } else if ((itemId & Constant.BTN_FLAG_SEARCH) != 0) {
            tag = Constant.FRAGMENT_FLAG_SEARCH;
            if (currFragTag.equals(Constant.FRAGMENT_FLAG_SHOW)){
                //更改顶部布局填充文件
                head_panel.removeAllViews();
                head = View.inflate(MainActivity.this, R.layout.search_head_layout, null);
                head_panel.addView(head);
            }

        } else if ((itemId & Constant.BTN_FLAG_SETTING) != 0) {
            tag = Constant.FRAGMENT_FLAG_SETTING;
            if (currFragTag.equals(Constant.FRAGMENT_FLAG_SHOW)){
                //更改顶部布局填充文件
                head_panel.removeAllViews();
                head = View.inflate(MainActivity.this, R.layout.search_head_layout, null);
                head_panel.addView(head);
            }
        }
        if (!tag.equals("")){
            setTabSelection(tag);
        }
    }
	private void setDefaultFirstFragment(String tag){
		setTabSelection(tag);
		bottomPanel.defaultBtnChecked();
	}
	
	private void commitTransactions(String tag){
		if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
			fragmentTransaction.commit();
            preFragTag = currFragTag;
			currFragTag = tag;
			fragmentTransaction = null;
		}
	}
	
	private FragmentTransaction ensureTransaction(){
		if(fragmentTransaction == null){
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}
		return fragmentTransaction;
		
	}
	
	private void attachFragment(int layout, Fragment f, String tag){
		if(f != null){
			if(f.isDetached()){
				ensureTransaction();
				fragmentTransaction.attach(f);
				
			}else if(!f.isAdded()){
				ensureTransaction();
				fragmentTransaction.add(layout, f, tag);
			}
		}
	}
	
	private Fragment getFragment(String tag){
		//得到tag所指向的Fragment
		Fragment f = fragmentManager.findFragmentByTag(tag);
		if(f == null){
			f = BaseFragment.newInstance(getApplicationContext(), tag);
		}
        if (tag.equals(Constant.FRAGMENT_FLAG_SHOW)) {
            showFragment= (ShowFragment) f;
        }
        if (tag.equals(Constant.FRAGMENT_FLAG_SEARCH)){
//            updateRecyclerView=(UpdateRecyclerView)f;
            newsFragment=(NewsFragment)f;
        }
        return f;
		
	}
	private void detachFragment(Fragment f){
		
		if(f != null && !f.isDetached()){
            //删除该Fragment
			ensureTransaction();
			fragmentTransaction.detach(f);
		}
	}
    void switchFragment(String tag){
        //如果当前的Fragment就是Tag所指的Fragment。则不进行处理
		if(TextUtils.equals(tag, currFragTag)){
			return;
		}
        //如果当前Fragment不为空
		if(currFragTag != null && !currFragTag.equals("")){
			detachFragment(getFragment(currFragTag));
		}
        //增加Fragment到布局中
		attachFragment(R.id.fragment_content, getFragment(tag), tag);
		commitTransactions(tag);
	}
	public  void setTabSelection(String tag) {
        //得到Fragment事务
		fragmentTransaction = fragmentManager.beginTransaction();
		switchFragment(tag);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        if (flag==2){
            //当MainActivity被SendActivity隐藏了时就直接将当前的Fragment换成上一个Fragment
            changeFragment(TagUtil.changeTagToId());
            flag=-1;
        }
        currFragTag="";
	}
    @Override
    public void onRestart(){
        super.onRestart();
        Log.e("mainActivity","正在执行onRestart方法");
        int itemId;
    }
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
	}
    public void changeFragment(int itemId){
        onBottomPanelClick(itemId);
        bottomPanel.changeButtonColor(itemId);
    }
    //contactsFragment的callbacks接口的实现
    public void setFlag(int flag){
          this.flag=flag;
    }
//    判断是否真的要退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            如果在main页面按了返回键，则判断是否需要退出程序
            showTips();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showTips() {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("退出程序").setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case UPDATE_TOPIC:
                if (resultCode==1){
                    //                更新topic
                    topics=data.getStringArrayExtra("topics");
                    for (int i=0;i<topics.length;i++){
                        LogUtil.e("topicValue"+topics[i]);
                    }
                    adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,topics);
                    spinner.setAdapter(adapter);
//                更新topic资源
                    for (int i=0;i<topics.length;i++){
                        editor.putString("topic"+i,topics[i]);
                    }
                    editor.putInt("topicSize",topics.length);
                    editor.commit();
                }else {
                    spinner.setSelection(0,true);
                }
                break;
        }
    }
   /* public void setHandler(Handler mHandler){
        mHandler=this.handler;
    }*/

}