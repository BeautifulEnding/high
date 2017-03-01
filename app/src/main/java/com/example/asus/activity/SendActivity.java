package com.example.asus.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.he.R;
import com.example.asus.util.DialogUtil;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.ScreenTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.example.asus.util.UploadUtil;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class SendActivity extends Activity{
    //    定义一个全局布局尺寸监听
    private ViewTreeObserver.OnGlobalLayoutListener listener = null;

    //定义请求代码
    public static final int GET_CAMERA = 0;
    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    //定义文本编辑框
    EditText editText = null;
    //定义左边的取消按钮
    TextView cancel;
    //定义右边的发送按钮
    Button send;
//    Spinner spinner = null;
    String[] topics = null;
    int flag = 0;
    Animation show_Animation = null;
    Animation hidden_Animation = null;
    ListView limit = null;
    ImageView photo = null;
    //    定义listview的资源
    int[] limitPhoto = new int[]{R.drawable.earth, R.drawable.at};
    String[] limitText = null;
    String[] result = new String[]{"公开", ""};
    //    定义一个变量表示当前照片数
    int photoNum = 1;

    private GridView gridView = null;
    private GridAdapter gridAdapter = null;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private String TAG = MainActivity.class.getSimpleName();
    RelativeLayout motionLayout = null;

    private Map<String,String> map=new HashMap<String,String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_contents_layout);
        photo=(ImageView)findViewById(R.id.photo);
        limit=(ListView)findViewById(R.id.limit);
        limitText=getResources().getStringArray(R.array.limit);
        //创建一个List对象，元素是Map
        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        for (int i=0;i<limitPhoto.length;i++){
            Map<String,Object> item=new HashMap<String, Object>();
            item.put("limitPhoto",limitPhoto[i]);
            item.put("limitText",limitText[i]);
            item.put("limitResult",result[i]);
            listItems.add(item);
        }
        SimpleAdapter adapter3=new SimpleAdapter(this,listItems,R.layout.limit_listview,
                new String[]{"limitPhoto","limitText","limitResult"},
                new int[]{R.id.photo,R.id.limit,R.id.result});
        limit.setAdapter(adapter3);
//        获得spinner
//        spinner=(Spinner)findViewById(R.id.spinner);
        topics=getResources().getStringArray(R.array.topic);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,topics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        获取系统的高度
        final Display display = getWindowManager().getDefaultDisplay() ;
        //得到文本编辑框
        editText=(EditText)findViewById(R.id.send_content);
        final RelativeLayout layout=(RelativeLayout) findViewById(R.id.layout);
        show_Animation=AnimationUtils.loadAnimation(this,R.anim.send_out_anim);
        hidden_Animation=AnimationUtils.loadAnimation(this,R.anim.input_hidden);
        motionLayout=(RelativeLayout)findViewById(R.id.motionLayout);
        gridView = (GridView) findViewById(R.id.gridView);
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 4 ? 4 : cols;
        gridView.setNumColumns(cols);
        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs) ){
                    PhotoPickerIntent intent = new PhotoPickerIntent(SendActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(9); // 最多选择照片数量
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, GET_CAMERA);
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(SendActivity.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                }
            }
        });

        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);

        editText.requestFocus();
        /*Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText,InputMethodManager.RESULT_SHOWN);
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        },300);*/

        //设置长度为100
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        cancel=(TextView)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send=(Button)findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                如果數據合理
                if (!TextUtils.equals(editText.getText(),"")){
//                    如果netConnected
                    if (isNetWorkConnected()){
                        //                将数据上传到服务器
                        map.put("topic",getIntent().getStringExtra("topic"));
                        map.put("id",getSharedPreferences("login",MODE_PRIVATE).getString("user_id",null));
                        map.put("content",editText.getText().toString().trim());
                        Thread thread=new Thread(){
                            @Override
                            public void run() {
                                super.run();
//                        FileUpload.uploadAuction(imagePaths,map,photoNum);
                                if (photoNum!=9){
                                    map.put("photoNum",(photoNum-1)+"");
                                }else {
                                    map.put("photoNum", photoNum+"");
                                }
                                map.put("tag",System.currentTimeMillis()+"");
                                String url= HttpUtil.BASE_URL+"sendContent.jsp";
                                UploadUtil.uploadMutiData(url,map,imagePaths);
//                        File file=new File(imagePaths.get(0));
//                        UploadUtil.uploadFile(file,url);
                            }
                        };
                        thread.start();
                        Toast.makeText(SendActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        MyApplication.getInstance().removeActivity(SendActivity.this);
                        finish();
                    }else {
                        DialogUtil.showDialog(SendActivity.this,"您正处于没有网络的异次元！",false);
                    }
                }else{
                    DialogUtil.showDialog(SendActivity.this,"内容不能为空！",false);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editText.getText())){
                    send.setBackgroundResource(R.drawable.button_enable);
                    send.setEnabled(true);
                    send.setTextColor(getResources().getColor(R.color.bg_color));
                }
                else{
                    send.setBackgroundResource(R.drawable.button_unenable);
                    send.setEnabled(false);
                    send.setTextColor(getResources().getColor(R.color.divider_color));
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case GET_CAMERA:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_CODE_PICK_IMAGE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }
    //    判断网络状态
    private boolean isNetWorkConnected(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
//        返回联网状态，如果有联网信息并且连了网，则返回true
        return (networkInfo!=null&&networkInfo.isConnected());
    }

    private void loadAdpater(ArrayList<String> paths){
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if (paths.contains("000000")){
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter  = new GridAdapter(imagePaths);
        photoNum=gridAdapter.getCount();
        ViewGroup.LayoutParams params=gridView.getLayoutParams();
        params.height= ScreenTools.instance(this).dip2px(100);
        if (photoNum>4 && photoNum < 9){
            params.height= ScreenTools.instance(this).dip2px(180);

        }
        if (photoNum>=9){
            params.height= ScreenTools.instance(this).dip2px(260);
        }
        gridView.setLayoutParams(params);
        gridView.setAdapter(gridAdapter);
        try{
            JSONArray obj = new JSONArray(imagePaths);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter{
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;
        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 10){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(SendActivity.this);
        }

        public int getCount(){
            return  listUrls.size();
        }
        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final String path=listUrls.get(position);
            if (path.equals("000000")){
                holder.image.setImageResource(R.drawable.add);
            }else {
                Glide.with(SendActivity.this)
                        .load(path)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }
}
