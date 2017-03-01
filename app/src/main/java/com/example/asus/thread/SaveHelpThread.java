package com.example.asus.thread;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.example.asus.File.SavePhoto;
import com.example.asus.fragment.ShowFragment;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/2/3 0003.
 */

public class SaveHelpThread extends Thread{
    private SharedPreferences contentPre;
    private SharedPreferences.Editor contentEditor;
//    private List<JSONObject> tempAuctions= ShowFragment.tempAuctions;
    private Handler handler;
    private Context mContext;
//    private double tag=ShowFragment.tag;
    public SaveHelpThread(Handler handler, Context context) throws Exception{
        this.handler=handler;
        mContext=context;
        contentPre=context.getSharedPreferences("content",Context.MODE_PRIVATE);
        contentEditor=contentPre.edit();
    }
    @Override
    public void run(){
        int flag=contentPre.getInt("flag",0);
       /* if (contentPre.getInt("size",0)+tempAuctions.size()>10){
            contentEditor.putInt("size",10);
        }else {
            contentEditor.putInt("size",contentPre.getInt("size",0)+tempAuctions.size());
        }
        for (int i=0;i<tempAuctions.size();i++) {
            try {
                contentEditor.putString("tag",tempAuctions.get(tempAuctions.size()-1).getString("tag"));
                Log.e("saveHelpThread",tempAuctions.get(tempAuctions.size()-1).getString("tag")+"-------");
                JSONObject object = tempAuctions.get(i);
                contentEditor.putString("auction" + flag + "id", object.getString("id"));
                contentEditor.putString("auction" + flag + "content", object.getString("content"));
                int j = streamToBitmap(object, "auction" + flag);
                contentEditor.putString("auction" + flag + "tag", object.getString("tag"));
                contentEditor.putInt("auction" + flag + "photoNum", j);
                flag = (flag + 1) % 10;
            } catch (Exception e) {
                Log.e("jsonException", e.getMessage());
            }*/
            contentEditor.putInt("flag",flag);
            contentEditor.commit();
            Log.e("成功保存数据","成功保存数据到手机中");
//        }

    }
    private int streamToBitmap(JSONObject jsonObj,String hostName) throws Exception{
        String name=null;
        int j=0;
        for (int i=0;i<9;i++) {
            name="image"+i;
            if (!jsonObj.has(name)){
                continue;
            }
            j++;
            String photoStream = jsonObj.getString(name);
            ByteArrayInputStream user_photo = new ByteArrayInputStream(Base64.decode(photoStream.getBytes(), Base64.DEFAULT));
            Bitmap bitmap= BitmapFactory.decodeStream(user_photo);
            new SavePhoto(bitmap,"/photo/"+hostName+name+".png");
            contentEditor.putString(hostName+name,SavePhoto.PHOTO_PATH+"/"+hostName+name+".png");
        }
        return j;
    }
}
