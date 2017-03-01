package com.example.asus.File;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class SavePhoto{
    public static final String SD_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/high";
    public static final String PHOTO_PATH= SD_PATH+"/photo";
    public static final String USER_PHOTO_PATH=SD_PATH+"/user_photo";
    //定义图片
    public SavePhoto(Bitmap photo,String path)throws IOException{
        File preFile=new File(SD_PATH);
        if (!preFile.exists()) {
            //如果文件不存在，创建该文件
            preFile.mkdir();
        }
        File prefile2=new File(PHOTO_PATH);
        if (!prefile2.exists()) {
            //如果文件不存在，创建该文件
            prefile2.mkdir();
        }
        File preFile3=new File(USER_PHOTO_PATH);
        if (!preFile3.exists()){
            preFile3.mkdir();
        }
        File file=new File(SD_PATH+path);
        //得到图片的输出流
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
        //将图片按原质量压缩到指定文件夹
        photo.compress(Bitmap.CompressFormat.PNG,100,bos);
        bos.flush();
        //关闭输出流
        bos.close();
    }
}
