package uilayouttest.example.com.bigtask.Entity;



import java.io.Serializable;

/**
 * Created by 朱慧 on 2019/2/26.
 */

public class TeaCeremony implements Serializable {

    private int id;
    private int imgPath;//图片地址
    private String teaCeremonyName;//茶艺名称

    private String content;
    private boolean hasLike;
    private int likeCount;

    public TeaCeremony(){
    }

    public TeaCeremony(int id,int imgPath,String teaCeremonyName,String content,int likeCount){
        this.id=id;
        this.imgPath=imgPath;
        this.teaCeremonyName=teaCeremonyName;
        this.content=content;
        this.likeCount=likeCount;
    }


    public void addLikeCount(){
        likeCount++;
    }

    public void delLikeCount(){
        likeCount--;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasLike() {
        return hasLike;
    }

    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getTeaCeremonyName() {
        return teaCeremonyName;
    }

    public void setTeaCeremonyName(String teaCeremonyName) {
        this.teaCeremonyName = teaCeremonyName;
    }

    @Override
    public String toString() {
        return "GoodsEntity{" +
                "imgPath='" + imgPath + '\'' +
                ", goodsName='" + teaCeremonyName + '\'' +
                '}';
    }


}
