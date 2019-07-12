package uilayouttest.example.com.bigtask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.Random;

import uilayouttest.example.com.bigtask.AdapterNew.TeaCeremonyAdapter;
import uilayouttest.example.com.bigtask.AdapterNew.VideoAdapter;
import uilayouttest.example.com.bigtask.AdapterNew.holder.VideoViewHolder;
import uilayouttest.example.com.bigtask.Entity.TeaCeremony;
import uilayouttest.example.com.bigtask.Utils.VideoDataUtil;

/**
 * Created by 朱慧 on 2019/2/20.
 */

public class HomeNewsFragment extends Fragment {

    private String weburl;
    private String channelName;
    private Bundle bundle;

    @Override
    public void onResume() {
        super.onResume();
        this.channelName = bundle.getString("name");

    }

    @Override
    public void setArguments(Bundle bundle) {//接收传入的数据
        weburl = bundle.getString("weburl");
        this.channelName = bundle.getString("name");
        this.bundle = bundle;
    }


    //下面是fragment onAttach方法的坑
    /*
    * onAttach(Context) is not called on pre API 23 versions of Android and onAttach(Activity) is deprecated
    * Use onAttachToContext instead
    */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        //do something
    }
    //    @Override
    //    public void onAttach(Activity activity) {
    //        super.onAttach(activity);
    //    }


    private View view;  //定义view用来设置fragment的layout
    public RecyclerView mCollectRecyclerView;//定义RecyclerView

    //定义以goodsentity实体类为对象的数据集合，悟茶道的
    public static ArrayList<TeaCeremony> goodsEntityList = new ArrayList<TeaCeremony>();

    //定义以goodsentity实体类为对象的数据集合，尝世态的
    public static ArrayList<TeaCeremony> goodsEntityList2 = new ArrayList<TeaCeremony>();

    //自定义recyclerveiw的适配器
    private TeaCeremonyAdapter mCollectRecyclerAdapter;


    //视频播放的recycleview
    private RecyclerView mRecyclerView;


    private RecyclerView recyclerView;

    ////刷新
    private SwipeRefreshLayout swipeRefreshLayout;



    //    步骤2：在onCreateView方法内复用rootview
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {//优化View减少View的创建次数
            //该部分可通过xml文件设计Fragment界面，再通过LayoutInflater转换为View组件
            //这里通过代码为fragment添加一个TextView


            if (this.channelName.equals("品百味")) {
                view = inflater.inflate(R.layout.home_recycler_view_exercise, container, false);
                initExercise();

            } else if (this.channelName.equals("练身体")) {
                view = inflater.inflate(R.layout.home_recycler_view_exercise, container, false);
                initBody();
            } else if (this.channelName.equals("尝世态")) {
                view = inflater.inflate(R.layout.home_recycle_view, container, false);

                swipeRefreshLayout = view.findViewById(R.id.home_tea_swipe_refresh_layout);
                swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshTeaItem();
                    }
                });

                //对recycleview进行配置
                initWorldSentimentRecyclerView();
                //模拟数据
                initWorldSentimentData();
            } else {
                view = inflater.inflate(R.layout.home_recycle_view, container, false);

                swipeRefreshLayout = view.findViewById(R.id.home_tea_swipe_refresh_layout);
                swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshTeaItem();
                    }
                });

                //对recycleview进行配置
                initRecyclerView();
                //模拟数据
                initData();
            }


        }


        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {//如果View已经添加到容器中，要进行删除，负责会报错
            parent.removeView(view);
        }
        return view;
    }

    //刷新处理
    private void refreshTeaItem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }


    //传入模拟数据

    /**
     * TODO 对recycleview进行配置
     */

    private void initRecyclerView() {
        //获取RecyclerView
        mCollectRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        //创建adapter
        mCollectRecyclerAdapter = new TeaCeremonyAdapter(getActivity(), goodsEntityList);
        //给RecyclerView设置adapter
        mCollectRecyclerView.setAdapter(mCollectRecyclerAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        mCollectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        mCollectRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
//        mCollectRecyclerAdapter.setOnItemClickListener(new CollectRecycleAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(View view, GoodsEntity data) {
//                //此处进行监听事件的业务处理
//                Toast.makeText(getActivity(),"我是item", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * TODO 模拟数据
     */
    private void initData() {
        Random random = new Random(47);
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                TeaCeremony goodsEntity = new TeaCeremony();
                //设置悟茶道头像的图片
                goodsEntity.setImgPath(R.mipmap.home_news_users_portrait_1);
//                goodsEntity.setTeaCeremonyName("我是第"+i+"个分享者");
                goodsEntity.setTeaCeremonyName("饮风而醉");
                goodsEntity.setLikeCount(random.nextInt(1000));
                goodsEntity.setHasLike(i % 4 == 1);
                goodsEntity.setContent("\t\t" + "茶道，就是品赏茶的美感之道。亦被视为一种烹茶饮茶的生活艺术，一种以茶为媒的生活礼仪，一种以茶修身的生活方式。它通过沏茶、赏茶、闻茶、饮茶、增进友谊，美心修德，学习礼法，领略传统美德，是很有益的一种和美仪式。喝茶能静心、静神，有助于陶冶情操、去除杂念。茶道，就是品赏茶的美感之道。亦被视为一种烹茶饮茶的生活艺术，一种以茶为媒的生活礼仪，一种以茶修身的生活方式。它通过沏茶、赏茶、闻茶、饮茶、增进友谊，美心修德，学习礼法，领略传统美德，是很有益的一种和美仪式。喝茶能静心、静神，有助于陶冶情操、去除杂念。 ");
                goodsEntityList.add(goodsEntity);
            } else {
                TeaCeremony goodsEntity = new TeaCeremony();
                goodsEntity.setImgPath(R.mipmap.home_news_users_portrait_2);
//                goodsEntity.setTeaCeremonyName("我是第"+i+"个分享者");
                goodsEntity.setTeaCeremonyName("橙子焦糖");
                goodsEntity.setLikeCount(random.nextInt(1000));
                goodsEntity.setHasLike(i % 4 == 1);
                goodsEntity.setContent("\t\t" + "现在人们饮茶，已经不仅仅是解渴解乏，而是一种交际、休闲的方式。这也是如今都市中茶馆大兴的根本原因。喝茶与喝酒不同，酒是越喝越热闹，越喝越兴奋，而茶，则越喝越清醒，越喝越淡泊。饮者不宜多，品格也要高雅。饮茶的最高境界，则是识道。饮茶有道，道在何处？有茶禅一味，有和敬清寂。若要将茶作为一种精神的物质载体，茶道就是饮者通过饮茶这个活动形式，感悟某种人生境界。感悟越深，境界越高。这个过程很难用儒、释、道任何一家的理论来固定。有时候，哪怕只要一点点的灵光感悟，足以开心怀，饮茶乐趣就在于此。");
                goodsEntityList.add(goodsEntity);
            }

        }
    }


//    尝世态
    //传入模拟数据

    /**
     * TODO 对recycleview进行配置
     */

    private void initWorldSentimentRecyclerView() {
        //获取RecyclerView
        mCollectRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        //创建adapter
        mCollectRecyclerAdapter = new TeaCeremonyAdapter(getActivity(), goodsEntityList2);
        //给RecyclerView设置adapter
        mCollectRecyclerView.setAdapter(mCollectRecyclerAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        mCollectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        mCollectRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
//        mCollectRecyclerAdapter.setOnItemClickListener(new TeaCeremonyAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(View view, TeaCeremony data) {
//                //此处进行监听事件的业务处理
////                Toast.makeText(getActivity(),"我是item", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(),HomeNewsFragment.class);
////                intent.putExtra("item_name",onContextItemSelected(this));
//
//            }
//
//        });
    }

    /**
     * TODO 模拟数据
     */
    private void initWorldSentimentData() {
        Random random = new Random(47);
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                TeaCeremony goodsEntity = new TeaCeremony();
                //设置尝世态头像的图片
                goodsEntity.setImgPath(R.mipmap.home_news_users_portrait_3);
//                goodsEntity.setTeaCeremonyName("我是第"+i+"个分享者");
                goodsEntity.setTeaCeremonyName("放歌笙箫");
                goodsEntity.setLikeCount(random.nextInt(1000));
                goodsEntity.setHasLike(i % 4 == 1);
                goodsEntity.setContent("\t\t" + "撑不住的时候，可以对自己说声“我好累”，但永远不要在心里承认说“我不行”。不要在最该奋斗的年纪选择了安逸。没什么好说的，一无所有就是奋斗的理由，我们试着长大，一路跌跌撞撞，然后遍体鳞伤，总有一天，你会站在最亮的地方，活成自己曾经渴望的模样。");
                goodsEntityList2.add(goodsEntity);
            } else {
                TeaCeremony goodsEntity = new TeaCeremony();
                goodsEntity.setImgPath(R.mipmap.home_news_users_portrait_4);
//                goodsEntity.setTeaCeremonyName("我是第"+i+"个分享者");
                goodsEntity.setTeaCeremonyName("青袂婉约");
                goodsEntity.setLikeCount(random.nextInt(1000));
                goodsEntity.setHasLike(i % 4 == 1);
                goodsEntity.setContent("\t\t" + "每个人真正强大起来都要度过一段没人帮忙，没人支持的日子。所有事情都是自己一个人撑，所有情绪都是只有自己知道。但只要咬牙撑过去，一切都不一样了。无论你是谁，无论你正在经历什么，坚持住，你定会看见最坚强的自己。");
                goodsEntityList2.add(goodsEntity);
            }

        }
    }


    //锻炼身体视频播放
    private void initExercise() {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        VideoAdapter adapter = new VideoAdapter(getActivity(), VideoDataUtil.getVideoListData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((VideoViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });


    }


    private void initBody() {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        VideoAdapter adapter = new VideoAdapter(getActivity(), VideoDataUtil.getVideoListData2());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((VideoViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }


    //    步骤1：在onDestroyView方法内把fragment的rootview从viewPager中remove
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mRecyclerView) {
            ((ViewGroup) mRecyclerView.getParent()).removeView(mRecyclerView);
        }
    }



}
