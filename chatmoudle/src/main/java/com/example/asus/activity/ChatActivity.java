package com.example.asus.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.asus.R;
import com.example.asus.adapter.EmoViewPagerAdapter;
import com.example.asus.adapter.EmoteAdapter;
import com.example.asus.adapter.MessageListAdapter;
import com.example.asus.client.Client;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.MessageList;
import com.example.asus.client.entity.MessageType;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.entity.Chat;
import com.example.asus.util.CacUtil;
import com.example.asus.util.CommonUtils;
import com.example.asus.util.FaceText;
import com.example.asus.util.FaceTextUtils;
import com.example.asus.util.LogUtil;
import com.example.asus.util.NotificationUtil;
import com.example.asus.util.ToastUtil;
import com.example.asus.view.EmoticonsEditText;
import com.example.asus.view.ViewPagerAdapter;
import com.google.gson.Gson;

public class ChatActivity extends Activity implements OnClickListener {
    // ListView mListView;
    private Button btn_chat_emo, btn_chat_send, btn_chat_add,
            btn_chat_keyboard, btn_speak, btn_chat_voice;
    private LinearLayout layout_more, layout_emo, layout_add;
    private ViewPager pager_emo;
    private TextView tv_picture, tv_camera, tv_location;
    RelativeLayout layout_record;
    private Handler mHandler = new Handler();
    TextView tv_voice_tips;
    ImageView iv_record;
    public static String tag = "ChatActivity";
    private ImageView titleBack;
    private MessageListAdapter adapter = null;
    private EmoticonsEditText messageInput = null;
    private Button messageSendBtn = null;
    private ImageButton userInfo;
    private ListView listView;
    private TextView tvChatTitle;
    private static final String LOG_TAG = "AudioRecordTest";
    /** 语音文件保存路径 */
    private String mFileName = null;
    /** 按住说话按钮 */
    private Button mBtnVoice;
    /** 用于语音播放 */
    private MediaPlayer mPlayer = null;
    /** 用于完成录音 */
    private MediaRecorder mRecorder = null;
    public  Message message = null;
    private Bitmap bp;
    private static final String PATH = Constant.SD_PATH+"Record/";
    public Client client;
    private User selfUser;
    private User friends;
    private MessageList messageList;
    private String localCameraPath;
    private MyBroadcastReceiver br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        MyApplication.getInstance().addActivity(this);
        Constant.CHAT_PATH=Constant.SD_PATH+"chat";
        selfUser= CacUtil.cacheLoad("selfMessage",this);
        friends=(User)getIntent().getSerializableExtra("friends");
        messageList=(MessageList)getIntent().getSerializableExtra("messageList");
        if (selfUser!=null){
            client=new Client(selfUser,this);
            client.start();
//            ManageClientThread.addClientConServerThread(user,client);
        }else{
            LogUtil.e("BootService user为空");
        }
        initView();
        // initData();
//        接收对方发送过来的消息
        init();
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("chat");
        br=new MyBroadcastReceiver();
        registerReceiver(br, myIntentFilter);
    }

    private void initView() {
        initBottomView();
        initVoiceView();
    }

    private void initVoiceView() {

        layout_record = (RelativeLayout) findViewById(R.id.layout_record);
        tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
        iv_record = (ImageView) findViewById(R.id.iv_record);
        btn_speak.setOnTouchListener(new OnTouchListener() {
            long beforeTime;
            long afterTime;
            int timeDistance;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String dir = null;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        beforeTime = System.currentTimeMillis();
                        try {
                            v.setPressed(true);
                            layout_record.setVisibility(View.VISIBLE);
                            tv_voice_tips
                                    .setText(getString(R.string.voice_cancel_tips));
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    layout_record.setVisibility(View.VISIBLE);
                                }
                            }, 300);
                            // recordManager.startRecording(targetId);
                        } catch (Exception e) {
                        }
                        dir = startVoice();
                        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
                        break;
                    case MotionEvent.ACTION_UP:
                        afterTime = System.currentTimeMillis();
                        v.setPressed(false);
                        layout_record.setVisibility(View.INVISIBLE);
                        mHandler.removeCallbacks(mSleepTask);
                        mHandler.removeCallbacks(mPollTask);
                        stopVoice();
                        String voiceFile =
                                CommonUtils.GetImageStr(mFileName);
                        if ("".equals(mFileName)) {
                            Toast.makeText(ChatActivity.this, "不能为空",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // (afterTime-beforeTime)/1000
                            try {
                                if (isNetWorkConnected()){
                                    if ((afterTime - beforeTime) < 500) {
                                        Toast.makeText(getApplicationContext(),
                                                "录音时间太短！", Toast.LENGTH_SHORT).show();
                                        File file = new File(mFileName);
                                        file.delete();
                                    } else {
                                        sendMessage(MessageType.VOICE_MESSAGE,voiceFile + "&"
                                                + (afterTime - beforeTime) / 1000,null);
                                    }
                                }else {
                                    ToastUtil.show(ChatActivity.this,"你还没联网呢！",Toast.LENGTH_SHORT);
                                }

                                // messageInput.setText("");
                            } catch (Exception e) {
                                ToastUtil.show(ChatActivity.this,"消息发送失败",Toast.LENGTH_SHORT);
                                // messageInput.setText(message);
                            }
//                            closeInput();
                        }
                        iv_record.setImageResource(R.drawable.chat_icon_voice1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        // initVoiceAnimRes();
    }

    /** 开始录音 */
    private String startVoice() {
        String dir = String.valueOf(System.currentTimeMillis());
        // 设置录音保存路径
        mFileName = PATH + dir + ".amr";
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Log.i(LOG_TAG, "SD Card is not mounted,It is  " + state + ".");
        }
        File directory = new File(mFileName).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            Log.i(LOG_TAG, "Path to file could not be created");
        }
//        Toast.makeText(getApplicationContext(), "开始录音", Toast.LENGTH_SHORT).show();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
        return dir;
    }

    /** 停止录音 */
    private void stopVoice() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(getApplicationContext(), "保存录音" + mFileName,Toast.LENGTH_SHORT).show();
    }

    private static final int POLL_INTERVAL = 300;
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mRecorder.getMaxAmplitude() / 2700.0;
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private Runnable mSleepTask = new Runnable() {
        public void run() {
        }
    };

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
            case 2:
                iv_record.setImageResource(R.drawable.chat_icon_voice1);
                break;
            case 3:
            case 4:
            case 5:
                iv_record.setImageResource(R.drawable.chat_icon_voice2);
                break;
            case 6:
            case 7:
            case 8:
                iv_record.setImageResource(R.drawable.chat_icon_voice3);
                break;
            case 9:
            case 10:
            case 11:
                iv_record.setImageResource(R.drawable.chat_icon_voice4);
                break;
            case 12:
            case 13:
            case 14:
                iv_record.setImageResource(R.drawable.chat_icon_voice5);
                break;
            default:
                iv_record.setImageResource(R.drawable.chat_icon_voice6);
                break;
        }
    }

    private void initBottomView() {
        btn_chat_add = (Button) findViewById(R.id.btn_chat_add);
        btn_chat_emo = (Button) findViewById(R.id.btn_chat_emo);
        btn_chat_add.setOnClickListener(this);
        btn_chat_emo.setOnClickListener(this);
        btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
        btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
        btn_chat_voice.setOnClickListener(this);
        btn_chat_keyboard.setOnClickListener(this);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        btn_chat_send.setOnClickListener(this);
        layout_more = (LinearLayout) findViewById(R.id.layout_more);
        layout_emo = (LinearLayout) findViewById(R.id.layout_emo);
        layout_add = (LinearLayout) findViewById(R.id.layout_add);
        initAddView();
        initEmoView();
        btn_speak = (Button) findViewById(R.id.btn_speak);
        messageInput = (EmoticonsEditText) findViewById(R.id.edit_user_comment);
        messageInput.setOnClickListener(this);
        messageInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

    }

    List<FaceText> emos;

    private void initEmoView() {
        pager_emo = (ViewPager) findViewById(R.id.pager_emo);
        emos = FaceTextUtils.faceTexts;

        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 2; ++i) {
            views.add(getGridView(i));
        }
        pager_emo.setAdapter(new EmoViewPagerAdapter(views));
    }

    private void initAddView() {
        tv_picture = (TextView) findViewById(R.id.tv_picture);
        tv_camera = (TextView) findViewById(R.id.tv_camera);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_picture.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
    }

    private View getGridView(final int i) {
        View view = View.inflate(this, R.layout.include_emo_gridview, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        List<FaceText> list = new ArrayList<FaceText>();
        if (i == 0) {
            list.addAll(emos.subList(0, 21));
        } else if (i == 1) {
            list.addAll(emos.subList(21, emos.size()));
        }
        final EmoteAdapter gridAdapter = new EmoteAdapter(ChatActivity.this,
                list);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                FaceText name = (FaceText) gridAdapter.getItem(position);
                String key = name.text.toString();
                try {
                    if (messageInput != null && !TextUtils.isEmpty(key)) {
                        int start = messageInput.getSelectionStart();
                        CharSequence content = messageInput.getText().insert(
                                start, key);
                        messageInput.setText(content);
                        CharSequence info = messageInput.getText();
                        if (info instanceof Spannable) {
                            Spannable spanText = (Spannable) info;
                            Selection.setSelection(spanText,
                                    start + key.length());
                        }
                    }
                } catch (Exception e) {

                }

            }
        });
        return view;
    }

    private void init() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 与谁聊天
        tvChatTitle = (TextView) findViewById(R.id.to_chat_name);
        if (friends!=null){
            tvChatTitle.setText(friends.getId());
        }else{
            tvChatTitle.setText("朋友");
        }
        userInfo = (ImageButton) findViewById(R.id.user_info);
        userInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatActivity.this, FriendInfoActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.chat_list);
        listView.setCacheColorHint(0);
        if (messageList==null){
            messageList= MessageList.parse(CacUtil.cacheLoad(Constant.LOAD_MESSAGELIST,this,friends.getId()));
        }
        if (messageList==null){
            messageList=new MessageList();
        }
        adapter = new MessageListAdapter(ChatActivity.this, messageList,
                listView,friends);
        listView.setAdapter(adapter);
        messageInput = (EmoticonsEditText) findViewById(R.id.edit_user_comment);
        messageSendBtn = (Button) findViewById(R.id.btn_chat_send);
        messageSendBtn.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            // 如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent();
//        switch (item.getItemId()) {
//            case R.id.menu_return_main_page:
//                intent.setClass(context, MainActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.menu_relogin:
//                intent.setClass(context, LoginActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.menu_exit:
//                isExit();
//                break;
//        }
        return true;

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int i = v.getId();
        if (i == R.id.edit_user_comment) {// mListView.setSelection(mListView.getCount() - 1);
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_add.setVisibility(View.GONE);
                layout_emo.setVisibility(View.GONE);
                layout_more.setVisibility(View.GONE);
            }

        } else if (i == R.id.btn_chat_emo) {
            if (layout_more.getVisibility() == View.GONE) {
                showEditState(true);
            } else {
                if (layout_add.getVisibility() == View.VISIBLE) {
                    layout_add.setVisibility(View.GONE);
                    layout_emo.setVisibility(View.VISIBLE);
                } else {
                    layout_more.setVisibility(View.GONE);
                }
            }


        } else if (i == R.id.btn_chat_send) {
            LogUtil.e("正在发送消息！");
            String message = messageInput.getText().toString();
            if (isNetWorkConnected()){
                if ("".equals(message)) {
                    Toast.makeText(ChatActivity.this, "不能为空", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    try {
                        sendMessage(MessageType.COM_MES,message,null);
                        messageInput.setText("");
                    } catch (Exception e) {
                        LogUtil.e(e.getMessage());
//                        ToastUtil.show(ChatActivity.this,"信息发送失败",Toast.LENGTH_SHORT);
                        messageInput.setText(message);
                    }
                }
            }else{
                ToastUtil.show(ChatActivity.this,"你还没联网呢！",Toast.LENGTH_SHORT);
            }

        } else if (i == R.id.btn_chat_add) {
            if (layout_more.getVisibility() == View.GONE) {
                layout_more.setVisibility(View.VISIBLE);
                layout_add.setVisibility(View.VISIBLE);
                layout_emo.setVisibility(View.GONE);
                hideSoftInputView();
            } else {
                if (layout_emo.getVisibility() == View.VISIBLE) {
                    layout_emo.setVisibility(View.GONE);
                    layout_add.setVisibility(View.VISIBLE);
                } else {
                    layout_more.setVisibility(View.GONE);
                }
            }


        } else if (i == R.id.btn_chat_voice) {
            messageInput.setVisibility(View.GONE);
            layout_more.setVisibility(View.GONE);
            btn_chat_voice.setVisibility(View.GONE);
            btn_chat_keyboard.setVisibility(View.VISIBLE);
            btn_speak.setVisibility(View.VISIBLE);
            hideSoftInputView();

        } else if (i == R.id.btn_chat_keyboard) {
            showEditState(false);

        } else if (i == R.id.tv_camera) {
            selectImageFromCamera();

        } else if (i == R.id.tv_picture) {
            selectImageFromLocal();

        } else if (i == R.id.tv_location) {
            selectLocationFromMap();

        } else {
        }
    }

    private void selectLocationFromMap() {
//        确定位置
        // Intent intent = new Intent(this, BMpActivityYY.class);
        // intent.putExtra("type", "select");
        // startActivityForResult(intent, 0x000003);
        // startActivity(intent);
    }

    public void selectImageFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, 2);
    }

    public void selectImageFromCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/high/image/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        localCameraPath = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, 1);
    }

    private void showEditState(boolean isEmo) {
        messageInput.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        messageInput.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this
                .getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(messageInput, 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:// 获取照相机照片
                    try {
                        sendMessage(MessageType.PICTURE_MESSAGE,CommonUtils.getImageBase64(localCameraPath),localCameraPath);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 2: // 获取本地照片
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(
                                    selectedImage, null, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex("_data");
                            String localSelectPath = cursor.getString(columnIndex);
                            LogUtil.e("localSelectPath:"+localSelectPath);
                            cursor.close();
                            if (localSelectPath == null
                                    || localSelectPath.equals("null")) {
                                Toast.makeText(getApplicationContext(),
                                        "找不到您想要的图片", Toast.LENGTH_SHORT);
                                return;
                            }
                            try {
//                                发送图片消息
                                sendMessage(MessageType.PICTURE_MESSAGE,CommonUtils.getImageBase64(
                                        localSelectPath),localSelectPath);
                                System.out.print("消息发送成功！！！！！！消息发送成功！！！！！！！");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                    break;
                case 3:// 获取个人地理位置信息
                    double latitude = data.getDoubleExtra("x", 0);
                    double longtitude = data.getDoubleExtra("y", 0);
                    String address = data.getStringExtra("address");
                    if (address != null && !address.equals("")) {
                        // sendLocationMessage(address, latitude, longtitude);
                    } else {
                        // ShowToast("无法获取到您的位置信息!");
                    }

                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        messageList.setContent(messageList.getMessage().get(messageList.getMessage().size()-1).getContent());
        messageList.setTitle(friends.getId());
        messageList.setLastTime(messageList.getMessage().get(messageList.getMessage().size()-1).getSendTime());
        messageList.setType(messageList.getMessage().get(messageList.getMessage().size()-1).getType());
        CacUtil.cacheSave(friends.getId(),this,messageList);
        if(bp != null && !bp.isRecycled()){

            // 回收并且置为null
            bp.recycle();
            bp = null;
        }
        MyApplication.getInstance().removeActivity(this);
        System.gc();
    }

    private void sendMessage(int messageType,String content,String photoPath){
        Message message=new Message();
        message.setType(messageType);
        message.setContent(content);
        message.setSender_id(selfUser.getId());
        message.setReceiver_id(friends.getId());
        message.setSendTime(System.currentTimeMillis());
        if (messageType==MessageType.PICTURE_MESSAGE){
            message.setLocalPhoto(photoPath);
        }
        if (messageType==MessageType.VOICE_MESSAGE){
            message.setLocalVoice(mFileName);
        }
        messageList.getMessage().add(message);
        adapter.notifyDataSetChanged();
        client.setMessage(new Gson().toJson(message));
    }
    //    判断网络状态
    private boolean isNetWorkConnected(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
//        返回联网状态，如果有联网信息并且连了网，则返回true
        return (networkInfo!=null&&networkInfo.isConnected());
//        判断wifi连接情况
        /*WifiManager wifimanager= (WifiManager) getSystemService(WIFI_SERVICE);
        wifimanager.isWifiEnabled();
        wifimanager.getWifiState();
*/
    }
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message=(Message) intent.getSerializableExtra("message");
            message.setMessageType(1);
            messageList.getMessage().add(message);
            adapter.notifyDataSetChanged();
        }
    }
}
