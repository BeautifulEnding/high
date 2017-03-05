package com.example.asus.util;

import android.content.Context;
import android.widget.Toast;

import com.example.asus.client.entity.User;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public class UserUtil {
    public static User getUser(String user_id, Context context){
        User user=CacheUtil.cacheLoad(user_id,context);
        if (user==null){
            user=findUser(user_id);
            if (user!=null){
                CacheUtil.cacheSave(user.getId(),context,user);
                return user;
            }else{
                ToastUtil.show(context,"该用户不存在", Toast.LENGTH_SHORT);
            }
        }else{
            return user;
        }
        return null;
    }

    public static User findUser(String name){
        String url=HttpUtil.BASE_URL+"findFriend.jsp?name="+name;
        try {
            String result=HttpUtil.getRequest(url);
            if (result!=null){
                JSONObject object=new JSONObject(result);
                User user=new User();
                user.setId(object.getString("user_id"));
                user.setScreen_name(object.getString("user_name"));
                user.setDescription(object.getString("user_self"));
                user.setProfile_image_url(object.getString("user_photo"));
                user.setGender(object.getString("user_sex"));
                user.setTelPhone(object.getString("user_tel"));
                return user;
            }
        }catch (Exception e){
            LogUtil.e("用户异常"+e.getMessage());
        }
        return null;
    }
}
