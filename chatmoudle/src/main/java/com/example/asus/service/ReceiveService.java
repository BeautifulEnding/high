package com.example.asus.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class ReceiveService extends IntentService {
    public ReceiveService(){
        super("ReceiveService");
    }
    @Override
    protected void onHandleIntent(Intent intent){
    }
    public void sendMessage(Intent intent){

    }
}
