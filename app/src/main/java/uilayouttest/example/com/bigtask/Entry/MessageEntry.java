package uilayouttest.example.com.bigtask.Entry;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by 朱慧 on 18-8-10.
 */

public class MessageEntry {

    private String username;
    private String nickname;
    private String content;
    private String time;
    private Message message;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
