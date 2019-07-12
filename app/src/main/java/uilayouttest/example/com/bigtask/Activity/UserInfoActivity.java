package uilayouttest.example.com.bigtask.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import uilayouttest.example.com.bigtask.DESUtil;
import uilayouttest.example.com.bigtask.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 朱慧 on 18-7-13.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_exit_login;
    private TextView tv_title_bar_center, tv_userinfo_nickname, tv_userinfo_signature, tv_user_info_gender,tv_user_info_address;
    private ImageView iv_user_info_back, iv_user_info_portrait;
    private RelativeLayout relate_userinfo_portrait, relate_userinfo_nickname, relate_userinfo_signature, relate_user_info_gender,relate_user_info_address;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ImmersionBar.with(this).init();

        initView();

    }

    String decryptUserNickName;

    public void initView() {
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        tv_title_bar_center.setText(getString(R.string.person_info));
        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(this);
        tv_userinfo_nickname = (TextView) findViewById(R.id.tv_userinfo_nickname);
        tv_user_info_gender = findViewById(R.id.tv_user_info_gender);
        tv_user_info_address = findViewById(R.id.tv_user_info_address);
        if(!JMessageClient.getMyInfo().getAddress().equals("")){
            tv_user_info_address.setText(JMessageClient.getMyInfo().getAddress().toString());
        }else{
            tv_user_info_address.setText("");
        }

        //从登录信息获取用户名、昵称
        try {
            String userNickName = JMessageClient.getMyInfo().getNickname();
            decryptUserNickName = DESUtil.decrypt(userNickName, DESUtil.key);
            System.out.println("解密后的昵称:" + decryptUserNickName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_userinfo_nickname.setText(decryptUserNickName);
        tv_userinfo_signature = (TextView) findViewById(R.id.tv_userinfo_signature);
        //设置signature
        tv_userinfo_signature.setText(JMessageClient.getMyInfo().getSignature());
        //设置性别
        if(JMessageClient.getMyInfo().getGender().toString().equals("female")){
            tv_user_info_gender.setText("女");
        }else if(JMessageClient.getMyInfo().getGender().toString().equals("male")){
            tv_user_info_gender.setText("男");
        }else{
            tv_user_info_gender.setText("");
        }

        relate_userinfo_portrait = (RelativeLayout) findViewById(R.id.relate_userinfo_portrait);
        relate_userinfo_portrait.setOnClickListener(this);
        relate_userinfo_nickname = (RelativeLayout) findViewById(R.id.relate_userinfo_nickname);
        relate_userinfo_nickname.setOnClickListener(this);
        relate_userinfo_signature = (RelativeLayout) findViewById(R.id.relate_userinfo_signature);
        relate_userinfo_signature.setOnClickListener(this);
        relate_user_info_gender = findViewById(R.id.relate_user_info_gender);
        relate_user_info_gender.setOnClickListener(this);
        relate_user_info_address  = findViewById(R.id.relate_user_info_address);
        relate_user_info_address.setOnClickListener(this);

        iv_user_info_portrait = (ImageView) findViewById(R.id.iv_user_info_portrait);

    }

    @Override
    protected void onResume() {
        super.onResume();
        JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                iv_user_info_portrait.setImageBitmap(bitmap);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //界面后退
            case R.id.iv_title_bar_back:
                UserInfoActivity.this.onBackPressed();
                break;
            //修改头像
            case R.id.relate_userinfo_portrait:
                Intent intent1 = new Intent(UserInfoActivity.this, ModifyPortraitActivity.class);
                startActivity(intent1);
                break;
            //修改昵称
            case R.id.relate_userinfo_nickname:
                View view = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_dialog);
                AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getResources().getString(R.string.modify_nickname))//设置对话框的标题
                        .setView(view)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nickname = editText.getText().toString();
//                                这里要加密
                                try {
                                    String a = DESUtil.ENCRYPTMethod(nickname, DESUtil.key).toUpperCase();
                                    System.out.println("加密后的昵称为:" + a);
                                    UserInfo userInfo = JMessageClient.getMyInfo();
                                    userInfo.setNickname(a);
                                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i == 0) {
//                                                这里来一个解密，不然无法同步
                                                try {
                                                    String b = DESUtil.decrypt(JMessageClient.getMyInfo().getNickname(), DESUtil.key);
                                                    System.out.println("解密后的昵称:" + b);
                                                    tv_userinfo_nickname.setText(b);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
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
                break;
            //修改性别
            case R.id.relate_user_info_gender:
                View view2 = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText2 = (EditText) view2.findViewById(R.id.et_dialog);
                AlertDialog dialog2 = new AlertDialog.Builder(UserInfoActivity.this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getResources().getString(R.string.modify_gender))//设置对话框的标题
                        .setView(view2)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String gender = editText2.getText().toString();
                                if (!(gender.equals("男"))&& !gender.equals("女")){
                                    Toast.makeText(UserInfoActivity.this,"类别请输入男/女", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    final UserInfo userInfo = JMessageClient.getMyInfo();
                                    if(gender.equals("男")){
                                        userInfo.setGender(UserInfo.Gender.male);
                                    }else {
                                        userInfo.setGender(UserInfo.Gender.female);
                                    }
                                    JMessageClient.updateMyInfo(UserInfo.Field.gender, userInfo, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i == 0) {
                                                if (userInfo.getGender() == UserInfo.Gender.female){
                                                    tv_user_info_gender.setText("女");
                                                }else{
                                                    tv_user_info_gender.setText("男");
                                                }

                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }

                            }
                        }).create();
                dialog2.show();
                break;
            case R.id.relate_user_info_address:
                View view4 = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText4 = (EditText) view4.findViewById(R.id.et_dialog);
                AlertDialog dialog4 = new AlertDialog.Builder(UserInfoActivity.this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getResources().getString(R.string.modify_address))//设置对话框的标题
                        .setView(view4)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String address = editText4.getText().toString();
                                if (address.equals("")){
                                    Toast.makeText(UserInfoActivity.this,"请输入地址", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    final UserInfo userInfo = JMessageClient.getMyInfo();
                                    userInfo.setAddress(address);
                                    JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i == 0) {
                                                tv_user_info_address.setText(address);
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            }
                        }).create();
                dialog4.show();
                break;

            //修改个性签名
            case R.id.relate_userinfo_signature:
                View view3 = getLayoutInflater().inflate(R.layout.dialog_item_edittext, null);
                final EditText editText3 = (EditText) view3.findViewById(R.id.et_dialog);
                AlertDialog dialog3 = new AlertDialog.Builder(UserInfoActivity.this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getResources().getString(R.string.modify_signature))//设置对话框的标题
                        .setView(view3)
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String signature = editText3.getText().toString();
                                UserInfo userInfo = JMessageClient.getMyInfo();
                                userInfo.setSignature(signature);
                                JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            tv_userinfo_signature.setText(JMessageClient.getMyInfo().getSignature());
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).create();
                dialog3.show();
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }
}
