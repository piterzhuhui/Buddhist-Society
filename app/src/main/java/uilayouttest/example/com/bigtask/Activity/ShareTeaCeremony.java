package uilayouttest.example.com.bigtask.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.classichu.lineseditview.LinesEditView;
import com.gyf.barlibrary.ImmersionBar;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import uilayouttest.example.com.bigtask.DESUtil;
import uilayouttest.example.com.bigtask.Entity.TeaCeremony;
import uilayouttest.example.com.bigtask.HomeNewsFragment;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.Tab1Pager;
import uilayouttest.example.com.bigtask.Tab4Pager;
import uilayouttest.example.com.bigtask.Views.AnimationButton;

public class ShareTeaCeremony extends AppCompatActivity implements View.OnClickListener {

    private LinesEditView share_et;
    private Button share_btn;
    private ImageView back;


    //    动画按钮
    private AnimationButton animationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_tea_ceremony);


        //沉浸式标题栏
        ImmersionBar.with(this).init();

        share_et = this.findViewById(R.id.activity_share_tea_ceremony_et_lines);
        share_et.setHintText("在这里分享你的茶道心得吧。。。。。。");

        back = this.findViewById(R.id.iv_title_bar_back);
        back.setOnClickListener(this);


               animationButton = findViewById(R.id.animation_btn);
        animationButton.setAnimationButtonListener(new AnimationButton.AnimationButtonListener() {
            @Override
            public void onClickListener() {

                if (share_et.getContentText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    animationButton.start();
//                Toast.makeText(getAc, share_et.getContentText().toString(), Toast.LENGTH_SHORT).show();
                    Log.i("这是分享消息", share_et.getContentText().toString());

                    // 分享实体
                    TeaCeremony goodsEntity = new TeaCeremony();
//                    这里解密
                    try {
                        String b = DESUtil.decrypt(JMessageClient.getMyInfo().getUserName(), DESUtil.key);
                        System.out.println("解密后的数据:" + b);
                        goodsEntity.setTeaCeremonyName(b);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


//                goodsEntity.setImgPath(JMessageClient.getMyInfo().getAvatarBitmap(civ_user_portrait.setImageBitmap(bitmap)));
//                设置图片这句话有问题
//                    goodsEntity.setImgPath(Tab4Pager.civ_user_portrait.getDrawable());


                    //加在列表中

                    goodsEntity.setContent("\t\t" + share_et.getContentText().toString());


                    HomeNewsFragment.goodsEntityList.add(goodsEntity);
                }


            }

            @Override
            public void animationFinish() {
                Toast.makeText(ShareTeaCeremony.this, "动画执行完毕", Toast.LENGTH_SHORT).show();
//                finish();
                animationButton.reset();

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.activity_share_tea_ceremony_btn:
//                Toast.makeText(this, this.share_et.getContentText().toString(), Toast.LENGTH_SHORT).show();
//
//                // 分享实体
//                TeaCeremony goodsEntity = new TeaCeremony();
//                goodsEntity.setTeaCeremonyName(JMessageClient.getMyInfo().getUserName());
////                goodsEntity.setImgPath(JMessageClient.getMyInfo().getAvatarBitmap(civ_user_portrait.setImageBitmap(bitmap)));
////                设置图片这句话有问题
//                goodsEntity.setImgPath(Tab4Pager.civ_user_portrait.getImageAlpha());
//                //加在列表中
//                goodsEntity.setContent("\t\t" + this.share_et.getContentText().toString());
//
//
//                HomeNewsFragment.goodsEntityList.add(goodsEntity);

//                break;
            case R.id.iv_title_bar_back:
                ShareTeaCeremony.this.onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }
}
