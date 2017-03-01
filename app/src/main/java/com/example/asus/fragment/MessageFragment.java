package com.example.asus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.asus.activity.MainActivity;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;

public class MessageFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//        其中的container为fragment所依附的容器，当fragment在activity中出现时，container就不会为空，在这container
//        为mainLayout中的frameLayout
//        container不为空时，false，表示以container的width和height来为资源布局
//若为true，则是将布局作为view添加到container中
       View rootView=inflater.inflate(R.layout.message_fragment_layout,container,false);
       return rootView;
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.currFragTag =Constant.FRAGMENT_FLAG_MESSAGE;
    }

}
