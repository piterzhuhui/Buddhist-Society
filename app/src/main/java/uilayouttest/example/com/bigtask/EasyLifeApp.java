package uilayouttest.example.com.bigtask;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import uilayouttest.example.com.bigtask.Utils.SharePreferenceUtils;
import uilayouttest.example.com.bigtask.Utils.SpeechSynthesizerUtil;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by 朱慧 on 18-11-24.
 */

public class EasyLifeApp extends Application {
    private final String APPID="5ca0271e";
    private String TAG = "EasyLifeApp";

    public static String USERNAME;

    @Override
    public void onCreate() {
        super.onCreate();

        // You can enable debug mode in developing state. You should close debug mode when release.
        JMessageClient.setDebugMode(true);
        //sdk初始化
        JMessageClient.init(this,true);//true - 启用消息漫游，false - 关闭。

        this.USERNAME = (String) SharePreferenceUtils.get(this, EasyLifeConstant.SPUSERNAME, "");

        new Thread(new Runnable() {
            @Override
            public void run() {
                //讯飞语音初始化工作
                SpeechUtility.createUtility(EasyLifeApp.this, SpeechConstant.APPID + "=" + APPID);
                //讯飞文本生成初始化工作
                SpeechSynthesizerUtil.getInstance().init(EasyLifeApp.this);




                //极光推送
                JPushInterface.setDebugMode(true);
                JPushInterface.init(EasyLifeApp.this);
                //极光即时通信
                JMessageClient.setDebugMode(true);
                JMessageClient.init(EasyLifeApp.this);

                Intent intent = new Intent(EasyLifeApp.this, EasyLifeService.class);
                //        4-22日添加，解决Android 8.0 的应用尝试在不允许其创建后台服务的情况下使用 startService() 函数，则该函数将引发一个 IllegalStateException的问题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }



                final FFmpeg fFmpeg = FFmpeg.getInstance(EasyLifeApp.this);
                try {
                    fFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                        @Override
                        public void onFailure() {
                            Log.i(TAG, "onFailure,FFmpeg加载失败");
                        }

                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "onFailure,FFmpeg加载成功");
                        }

                        @Override
                        public void onStart() {
                            Log.i(TAG, "onFailure,FFmpeg加载开始");
                        }

                        @Override
                        public void onFinish() {
                            Log.i(TAG, "onFailure,FFmpeg加载完成");
                        }
                    });
                } catch (FFmpegNotSupportedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

        Log.i("EasyLifeApp", "i am onCreate....");




    }
}
