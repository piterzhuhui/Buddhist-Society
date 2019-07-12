package uilayouttest.example.com.bigtask.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;

import uilayouttest.example.com.bigtask.LoginActivity;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.Views.SplashView;

public class Splash extends AppCompatActivity  implements SplashView.todoStartActivity{

    private SplashView mSplashView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
//        getSupportActionBar().hide();//隐藏标题栏

        mSplashView=(SplashView)findViewById(R.id.mSplashViewid);
        mSplashView.setTodoStartActivity(this);
        //沉浸式标题栏
        ImmersionBar.with(this).init();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }

    public void startActivityTodo(){
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(1000);//使程序休眠1秒
//                    Intent it=new Intent(getApplicationContext(),LoginActivity.class);//启动MainActivity
                    startActivity(new Intent(Splash.this,JudgeLogin.class));
//                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
//        finish();
    }

    @Override
    public void startActivity() {
        startActivityTodo();
    }
}
