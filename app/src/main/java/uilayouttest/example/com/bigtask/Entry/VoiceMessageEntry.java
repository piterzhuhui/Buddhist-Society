package uilayouttest.example.com.bigtask.Entry;



/**
 * Created by 朱慧 on 18-8-10.
 */

public class VoiceMessageEntry extends BaseMessageEntry{

    float time;
    String filePath;
    public VoiceMessageEntry(float time, String filePath) {
        super();
        this.time = time;
        this.filePath = filePath;
    }
    public float getTime() {
        return time;
    }
    public void setTime(float time) {
        this.time = time;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
