package com.example.asus.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.asus.he.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class GridviewImageAdapter extends BaseAdapter{
    private Context mContext;
    private SharedPreferences auPre;
    private int position;
    public GridviewImageAdapter(Context context,int position){
        Log.e("GridviewImageAdapter","正在执行GridviewImageAdapter---------------------------");
        mContext=context;
        auPre=context.getSharedPreferences("content",Context.MODE_PRIVATE);
        this.position=position;
    }
    public int getCount(){
        Log.e("图片数量",auPre.getInt("auction"+position+"photoNum",0)+"--------------");
        return auPre.getInt("auction"+position+"photoNum",0);
    }
    @Override
    public String getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.gridview_item,null,false).findViewById(R.id.image);
            Drawable drawable=Drawable.createFromPath(auPre.getString("auction"+this.position+"image"+(position+1),null));
            Log.e("gridViewImageAdapter","auction"+this.position+"image"+(position+1)+"---------------");
            if (drawable==null){
                convertView.setBackground(mContext.getDrawable(R.drawable.loading));
            }else {
                convertView.setBackground(drawable);
            }
        }
        return convertView;
    }
}
