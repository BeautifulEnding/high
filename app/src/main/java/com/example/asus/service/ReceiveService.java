package com.example.asus.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.asus.client.ClientThread;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
