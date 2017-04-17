package com.example.asus.fragment;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.activity.MainActivity;
import com.example.asus.adapter.SimpleAdapter;
import com.example.asus.constant.Constant;
import com.example.asus.entity.Content;
import com.example.asus.entity.ContentList;
import com.example.asus.he.R;
import com.example.asus.ui.HeaderAndFooterRecyclerViewAdapter;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.DensityUtil;
import com.example.asus.util.DownloadUtil;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.RecyclerViewUtils;
import com.example.asus.util.ToastUtil;
import com.example.asus.view.HomeFragmentView;
import com.example.asus.view.HomeHeadView;
import com.example.asus.widget.EndlessRecyclerOnScrollListener;
import com.example.asus.widget.LoadingFooter;
import com.example.asus.widget.RecyclerViewStateUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13 0013.
 */
public class ShowFragment extends BaseFragment implements HomeFragmentView{
    HomeFragmentView homeFragmentView;
//    private  EndlessRecyclerOnScrollListener mOnScrollListener
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    RecyclerView mRecyclerView=null;
//    记录从服务器得到的值
    private List<JSONObject> objects=null;
//    记录content对象
    private ArrayList<Content> contents=new ArrayList<>();
//    private Spinner spinner=null;
    private TextView topicNameView;
    public static long tag;
    public List<Long> topicsTag=new ArrayList<>();
    private SharedPreferences loginPre;
    private SharedPreferences.Editor contentEditor;
    private SharedPreferences contentPre;
    private SharedPreferences tagPre;
    private SharedPreferences.Editor tagEditor;
//    是否发布了内容
    private boolean haveContent=false;
//    刷新布局
    private SwipeRefreshLayout mSwipeRefreshLayout;
//    话题
//    private String topic;
//    适配器
    private SimpleAdapter mAdapter=null;
//    private Handler mHandler;
    private MainActivity mActivity;
    private TextView noContentView;
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.showfragment_layout, container, false);
        homeFragmentView=this;
        loginPre=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
//        contentPre中只存放了tag的值
        contentPre=getActivity().getSharedPreferences("content",Context.MODE_PRIVATE);
        contentEditor=contentPre.edit();
        tagPre=mActivity.getSharedPreferences("tag",Context.MODE_PRIVATE);
        tagEditor=tagPre.edit();
//        得到tag的值
//        tag=Long.parseLong(contentPre.getString("tag",0+""));
        tag=contentPre.getLong("tag",0);
//        LogUtil.e("tag---------------------->"+tag);
//        spinner=(Spinner) getActivity().findViewById(R.id.spinner);
        topicNameView=(TextView)getActivity().findViewById(R.id.name);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_widget);
        noContentView=(TextView) rootView.findViewById(R.id.noContent);
//        初始化RecycleView
        initRecyclerView();
//        初始化刷新的布局
        initRefreshLayout();
//        判断第一次初始化时是否需要刷新
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
//                如果本地存在所有话题的缓存
                if (tag!=0) {
//                    本地存在缓存，直接加载
//                   boolean cache= cacheLoad(spinner.getSelectedItem().toString(),getActivity());
                    boolean cache= CacheUtil.cacheLoad(topicNameView.getText().toString(),mActivity,contents,homeFragmentView);
                    if (!cache){
//                        如果没有缓存，刷新数据
                        refreshData();
                    }else{
                        mRecyclerView.setVisibility(View.VISIBLE);
                        noContentView.setVisibility(View.GONE);
                    }
                } else {
//                    需要刷新
                    if (contentPre.getBoolean("haveContent",false)){
                        refreshData();
                    }else{
                        //没有人发布内容，显示头
                        /*TextView textView=new TextView(getActivity());
                        textView.setText("还没有人发布内容");
                        textView.setTextSize(22);
                        mHeaderAndFooterRecyclerViewAdapter.addHeaderView(textView);*/
//                        RecyclerViewUtils.setHeaderView(mRecyclerView, new HomeHeadView(getActivity(),"noContent"));
                        mRecyclerView.setVisibility(View.GONE);
                        noContentView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return rootView;
    }

    public void initRecyclerView() {
        mAdapter=new SimpleAdapter(getActivity(),contents);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new HomeHeadView(getActivity(),"search"));
    }


    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                向服务器提交刷新请求，并更新tag值，确定haveContent
//                保存缓存到本机中
                refreshData();
                LogUtil.e("刷新成功");
            }
        });
        mSwipeRefreshLayout.setProgressViewOffset(false, DensityUtil.dp2px(getActivity(), 10), DensityUtil.dp2px(getActivity(), 10 + 65));
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.what==mActivity.UPDATE){
//
                }
            }
        };*/
    }
    //当该Fragment从它所属的activity中被删除时回调该方法
    @Override
    public void onDetach(){
        LogUtil.e("正在执行onDetach方法");
        super.onDetach();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mActivity=(MainActivity)activity;
//        mActivity.setHandler(mHandler);
    }
    @Override
    public void onResume(){
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_SHOW;
    }
    private List<JSONObject> receiveContent(){
        String topic=topicNameView.getText().toString();
        String url="";
        if (topic.equals("所有")){
            url= HttpUtil.BASE_URL+"receiveContent.jsp?topic="+topic+"&tag="+tag;
        }else{
            url= HttpUtil.BASE_URL+"receiveContent.jsp?topic="+topic+"&tag="+tagPre.getLong(topic,0);
        }
        try {
            return DownloadUtil.downloadContent(url);
        }catch (Exception e){
            Log.e("ReceiveException",e.getMessage());
        }
        return null;
    }

    private void refreshData(){
        LogUtil.e("正在刷新");
//向服务器发送请求,得到服务器传送过来的JSONObject对象
        homeFragmentView.showLoadingIcon();
        objects=receiveContent();
        if (objects!=null){
            LogUtil.e("object size----------->"+objects.size()+"");
            try{
                if (objects.size()==1 && objects.get(0).has("update")){
                    if (objects.get(0).getString("update").equals("noUpdate")){
                        noUpdate();
                    }else {
                        noContent();
                    }
                }else{
                        LogUtil.e("当前话题有更新");
                        getContentList();
                }
            }catch (Exception e){
                homeFragmentView.hideLoadingIcon();
                LogUtil.e("捕获到异常"+e.getMessage());
            }
        }
        homeFragmentView.hideLoadingIcon();
    }
    /**
     * 把列表滑动到顶部，refreshDrata为true的话，会同时获取更新的数据
     *
     * @param refreshData
     */
    @Override
    public void scrollToTop(boolean refreshData) {
        mRecyclerView.scrollToPosition(0);
        if (refreshData) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
//                    刷新
                    refreshData();
                }
            });
        }
    }

    @Override
    public void showRecyclerView() {
        if (mSwipeRefreshLayout.getVisibility() != View.VISIBLE) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideRecyclerView() {
        if (mSwipeRefreshLayout.getVisibility() != View.GONE) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyBackground(String text) {

    }

    @Override
    public void hideEmptyBackground() {

    }

    @Override
    public void popWindowsDestory() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


    @Override
    public void updateListView(ArrayList<Content> statuselist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        contents= statuselist;
        mAdapter.setData(statuselist);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoadingIcon() {

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, contents.size(), LoadingFooter.State.Loading, null);
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }


    @Override
    public void setGroupName(String userName) {

    }

    @Override
    public void setUserName(String userName) {
    }

//滚动监听，发生滚动时触发该事件
    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (contents != null && contents.size() > 0) {
                showEndFooterView();
//                加载更多的内容
                LogUtil.e("正在加载更多的内容");
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    /***
     * 改变spinner中的值时更新showFragment中的recyclerView
     */
    public void updateRecyclerView(){
//        更新recyclerView中的数据
        LogUtil.e("正在更新listView");
        mRecyclerView.removeAllViews();
        contents.clear();
        mAdapter.notifyDataSetChanged();
//        boolean cache=cacheLoad(spinner.getSelectedItem().toString(),mActivity);
        boolean cache= CacheUtil.cacheLoad(topicNameView.getText().toString(),mActivity,contents,homeFragmentView);
        if (!cache){
            LogUtil.e("正在加载没有内容的headView");
            /*TextView view=new TextView(mActivity);
            view.setText("还没有人发布该话题的内容");
            ViewGroup.LayoutParams params=view.getLayoutParams();
            params.width= WindowManager.LayoutParams.MATCH_PARENT;
            params.height= WindowManager.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
            view.setGravity(Gravity.CENTER);*/
            /*View view=View.inflate(mActivity,R.layout.no_content_headview_showfragment,null);
            //            RecyclerViewUtils.setHeaderView(mRecyclerView,View.inflate(mActivity,R.layout.no_content_headview_showfragment,null));
            mHeaderAndFooterRecyclerViewAdapter.addHeaderView(view);
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.green));*/
            if (noContentView.getVisibility()==View.GONE){
                noContentView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }else{
            if (noContentView.getVisibility()==View.VISIBLE){
                noContentView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
//                    refreshData();
    }

    private void noUpdate(){
        LogUtil.e("当前内容已为最新");
        //                    说明当前客户端的内容已为最新
        //                更新布局
//                    RecyclerViewUtils.setHeaderView(mRecyclerView, new HomeHeadView(getActivity()));
        homeFragmentView.hideLoadingIcon();
        ToastUtil.show(getActivity(),"当前内容已为最新", Toast.LENGTH_SHORT);
        if (mRecyclerView.getVisibility()==View.GONE){
            mRecyclerView.setVisibility(View.VISIBLE);
            noContentView.setVisibility(View.GONE);
        }
    }

    private void noContent(){
        LogUtil.e("当前话题么有内容");
        //                        说明当前话题并没有内容
        homeFragmentView.hideLoadingIcon();
        if (mRecyclerView.getVisibility()==View.VISIBLE){
            mRecyclerView.setVisibility(View.GONE);
            noContentView.setVisibility(View.VISIBLE);
        }
    }

    private void getContentList() throws Exception{
        //                更新布局
        if (mRecyclerView.getVisibility()==View.GONE){
            mRecyclerView.setVisibility(View.VISIBLE);
            noContentView.setVisibility(View.GONE);
        }
        for (int i=0;i<objects.size();i++){
                        /*Parcel parcel=Parcel.obtain();
                        parcel.setDataPosition(0);
                        parcel.writeString(Constant.USER_PHOTO+objects.get(i).getString("id"));
                        parcel.writeString(objects.get(i).getString("id"));
                        parcel.writeString(objects.get(i).getString("content"));*/
            Content content=new Content();
            content.setUser_photo(Constant.USER_PHOTO+objects.get(i).getString("photo"));
            content.setId(objects.get(i).getString("id"));
            content.setContent(objects.get(i).getString("content"));
            String name="";
            ArrayList<String> photo=new ArrayList<String>();
            for (int j=0;j<9;j++) {
                name="image"+j;
                if (!objects.get(i).has(name)){
                    continue;
                }
                photo.add(Constant.CONTENT_PHOTO+objects.get(i).getString(name));
            }
            content.setPhoto(photo);
            content.setTag(objects.get(i).getLong("tag"));
            contents.add(content);
        }
        ContentList contentList=new ContentList();
        contentList.setContents(contents);
//        cacheSave(spinner.getSelectedItem().toString(),getActivity(),contentList);
        CacheUtil.cacheSave(topicNameView.getText().toString(),mActivity,contentList);
        if (mHeaderAndFooterRecyclerViewAdapter.getHeaderViewsCount()>0){
            mHeaderAndFooterRecyclerViewAdapter.removeHeaderView(mHeaderAndFooterRecyclerViewAdapter.getHeaderView());
        }
//                 添加搜索框
        RecyclerViewUtils.setHeaderView(mRecyclerView, new HomeHeadView(getActivity(),"search"));
        homeFragmentView.hideLoadingIcon();
//                homeFragmentView.scrollToTop(false);
        homeFragmentView.updateListView(contents);
//                homeFragmentView.showRecyclerView();

        if (topicNameView.getText().toString().equals("所有")){
            LogUtil.e("当前话题为所有");
//            更新tag
            tag=contents.get(contents.size()-1).getTag();
//            更新havaContent
            haveContent=true;
//            保存缓存
            contentEditor.putBoolean("haveContent",haveContent);
            contentEditor.putLong("tag",tag);
            contentEditor.commit();
        }else{
            tagEditor.putLong(topicNameView.getText().toString(),contents.get(contents.size()-1).getTag());
            tagEditor.commit();
        }
    }
}
