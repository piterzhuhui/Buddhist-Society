package uilayouttest.example.com.bigtask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import uilayouttest.example.com.bigtask.Views.LoadingDialog;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GO_TO_REGIST = 100;

    public LoadingDialog loadingDialog;

    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name, et_psw;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName, psw;
    private boolean isTrue = false;



    /**
     * 获取控件中的字符串
     */
    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);




        loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setTextContent(getString(R.string.login_now));


        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);


        //状态栏浸染
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //立即注册
        TextView textView = (TextView) findViewById(R.id.tv_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        //登录进入主页面
        Button login_in = (Button) findViewById(R.id.btn_login);
        login_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditString();

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "密码或用户名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String jiemiUserName = userName;
                    String jiemiUserPsw = psw;

                    try {
                        String a = DESUtil.ENCRYPTMethod(jiemiUserName, DESUtil.key).toUpperCase();
                        System.out.println("加密后的用户名为:" + a);
                        String b = DESUtil.ENCRYPTMethod(jiemiUserPsw, DESUtil.key).toUpperCase();
                        System.out.println("加密后的密码为:" + b);
                        JMessageClient.login(a, b, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    loadingDialog.dismiss();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "错误原因：" + s, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loadingDialog.show();
                }
            }
        });

    }

    /**
     * startActivityForResult()
     * 当RegistActivity  finish()后  将会自动调用该onActivityResult方法。
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        回传数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_GO_TO_REGIST:
                //判断注册是否成功  如果注册成功
                if (resultCode == RESULT_OK) {
                    //则获取data中的账号和密码  动态设置到EditText中
                    String username = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    et_user_name.setText(username);
                    et_psw.setText(password);
                }
                break;
        }
    }


}
