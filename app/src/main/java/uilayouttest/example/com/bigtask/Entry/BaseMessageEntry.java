package uilayouttest.example.com.bigtask.Entry;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by 朱慧 on 18-8-10.
 */

public class BaseMessageEntry implements IMessage {
    public final static int SENDMESSAGE=0;
    public final static int RECEIVEMESSAGE=1;
    private String userName;
    private int viewType=1;



    private long id;
    private String text;
    private String timeString;
    private MessageType type;
    private IUser user;
    private String mediaFilePath;
    private long duration;
    private String progress;
    private Message message;
    private int position;
    private long msgID;



    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String getMsgId() {
        return String.valueOf(id);
    }

    @Override
    public IUser getFromUser() {
        if (user == null) {
//            return new DefaultUser("0", "user1", null);
        }
        return user;
    }
    @Override
    public long getDuration() {
        return duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String getProgress() {
        return progress;
    }

    @Override
    public MessageType getType() {
        return type;
    }
    @Override
    public String getTimeString() {
        return timeString;
    }
    @Override
    public MessageStatus getMessageStatus() {
        return MessageStatus.SEND_SUCCEED;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getMediaFilePath() {
        return mediaFilePath;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", timeString='" + timeString + '\'' +
                ", type=" + type +
                ", user=" + user +
                ", mediaFilePath='" + mediaFilePath + '\'' +
                ", duration=" + duration +
                ", progress='" + progress + '\'' +
                ", position=" + position +
                ", msgID=" +  msgID+
                '}';
    }

}
