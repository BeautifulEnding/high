package com.example.asus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.activity.AddFriend;
import com.example.asus.activity.MainActivity;
import com.example.asus.adapter.FriendRequestAdapter;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;
import com.example.asus.util.CacheUtil;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.ToastUtil;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsFragment extends BaseFragment{
    //这是搜索页面Search,缺个通讯录好友页面
//	定义listView，显示好友的首字母索引
	private ListView listView;
	private ArrayAdapter indexAdapter;
//	回到最上端箭头
	private ImageView reverse;
//	索引整个布局,还没实现变换颜色
	private LinearLayout index;
//	阴影效果
	private View shadow;
//	搜索
	private ImageView search;
//	添加
	private ImageView add;
	private TextView hideEdieText;
	private EditText editText;
//	好友列表
	private ListView friendView;
//	好友
    private RecyclerView recyclerView;
	private FriendRequestAdapter friendRequestAdapter;
	private TextView noFriendTextView;
//	好友listView的item
	private List<Map<String,Object>> listItems=new ArrayList<>();
	private SimpleAdapter friendAdapter;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.news_layout, container,false);
		initView(newsLayout);
		initEvent();
		return newsLayout;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_SEARCH;
	}
    @Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	private void initView(View newsLayout){
		shadow= newsLayout.findViewById(R.id.shadow);
		index=(LinearLayout)newsLayout.findViewById(R.id.index);
		reverse=(ImageView)newsLayout.findViewById(R.id.index_arrow);
		search=(ImageView)getActivity().findViewById(R.id.search_friend);
		add=(ImageView)getActivity().findViewById(R.id.add_friend);
		listView=(ListView) newsLayout.findViewById(R.id.index_search);
		indexAdapter=new ArrayAdapter(getActivity(),R.layout.index_search_friends_list_layout,getResources().getStringArray(R.array.index_search_friends));
		listView.setAdapter(indexAdapter);
		hideEdieText=(TextView) getActivity().findViewById(R.id.hideEditText);
		editText=(EditText)getActivity().findViewById(R.id.editText);
		friendView=(ListView)newsLayout.findViewById(R.id.friend_listView);
		friendAdapter=new SimpleAdapter(getActivity(),listItems,R.layout.setting_list_item,
				new String[]{"user_photo","user_name"},new int[]{R.id.header,R.id.name});
		//为ListView设置Adapter
		friendView.setAdapter(friendAdapter);
		recyclerView=(RecyclerView)newsLayout.findViewById(R.id.add_friend_request);
		noFriendTextView=(TextView)newsLayout.findViewById(R.id.noFriends);
		//		初始化recyclerView
		friendRequestAdapter=new FriendRequestAdapter(getActivity(),friendAdapter,listItems);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(friendRequestAdapter);
	}
	private void initEvent(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				shadow.setVisibility(View.VISIBLE);
				shadow.setAlpha(0.5f);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				shadow.setVisibility(View.GONE);
			}
		});
		reverse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideEdieText.setVisibility(View.GONE);
				editText.setVisibility(View.VISIBLE);
			}
		});

		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideEdieText.setVisibility(View.GONE);
				editText.setVisibility(View.VISIBLE);
				editText.setText("");
//                editText.setHint("请在此处输入话题名称");
				editText.requestFocus();
			}
		});

		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null && event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
//					HttpUtil.getRequest(HttpUtil.BASE_URL)
					editText.setVisibility(View.GONE);
					hideEdieText.setVisibility(View.VISIBLE);
//					向服务器查询该用户，若存在则显示该用户，否则，输出该用户不存在
					String name=editText.getText().toString();
					User user=findUser(name);
					if (user!=null){
						Intent intent=new Intent(getActivity(), AddFriend.class);
						intent.putExtra("friend",user);
						startActivity(intent);
					}else{
						ToastUtil.show(getActivity(),"该用户不存在",Toast.LENGTH_SHORT);
					}
				}
				return false;
			}
		});

		friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}
	public void updateRecyclerView(Message message){
		if (noFriendTextView.getVisibility()==View.VISIBLE){
			noFriendTextView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			friendView.setVisibility(View.VISIBLE);
		}
//		如果不存在该用户的缓存
		User user=CacheUtil.cacheLoad(message.getSender_id(),getActivity());
		if (user==null){
			user=findUser(message.getSender_id());
			if (user!=null){
				CacheUtil.cacheSave(user.getId(),getActivity(),user);
				friendRequestAdapter.setUser(user);
				friendRequestAdapter.notifyDataSetChanged();
			}else{
				ToastUtil.show(getActivity(),"该用户不存在",Toast.LENGTH_SHORT);
			}
		}else{
			CacheUtil.cacheSave(user.getId(),getActivity(),user);
			friendRequestAdapter.setUser(user);
			friendRequestAdapter.notifyDataSetChanged();
		}
	}

	private User findUser(String name){
		String url=HttpUtil.BASE_URL+"findFriend.jsp?name="+name;
		try {
			String result=HttpUtil.getRequest(url);
			if (result!=null){
				JSONObject object=new JSONObject(result);
				User user=new User();
							/*user.id=object.getString("user_id");
							user.screen_name=object.getString("user_name");
							user.description=object.getString("user_self");
							user.profile_image_url=Constant.USER_PHOTO+object.getString("user_photo");
							user.gender=object.getString("user_sex");
							user.telPhone=object.getString("user_tel");
							user.birthday=object.getString("user_age");*/
				user.setId(object.getString("user_id"));
				user.setScreen_name(object.getString("user_name"));
				user.setDescription(object.getString("user_self"));
				user.setProfile_image_url(object.getString("user_photo"));
				user.setGender(object.getString("user_sex"));
				user.setTelPhone(object.getString("user_tel"));
				CacheUtil.cacheSave(name,getActivity(),user);
				return user;
			}
		}catch (Exception e){
			LogUtil.e("用户异常"+e.getMessage());
		}
		return null;
	}
}
