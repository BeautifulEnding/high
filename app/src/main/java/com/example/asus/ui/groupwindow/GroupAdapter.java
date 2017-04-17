package com.example.asus.ui.groupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.he.R;
import com.example.asus.util.LogUtil;

import java.util.ArrayList;
public class GroupAdapter extends BaseAdapter {

    private ArrayList<String> mDatas = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private IGroupItemClick mIGroupItemClick;
    private static final int ITEM_TYPE_HEAD_FIRST = 0;
    private static final int ITEM_TYPE_HEAD_SECOND = 1;
    private static final int ITEM_TYPE_GROUP = 2;
    private ArrayList<Boolean> mSelectList = new ArrayList<Boolean>();
    private ArrayList<String> datas;

    public GroupAdapter(Context context, ArrayList<String> mDatas) {
        this.mDatas = mDatas;
        initSelectList();
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
            return ITEM_TYPE_GROUP;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FirstHeadHolder firstHeadHolder = null;
        SecondHeadHolder secondHeadHolder = null;
        GroupViewHolder groupViewHolder = null;
        final int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case ITEM_TYPE_HEAD_FIRST:
                    convertView = layoutInflater.inflate(R.layout.home_grouplist_pop_firstheadview, parent, false);
                    firstHeadHolder = new FirstHeadHolder(convertView);
                    convertView.setTag(firstHeadHolder);
                    break;
                case ITEM_TYPE_HEAD_SECOND:
                    convertView = layoutInflater.inflate(R.layout.home_grouplist_pop_secondheadview, parent, false);
                    secondHeadHolder = new SecondHeadHolder(convertView);
                    convertView.setTag(secondHeadHolder);
                    break;
                case ITEM_TYPE_GROUP:
                    convertView = layoutInflater.inflate(R.layout.home_grouplist_pop_item, parent, false);
                    groupViewHolder = new GroupViewHolder(convertView);
                    convertView.setTag(groupViewHolder);
            }
        } else {
            switch (type) {
                case ITEM_TYPE_HEAD_FIRST:
                    firstHeadHolder = (FirstHeadHolder) convertView.getTag();
                    break;
                case ITEM_TYPE_HEAD_SECOND:
                    secondHeadHolder = (SecondHeadHolder) convertView.getTag();
                    break;
                case ITEM_TYPE_GROUP:
                    groupViewHolder = (GroupViewHolder) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case ITEM_TYPE_HEAD_FIRST:
                firstHeadHolder.home.setSelected(mSelectList.get(0));
                firstHeadHolder.home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIGroupItemClick.onGroupItemClick(position, 0, "所有");
                        deSelectAll();
                        ((TextView) v).setSelected(true);
                        mSelectList.set(0, true);
                        notifyDataSetChanged();
                    }
                });
                break;
            case ITEM_TYPE_HEAD_SECOND:
                secondHeadHolder.friendCircle.setSelected(mSelectList.get(1));
                secondHeadHolder.friendCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIGroupItemClick.onGroupItemClick(position, 1, "帮助");
                        deSelectAll();
                        ((LinearLayout) v).setSelected(true);
                        mSelectList.set(1, true);
                        notifyDataSetChanged();
                    }
                });
                break;

            case ITEM_TYPE_GROUP:
                LogUtil.e("GroupPosition:"+position);
                groupViewHolder.groupitem.setText(mDatas.get(position));
                if (mSelectList.size() > 2 && mSelectList.get(position).compareTo(Boolean.FALSE) == 0) {
                    LogUtil.e("setSelected(false)");
                    groupViewHolder.groupitem.setSelected(false);
                } else {
                    LogUtil.e("setSelected(true)");
                    groupViewHolder.groupitem.setSelected(true);
                }
                groupViewHolder.groupitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LogUtil.e(position+Long.valueOf(mDatas.get(position))+mDatas.get(position));
                        mIGroupItemClick.onGroupItemClick(position,2, mDatas.get(position));
                        deSelectAll();
                        ((TextView) v).setSelected(true);
                        mSelectList.set(position, true);
                        notifyDataSetChanged();
                    }
                });
                break;
        }
        return convertView;
    }


    public void setOnGroupItemClickListener(IGroupItemClick groupItemClickListener) {
        this.mIGroupItemClick = groupItemClickListener;
    }

    private void deSelectAll() {
        for (int i = 0; i < mSelectList.size(); i++) {
            mSelectList.set(i, false);
        }
    }


    public void initSelectList() {
        if (mSelectList.size() == mDatas.size()) {
            return;
        }
        if (mDatas.size() > 0 && mSelectList.size() < mDatas.size()) {
            mSelectList.clear();
            for (int i = 0; i < mDatas.size(); i++) {
                if (i == 0) {
                    mSelectList.add(i, true);
                } else {
                    mSelectList.add(false);
                }
            }
        }
    }

    public void setDatas(ArrayList<String> datas) {
        this.datas = datas;
        initSelectList();
    }

    public class FirstHeadHolder {
        public TextView home;

        public FirstHeadHolder(View view) {
            home = (TextView) view.findViewById(R.id.allweibo);
        }
    }

    public class SecondHeadHolder {
        public TextView home;
        public LinearLayout friendCircle;

        public SecondHeadHolder(View view) {
            home = (TextView) view.findViewById(R.id.allweibo);
            friendCircle = (LinearLayout) view.findViewById(R.id.bestfriend);
        }
    }

    protected class GroupViewHolder {
        private TextView groupitem;
        public GroupViewHolder(View view) {
            groupitem = (TextView) view.findViewById(R.id.groupitem);
        }
    }


}
