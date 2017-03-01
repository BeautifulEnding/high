package com.example.asus.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.constant.Constant;
import com.example.asus.he.R;

public class ImageText extends LinearLayout{
    //一个ImageText就是一个单独的布局
	private Context mContext = null;
	private ImageView mImageView = null;
	private TextView mTextView = null;
	private final static int DEFAULT_IMAGE_WIDTH = 64;
	private final static int DEFAULT_IMAGE_HEIGHT = 64;
	private int CHECKED_COLOR = Color.rgb(29, 118, 199); //选中蓝色
    private int UNCHECKED_COLOR = Color.GRAY;   //没选中自然灰
	public ImageText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public ImageText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
        //获取布局填充，根文件为this
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //加载布局文件
		View parentView = inflater.inflate(R.layout.image_text_layout, this,true);
		mImageView = (ImageView)findViewById(R.id.image_iamge_text);
		mTextView = (TextView)findViewById(R.id.text_iamge_text);
	}
	public void setImage(int id){
		if(mImageView != null){
			mImageView.setImageResource(id);
			setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		}
	}

	public void setText(String s){
		if(mTextView != null){
			mTextView.setText(s);
			mTextView.setTextColor(UNCHECKED_COLOR);
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}
	private void setImageSize(int w, int h){
		if(mImageView != null){
            //得到布局参数（大小）
            //设置图像的布局参数（大小）
			ViewGroup.LayoutParams params = mImageView.getLayoutParams();
			params.width = w;
			params.height = h;
			mImageView.setLayoutParams(params);
		}
	}
    //设置按钮被选中
	public void setChecked(int itemID){
        //更改选中按钮的颜色及图片
		if(mTextView != null){
			mTextView.setTextColor(CHECKED_COLOR);
		}
        //定义选中的图像的ID
		int checkDrawableId = -1;
		switch (itemID){
        case Constant.BTN_FLAG_SHOW:
             checkDrawableId = R.drawable.show_selected;
             break;
		case Constant.BTN_FLAG_MESSAGE:
			checkDrawableId = R.drawable.message_selected;
			break;
		case Constant.BTN_FLAG_SEND:
			checkDrawableId = R.drawable.send_selected;
			break;
        case Constant.BTN_FLAG_SEARCH:
            checkDrawableId = R.drawable.search_selected;
            break;
		case Constant.BTN_FLAG_SETTING:
			checkDrawableId = R.drawable.me_selected;
			break;
		default:break;
		}
		if(mImageView != null){
			mImageView.setImageResource(checkDrawableId);
		}
	}


	


}
