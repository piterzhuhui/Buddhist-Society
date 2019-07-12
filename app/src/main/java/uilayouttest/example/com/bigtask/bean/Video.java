package uilayouttest.example.com.bigtask.bean;

/**
 * Created by XiaoJianjun on 2017/5/21.
 * 视频
 */
public class Video {
    private String title;
    private long length;
    private String imageUrl;
    private String videoUrl;

    private int imageUrl2;
    private String videoUrl2;

    public Video(String title, long length,int imageUrl, String videoUrl) {
        this.title = title;
        this.length = length;
        this.imageUrl2 = imageUrl;
        this.videoUrl2 = videoUrl;
    }


    public Video(String title, long length,String imageUrl, String videoUrl) {
        this.title = title;
        this.length = length;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public int getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(int imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getVideoUrl2() {
        return videoUrl2;
    }

    public void setVideoUrl2(String videoUrl2) {
        this.videoUrl2 = videoUrl2;
    }
}
