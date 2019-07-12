package uilayouttest.example.com.bigtask.Activity;

import cn.jpush.im.android.api.model.UserInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gyf.barlibrary.ImmersionBar;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.api.BasicCallback;
import uilayouttest.example.com.bigtask.DESUtil;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.Views.CircleImageView;

public class FriendCenter extends AppCompatActivity implements View.OnClickListener{
    private String friendUsername;
    private ImageView iv_back;
    private TextView tv_user_info_username, tv_fragment_user_info_signature,tv_friend_name,tv_friend_nickname,tv_friend_signature;
    public static CircleImageView civ_user_portrait;
    private String TAG = "UserInfoFragment";
    private Button btn_delete_friend;
    private UserInfo friend_user_info;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this).init();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("username");
        friendUsername = bundle.getString("username");
        setContentView(R.layout.activity_friend_center);

        iv_back = this.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_user_info_username = (TextView) this.findViewById(R.id.tv_userinfo_username);
        civ_user_portrait = (CircleImageView) this.findViewById(R.id.civ_user_portrait);
        tv_fragment_user_info_signature = (TextView) this.findViewById(R.id.tv_fregment_userinfo_signature);
        tv_friend_name = this.findViewById(R.id.tv_friend_name);
        tv_friend_nickname = this.findViewById(R.id.tv_friend_nickname);
        tv_fragment_user_info_signature = this.findViewById(R.id.tv_friend_personalized_signature);
        btn_delete_friend = this.findViewById(R.id.btn_delete_friend);
        btn_delete_friend.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_delete_friend:
//                删除好友确认
                final EditText editText = (EditText) view.findViewById(R.id.et_dialog);
                AlertDialog dialog = new AlertDialog.Builder(FriendCenter.this)
                        .setIcon(R.mipmap.friend_cry)//设置标题的图片
                        .setTitle(getResources().getString(R.string.delete_friend_sure))//设置对话框的标题
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friend_user_info.removeFromFriendList(new BasicCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String responseMessage) {
                                        if (0 == responseCode) {
                                            //移出好友列表成功
                                            Log.i("删除好友","删除好友成功");
                                            onBackPressed();
                                        } else {
                                            //移出好友列表失败
                                            Log.i("删除好友","删除好友失败");
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();


                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();




        JMessageClient.getUserInfo(friendUsername, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if(i==0){
//                    获取好友
                    friend_user_info = userInfo;
                    try {
                        String username = DESUtil.decrypt(userInfo.getUserName(), DESUtil.key);
                        String userNickname = DESUtil.decrypt(userInfo.getNickname(), DESUtil.key);
                        System.out.println("解密后的数据:" + username+"-"+userNickname);
                        tv_user_info_username.setText(username + ":" + userNickname);
                        tv_friend_name.setText(username);
                        tv_friend_nickname.setText(userNickname);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            civ_user_portrait.setImageBitmap(bitmap);
                        }
                    });
                    tv_fragment_user_info_signature.setText(userInfo.getSignature());

                    tv_fragment_user_info_signature.setText(userInfo.getSignature());
                }
            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }
}
