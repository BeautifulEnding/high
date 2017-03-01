package com.example.asus.File;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class SaveFileBase {
    //定义app文件保存路径
    //先得到手机自身的SD卡路径
    public static final String SD_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/high";
    public static void saveBitmap(ByteArrayOutputStream stream,String path){
        File prefile2=new File(SavePhoto.PHOTO_PATH);
        if (!prefile2.exists()) {
            //如果文件不存在，创建该文件
            prefile2.mkdir();
        }
        File file=new File(SavePhoto.PHOTO_PATH+path);
        try {
            stream.writeTo(new FileOutputStream(file));
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
