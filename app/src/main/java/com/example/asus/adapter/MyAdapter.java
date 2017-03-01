package com.example.asus.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.asus.he.R;
import com.example.asus.util.ViewHolder;


public class MyAdapter extends CommonAdapter<String>
{

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    private Button complish=null;
    private int num=0;
    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath,Button complish,int num)
    {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.complish=complish;
        this.num=num;
    }

    @Override
    public void convert(final ViewHolder helper, final String item)
    {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener()
        {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v)
            {

                    // 已经选择过该图片
                    if (mSelectedImage.contains(mDirPath + "/" + item))
                    {
                        mSelectedImage.remove(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.picture_unselected);
                        mImageView.setColorFilter(null);
                    } else
                    // 未选择该图片
                    {
                        if (mSelectedImage.size()<num){
                            mSelectedImage.add(mDirPath + "/" + item);
                            mSelect.setImageResource(R.drawable.pictures_selected);
                            mImageView.setColorFilter(Color.parseColor("#77000000"));
                        }

                    }
                if (!MyAdapter.mSelectedImage.isEmpty()){
                    complish.setText("完成"+MyAdapter.mSelectedImage.size()+"/"
                            +num);
                    complish.setEnabled(true);
                    complish.setBackgroundResource(R.drawable.button_enable);
                }
                else{
                    complish.setEnabled(false);
                    complish.setBackgroundResource(R.drawable.button_unenable);
                    complish.setText("完成");
                }

                }

        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item))
        {
        mSelect.setImageResource(R.drawable.pictures_selected);
        mImageView.setColorFilter(Color.parseColor("#77000000"));
        }

        }
        }
