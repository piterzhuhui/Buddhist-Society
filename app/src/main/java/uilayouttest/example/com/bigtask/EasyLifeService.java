package uilayouttest.example.com.bigtask;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import uilayouttest.example.com.bigtask.Entry.MessageEntry;
import uilayouttest.example.com.bigtask.Utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by 朱慧 on 18-11-24.
 *
 * 接收到聊天信息先是在内存维护一个数组，退出时写入数据库，在创建时先从数据库里读取数据。
 *调试时要注意之前的数据是否一直存在数据库里
 *
 */

public class EasyLifeService extends Service {

    private String TAG = "EasyLifeService";
    private SmartChatBinder binder = new SmartChatBinder();
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private List<MessageEntry> messageList = new ArrayList<>();
    private getMessageListener getMessage;
    public boolean isLoadMessageListFromDB = false;

    public void setGetMessageListenr(getMessageListener listenr) {
        this.getMessage = listenr;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "我的服务，onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "我的服务，onCreate");
//        4-22日添加，解决Android 8.0 的应用尝试在不允许其创建后台服务的情况下使用 startService() 函数，则该函数将引发一个 IllegalStateException的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1,new Notification());
        }
        initSQlite();
//        JMessageClient.registerEventReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "我的服务，onStartcommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        saveMessageListToDB();
        Log.i(TAG, "我的服务，onDestroy");
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.i(TAG, "我的服务，stop");
        return super.stopService(name);


    }



    public class SmartChatBinder extends Binder {
        public EasyLifeService getService() {
            //返回当前对象，可以客户端调用Service公共方法
            return EasyLifeService.this;
        }
    }

    public List<MessageEntry> getMessageList() {
        return messageList;
    }

    public interface getMessageListener {
        void getMessageList(List<MessageEntry> messageList);
    }

    //username content time
    public void saveMessageListToDB() {
        Log.i(TAG, "正在保存数据");
        isLoadMessageListFromDB = true;
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        db.delete(MySQLiteOpenHelper.TABLEMESSAGELIST, null, null);
        for (MessageEntry entry : messageList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", entry.getUsername());
            contentValues.put("content", entry.getContent());
            contentValues.put("time", entry.getTime());
            Log.i(TAG, entry.getUsername());
            db.insertOrThrow(MySQLiteOpenHelper.TABLEMESSAGELIST, null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    //防止在未登陆时无法初始化数据库而报错
    public void initSQlite() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
    }

    //全局只需调用一次，将数据加载到内存
    public void getMessageFromDB() {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        String sql = "select * from " + MySQLiteOpenHelper.TABLEMESSAGELIST;
        StringBuffer sb = new StringBuffer();
        Cursor cursor = db.rawQuery(sql, null);
        //防止多次读入数据
        messageList.clear();
        while (cursor.moveToNext()) {
            MessageEntry entry = new MessageEntry();
            entry.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            entry.setTime(cursor.getString(cursor.getColumnIndex("time")));
            entry.setContent(cursor.getString(cursor.getColumnIndex("content")));
            messageList.add(entry);
        }

        if (getMessage != null) {
            getMessage.getMessageList(messageList);
        }
        Log.i(TAG, "getMessageFromDB:" + sb.toString());
        db.close();
    }






}
