package uilayouttest.example.com.bigtask.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import uilayouttest.example.com.bigtask.Activity.ChatActivity;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO：接收广播的逻辑
        Log.i(TAG, "onReceive");

        this.context = context;

    }

    // 主线程
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationClickEvent event) {
        Toast.makeText(context,"收到消息为："+event.getMessage().getContent().toJson(),Toast.LENGTH_SHORT).show();

        Log.i(TAG, "通知栏点击" + event.getMessage().getContent().toJson());
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle data = new Bundle();
        data.putString("username", event.getMessage().getFromUser().getUserName());
        Log.i(TAG, event.getMessage().getFromUser().getUserName());
        //在service启动activity必须加入这句
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username", data);
        context.startActivity(intent);

    }




}
