package com.example.asus.broadreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.asus.activity.MainActivity;
import com.example.asus.activity.MyApplication;
import com.example.asus.client.entity.Message;
import com.example.asus.constant.Constant;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class AddFriendReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        MainActivity activity=(MainActivity) MyApplication.getInstance().getActivity();
        activity.changeFragment(Constant.BTN_FLAG_SEARCH);
        activity.newsFragment.updateRecyclerView((Message) intent.getSerializableExtra("message"));
    }
}
