package uilayouttest.example.com.bigtask.Activity;

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

import cn.jpush.im.android.api.JMessageClient;
import uilayouttest.example.com.bigtask.DESUtil;
import uilayouttest.example.com.bigtask.Entity.TeaCeremony;
import uilayouttest.example.com.bigtask.HomeNewsFragment;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.Tab4Pager;
import uilayouttest.example.com.bigtask.Views.AnimationButton;

public class ShareWorldSentiment extends AppCompatActivity implements View.OnClickListener {

    private LinesEditView share_et;
    private AnimationButton share_btn;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_world_sentiment);


        //沉浸式标题栏
        ImmersionBar.with(this).init();

        share_et = this.findViewById(R.id.activity_share_world_sentiment_et);
//        share_btn = this.findViewById(R.id.activity_share_world_sentiment_btn);
        share_btn = findViewById(R.id.activity_share_world_sentiment_btn);
        share_btn.setAnimationButtonListener(new AnimationButton.AnimationButtonListener() {
            @Override
            public void onClickListener() {

                if (share_et.getContentText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    share_btn.start();
                    // 分享实体
                    TeaCeremony goodsEntity = new TeaCeremony();
                    try {
                        String b = DESUtil.decrypt(JMessageClient.getMyInfo().getUserName(), DESUtil.key);
                        System.out.println("解密后的数据:" + b);
                        goodsEntity.setTeaCeremonyName(b);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                goodsEntity.setImgPath(JMessageClient.getMyInfo().getAvatarBitmap(civ_user_portrait.setImageBitmap(bitmap)));
//                设置图片这句话有问题
//                goodsEntity.setImgPath(Tab4Pager.civ_user_portrait.getImageAlpha());
                    //加在列表中
                    goodsEntity.setContent("\t\t" + share_et.getContentText().toString());

                    HomeNewsFragment.goodsEntityList2.add(goodsEntity);
                }


            }

            @Override
            public void animationFinish() {
                Toast.makeText(ShareWorldSentiment.this, "动画执行完毕", Toast.LENGTH_SHORT).show();
//                finish();
                share_btn.reset();

            }
        });




//        share_btn.setOnClickListener(this);
        back = this.findViewById(R.id.iv_title_bar_back);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_share_world_sentiment_btn:
                Toast.makeText(this,this.share_et.getContentText().toString(), Toast.LENGTH_SHORT).show();

//                // 分享实体
//                TeaCeremony goodsEntity=new TeaCeremony();
//                goodsEntity.setTeaCeremonyName(JMessageClient.getMyInfo().getUserName());
////                goodsEntity.setImgPath(JMessageClient.getMyInfo().getAvatarBitmap(civ_user_portrait.setImageBitmap(bitmap)));
////                设置图片这句话有问题
////                goodsEntity.setImgPath(Tab4Pager.civ_user_portrait.getImageAlpha());
//                //加在列表中
//                goodsEntity.setContent("\t\t"+this.share_et.getContentText().toString());
//
//                HomeNewsFragment.goodsEntityList2.add(goodsEntity);


                break;
            case R.id.iv_title_bar_back:
                ShareWorldSentiment.this.onBackPressed();
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
