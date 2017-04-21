package com.example.asus.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.asus.adapter.HelpAdapter;
import com.example.asus.adapter.SimpleAdapter;
import com.example.asus.client.entity.MessageList;
import com.example.asus.he.R;
import com.example.asus.ui.HeaderAndFooterRecyclerViewAdapter;
import com.example.asus.util.CacUtil;
import com.example.asus.util.RecyclerViewUtils;
import com.example.asus.view.HomeHeadView;

import org.w3c.dom.Text;

public class HelpMessageActivity extends Activity {
    private HelpAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noMessageView;
    private MessageList messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_help_message);
        messageList=MessageList.parse(CacUtil.cacheLoad(0,this,"helpMessage"));
        noMessageView=(TextView)findViewById(R.id.noHelp);
        if (messageList!=null){
            noMessageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        initRecyclerView();
    }

    public void initRecyclerView() {
        recyclerView=(RecyclerView)findViewById(R.id.help_message);
        adapter=new HelpAdapter(messageList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
