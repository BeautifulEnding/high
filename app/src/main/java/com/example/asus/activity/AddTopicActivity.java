package com.example.asus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.adapter.TopicAdapter;
import com.example.asus.adapter.TopicListViewAdapter;
import com.example.asus.he.R;
import com.example.asus.util.LogUtil;
import com.example.asus.util.ToastUtil;

import java.util.ArrayList;

public class AddTopicActivity extends Activity {
//    private LinearLayout updateTopicLayout;
    private String[] topics;
//    颜色资源
    int[] colorRes=new int[]{ R.color.button_bg,R.color.colorAccent,
        R.color.home_weiboitem_title_profile_name,R.color.red,
        R.color.green};
//    返回
    private ImageView cancel;
    private ImageView addTopic;
    private Button saveButton;
//    所有话题组件
//    ArrayList<View> topicViews=new ArrayList<>();
//    是否修改
    private boolean update=false;
//    所有的EditText
//    private ArrayList<EditText> editTexts=new ArrayList<>();
//    private RecyclerView recyclerView;
    private ListView recyclerView;
//    输入话题名
    private EditText editText;
//    private TopicAdapter adapter;
    private TopicListViewAdapter adapter;
    private View replaceName;
    private String editTextValue;
    private ArrayList<String> topicsList=new ArrayList<>();
//    同名
    private boolean sameName=false;
//    查看所有已存在话题
    private TextView findAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        MyApplication.getInstance().addActivity(this);
        topics=getIntent().getStringArrayExtra("topics");
        for (int i=1;i<topics.length-1;i++){
            topicsList.add(topics[i]);
        }
        initView();
        initRecyclerView();
        initEvent();
    }
    private void initView(){
//        updateTopicLayout=(LinearLayout)findViewById(R.id.update_topic);
//        recyclerView=(RecyclerView)findViewById(R.id.topic);
        recyclerView=(ListView) findViewById(R.id.topic);
        editText=(EditText)findViewById(R.id.topic_name);
        replaceName=findViewById(R.id.replace_name);
        cancel=(ImageView)findViewById(R.id.cancel);
        addTopic=(ImageView)findViewById(R.id.add_topic);
        saveButton=(Button)findViewById(R.id.save);
        findAll=(TextView)findViewById(R.id.find_all_topic);
    }
    private void initRecyclerView(){
//        adapter=new TopicAdapter(this,topicsList);
        adapter=new TopicListViewAdapter(this,topicsList);
//        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        RecyclerViewUtils.setHeaderView(mRecyclerView, new HomeHeadView(getActivity()));
        registerForContextMenu(recyclerView);
    }
    private void initEvent(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTopicActivity.this.setResult(0);
                MyApplication.getInstance().removeActivity(AddTopicActivity.this);
                finish();
            }
        });
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceName.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                editText.setText("");
//                editText.setHint("请在此处输入话题名称");
                editText.requestFocus();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                保存修改
                LogUtil.e("topListSize"+topicsList.size());
                String[] topics=new String[topicsList.size()+2];
                LogUtil.e("topicsLength"+topics.length);
                topics[0]="所有";
                topics[topics.length-1]="自定义";
                for (int i=1;i<topics.length-1;i++){
                    topics[i]=topicsList.get(i-1);
                }
                Intent intent=new Intent();
                intent.putExtra("topics",topics);
                AddTopicActivity.this.setResult(1,intent);
                MyApplication.getInstance().removeActivity(AddTopicActivity.this);
                finish();
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null && event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    editTextValue=editText.getText().toString();
                    for (int i=0;i<topicsList.size();i++){
                        if (editTextValue.equals(topicsList.get(i))){
                            sameName=true;
                        }
                    }
                    if (sameName){
                        ToastUtil.show(AddTopicActivity.this,"撞名了，尴尬！", Toast.LENGTH_SHORT);
                    }else {
                        topicsList.add(editTextValue);
                        adapter.notifyDataSetChanged();
                        editText.setVisibility(View.GONE);
                        replaceName.setVisibility(View.VISIBLE);
                        if (!update){
                            update=true;
                            saveButton.setBackground(getResources().getDrawable(R.drawable.sure_button_bg));
                            saveButton.setEnabled(true);
                        }
                    }
                }
                return false;
            }
        });

        findAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                弹出一个新的popupWindow

            }
        });
    }
    /***
     * 注册上下文菜单
     * @param menu
     * @param source
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu,View source,ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.delete_topic_menu,menu);
        menu.setHeaderTitle("话题");
    }

    /***
     * 单击菜单项时触发该方法
     * @param menuItem
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        LogUtil.e("正在执行onContextItemSelected方法");
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        if (!update){
            update=true;
            saveButton.setBackground(getResources().getDrawable(R.drawable.sure_button_bg));
            saveButton.setEnabled(true);
        }
        switch (menuItem.getItemId()){
            case R.id.totop:
//                置顶
                String string=topicsList.remove(info.position);
                topicsList.add(0,string);
                break;
            case R.id.delete:
//                删除
                topicsList.remove(info.position);
                break;
            case R.id.deleteAll:
//                删除所有
                topicsList.removeAll(topicsList);
                break;
            default:
                LogUtil.e("menuItem.getItemId"+menuItem.getItemId());
                topicsList.remove(info.position);
                break;
        }
        adapter.notifyDataSetChanged();
        return true;
    }
}
