package uilayouttest.example.com.bigtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jpeng.jptabbar.JPTabBar;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.UserInfo;
import uilayouttest.example.com.bigtask.Activity.ChatActivity;
import uilayouttest.example.com.bigtask.AdapterNew.FriendsListRecyclerAdapter;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab3Pager extends Fragment implements ItemClickListener {
    JPTabBar mTabBar;

    private String TAG = "Tab3Pager";

    private RecyclerView recyclerView;
    private FriendsListRecyclerAdapter messageListRecyclerAdapter;
    private List<UserInfo> datas;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView tv_title_bar_center;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.tab3,null);
        init(layout);

//        //注册
//        JMessageClient.registerEventReceiver(this);


        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageListRecyclerAdapter = new FriendsListRecyclerAdapter();
        messageListRecyclerAdapter.setOnItemClickListener(this);
        datas = new ArrayList<>();
        recyclerView.setAdapter(messageListRecyclerAdapter);


        //刷新
        swipeRefreshLayout =layout.findViewById(R.id.base_swipe_refresh_message);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage();
            }
        });

        return layout;
    }

    /**
     * 初始化
     */
    private void init(View layout) {
//        mTabBar = ((MainActivity)getActivity()).getTabbar();
//        tv_title_bar_center = (TextView)layout.findViewById(R.id.tv_title_bar_center);
//        tv_title_bar_center.setText(getString(R.string.message));
//        ((RadioGroup)layout.findViewById(R.id.radioGroup1)).setOnCheckedChangeListener(this);
//        ((RadioGroup)layout.findViewById(R.id.radioGroup2)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        JMessageClient.unRegisterEventReceiver(this);
    }


    //刷新处理
    private void refreshMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
//                        initData();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                        ContactManager.getFriendList(new GetUserInfoListCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                                if (0 == responseCode) {
                                    //获取好友列表成功
                                    Toast.makeText(getActivity(),"获取消息列表成功",Toast.LENGTH_SHORT).show();
                                    messageListRecyclerAdapter.setDatas(userInfoList);
                                    datas = userInfoList;
                                    messageListRecyclerAdapter.notifyDataSetChanged();
                                } else {
                                    //获取好友列表失败
                                    Toast.makeText(getActivity(),"获取消息列表失败",Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "获取消息列表失败");
                                }
                            }
                        });


                    }
                });
            }
        }).start();
    }



    @Override
    public void onResume() {
        super.onResume();
//        //注册事件，以接收各种用户请求
//        JMessageClient.registerEventReceiver(this);
        ContactManager.getFriendList(new GetUserInfoListCallback(){
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    Toast.makeText(getActivity(),"获取消息列表成功",Toast.LENGTH_SHORT).show();
                    messageListRecyclerAdapter.setDatas(userInfoList);
                    datas = userInfoList;
                    messageListRecyclerAdapter.notifyDataSetChanged();
                } else {
                    //获取好友列表失败
                    Toast.makeText(getActivity(),"获取消息列表失败",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "获取消息列表失败");
                }
            }
        });
    }

//    public void onEventMainThread(ContactNotifyEvent event) {
//        String message = event.getReason();
//        Log.e("bean===", event.getFromUsername() + "," + event.getReason());
//    }


    @Override
    public void OnItemClick(View v, int position) {
        String username = ((UserInfo) datas.get(position)).getUserName();
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        Bundle data = new Bundle();
        data.putString("username", username);
        intent.putExtra("username", data);
        startActivity(intent);

    }
}
