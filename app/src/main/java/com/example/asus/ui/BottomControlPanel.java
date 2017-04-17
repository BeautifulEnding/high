package com.example.asus.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.asus.constant.Constant;
import com.example.asus.he.R;

public class BottomControlPanel extends RelativeLayout implements View.OnClickListener{
	private Context mContext;
    private ImageText mShowBtn = null;
	private ImageText mMsgBtn = null;
    private ImageText mSendBtn=null;
	private ImageText mSearchBtn = null;
	private ImageText mSettingBtn = null;
    //设置默认背景颜色
//	private int DEFALUT_BACKGROUND_COLOR = Color.rgb(243, 243, 243); //Color.rgb(192, 192, 192)
    //定义BottomPanelCallback回调接口
	private BottomPanelCallback mBottomCallback = null;
    //定义ImageText集合
	private List<ImageText> viewList = new ArrayList<ImageText>();

	public interface BottomPanelCallback{
        //接口必须实现的方法
		public void onBottomPanelClick(int itemId);
	}
	public BottomControlPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onFinishInflate() {
        //当完成填充布局时，加载布局中的View
		// TODO Auto-generated method stub
		mMsgBtn = (ImageText)findViewById(R.id.btn_message);
		mShowBtn = (ImageText)findViewById(R.id.btn_show);
		mSearchBtn = (ImageText)findViewById(R.id.btn_search);
		mSettingBtn = (ImageText)findViewById(R.id.btn_setting);
        mSendBtn=(ImageText)findViewById(R.id.btn_send);
//		setBackgroundColor(DEFALUT_BACKGROUND_COLOR);
        viewList.add(mShowBtn);
        viewList.add(mMsgBtn);
        viewList.add(mSendBtn);
		viewList.add(mSearchBtn);
		viewList.add(mSettingBtn);
	}
    public void initBottomPanel(){
        //初始化底部容器
        if(mShowBtn != null){
            //设置每个ImageText的图像和文字
            mShowBtn.setImage(R.drawable.show_unselected);
            mShowBtn.setText("首页");
        }
        if(mMsgBtn != null){
            mMsgBtn.setImage(R.drawable.message_unselected);
            mMsgBtn.setText("消息");
        }
        if(mSendBtn != null){
            mSendBtn.setImage(R.drawable.send_selected);
            mSendBtn.setText("发布");
        }
        if(mSearchBtn != null){
            mSearchBtn.setImage(R.drawable.search_unselected);
            mSearchBtn.setText("好友");
        }
        if(mSettingBtn != null){
            mSettingBtn.setImage(R.drawable.me_unselected);
            mSettingBtn.setText("我");
        }
        setBtnListener();

    }
	private void setBtnListener(){
        //为每个ImageText设置事件监听器
		int num = this.getChildCount();
		for(int i = 0; i < num; i++){
			View v = getChildAt(i);
			if(v != null){
				v.setOnClickListener(this);
			}
		}
	}
    //设置底部容器回调对象
	public void setBottomCallback(BottomPanelCallback bottomCallback){
		mBottomCallback = bottomCallback;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		initBottomPanel();
		int index = -1;
		switch(v.getId()){
		case R.id.btn_show:
			index = Constant.BTN_FLAG_SHOW;
			mShowBtn.setChecked(Constant.BTN_FLAG_SHOW);
			break;
        case R.id.btn_message:
            index = Constant.BTN_FLAG_MESSAGE;
            mMsgBtn.setChecked(Constant.BTN_FLAG_MESSAGE);
            break;
        case R.id.btn_send:
             index = Constant.BTN_FLAG_SEND;
             mSendBtn.setChecked(Constant.BTN_FLAG_SEND);
             break;
		case R.id.btn_search:
			index = Constant.BTN_FLAG_SEARCH;
			mSearchBtn.setChecked(Constant.BTN_FLAG_SEARCH);
			break;
		case R.id.btn_setting:
			index = Constant.BTN_FLAG_SETTING;
			mSettingBtn.setChecked(Constant.BTN_FLAG_SETTING);
			break;
		default:break;
		}
		if(mBottomCallback != null){
            //如果实现该接口的对象不为空，则该对象负责处理按钮的点击事件，在这为MainActivity
			mBottomCallback.onBottomPanelClick(index);
		}
	}
	public void defaultBtnChecked(){
        //设置默认选中的按钮为show
		if(mShowBtn != null){
			mShowBtn.setChecked(Constant.BTN_FLAG_SHOW);
		}
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}
    public void changeButtonColor(int itemId){
        switch (itemId){
            case Constant.BTN_FLAG_MESSAGE:
                mMsgBtn.setChecked(itemId);
                break;
            case Constant.BTN_FLAG_SETTING:
                mSettingBtn.setChecked(itemId);
                break;
            case Constant.BTN_FLAG_SEARCH:
               mSearchBtn.setChecked(itemId);
                break;
            case Constant.BTN_FLAG_SHOW:
                mShowBtn.setChecked(itemId);
                break;
        }
    }

}
