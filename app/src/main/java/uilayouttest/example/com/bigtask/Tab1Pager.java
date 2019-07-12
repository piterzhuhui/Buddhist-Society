package uilayouttest.example.com.bigtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import uilayouttest.example.com.bigtask.Db.ChannelDb;
import uilayouttest.example.com.bigtask.Entity.Channel;

import com.bumptech.glide.Glide;
import com.jpeng.jptabbar.JPTabBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import uilayouttest.example.com.bigtask.AdapterNew.PageFragmentAdapter;
import uilayouttest.example.com.bigtask.Views.CircleImageView;

/**
 * Created by jpeng on 16-11-14.
 */
public class Tab1Pager extends Fragment implements View.OnClickListener, TextWatcher, ViewPager.OnPageChangeListener {

    private EditText mNumberEt;
    private ImageButton mMinusIb, mPlusIb;
    private Button mShowTextBtn, mHideBtn, mShowCircleBtn;
    private JPTabBar mTabBar;
    private Context mContext;

    private UserInfo userInfo;

    //首页ViewPager+Fragment实现顶部导航滑动
    private ViewPager viewPager;
    private RadioGroup rgChannel = null;
    private HorizontalScrollView hvChannel;
    private PageFragmentAdapter adapter = null;
    private List<Fragment> fragmentList = new ArrayList();


    //最上面那个刷新
    private SwipeRefreshLayout swipeRefreshLayout;


    //侧边栏
    private List<Integer> imageList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private TextView card_name_view;
    private NavigationView navigationView;
    View headerView;
    Menu menuView;
    CircleImageView icon_image;
    TextView card_signature;
    MenuItem nav_nickname;
    MenuItem nav_gender;
    MenuItem nav_address;


    //toolbar头像
    ActionBar actionbar;

    View myZhuYeView;
    List<Integer> ImageUrlData;//注意坑在这里 我之前写的是 List<String> ImageUrlData因为Glide.with(getActivity()).load(url).into(imageView);load里面需要整型
    List<String> ContentData;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


//        View layout = inflater.inflate(R.layout.tab1, null);
//        init(layout);
//        return layout;
        this.mContext = getActivity();
        myZhuYeView = LayoutInflater.from(getActivity()).inflate(R.layout.tab1, container, false);
//        View myZhuYeView = inflater.inflate(R.layout.tab1, null);


//        状态栏与toolbar颜色一致
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }


        Toolbar toolbar = (Toolbar) myZhuYeView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) myZhuYeView.findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) myZhuYeView.findViewById(R.id.nav_view);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (actionbar != null) {
//            actionbar.setDisplayHomeAsUpEnabled(true);
//            //设置显示图标
//            actionbar.setHomeAsUpIndicator(R.mipmap.cat);
//        }

        navView.setCheckedItem(R.id.nav_nickname);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //点击菜单项关闭侧滑视图
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //轮播图初始化
        initBanner();

        //实现首页导航切换初始化
        initView();


        // 侧边栏
        //名片夹改变
        navigationView = myZhuYeView.findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        menuView = navigationView.getMenu();
        card_name_view = headerView.findViewById(R.id.home_card_name);
        nav_nickname = menuView.findItem(R.id.nav_nickname);
        nav_gender = menuView.findItem(R.id.nav_gender);
        nav_address = menuView.findItem(R.id.nav_address);
        UserInfo userInfo = JMessageClient.getMyInfo();
//        姓名和昵称解密
        try {
            String b = DESUtil.decrypt(userInfo.getUserName().toString(), DESUtil.key);
            System.out.println("解密后的数据:" + b);
            card_name_view.setText(b);
            String nickname = DESUtil.decrypt(userInfo.getNickname().toString(), DESUtil.key);
            nav_nickname.setTitle(nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        性别
        if(userInfo.getGender()!= null){
            if (userInfo.getGender().toString().equals("male")) {
                nav_gender.setTitle("男");
            } else if (userInfo.getGender().toString().equals("female")) {
                nav_gender.setTitle("女");
            } else {
                nav_gender.setTitle("");
            }
        }else{
            nav_gender.setTitle("");
        }




//        地址
        nav_address.setTitle(userInfo.getAddress().toString());
        //头像
        icon_image = headerView.findViewById(R.id.icon_image);
        //昵称
        card_signature = headerView.findViewById(R.id.card_signature);

        setHasOptionsMenu(true);
//        initdata();

        //最上面那个刷新
        swipeRefreshLayout = (SwipeRefreshLayout) myZhuYeView.findViewById(R.id.base_swipe_refresh_widget);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        //设置不可见
        swipeRefreshLayout.setEnabled(false);

        return myZhuYeView;


    }

    //首页那四个功能导航切换
    private void initView() {
        rgChannel = myZhuYeView.findViewById(R.id.rgChannel);
        viewPager = myZhuYeView.findViewById(R.id.vpNewsList);
        hvChannel = myZhuYeView.findViewById(R.id.hvChannel);
        rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        viewPager.setCurrentItem(checkedId);
                    }
                });

        viewPager.addOnPageChangeListener(this);

        //设置ViewPager的缓存界面数，避免缓存界面太多导致内存吃紧。
        viewPager.setOffscreenPageLimit(3);

        initTab();
        initViewPager();
        rgChannel.check(0);

    }

    private void initTab() {
        List<Channel> channelList = ChannelDb.getSelectedChannel();
        for (int i = 0; i < channelList.size(); i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(getActivity()).
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i).getName());
            RadioGroup.LayoutParams params = new
                    RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);

            rgChannel.addView(rb, params);
        }

    }

    private void initViewPager() {
        List<Channel> channelList = ChannelDb.getSelectedChannel();
        for (int i = 0; i < channelList.size(); i++) {
            HomeNewsFragment frag = new HomeNewsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("weburl", channelList.get(i).getWeburl());
            bundle.putString("name", channelList.get(i).getName());
            frag.setArguments(bundle);
            fragmentList.add(frag);
        }
        adapter = new PageFragmentAdapter(super.getFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(0);
    }


    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     * //     * @param idx
     */
    private void setTab(int idx) {
        RadioButton rb = (RadioButton) rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        super.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        setTab(position);
    }


    //toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.backup:
//                Toast.makeText(mContext, "You clicked BackUp", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.delete:
//                Snackbar.make(myZhuYeView, "Date deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(mContext, "Data restoryed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                break;
            case R.id.setting:
                Toast.makeText(mContext, "You clicked setting", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    //    private void initdata(){
//        TextView textView = myZhuYeView.findViewById(R.id.)
//
//    }

    //轮播图初始化
    private void initBanner() {
//        Toolbar toolbar = (Toolbar)myZhuYeView.findViewById(R.id.toolbar);

        Banner myBanner = (Banner) myZhuYeView.findViewById(R.id.banner);

        ImageUrlData = new ArrayList<>();
        ContentData = new ArrayList<>();
        ImageUrlData.add(R.mipmap.home_1);
        ImageUrlData.add(R.mipmap.home_2);
        ImageUrlData.add(R.mipmap.home_3);
        ImageUrlData.add(R.mipmap.home_4);
        ContentData.add("茶道,产自峨眉高山绿茶,属于明前茶,非常受爱茶人士的欢迎.茶道有3个等级,分别的是论道级,静心级,品味级.");
        ContentData.add("生命的意义在于时刻运动，运动使身体更加的强壮，远离病魔的困扰，能够强身健体。");
        ContentData.add("成长，本身就是一个很痛的词。你想要的或好或坏，时间都会给你");
        ContentData.add("美食，顾名思义就是美味的食物，贵的有山珍海味，便宜的有街边小吃。其实美食是不分贵贱的，只要是自己喜欢的，都可以称之为美食。");

        myBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        myBanner.setImageLoader(new MyLoader());
        myBanner.setImages(ImageUrlData);
        myBanner.setBannerTitles(ContentData);
        myBanner.setBannerAnimation(Transformer.Default);
        myBanner.setDelayTime(3000);
        myBanner.isAutoPlay(true);
        myBanner.setIndicatorGravity(BannerConfig.CENTER);
        myBanner.start();

    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
//Glide设置图片的简单用法
            Glide.with(getActivity()).load(path).into(imageView);

        }

    }


    @Override
    public void onClick(View v) {
        int count = Integer.parseInt(mNumberEt.getText().toString());
        if (v == mMinusIb) {
            count--;
            mNumberEt.setText(count + "");
        } else if (v == mPlusIb) {
            count++;
            mNumberEt.setText(count + "");
        } else if (v == mShowTextBtn) {
            mTabBar.showBadge(0, "文字", true);
        } else if (v == mShowCircleBtn) {
            mTabBar.showCircleBadge(0, true);
        } else {
            mTabBar.hideBadge(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null && s.toString().equals("0")) {
            mTabBar.showBadge(0, "" + 0, true);
            mTabBar.hideBadge(0);
            return;
        }
        if (s.toString().equals("")) {
            mTabBar.showBadge(0, "" + 0, true);
            return;
        }
        int count = Integer.parseInt(s.toString());
        if (mTabBar != null)
            mTabBar.showBadge(0, count + "", true);
    }

    public void clearCount() {
        //当徽章拖拽爆炸后,一旦View被销毁,不判断就会空指针异常
        if (mNumberEt != null)
            mNumberEt.setText("0");
    }


    @Override
    public void onResume() {
        super.onResume();

        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        userInfo = JMessageClient.getMyInfo();
//        tv_userinfo_username.setText(userInfo.getUserName() + ":" + userInfo.getNickname());
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                icon_image.setImageBitmap(bitmap);
                if (actionbar != null) {
                    actionbar.setDisplayHomeAsUpEnabled(true);
                    //设置显示图标
                    if (icon_image.getBackground() == null) {
                        actionbar.setHomeAsUpIndicator(R.mipmap.cat);
                    } else {
                        actionbar.setHomeAsUpIndicator(icon_image.getDrawable());
                    }

                }
            }
        });
        card_signature.setText(userInfo.getSignature());


    }
}
