package uilayouttest.example.com.bigtask.Fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uilayouttest.example.com.bigtask.Activity.ChatActivity;
import uilayouttest.example.com.bigtask.AdapterNew.MessageRecyclerViewAdapter;
import uilayouttest.example.com.bigtask.Entry.MessageEntry;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.EasyLifeConstant;
import uilayouttest.example.com.bigtask.EasyLifeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 朱慧 on 18-11-2.
 */

public class MessageListFragment extends Fragment implements ItemClickListener {

    private String TAG = "MessageListFragment";

    private RecyclerView recyclerView;
    private List<MessageEntry> datas;
    private MessageRecyclerViewAdapter adapter;

    private ServiceConnection serviceConnection;
    private EasyLifeService easyLifeService;

    public MessageListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.message_list_fragment,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageEntry messageEntry0=new MessageEntry();
        datas=new ArrayList<>();
        messageEntry0.setUsername(getString(R.string.app_name));
        messageEntry0.setNickname("我叫小智");
        messageEntry0.setContent("快来自言智语吧");
        messageEntry0.setTime("2018.3.9");
        datas.add(messageEntry0);
        adapter=new MessageRecyclerViewAdapter();
        adapter.setDatas(datas);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "绑定服务成功");
                EasyLifeService.SmartChatBinder smartChatBinder = (EasyLifeService.SmartChatBinder) service;
                easyLifeService = smartChatBinder.getService();
//                easyLifeService.setGetMessageListenr(MessageListFragment.this);
                //只能在应用的周期里调用一次，否则会重复读入数据。
                easyLifeService.initSQlite();
                easyLifeService.getMessageFromDB();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                easyLifeService = null;
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (easyLifeService != null && datas.size() < 2) {
            List<MessageEntry> mesageEntries = easyLifeService.getMessageList();
            for (MessageEntry entry : mesageEntries) {
                datas.add(entry);
            }
        }
    }

    @Override
    public void OnItemClick(View v, int position) {
        Intent intent=new Intent();
        intent.setClass(getContext(), ChatActivity.class);
        Bundle data = new Bundle();
        if (position == 0) {
            data.putString("username", EasyLifeConstant.APPNAME);
        } else {
            data.putString("username", datas.get(position).getUsername());
        }
        intent.putExtra("username", data);
        startActivity(intent);
    }

    public void initView() {
        // ((MainActivity)getActivity()).setTitlebar(this);
        Intent intent = new Intent(getActivity(), EasyLifeService.class);
        getActivity().bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }


    @Override
    public void onPause() {
        super.onPause();
        //不能在这里保存数据库，因为此时服务EasyLifeService还没有停止，如果重复打开关闭fragement即会导致重复写入数据库操作，造成数据库混乱
        easyLifeService.saveMessageListToDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }

}
