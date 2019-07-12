package uilayouttest.example.com.bigtask.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import uilayouttest.example.com.bigtask.R;

public class HomeTeaCeremonyDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_user_info_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tea_ceremony_detail);

        //沉浸式标题栏
        ImmersionBar.with(this).init();


        //接收数据
        Bundle bundle=getIntent().getExtras();


        //接收名字
        String name=bundle.getString("item_name");
        TextView tea_name=(TextView) findViewById(R.id.item_detail_tea_name);
        tea_name.setText(name);

        //接收内容
        String message=bundle.getString("item_content");
        TextView tea_content=(TextView) findViewById(R.id.item_detail_tea_content);
        tea_content.setText("\t\t"+message);

        //接收图片
        Integer url = bundle.getInt("item_photo");
        ImageView img = (ImageView) findViewById(R.id.item_detail_tea_image);
        Glide.with(HomeTeaCeremonyDetailActivity.this)
                .load(url)
                .into(img);


//设置标题栏数据
        init();
    }


    //设置标题栏数据
    private void init() {
        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_bar_back:
                HomeTeaCeremonyDetailActivity.this.onBackPressed();
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
        this.finish();
    }
}
