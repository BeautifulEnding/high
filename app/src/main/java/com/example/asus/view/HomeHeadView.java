package com.example.asus.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.asus.he.R;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeHeadView extends RelativeLayout {

    public HomeHeadView(Context context,String style ) {
        super(context);
        if (style.equals("search")){
            initSearch(context);
        }else{
            initNoContent(context);
        }

    }

    public HomeHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSearch(context);
    }

    public HomeHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSearch(context);
    }

    public void initSearch(Context context) {
        inflate(context, R.layout.headview_homefragment, this);
    }

    public void initNoContent(Context context){
        inflate(context,R.layout.no_content_headview_showfragment,null);
    }
}
