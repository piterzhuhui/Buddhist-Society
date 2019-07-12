package uilayouttest.example.com.bigtask.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.jpush.im.android.api.JMessageClient;
import uilayouttest.example.com.bigtask.LoginActivity;
import uilayouttest.example.com.bigtask.MainActivity;
import uilayouttest.example.com.bigtask.R;

public class JudgeLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_login);

        //        判读是否登录
        if(!(JMessageClient.getMyInfo() == null)){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
