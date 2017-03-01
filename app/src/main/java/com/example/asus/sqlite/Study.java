package com.example.asus.sqlite;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/11/23 0023.
 */
public class Study extends IntentService{
//    创建或打开数据库（此处需要使用绝对路径）
    SQLiteDatabase database;
    public Study(){
        super("study");
    }
//    IntentService会使用单独的线程来执行该方法的代码
    @Override
    protected void onHandleIntent(Intent intent){
        database=SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/high.database",null);

    }

}
