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
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.client.Client;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.fragment.BaseFragment;
import com.example.asus.fragment.NewsFragment;
import com.example.asus.fragment.ShowFragment;
import com.example.asus.he.R;
import com.example.asus.ui.BottomControlPanel;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.ScreenUtil;
import com.example.asus.ui.groupwindow.GroupPopWindow;
import com.example.asus.ui.groupwindow.IGroupItemClick;
import com.example.asus.util.CacUtil;
import com.example.asus.util.DensityUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.NotificationUtil;
import com.example.asus.util.TagUtil;
import com.example.asus.util.UserUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Timer;
import java.util.TimerTask;

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
//    private Spinner spinner;
    private LinearLayout mGroup;
    private GroupPopWindow mPopWindow;
    public TextView mUserNameTextView;
//    spinner的适配器
//    ArrayAdapter<String> adapter=null;
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
    public Client client;
    private UpdateRecyclerView updateRecyclerView;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        User user= CacUtil.cacheLoad("selfMessage",this);
        if (user!=null){
            LogUtil.e("启动client线程");
            client=new Client(user,this);
            client.start();
        }else{
            LogUtil.e("BootService user为空");
        }
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
//        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,topics);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        head_panel.addView(head);
        //初始化页面
		initUI();
		fragmentManager = getFragmentManager();
        //设置默认显示的界面为show页面
		setDefaultFirstFragment(Constant.FRAGMENT_FLAG_SHOW);
        mGroup=(LinearLayout)findViewById(R.id.group);
        mUserNameTextView = (TextView)findViewById(R.id.name);
        initGroupWindows();
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
            switch (message.getType()){
                case MessageType.ADD_FRIEND:
//                    添加对方好友
                    client.setMessage(new Gson().toJson(message));
                    break;
                case MessageType.RECEIVE_FRIEND:
//                    接收到来自别人的好友请求，如果没有及时处理应该在下一次显示该消息
                    if (currFragTag==Constant.FRAGMENT_FLAG_SEARCH){
//                        LogUtil.e("更新newFragment");
                        newsFragment.updateRecyclerView(message);
                    }else{
                        NotificationUtil.showHangNotification(message,MainActivity.this);
//                        LogUtil.e("发送通知");
                    }

                    break;
                case MessageType.NOTIFICATION :
//                    将当前的Fragment更换成好友Fragment
                    changeFragment(Constant.BTN_FLAG_SEARCH);
                    break;
                case MessageType.AGREE_FRIEND:
//                    同意添加对方为好友
                    client.setMessage(new Gson().toJson(message));
                    User user= UserUtil.getUser(message.getReceiver_id(),context);
                    newsFragment.updateListView(user);
                    break;
                case MessageType.ACCEPT_FRIEND:
//                    对方同意添加我为好友
                    if (currFragTag==Constant.FRAGMENT_FLAG_SEARCH){
//                        LogUtil.e("更新newFragment");
                        user= UserUtil.getUser(message.getSender_id(),context);
                        newsFragment.updateListView(user);
                    }else{
                        NotificationUtil.showHangNotification(message,MainActivity.this);
//                        LogUtil.e("发送通知");
                    }
                    break;
                case MessageType.ACCEPT_NOTIFICATION:
                    changeFragment(Constant.BTN_FLAG_SEARCH);
                    break;
                case MessageType.COM_MES:case MessageType.PICTURE_MESSAGE:case MessageType.VOICE_MESSAGE:
                    LogUtil.e("接收到聊天消息");
                    NotificationUtil.showHangNotification(message,MainActivity.this);
                    break;
                case MessageType.NOTIFICATION_LM:
                    Intent intent2=new Intent(MainActivity.this, ChatActivity.class);
                    User user2=UserUtil.getUser(message.getSender_id(),MainActivity.this);
                    intent.putExtra("friends",user2);
                    startActivity(intent2);
                    break;
                case MessageType.HELP_MESSAGE:case MessageType.TOGETHER_MESSAGE:case MessageType.ONE_MESSAGE:
                    client.setMessage(new Gson().toJson(message));
                    break;
                case MessageType.NOTIFICATION_HELP:
                    Intent intent1=new Intent(MainActivity.this,HelpMessageActivity.class);
//                    intent1.putExtra("message",message);
//                    CacheUtil.cacheSave("helpMessage",MainActivity.this,message);
                    message.setMessageType(0);
                    CacUtil.cacheAppend("helpMessage",MainActivity.this,message);
                    startActivity(intent1);
                    break;
                case MessageType.NOTIFICATION_TOGETHER:
                    Intent intent3=new Intent(MainActivity.this,TogetherMessageActivity.class);
//                    intent3.putExtra("message",message);
                    startActivity(intent3);
                    break;
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
    private void initGroupWindows() {
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                mPopWindow = GroupPopWindow.getInstance(MainActivity.this, ScreenUtil.getScreenWidth(MainActivity.this) * 3 / 5, ScreenUtil.getScreenHeight(MainActivity.this) * 2 / 3);
                mPopWindow.showAtLocation(mUserNameTextView, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mUserNameTextView.getHeight() + statusBarHeight + DensityUtil.dp2px(MainActivity.this, 8));
                mPopWindow.setOnGroupItemClickListener(new IGroupItemClick() {
                    @Override
                    public void onGroupItemClick(int position, long groupId, String groupName) {
//                        mCurrentGroup = groupId;
//                        if (groupId != Constants.GROUP_TYPE_ALL) {
//                            setGroupName(groupName);
//                        } else {
//                            setGroupName(mUserName);
//                        }
                        mUserNameTextView.setText(groupName);
                        mPopWindow.dismiss();
//                        mHomePresent.pullToRefreshData(groupId, mContext);
                        showFragment.updateRecyclerView();
                    }
                });
                if (mPopWindow.isShowing()) {
                    mPopWindow.scrollToSelectIndex();
                }
            }
        });
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
            head_panel.addView(head);
            mGroup=(LinearLayout)findViewById(R.id.group);
            mUserNameTextView = (TextView)findViewById(R.id.name);
            initGroupWindows();
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
//                    adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,topics);
//                    spinner.setAdapter(adapter);
//                更新topic资源
                    for (int i=0;i<topics.length;i++){
                        editor.putString("topic"+i,topics[i]);
                    }
                    editor.putInt("topicSize",topics.length);
                    editor.commit();
                }else {
//                    spinner.setSelection(0,true);
                }
                break;
        }
    }
   /* public void setHandler(Handler mHandler){
        mHandler=this.handler;
    }*/
    @Override
    public void  onDestroy(){
        super.onDestroy();
        Message message=new Message();
        message.setType(MessageType.EXIT);
        client.setMessage(new Gson().toJson(message));
//        client.stop();
        LogUtil.e("mainActivity被销毁");
        unregisterReceiver(br);
    }



}