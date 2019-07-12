package uilayouttest.example.com.bigtask.Activity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uilayouttest.example.com.bigtask.AdapterNew.ChatRecyclerViewAdapter;
import uilayouttest.example.com.bigtask.DESUtil;
import uilayouttest.example.com.bigtask.Entry.BaseMessageEntry;
import uilayouttest.example.com.bigtask.Entry.MessageEntry;
import uilayouttest.example.com.bigtask.Entry.TextMessageEntry;
import uilayouttest.example.com.bigtask.Entry.VoiceMessageEntry;
import uilayouttest.example.com.bigtask.Interface.ItemClickListener;
import uilayouttest.example.com.bigtask.R;
import uilayouttest.example.com.bigtask.EasyLifeApp;
import uilayouttest.example.com.bigtask.EasyLifeConstant;
import uilayouttest.example.com.bigtask.Utils.DeleteFileUtil;
import uilayouttest.example.com.bigtask.Utils.MediaManager;
import uilayouttest.example.com.bigtask.Utils.MySQLiteOpenHelper;
import uilayouttest.example.com.bigtask.Utils.OkhttpHelper;
import uilayouttest.example.com.bigtask.Utils.RecognizerHelper;
import uilayouttest.example.com.bigtask.Utils.SpeechSynthesizerUtil;
import uilayouttest.example.com.bigtask.Utils.TranslateUtil;
import uilayouttest.example.com.bigtask.Views.AudioRecorderButton;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.iflytek.cloud.SpeechRecognizer;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.MessageSendingOptions;

//聊天
import cn.jpush.im.api.BasicCallback;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by 朱慧 on 18-5-1.
 */

public class ChatActivity extends AppCompatActivity implements Callback, ItemClickListener {

    private String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    //防止在不同线程
    private List<BaseMessageEntry> datas;
    private ChatRecyclerViewAdapter adapter;

    private TextView tv_send;
    private EditText et_input_text;

    private OkhttpHelper okhttpHelper;

    private ImageButton btn_voice_chat, btn_btn_select_language;

    private AudioRecorderButton mAudioRecorderButton;

    private boolean isVoiceMode = false;

    private boolean isChinese = true;

    private String friendUsername;


    private String receiver_text;

    List<Conversation> conversations;
    Conversation conversation;

    private TextView tv_title_bar_center;
    private ImageView iv_title_bar_back;

    private RecognizerHelper recognizerHelper;
    private SpeechRecognizer speechRecognizer;

    private int clickPosition;

    private MySQLiteOpenHelper mySQLiteOpenHelper;


    private FFmpeg fFmpeg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        状态栏与toolbar颜色一致
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("username");
        friendUsername = bundle.getString("username");
        setContentView(R.layout.activity_chat);


        //注册接收者
        conversations = JMessageClient.getConversationList();
        conversation = JMessageClient.getSingleConversation(friendUsername, "");


        initAudio();
        sendMessage();
        init();

    }


    private void sendMessage() {
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextMessageEntry messageEntry = new TextMessageEntry();
                messageEntry.setUserName(JMessageClient.getMyInfo().getUserName());
                messageEntry.setContent(et_input_text.getText().toString());
                if (friendUsername.equals(EasyLifeConstant.APPNAME)) {
                    okhttpHelper.postToTuLingRobot(et_input_text.getText().toString(), "123456");
                }
                messageEntry.setViewType(TextMessageEntry.SENDMESSAGE);
                datas.add(messageEntry);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size() - 1);


                MessageContent content = new TextContent(et_input_text.getText().toString());
                //创建一条消息
                Message message = conversation.createSendMessage(content);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i(TAG, "发送结果: i=" + i + ",s=" + s);
                    }
                });
                MessageSendingOptions options = new MessageSendingOptions();
                options.setRetainOffline(false);
                //发送消息
                JMessageClient.sendMessage(message);
                et_input_text.setText("");


            }
        });
    }

    private void initAudio() {

        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                VoiceMessageEntry voiceMessageEntry = new VoiceMessageEntry(seconds, filePath);
                Log.i(TAG, filePath);
                voiceMessageEntry.setUserName(JMessageClient.getMyInfo().getUserName());
                voiceMessageEntry.setViewType(BaseMessageEntry.SENDMESSAGE);
                try {
                    Message voiceMessage = JMessageClient.createSingleVoiceMessage(friendUsername, null, new File(filePath), (int) seconds);
                    voiceMessage.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Log.i(TAG, "发送语音成功");
                            }
                        }
                    });
                    JMessageClient.sendMessage(voiceMessage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                datas.add(voiceMessageEntry);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size() - 1);
            }
        });


    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void init() {


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.i(TAG, "当前用户名为：" + EasyLifeApp.USERNAME);


        adapter = new ChatRecyclerViewAdapter(this, JMessageClient.getMyInfo().getUserName(), friendUsername);
        adapter.setOnItemClickListener(this);

        datas = new ArrayList<>();

        okhttpHelper = new OkhttpHelper();
        okhttpHelper.setCallback(this);


        et_input_text = (EditText) findViewById(R.id.et_input_text);


        btn_voice_chat = (ImageButton) findViewById(R.id.btn_voice_chat);
        btn_voice_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoiceMode) {
                    et_input_text.setVisibility(View.VISIBLE);
                    mAudioRecorderButton.setVisibility(View.GONE);
                    isVoiceMode = !isVoiceMode;
                } else {
                    et_input_text.setVisibility(View.GONE);
                    mAudioRecorderButton.setVisibility(View.VISIBLE);
                    isVoiceMode = !isVoiceMode;
                    //关闭弹出的键盘
                    closeKeyboard();
                }

            }
        });

        btn_btn_select_language = (ImageButton) findViewById(R.id.btn_select_language);
        btn_btn_select_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChinese) {
                    isChinese = false;
                    btn_btn_select_language.setBackground(getResources().getDrawable(R.drawable.english));
                } else {
                    isChinese = true;
                    btn_btn_select_language.setBackground(getResources().getDrawable(R.drawable.chinese));
                }
            }
        });

        adapter.setDatas(datas);
        recyclerView.setAdapter(adapter);


        //创建跨应用会话
        conversation = Conversation.createSingleConversation(friendUsername, null);
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        // 这里解密
        try {
            String b = DESUtil.decrypt(friendUsername, DESUtil.key);
            System.out.println("解密后的数据:" + b);
            tv_title_bar_center.setText(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iv_title_bar_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_title_bar_back.setVisibility(View.VISIBLE);
        iv_title_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);
        recognizerHelper = new RecognizerHelper(speechRecognizer);

        try {
            String userName = DESUtil.decrypt(friendUsername, DESUtil.key);
            System.out.println("解密后的数据:" + userName);

            String sql = "create table if not exists " + userName + " (type integer,username text,content text,RecOrSend integer,VoiceTime real,VoicePath text)";
            mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
            SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
            db.execSQL(sql);
            String sql2 = "select * from " + userName;
            Cursor cursor = db.rawQuery(sql2, null);
            while (cursor.moveToNext()) {
                int MessageType = cursor.getInt(cursor.getColumnIndex("type"));
                if (MessageType == MySQLiteOpenHelper.MessageTextType) {
                    TextMessageEntry messageEntry = new TextMessageEntry();
                    messageEntry.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                    messageEntry.setContent(cursor.getString(cursor.getColumnIndex("content")));
                    messageEntry.setViewType(cursor.getInt(cursor.getColumnIndex("RecOrSend")));
                    datas.add(messageEntry);
                } else if (MessageType == MySQLiteOpenHelper.MessageVoiceType) {
                    float seconds = cursor.getFloat(cursor.getColumnIndex("VoiceTime"));
                    String filePath = cursor.getString(cursor.getColumnIndex("VoicePath"));
                    VoiceMessageEntry voiceMessageEntry = new VoiceMessageEntry(seconds, filePath);
                    Log.i(TAG, filePath);
                    voiceMessageEntry.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                    voiceMessageEntry.setViewType(cursor.getInt(cursor.getColumnIndex("RecOrSend")));
                    datas.add(voiceMessageEntry);
                }
            }
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(datas.size() - 1);
            db.close();

            fFmpeg = FFmpeg.getInstance(this);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(Call call, IOException e) {
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String answer = response.body().string();
        TextMessageEntry messageEntry = new TextMessageEntry();
        messageEntry.setUserName(friendUsername);
        messageEntry.setContent(okhttpHelper.parseTuLingResult(answer));
        messageEntry.setViewType(TextMessageEntry.RECEIVEMESSAGE);
        datas.add(messageEntry);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size() - 1);

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        // 解密
        try {
            String userName = DESUtil.decrypt(friendUsername, DESUtil.key);
            System.out.println("解密后的数据:" + userName);
            db.delete(userName, null, null);
            for (BaseMessageEntry entry : datas) {
                ContentValues contentValues = new ContentValues();
                if (entry instanceof VoiceMessageEntry) {
                    contentValues.put("type", MySQLiteOpenHelper.MessageVoiceType);
                    contentValues.put("username", entry.getUserName());
                    contentValues.put("RecOrSend", entry.getViewType());
                    contentValues.put("VoicePath", ((VoiceMessageEntry) entry).getFilePath());
                    contentValues.put("VoiceTime", ((VoiceMessageEntry) entry).getTime());
                } else if (entry instanceof TextMessageEntry) {
                    contentValues.put("type", MySQLiteOpenHelper.MessageTextType);
                    contentValues.put("username", entry.getUserName());
                    contentValues.put("RecOrSend", entry.getViewType());
                    contentValues.put("content", ((TextMessageEntry) entry).getContent());
                }
                db.insertOrThrow(userName, null, contentValues);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void OnItemClick(View v, final int position) {
        if (datas.get(position) instanceof VoiceMessageEntry) {
            MediaManager.playSound(((VoiceMessageEntry) datas.get(position)).getFilePath(), null);
            clickPosition = position;
            if (((VoiceMessageEntry) datas.get(position)).getViewType() == VoiceMessageEntry.RECEIVEMESSAGE) {
                ((VoiceMessageEntry) datas.get(position)).getFilePath();

                DeleteFileUtil.deleteFile(getCacheDir() + "/receiverReconizer.wav");
                try {

                    /**
                     * ffmpeg -i INPUT -ac CHANNELS -ar FREQUENCY -acodec PCMFORMAT OUTPUT

                     INPUT : mp3文件,假如音频流没有任何的声音也会导致转换失败。

                     CHANNELS ：值得选项为1和2

                     PCMFORMAT ：值得选项为pcm_u8，pcm_s16le ，pcm_s16be，pcm_u16le，pcm_u16be

                     FREQUENCY ：8000，11025 ，22050，44100

                     例如：ffmpeg -i F:\test\dizi.mp3 -ar 44100 -ac 1 -acodec pcm_u8 F:\test\dizi.wav

                     输出44100采样率，1个声道，8bits的wav文件  注意查看log，找出转换失败的原因
                     */
                    String[] commands = new String[9];
                    commands[0] = "-i";
                    commands[1] = ((VoiceMessageEntry) datas.get(position)).getFilePath();
                    commands[2] = "-ac";
                    commands[3] = "1";
                    commands[4] = "-ar";
                    //改为16KHZ的识别率高很多,官方是指定8K和16K
                    commands[5] = "22050";
                    commands[6] = "-acodec";
                    //四种格式仅仅只有这种可以
                    commands[7] = "pcm_s16le";
                    commands[8] = getCacheDir() + "/receiverReconizer.wav";
                    fFmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
                        @Override
                        public void onSuccess(String message) {
                            super.onSuccess(message);
                            Log.i(TAG, "执行ffmpeg成功：" + message);
                            recognizerHelper.recognizeStream(getCacheDir() + "/receiverReconizer.wav");
                        }

                        @Override
                        public void onProgress(String message) {
                            super.onProgress(message);
                            Log.i(TAG, "正在执行ffmpeg：" + message);
                        }

                        @Override
                        public void onFailure(String message) {
                            super.onFailure(message);
                            Log.i(TAG, "执行ffmpeg失败：" + message);
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            Log.i(TAG, "开始执行ffmpeg");
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            Log.i(TAG, "执行ffmpeg完成");
                        }
                    });
                } catch (FFmpegCommandAlreadyRunningException e) {
                    e.printStackTrace();
                }

            } else {
                recognizerHelper.recognizerFromAmr(((VoiceMessageEntry) datas.get(position)).getFilePath());

            }

        } else if (datas.get(position) instanceof TextMessageEntry) {
            String fromLanguage, toLanguage;
            if (isChinese) {
                fromLanguage = "英文";
                toLanguage = "中文";
            } else {
                fromLanguage = "中文";
                toLanguage = "英文";
            }
            String content = ((TextMessageEntry) datas.get(position)).getContent();
            TranslateUtil.translate(fromLanguage, toLanguage, content, new TranslateListener() {
                @Override
                public void onError(TranslateErrorCode translateErrorCode, String s) {

                }

                @Override
                public void onResult(Translate translate, String s, String s1) {
                    if (translate.getTranslations().size() > 0) {
                        ((TextMessageEntry) datas.get(position)).setContent(translate.getTranslations().get(0));
                        SpeechSynthesizerUtil.getInstance().speakText(translate.getTranslations().get(0));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
            });
        }
    }

}
