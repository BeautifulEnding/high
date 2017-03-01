package com.example.asus.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.asus.he.R;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class OvalIamgeView extends ImageView {
    //创建并定义画笔
    Paint paint=new Paint();
   public OvalIamgeView(Context context){
        super(context);
   }
    public  OvalIamgeView(Context context ,AttributeSet set){
        super(context,set);
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
//        设施画笔的颜色
        paint.setColor(getResources().getColor(R.color.bg_color));
    }
}
