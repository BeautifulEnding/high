package com.example.asus.ui;

import android.content.Context;

import com.example.asus.entity.Content;
/**
 * Created by wenmingvs on 16/6/26.
 */
public interface DetailActivityPresent {
    public void pullToRefreshData(int groupId, Content status, Context context);

    public void requestMoreData(int groupId, Content status, Context context);
}
