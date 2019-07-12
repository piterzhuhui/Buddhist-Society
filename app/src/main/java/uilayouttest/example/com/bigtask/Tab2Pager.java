package uilayouttest.example.com.bigtask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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
import cn.jpush.im.api.BasicCallback;
import uilayouttest.example.com.bigtask.Activity.FriendCenter;
import uilayouttest.example.com.bigtask.AdapterNew.FriendsListRecyclerAdapter;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab2Pager extends Fragment implements ItemClickListener {


    private String TAG = "Tab2Pager";

    private SwipeRefreshLayout swipeRefreshLayout;



    private RecyclerView recyclerView;
    private FriendsListRecyclerAdapter friendsListRecyclerAdapter;
    private List<UserInfo> datas;




    //添加好友
    private TextView add_friends;




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        JMessageClient.registerEventReceiver(this);

        View layout = inflater.inflate(R.layout.tab2, null);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview_friends_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsListRecyclerAdapter = new FriendsListRecyclerAdapter();
        friendsListRecyclerAdapter.setOnItemClickListener(this);
        datas = new ArrayList<>();
        recyclerView.setAdapter(friendsListRecyclerAdapter);

        datas = new ArrayList<>();


        //刷新
        swipeRefreshLayout =layout.findViewById(R.id.base_swipe_refresh_widget);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFriend();
            }
        });

                //添加好友
        add_friends = layout.findViewById(R.id.add_friend);

        add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_dialog);
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setIcon(R.mipmap.cat)
                        .setTitle(getResources().getString(R.string.add_friends))
                        .setView(view)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String username = editText.getText().toString();
//                                被邀请方用户的appKey,如果为空则默认从本应用appKey下查找用户。
                                try {
                                    String a = DESUtil.ENCRYPTMethod(username, DESUtil.key).toUpperCase();
                                    System.out.println("加密后的用户名为:" + a);
                                    ContactManager.sendInvitationRequest(a, null, "hello", new BasicCallback() {
                                        @Override
                                        public void gotResult(int responseCode, String responseMessage) {
                                            if (0 == responseCode) {
                                                //好友请求请求发送成功
                                                Log.i(TAG, "添加好友，请求成功" + responseMessage);
                                                Toast.makeText(getActivity(),"添加好友，请求成功",Toast.LENGTH_SHORT).show();
                                            } else {
                                                //好友请求发送失败
                                                Log.i(TAG, "添加好友，请求失败" + responseMessage);
                                                Toast.makeText(getActivity(),"添加好友，请求失败"+responseMessage,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });


        initTitle(layout);
        return layout;
    }


    @Override
    public void onPause() {
        super.onPause();
        JMessageClient.unRegisterEventReceiver(this);
    }




    @Override
    public void onResume() {
        super.onResume();
//        //注册事件，以接收各种用户请求
        JMessageClient.registerEventReceiver(this);


        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
//                    Toast.makeText(getActivity(),"获取好友列表成功",Toast.LENGTH_SHORT).show();
//                    try {
//                        String a = DESUtil.ENCRYPTMethod(jiami, key).toUpperCase();
//                        System.out.println("加密后的数据为:" + a);
//                        String b = DESUtil.decrypt(a, key);
//                        System.out.println("解密后的数据:" + b);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    List<UserInfo> decrypt_userInfoList = userInfoList;

                    friendsListRecyclerAdapter.setDatas(userInfoList);
                    datas = userInfoList;
                    friendsListRecyclerAdapter.notifyDataSetChanged();



                } else {
                    //获取好友列表失败
                    Toast.makeText(getActivity(),"获取好友列表失败",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "获取好友列表失败");
                }
            }
        });



    }


    @Override
    public void OnItemClick(View v, int position) {
        String username = ((UserInfo) datas.get(position)).getUserName();
        Intent intent = new Intent(getActivity(), FriendCenter.class);
        Bundle data = new Bundle();
        data.putString("username", username);
        intent.putExtra("username", data);
        startActivity(intent);

//        Toast.makeText(getActivity(),"好友中心还未开发哦",Toast.LENGTH_SHORT).show();

    }



    //刷新处理
    private void refreshFriend(){
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
                                    Toast.makeText(getActivity(),"获取好友列表成功",Toast.LENGTH_SHORT).show();
                                    friendsListRecyclerAdapter.setDatas(userInfoList);
                                    datas = userInfoList;
                                    friendsListRecyclerAdapter.notifyDataSetChanged();
                                } else {
                                    //获取好友列表失败
                                    Toast.makeText(getActivity(),"获取好友列表失败",Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "获取好友列表失败");
                                }
                            }
                        });


                    }
                });
            }
        }).start();
    }



//    //子线程模式
//    public void onEvent(final ContactNotifyEvent event) {
//        String reason = event.getReason();
//        final String fromUsername = event.getFromUsername();
//        String appkey = event.getfromUserAppKey();
//
//        Log.i("这是好友测试",event.getReason());
//        Log.i("这是好友测试",event.getType().toString());
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (event.getType()) {
//                    case invite_received://收到好友邀请
//                        Toast.makeText(getContext(),"收到好友请求",Toast.LENGTH_SHORT).show();
//                        AlertDialog dialogReceived = new AlertDialog.Builder(getContext())
//                                .setIcon(R.mipmap.icon)//设置标题的图片
//                                .setTitle(getString(R.string.friend_invite_received))//设置对话框的标题
//                                .setMessage(fromUsername + getString(R.string.friend_invite_tip))//设置对话框的内容
//                                //设置对话框的按钮
//                                .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ContactManager.declineInvitation(fromUsername, null, "", null);
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ContactManager.acceptInvitation(fromUsername, null, new BasicCallback() {
//                                            @Override
//                                            public void gotResult(int i, String s) {
//                                                if (i == 0) {
//                                                    ContactManager.getFriendList(new GetUserInfoListCallback() {
//                                                        @Override
//                                                        public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
//                                                            if (0 == responseCode) {
//                                                                //获取好友列表成功
////                                                        friendsListRecyclerAdapter.setDatas(userInfoList);
////                                                        datas = userInfoList;
////                                                        friendsListRecyclerAdapter.notifyDataSetChanged();
//                                                            } else {
//                                                                //获取好友列表失败
//                                                                Log.i(TAG, "获取好友列表失败");
//                                                            }
//                                                        }
//                                                    });
//                                                }
//                                            }
//                                        });
//
//                                        dialog.dismiss();
//                                    }
//                                }).create();
//                        //在dialog  show方法之前添加如下代码，表示该dialog是一个系统的dialog**
//                        dialogReceived.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//                        dialogReceived.show();
//                        break;
//                    case invite_accepted://对方接收了你的好友邀请
//                        AlertDialog dialogAccept = new AlertDialog.Builder(getContext())
//                                .setIcon(R.mipmap.icon)//设置标题的图片
//                                .setTitle(getString(R.string.add_friends))//设置对话框的标题
//                                .setMessage(fromUsername + getString(R.string.friend_invite_accept))//设置对话框的内容
//                                //设置对话框的按钮
//                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).create();
//                        dialogAccept.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//                        dialogAccept.show();
//                        break;
//                    case invite_declined://对方拒绝了你的好友邀请
//                        AlertDialog dialogReject = new AlertDialog.Builder(getContext())
//                                .setIcon(R.mipmap.icon)//设置标题的图片
//                                .setTitle(getString(R.string.add_friends))//设置对话框的标题
//                                .setMessage(fromUsername + getString(R.string.friend_invite_reject))//设置对话框的内容
//                                //设置对话框的按钮
//                                .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).create();
//                        dialogReject.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//                        dialogReject.show();
//                        break;
//                    case contact_deleted://对方将你从好友中删除
//                        //...
//                        Log.i(TAG, "对方将你从好友中删除");
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });


//    }




    private void initData() {
//               List<FriendsGroupBean> groups = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            FriendsGroupBean groupBean = new FriendsGroupBean();
//            groupBean.setGroupName("群组" + i);
//            childs = new ArrayList<>();
//            for (int j = 0; j < 9; j++) {
//                FriendsChildBean childBean = new FriendsChildBean();
//                childBean.setChildName("成员" + j);
//                childs.add(childBean);
//            }
//            groupBean.setChilds(childs);
//            groups.add(groupBean);
//        }
//        adapter = new FriendsDataAdapter(groups, getContext());
//        explistview.setAdapter(adapter);
//        //设置悬浮头部VIEW
//        headView = View.inflate(getActivity(), R.layout.friends_group, null);
//
//        explistview.setHeaderView(headView);
//        explistview.setGroupDataListener(new CustomExpandableListView.HeaderDataListener() {
//            @Override
//            public void setData(int groupPosition) {
//                if (groupPosition < 0)
//                    return;
//                String groupData = ((FriendsGroupBean) adapter.getGroup(groupPosition)).getGroupName();
//                ((TextView) headView.findViewById(R.id.group_title)).setText("展开" + groupData);
//            }
//        });
//        explistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(getActivity(), childs.get(childPosition).getChildName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), FriendChatActivity.class);
//                startActivity(intent);
//                return true;
//            }
//        });

    }


//    private void initView(){
//        explistview = (CustomExpandableListView) layout.findViewById(R.id.explistview);
//    }

    private void initTitle(View layout) {
//
//        tv_title_bar_center = (TextView)layout.findViewById(R.id.tv_title_bar_center);
//        tv_title_bar_center.setText(getString(R.string.my_friend));
    }






    public void onEventMainThread(ContactNotifyEvent event) {
        String message = event.getReason();

        Log.e("bean===", event.getFromUsername() + "," + event.getReason());
    }






    @Override
    public void onDestroy() {
        //销毁
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();

    }

}
