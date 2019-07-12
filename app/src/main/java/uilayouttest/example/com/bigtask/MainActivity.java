package uilayouttest.example.com.bigtask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import uilayouttest.example.com.bigtask.Activity.ShareTeaCeremony;
import uilayouttest.example.com.bigtask.Activity.ShareWorldSentiment;
import uilayouttest.example.com.bigtask.Entity.PathItem;

import static org.litepal.LitePalApplication.getContext;

import static uilayouttest.example.com.bigtask.R.id.tabbar;


public class MainActivity extends AppCompatActivity implements BadgeDismissListener, OnTabSelectListener {
    //这里是侧滑菜单


    @Titles
    private static final int[] mTitles = {R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4};

    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.tab1_selected, R.mipmap.tab2_selected, R.mipmap.tab3_selected, R.mipmap.tab4_selected};

    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal};

    //弹出菜单
    private List<PathItem> pathItemList;


    private List<Fragment> list = new ArrayList<>();

    private ViewPager mPager;

    private JPTabBar mTabbar;

    private Tab1Pager mTab1;

    private Tab2Pager mTab2;

    private Tab3Pager mTab3;

    private Tab4Pager mTab4;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JMessageClient.registerEventReceiver(this);

        mTabbar = (JPTabBar) findViewById(tabbar);
        mPager = (ViewPager) findViewById(R.id.view_pager);

        Toast.makeText(this, "头像请前往个人中心进行设置哦", Toast.LENGTH_SHORT).show();

        mTabbar.setTabTypeFace("fonts/Jaden.ttf");
        mTab1 = new Tab1Pager();
        mTab2 = new Tab2Pager();
        mTab3 = new Tab3Pager();
        mTab4 = new Tab4Pager();
        mTabbar.setGradientEnable(true);
        mTabbar.setPageAnimateEnable(true);
        mTabbar.setTabListener(this);
        list.add(mTab1);
        list.add(mTab2);
        list.add(mTab3);
        list.add(mTab4);

        mPager.setAdapter(new Adapter(getSupportFragmentManager(), list));
        mTabbar.setContainer(mPager);
        //设置Badge消失的代理
        mTabbar.setDismissListener(this);
        mTabbar.setTabListener(this);

        //弹出动画框
        pathItemList = new ArrayList<>();
        pathItemList.add(new PathItem().name("悟茶道").imageResId(R.drawable.ic_snyg).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new PathItem().name("尝世态").imageResId(R.drawable.ic_tmgj).backgroundResId(R.drawable.bg_blue_oval));

        //弹出动画框
        if (mTabbar.getMiddleView() != null)
            mTabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"中间点击",Toast.LENGTH_SHORT).show();;
                    PathPopupWindow popupWindow = new PathPopupWindow(MainActivity.this, pathItemList);
                    popupWindow.setOnPathItemClickListener(new PathPopupWindow.OnPathItemClickListener() {
                        @Override
                        public void onItemClick(int position, PathItem item) {
//                        Toast.makeText(MainActivity.this,"点击了--->"+item.name,Toast.LENGTH_LONG).show();

                            switch (item.name) {
                                case "悟茶道":
                                    Intent intent = new Intent(MainActivity.this, ShareTeaCeremony.class);
                                    startActivity(intent);
                                    break;
                                case "尝世态":
                                    Intent intent3 = new Intent(MainActivity.this, ShareWorldSentiment.class);
                                    startActivity(intent3);
                                    break;
                            }

                        }
                    });
                    popupWindow.show(v);
                }
            });


        // “2” 表示缓存当前界面每一侧的界面数
        mPager.setOffscreenPageLimit(3);


    }

    @Override
    public void onDismiss(int position) {
        mTab1.clearCount();
    }


    @Override
    public void onTabSelect(int index) {
//        Toast.makeText(MainActivity.this,"choose the tab index is "+index,Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onInterruptSelect(int index) {
        return false;
    }

    public JPTabBar getTabbar() {
        return mTabbar;
    }


    AlertDialog dialog;
    private TextView vertifyView;
    private CountDownTimer timer;

    //    这是消息接收弹出框dialog
    public void showCustomizeDialog(String msg) {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        final AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.dialog_receive_message, null);
        TextView v = dialogView.findViewById(R.id.dialog_content);
        v.setText(msg);
        vertifyView = dialogView.findViewById(R.id.mOffTextView);

        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("我已阅读，确认焚毁",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        dialog.dismiss();
                    }
                });

        this.dialog = customizeDialog.show();

//        设置一个定时器，10秒
        timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                vertifyView.setText((millisUntilFinished / 1000)+"秒" );
            }

            @Override
            public void onFinish() {
                vertifyView.setEnabled(true);
                dialog.dismiss();
            }
        };

        timer.start();


////        设置一个定时器,但是这个不能显示时间
//        final Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                t.cancel();
//                dialog.dismiss();
//                return;
//            }
//        },5000);


    }


    //    这是锁住的消息弹出框
    AlertDialog dialog2;
    String msg_dialog_message;
    public void showDialogMessageLock(String msg) {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        final AlertDialog.Builder messageLockDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.dialog_message_lock, null);

        msg_dialog_message = msg;
        messageLockDialog.setView(dialogView);


        messageLockDialog.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog2.dismiss();
                        showCustomizeDialog(msg_dialog_message);
                    }

                });

        messageLockDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog2.dismiss();
                    }
                });

        this.dialog2 = messageLockDialog.show();



    }


    //    这是可以用的消息事件接收
    String decrypt_friend_name;
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                final TextContent textContent = (TextContent) msg.getContent();
                final String friend_name = (String) msg.getFromUser().getUserName();
                try {
                    decrypt_friend_name =  DESUtil.decrypt(friend_name, DESUtil.key);
                    System.out.println("MainActivity:" + decrypt_friend_name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textContent.getText();
                Log.i("这是测试的消息事件", textContent.getText());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialogMessageLock("好友：" + decrypt_friend_name + "，内容：" + textContent.getText());

                    }
                });
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                Log.i("MainActivity","收到一条语音消息");
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;
        }
    }


    //    //子线程模式
    private String username;
    public void onEvent(final ContactNotifyEvent event) {
        String reason = event.getReason();
        final String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        Log.i("这是好友测试",event.getReason());
        Log.i("这是好友测试",event.getType().toString());


        try {
            username = DESUtil.decrypt(event.getFromUsername(), DESUtil.key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("这是好友测试",username);
        Toast.makeText(this,"收到好友"+username+"请求，如需添加，请自行前往好友列表添加",Toast.LENGTH_LONG).show();
        switch (event.getType()) {
            case invite_received://收到好友邀请
                Toast.makeText(getContext(),"收到好友"+username+"请求",Toast.LENGTH_SHORT).show();
                        AlertDialog dialogReceived = new AlertDialog.Builder(getContext())
                                .setIcon(R.mipmap.icon)//设置标题的图片
                                .setTitle(getString(R.string.friend_invite_received))//设置对话框的标题
                                .setMessage(fromUsername + getString(R.string.friend_invite_tip))//设置对话框的内容
                                //设置对话框的按钮
                                .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ContactManager.declineInvitation(fromUsername, null, "", null);
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ContactManager.acceptInvitation(fromUsername, null, new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                if (i == 0) {
                                                }
                                            }
                                        });

                                        dialog.dismiss();
                                    }
                                }).create();
                        //在dialog  show方法之前添加如下代码，表示该dialog是一个系统的dialog**
                        dialogReceived.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                        dialogReceived.show();
                break;
            case invite_accepted://对方接收了你的好友邀请
                AlertDialog dialogAccept = new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.add_friends))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_accept))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialogAccept.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialogAccept.show();
                break;
            case invite_declined://对方拒绝了你的好友邀请
                AlertDialog dialogReject = new AlertDialog.Builder(getContext())
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.add_friends))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_reject))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialogReject.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialogReject.show();
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                Log.i(TAG, "对方将你从好友中删除");
                break;
            default:
                break;
        }

    }


}
