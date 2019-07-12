package uilayouttest.example.com.bigtask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpeng.jptabbar.JPTabBar;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import uilayouttest.example.com.bigtask.Activity.ContactAuthorActivity;
import uilayouttest.example.com.bigtask.Activity.JudgeLogin;
import uilayouttest.example.com.bigtask.Activity.UserInfoActivity;
import uilayouttest.example.com.bigtask.Utils.SharePreferenceUtils;
import uilayouttest.example.com.bigtask.Views.CircleImageView;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab4Pager extends Fragment implements View.OnClickListener {

    private ImageView iv_modify_user_info;
    private TextView tv_userinfo_username, tv_fregment_userinfo_signature, tv_recognize_car, tv_recognize_animal, tv_recognize_plant, tv_recognize_food, tv_contact_author;
    public static CircleImageView civ_user_portrait;
    private String TAG = "UserInfoFragment";
    private View layout;
    private UserInfo userInfo;
    private Button btn_exit_login;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View layout = inflater.inflate(R.layout.tab4, null);
        layout = inflater.inflate(R.layout.tab4, container, false);
        //layout.findViewById(R.id.button1).setOnClickListener(this);
        initView();
        return layout;
    }


    public void initView() {
        iv_modify_user_info = (ImageView) layout.findViewById(R.id.iv_modify_user_info);
        iv_modify_user_info.setOnClickListener(this);
        tv_userinfo_username = (TextView) layout.findViewById(R.id.tv_userinfo_username);
        civ_user_portrait = (CircleImageView) layout.findViewById(R.id.civ_user_portrait);
        tv_fregment_userinfo_signature = (TextView) layout.findViewById(R.id.tv_fregment_userinfo_signature);
//        tv_recognize_car = (TextView) layout.findViewById(R.id.tv_recognize_car);
//        tv_recognize_car.setOnClickListener(this);
//        tv_recognize_animal = (TextView) layout.findViewById(R.id.tv_recognize_animal);
//        tv_recognize_animal.setOnClickListener(this);
//        tv_recognize_plant = (TextView) layout.findViewById(R.id.tv_recognize_plant);
//        tv_recognize_plant.setOnClickListener(this);
//        tv_recognize_food = (TextView) layout.findViewById(R.id.tv_recognize_food);
//        tv_recognize_food.setOnClickListener(this);
        tv_contact_author = (TextView) layout.findViewById(R.id.tv_contact_author);
        tv_contact_author.setOnClickListener(this);
        btn_exit_login = (Button) layout.findViewById(R.id.btn_exit_login);
        btn_exit_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        JPTabBar tabBar = (JPTabBar) ((Activity) getContext()).findViewById(R.id.tabbar);
        tabBar.setTabTypeFace("fonts/Jaden.ttf");

        Bundle data = new Bundle();
        switch (view.getId()) {
            //修改用户信息
            case R.id.iv_modify_user_info:
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            //展示我的作品
//            case R.id.tv_recognize_car:
//                Intent intent1 = new Intent(getActivity(), ShowMyProductionActivity.class);
////                data.putInt(RecognizerHelper.type, RecognizerHelper.CAR);
////                intent1.putExtra("data", data);
//                startActivity(intent1);
//                break;
//            //展示我的下载列表
//            case R.id.tv_recognize_animal:
////                Intent intent2 = new Intent(getActivity(), RecognizerActivity.class);
////                data.putInt(RecognizerHelper.type, RecognizerHelper.ANIMAL);
////                intent2.putExtra("data", data);
////                startActivity(intent2);
//                break;
//            //显示浏览历史
//            case R.id.tv_recognize_plant:
////                Intent intent3 = new Intent(getActivity(), RecognizerActivity.class);
////                data.putInt(RecognizerHelper.type, RecognizerHelper.PLANT);
////                intent3.putExtra("data", data);
////                startActivity(intent3);
//                break;
//            case R.id.tv_recognize_food:
////                Intent intent4 = new Intent(getActivity(), RecognizerActivity.class);
////                data.putInt(RecognizerHelper.type, RecognizerHelper.FOOD);
////                intent4.putExtra("data", data);
////                startActivity(intent4);
//                break;
            case R.id.tv_contact_author:
                Intent intent5 = new Intent(getActivity(), ContactAuthorActivity.class);
                startActivity(intent5);
                break;
            //退出登录
            case R.id.btn_exit_login:
                JMessageClient.logout();
                SharePreferenceUtils.put(getActivity(), EasyLifeConstant.SPISLOGINKEY, false);
                Intent intent6 = new Intent(getActivity(), JudgeLogin.class);
                startActivity(intent6);

                break;
        }


    }



    String userName;
    String decryptUserName;
    String userNickName;
    String decryptUserNickName;

    @Override
    public void onResume() {
        super.onResume();

        userInfo = JMessageClient.getMyInfo();
        try {

            userName = userInfo.getUserName();
            decryptUserName = DESUtil.decrypt(userName, DESUtil.key);
            System.out.println("解密后的用户名:" + decryptUserName);

            userNickName = userInfo.getNickname();
            decryptUserNickName = DESUtil.decrypt(userNickName, DESUtil.key);
            System.out.println("解密后的昵称:" + decryptUserNickName);

        } catch (Exception e) {
            e.printStackTrace();
        }


        tv_userinfo_username.setText(decryptUserName + ":" + decryptUserNickName);


        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                civ_user_portrait.setImageBitmap(bitmap);
            }
        });
        tv_fregment_userinfo_signature.setText(userInfo.getSignature());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
