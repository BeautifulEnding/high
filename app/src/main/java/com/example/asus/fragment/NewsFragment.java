package com.example.asus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.activity.AddFriend;
import com.example.asus.activity.ChatActivity;
import com.example.asus.activity.MainActivity;
import com.example.asus.adapter.FriendAdapter;
import com.example.asus.adapter.FriendRequestAdapter;
import com.example.asus.client.entity.Message;
import com.example.asus.client.entity.User;
import com.example.asus.constant.Constant;
import com.example.asus.he.R;
import com.example.asus.util.CacUtil;
import com.example.asus.util.HttpUtil;
import com.example.asus.util.LogUtil;
import com.example.asus.util.ToastUtil;
import com.example.asus.util.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment{
    //通讯录好友页面
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
//	List<Map<String,Object>> listItems=new ArrayList<>();
	private FriendAdapter friendAdapter;
	private List<String> response;
	private List<Message> messageList=new ArrayList<>();
	private List<User> userList=new ArrayList<>();
//	自己
	private User selfUser;
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
		selfUser=UserUtil.getUser("selfMessage",getActivity());
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
		registerForContextMenu(friendView);
		recyclerView=(RecyclerView)newsLayout.findViewById(R.id.add_friend_request);
		noFriendTextView=(TextView)newsLayout.findViewById(R.id.noFriends);
		//		初始化recyclerView
		friendRequestAdapter=new FriendRequestAdapter(getActivity(),this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(friendRequestAdapter);
		response= CacUtil.cacheLoad(getActivity(),Constant.LOAD_USER);
		userList.clear();
		if (response ==null){
			//			到服务器去查看是否存在好友，不能只单单考虑缓存里的内容
			String url=HttpUtil.BASE_URL+"getFriends.jsp?user_id="+selfUser.getId();
			try{
				JSONObject object=new JSONObject(HttpUtil.getRequest(url));
				if (object.length()!=0){
					LogUtil.e("有好友");
					for (int i=0;i<object.length();i++){
						LogUtil.e(object.getString("friend"+i));
						User user=UserUtil.findUser(object.getString("friend"+i));
						CacUtil.cacheSave(user.getId(),getActivity(),user);
						userList.add(user);
					}
					if (noFriendTextView.getVisibility()==View.VISIBLE){
						noFriendTextView.setVisibility(View.GONE);
						recyclerView.setVisibility(View.VISIBLE);
						friendView.setVisibility(View.VISIBLE);
					}
					friendAdapter=new FriendAdapter(userList,getActivity());
					//为ListView设置Adapter
					friendView.setAdapter(friendAdapter);
				}else{
					LogUtil.e("没有好友");
				}
			}catch (Exception e){
				LogUtil.e(e.getMessage());
			}
		}
		if (response!=null){
			if (noFriendTextView.getVisibility()==View.VISIBLE){
				noFriendTextView.setVisibility(View.GONE);
				recyclerView.setVisibility(View.VISIBLE);
				friendView.setVisibility(View.VISIBLE);
			}
			for (int i=0;i<response.size();i++) {
				User user = User.parse(response.get(i));
				userList.add(user);
			}
				friendAdapter=new FriendAdapter(userList,getActivity());
				//为ListView设置Adapter
				friendView.setAdapter(friendAdapter);
		}


		//		加载好友请求
		response= CacUtil.cacheLoad(getActivity(),Constant.LOAD_MESSAGE);
		messageList.clear();
		if (response!=null){
			for (int i=0;i<response.size();i++){
				messageList.add(Message.parse(response.get(i)));
			}
			if (messageList != null && messageList.size()!=0 ){
				LogUtil.e("加载到好友请求:  "+messageList.get(0).getContent());
				for (int i=0;i<messageList.size();i++){
					updateRecyclerView(messageList.get(i));
				}
			}
		}else {
			LogUtil.e("没有加载到好友请求");
		}
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
					User user=UserUtil.findUser(name);
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
//				现在是直接转到聊天activity，需要为friendView设置上下文菜单
				Intent intent=new Intent(getActivity(), ChatActivity.class);
//				TextView textView=(TextView)view.findViewById(R.id.name);
//				intent.putExtra("user_name",textView.getText().toString());
				User user=userList.get(position);
				intent.putExtra("friends",user);
				getActivity().startActivity(intent);
			}
		});
	}
	public void updateRecyclerView(Message message){
		if (noFriendTextView.getVisibility()==View.VISIBLE){
			noFriendTextView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			friendView.setVisibility(View.VISIBLE);
		}
//		得到用户
		User user= UserUtil.getUser(message.getSender_id(),getActivity());
		if (user!=null){
			friendRequestAdapter.setUser(user);
			friendRequestAdapter.notifyDataSetChanged();
		}
	}
	public void updateListView(User user){
		if (noFriendTextView.getVisibility()==View.VISIBLE){
			noFriendTextView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			friendView.setVisibility(View.VISIBLE);
		}
		friendAdapter.setUser(user);
		friendAdapter.notifyDataSetChanged();
	}

	/***
	 * 注册上下文菜单
	 * @param menu
	 * @param source
	 * @param menuInfo
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View source, ContextMenu.ContextMenuInfo menuInfo){
		MenuInflater inflater=new MenuInflater(getActivity());
		inflater.inflate(R.menu.alter_friend_message,menu);
		menu.setHeaderTitle("设置");
	}

	/***
	 * 单击菜单项时触发该方法
	 * @param menuItem
	 * @return
	 */
	@Override
	public boolean onContextItemSelected(MenuItem menuItem){
		LogUtil.e("正在执行onContextItemSelected方法");
		AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
		switch (menuItem.getItemId()){
			case R.id.alterName:
//                修改名字
				break;
			case R.id.delete:
//                删除
				userList.remove(info.position);
				break;
		}
		friendAdapter.notifyDataSetChanged();
		return true;
	}
}
