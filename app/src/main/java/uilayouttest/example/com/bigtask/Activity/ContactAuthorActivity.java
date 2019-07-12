package uilayouttest.example.com.bigtask.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import uilayouttest.example.com.bigtask.AdapterNew.ContactAuthorViewPagerAdapter;
import uilayouttest.example.com.bigtask.R;



/**
 * Created by 朱慧 on 18-5-6.
 */

public class ContactAuthorActivity extends AppCompatActivity {

    int[] imageResIds = new int[]{R.mipmap.contact_author_1, R.mipmap.contact_author_2, R.mipmap.contact_author_3, R.mipmap.contact_author_4};

    private ViewPager viewPager;

    private ImageView iv_user_info_back;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();

        setContentView(R.layout.activity_contact_author);

        init();


    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_contact_author);
        viewPager.setAdapter(new ContactAuthorViewPagerAdapter(this, imageResIds));

        iv_user_info_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_user_info_back.setVisibility(View.VISIBLE);
        iv_user_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % imageResIds.length);
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
    }

}
