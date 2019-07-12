package uilayouttest.example.com.bigtask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import uilayouttest.example.com.bigtask.Views.LoadingDialog;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GO_TO_REGIST = 100;

    private TextView tv_main_title;//标题
    private ImageView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name, et_psw, et_psw_again, et_secret_key;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName, psw, pswAgain, secret_key;




    //标题布局
    private RelativeLayout rl_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //沉浸式标题栏
        ImmersionBar.with(this).init();
        Button register = (Button) findViewById(R.id.btn_register);

        init();


    }


    public LoadingDialog loadingDialog_1;

    //注册判断
    private void init() {
        tv_back = findViewById(R.id.iv_title_bar_back);
        //布局根元素
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
//        et_secret_key = findViewById(R.id.et_secret_key);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish之前  回传给LoginActivity两个参数 将参数回传给登录界面
                Intent data = new Intent();
                data.putExtra("username", userName);
                data.putExtra("password", psw);
                setResult(RESULT_OK, data);
                //返回键
                RegisterActivity.this.finish();
            }
        });

        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                //判断输入框内容
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!psw.equals(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                    /**
                     *从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
                     */
                }
                else {

                    //把账号、密码和账号标识保存到sp里面


                    /**
                     * @param param3 RegisterOptionalUserInfo()
                     * @since 2.3.0
                     */
//                    用户的其他信息
//                    RegisterOptionalUserInfo u = new RegisterOptionalUserInfo();
//                    u.setAddress(secret_key);
//                    u.setBirthday(19190202);
//                    u.setGender(UserInfo.Gender.female);



                    String jiamiUserName = userName;
                    String jiamiUserPsw = psw;

                    try {
                        String a = DESUtil.ENCRYPTMethod(jiamiUserName, DESUtil.key).toUpperCase();
                        System.out.println("加密后的用户名为:" + a);
                        String b = DESUtil.ENCRYPTMethod(jiamiUserPsw, DESUtil.key).toUpperCase();
                        System.out.println("加密后的密码为:" + b);

                        JMessageClient.register(a, b,new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    RegisterActivity.this.finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "错误原因:" + s, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
//
//
        });
    }


    /**
     * 获取控件中的字符串
     */
    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();
//        secret_key = et_secret_key.getText().toString().trim();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }
}
