package com.example.asus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.asus.he.R;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class RefreshableView extends LinearLayout implements View.OnTouchListener {
    /**
     * 下拉状态
     */
    public static final int STATUS_PULL_TO_REFRESH = 0;
    /**
     * 释放立即刷新状态
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 1;
    /**
     * 正在刷新状态
     */
    public static final int STATUS_REFRESHING = 2;
    /**
     * 刷新完成或未刷新状态
     */
    public static final int STATUS_REFRESH_FINISHED = 3;
    /**
     * 加载完成或未加载状态
     */
    public static final int STATUS_LOAD_FINISHED=3;
    /**
     * 上拉状态
     */
    public static final int STATUS_PULL_TO_LOAD=0;
    /**
     * 释放加载状态
     */
    public static final int STATUS_RELEASE_TO_LOAD=1;
    /**
     * 加载状态
     */
    public static final int STATUS_LOADING=2;
    /**
     * 下拉头部回滚的速度
     */
    public static final int SCROLL_SPEED = -20;
    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;
    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;
    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;
    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;
    /**
     * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
     */
    private static final String UPDATED_AT = "updated_at";
    /**
     * 下拉刷新的回调接口
     */
    private PullToRefreshListener mListener;
    /**
     * 向下滑动显示其余内容的回调接口
     */
    private ShowOtherContentListener mListen;
    /**
     * 用于存储上次更新时间
     */
    private SharedPreferences preferences;
    /**
     * 下拉头的View
     */
    private View header;
    /**
     * 上拉尾的view
     */
    private View bottom;
    /**
     * 需要去下拉刷新的ListView
     */
    private ListView listView;
    /**
     * 刷新时显示的进度条
     */
    private ProgressBar headProgressBar;
    /**
     * 加载时显示的进度条
     */
    private ProgressBar bottomProgressBar;
    /**
     * 指示下拉和释放的箭头
     */
    private ImageView downArrow;
    /**
     * 指示上拉的箭头
     */
    private ImageView upArrow;
    /**
     * 指示下拉和释放的文字描述
     */
    private TextView description;
    /**
     * 指示上拉的文字
     */
    private TextView upDescription;
    /**
     * 上次更新时间的文字描述
     */
    private TextView updateAt;
    /**
     * 下拉头的布局参数
     */
    private MarginLayoutParams headerLayoutParams;
    /**
     * 上拉尾的布局参数
     */
    private MarginLayoutParams bottomLayoutParams;
    /**
     * 上次更新时间的毫秒值
     */
    private long lastUpdateTime;
    /**
     * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
     */
    private int mId = -1;
    /**
     * 下拉头的高度
     */
    private int hideHeaderHeight;
    private int width;
    private int height;
    /**
     * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
     */
    private int currentStatus = STATUS_REFRESH_FINISHED;
    /**
     * 上拉状态
     */
    private int bottomStatus=STATUS_LOAD_FINISHED;
    /**
     * 记录上一次的状态是什么，避免进行重复操作
     */
    private int lastStatus = currentStatus;
    /**
     * 手指按下时的屏幕纵坐标
     */
    private float yDown;
    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;
    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;
    /**
     * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
     */
    private boolean ableToPull;
    private boolean ableToLoad;
    private double downInitY;
    private double downEndY;
    /**
     * 下拉刷新控件的构造函数，会在运行时动态添加一个下拉头的布局。
     *
     * @param context
     * @param attrs
     */
    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        header = LayoutInflater.from(context).inflate(R.layout.pull_torefresh, null, true);
        headProgressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
        downArrow = (ImageView) header.findViewById(R.id.arrow);
        description = (TextView) header.findViewById(R.id.description);
        updateAt = (TextView) header.findViewById(R.id.updated_at);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        bottom=LayoutInflater.from(context).inflate(R.layout.pull_toload,null,true);
        upArrow=(ImageView)bottom.findViewById(R.id.arrow);
        bottomProgressBar=(ProgressBar)bottom.findViewById(R.id.progress_bar);
        upDescription=(TextView)bottom.findViewById(R.id.description);
        refreshUpdatedAtValue();
        setOrientation(VERTICAL);
        addView(header, 0);
    }
    /**
     * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("onLayout","正在执行RefreshableView的onLayout方法"+loadOnce);
        if (!loadOnce){
            listView = (ListView) getChildAt(1);
            listView.setOnTouchListener(this);
            loadOnce = true;
            addView(bottom,2);
            hideHeaderHeight = -header.getHeight();
            headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin = hideHeaderHeight;
            header.setLayoutParams(headerLayoutParams);
            bottomLayoutParams=(MarginLayoutParams)bottom.getLayoutParams();
            bottomLayoutParams.bottomMargin=hideHeaderHeight;
            bottom.setLayoutParams(bottomLayoutParams);
        }
    }
    /**
     * 当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setIsAbleToPull(event);
        if (ableToPull) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    yDown = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove = event.getRawY();
                    int distance = (int) (yMove - yDown);
                    // 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
                    if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
                        return false;
                    }
                    if (distance < touchSlop) {
                        return false;
                    }
                    if (currentStatus != STATUS_REFRESHING) {
                        if (headerLayoutParams.topMargin > 0) {
                            currentStatus = STATUS_RELEASE_TO_REFRESH;
                        } else {
                            currentStatus = STATUS_PULL_TO_REFRESH;
                        }
                        // 通过偏移下拉头的topMargin值，来实现下拉效果
                        headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
                        header.setLayoutParams(headerLayoutParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                        // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
                        new RefreshingTask().execute();
                    } else if (currentStatus == STATUS_PULL_TO_REFRESH) {
                        // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                        new HideHeaderTask().execute();
                    }
                    break;
            }
            // 时刻记得更新下拉头中的信息
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH) {
                updateHeaderView();
                // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
                listView.setPressed(false);
                listView.setFocusable(false);
                listView.setFocusableInTouchMode(false);
                lastStatus = currentStatus;
                // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
                return true;
            }
        }else{
            /*switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    downEndY=event.getY();
                    break;
                case MotionEvent.ACTION_DOWN:
                    downInitY=event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    downInitY=event.getY();
                    break;
            }
            if (downEndY-downInitY>ScreenTools.instance(getContext()).getScreenHeight()){
                mListen.onShowOtherContent();
            }*/
            setIsAbleToLoad(event);
            if (ableToLoad){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        yDown = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float yMove = event.getRawY();
                        int distance = (int) (yMove - yDown);
                        // 如果手指是下滑状态，并且上拉尾是完全隐藏的，就屏蔽上拉事件
                        if (distance >= 0 && !bottom.hasWindowFocus()) {
                            return false;
                        }
                        if (distance < touchSlop) {
                            return false;
                        }
                        if (bottomStatus != STATUS_LOADING) {
                            if ( bottomLayoutParams.bottomMargin> 0) {
                                bottomStatus = STATUS_RELEASE_TO_LOAD;
                            } else {
                                bottomStatus = STATUS_PULL_TO_LOAD;
                            }
                            // 通过偏移下拉头的topMargin值，来实现下拉效果
                            bottomLayoutParams.bottomMargin = (distance / 2) + hideHeaderHeight;
                            bottom.setLayoutParams(bottomLayoutParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    default:
                        if (bottomStatus == STATUS_RELEASE_TO_LOAD) {
                            // 松手时如果是释放立即加载状态，就去调用加载任务
                            new LoadingTask().execute();
                        } else if (bottomStatus == STATUS_PULL_TO_LOAD) {
                            // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                            new HideBottomTask().execute();
                        }
                        break;
                }
                // 时刻记得更新上拉头中的信息
                if (bottomStatus == STATUS_PULL_TO_LOAD
                        || currentStatus == STATUS_RELEASE_TO_LOAD) {
                    updateBottomView();
                    // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
                    listView.setPressed(false);
                    listView.setFocusable(false);
                    listView.setFocusableInTouchMode(false);
                    lastStatus = currentStatus;
                    // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 给下拉刷新控件注册一个监听器。
     *
     * @param listener
     *            监听器的实现。
     * @param id
     *            为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
     */
    public void setOnRefreshListener(PullToRefreshListener listener, int id) {
        mListener = listener;
        mId = id;
    }
    public void setOnShowOtherContentListener(ShowOtherContentListener listener){
        mListen=listener;
    }
    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
     */
    public void finishRefreshing() {
        currentStatus = STATUS_REFRESH_FINISHED;
        preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
        new HideHeaderTask().execute();
    }
    /**
     * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
     * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
     *
     * @param event
     */
    private void setIsAbleToPull(MotionEvent event) {
        View firstChild = listView.getChildAt(0);
        if (firstChild != null) {
            int firstVisiblePos = listView.getFirstVisiblePosition();
            if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
                if (!ableToPull) {
                    yDown = event.getRawY();
                }
                // 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
                ableToPull = true;
            } else {
                if (headerLayoutParams.topMargin != hideHeaderHeight) {
                    headerLayoutParams.topMargin = hideHeaderHeight;
                    header.setLayoutParams(headerLayoutParams);
                }
                ableToPull = false;
            }
        } else {
            // 如果ListView中没有元素，也应该允许下拉刷新
            ableToPull = true;
        }
    }
    /**
     * 是否允许加载
     */
    private void setIsAbleToLoad(MotionEvent event) {
        View endChild=listView.getChildAt(listView.getChildCount()-1);
        if (endChild!= null) {
            int endVisiblePos = listView.getLastVisiblePosition();
            if (endVisiblePos == height && endChild.getTop() ==height ) {
                if (!ableToLoad) {
                    yDown = event.getRawY();
                }
                // 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
                ableToLoad = true;
            } else {
                ableToPull = false;
            }
        } else {
            ableToLoad=false;
        }
    }
    /**
     * 更新下拉头中的信息。
     */
    private void updateHeaderView() {
        if (lastStatus != currentStatus) {
            if (currentStatus == STATUS_PULL_TO_REFRESH) {
                description.setText(getResources().getString(R.string.pull_to_refresh));
                downArrow.setVisibility(View.VISIBLE);
                headProgressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                description.setText(getResources().getString(R.string.release_to_refresh));
                downArrow.setVisibility(View.VISIBLE);
                headProgressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_REFRESHING) {
                description.setText(getResources().getString(R.string.refreshing));
                headProgressBar.setVisibility(View.VISIBLE);
                downArrow.clearAnimation();
                downArrow.setVisibility(View.GONE);
            }
            refreshUpdatedAtValue();
        }
    }
    /**
     * 更新上拉尾中的信息。
     */
    private void updateBottomView() {
            if (bottomStatus == STATUS_PULL_TO_LOAD) {
                upDescription.setText(getResources().getString(R.string.pull_to_load));
                upArrow.setVisibility(View.VISIBLE);
                bottomProgressBar.setVisibility(View.GONE);
            } else if (bottomStatus == STATUS_RELEASE_TO_LOAD) {
                upDescription.setText(getResources().getString(R.string.release_to_load));
                upArrow.setVisibility(View.VISIBLE);
                bottomProgressBar.setVisibility(View.GONE);
            } else if (bottomStatus == STATUS_LOADING) {
                upDescription.setText(getResources().getString(R.string.loading));
                bottomProgressBar.setVisibility(View.VISIBLE);
                upArrow.setVisibility(View.GONE);
            }
    }
    /**
     * 根据当前的状态来旋转箭头。
     */
    private void rotateArrow() {
        float pivotX = downArrow.getWidth() / 2f;
        float pivotY = downArrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == STATUS_PULL_TO_REFRESH) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        downArrow.startAnimation(animation);
    }
    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    private void refreshUpdatedAtValue() {
        lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue;
        if (lastUpdateTime == -1) {
            updateAtValue = getResources().getString(R.string.not_updated_yet);
        } else if (timePassed < 0) {
            updateAtValue = getResources().getString(R.string.time_error);
        } else if (timePassed < ONE_MINUTE) {
            updateAtValue = getResources().getString(R.string.updated_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "个月";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat + "年";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        }
        updateAt.setText(updateAtValue);
    }
    /**
     * 正在刷新，在此任務中会去回调注册进来的下拉刷新监听器。
     *
     * @author guolin
     */
//    更改result類型，在doInBackground中進行對listView的更新，最後更新結果將在onPostExecute中得到
    class RefreshingTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            int topMargin = headerLayoutParams.topMargin;
            Log.e("topMargin",topMargin+"");
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                Log.e("doInBackground:",topMargin+"");
                if (topMargin <= 0) {
                    break;
                }
//                更新進度
                publishProgress(topMargin);
//                this.sleep(10);
            }
            currentStatus = STATUS_REFRESHING;
            publishProgress(0);
            if (mListener != null) {
                mListener.onRefresh();
            }
            return 5;
        }
        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            headerLayoutParams.topMargin = topMargin[0];
            Log.e("onProgressUpdate",headerLayoutParams.topMargin+"");
            header.setLayoutParams(headerLayoutParams);
        }

        protected void onPostExecute(Integer result) {
//            new HideHeaderTask().execute();
            Log.e("任務","任務執行完"+result);
        }

    }
    //    更改result類型，在doInBackground中進行對listView的更新，最後更新結果將在onPostExecute中得到
    class LoadingTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            int bottomMargin = bottomLayoutParams.bottomMargin;
            while (true) {
                bottomMargin = bottomMargin + SCROLL_SPEED;
                if (bottomMargin <= 0) {
                    break;
                }
//                更新進度
                publishProgress(bottomMargin);
//                this.sleep(10);
            }
            bottomStatus = STATUS_LOADING;
            publishProgress(0);
            if (mListen != null) {
                mListen.onShowOtherContent();
            }
            return 5;
        }
        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateBottomView();
            bottomLayoutParams.bottomMargin = topMargin[0];
            bottom.setLayoutParams(bottomLayoutParams);
        }

        protected void onPostExecute(Integer result) {
            Log.e("任務","任務執行完"+result);
        }

    }
    /**
     * 隐藏下拉头，当未进行下拉刷新或下拉刷新完成后，此方法将会使下拉头重新隐藏。
     *
     * @author guolin
     */
    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= hideHeaderHeight) {
                    topMargin = hideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
//                sleep(10);
            }
            return topMargin;
        }
        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }
        @Override
        protected void onPostExecute(Integer topMargin) {
            headerLayoutParams.topMargin = topMargin;
            header.setLayoutParams(headerLayoutParams);
            currentStatus = STATUS_REFRESH_FINISHED;
        }
    }
    /**
     * 隐藏上拉尾，当未进行上拉或上拉完成后，此方法将会使上拉尾重新隐藏。
     *
     * @author guolin
     */
    class HideBottomTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int bottomMargin = bottomLayoutParams.bottomMargin;
            while (true) {
                bottomMargin = bottomMargin + SCROLL_SPEED;
                if (bottomMargin <= hideHeaderHeight) {
                    bottomMargin = hideHeaderHeight;
                    break;
                }
                publishProgress(bottomMargin);
            }
            return bottomMargin;
        }
        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            bottomLayoutParams.bottomMargin = topMargin[0];
            bottom.setLayoutParams(bottomLayoutParams);
        }
        @Override
        protected void onPostExecute(Integer bottomMargin) {
            bottomLayoutParams.bottomMargin = bottomMargin;
            bottom.setLayoutParams(bottomLayoutParams);
            currentStatus = STATUS_LOAD_FINISHED;
        }
    }
    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
     *
     * @author guolin
     */
    public interface PullToRefreshListener {
        /**
         * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
         */
        void onRefresh();
    }
    public interface ShowOtherContentListener{
        void onShowOtherContent();
    }
}
