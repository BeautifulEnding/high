package com.example.asus.util;

import com.example.asus.activity.MainActivity;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class TagUtil{
    private static int itemId;
    public static int changeTagToId(){
        itemId=-1;
        //激发callbacks的方法
        switch (MainActivity.preFragTag){
            case com.example.asus.constant.Constant.FRAGMENT_FLAG_MESSAGE:
                itemId= com.example.asus.constant.Constant.BTN_FLAG_MESSAGE;
                break;
            case com.example.asus.constant.Constant.FRAGMENT_FLAG_SEARCH:
                itemId= com.example.asus.constant.Constant.BTN_FLAG_SEARCH;
                break;
            case com.example.asus.constant.Constant.FRAGMENT_FLAG_SHOW:
                itemId= com.example.asus.constant.Constant.BTN_FLAG_SHOW;
                break;
            case com.example.asus.constant.Constant.FRAGMENT_FLAG_SETTING:
                itemId= com.example.asus.constant.Constant.BTN_FLAG_SETTING;
                break;
        }
        return itemId;
    }
}
