package uilayouttest.example.com.bigtask.Utils;

import uilayouttest.example.com.bigtask.bean.Video;

import java.util.ArrayList;
import java.util.List;



public class VideoDataUtil {
//    这是美食视频
    public static List<Video> videoList = new ArrayList<>();

//    这是锻炼视频
    public static List<Video> videoList2 = new ArrayList<>();

//    美食
    public static List<Video> getVideoListData() {
//        List<Video> videoList = new ArrayList<>();
        videoList.add(new Video("缤纷西米水晶粽！",
                98000,
                "http://i1.letvimg.com/lc04_isvrs/201705/27/15/26/c7a4aa7b-8783-4c9f-b9f2-7a55433d234d.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/ColorfulSammyCrystalZongzi.mp4"));
        videoList.add(new Video("消暑清凉绿豆糕",
                413000,
                "http://i3.letvimg.com/lc05_isvrs/201705/21/13/45/ea71278b-d697-4ecc-9f01-2b08c0b761e4.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/CoolMungBeanCakeforSummerElimination.mp4"));
        videoList.add(new Video("万圣节吸血鬼獠牙曲奇！",
                439000,
                "http://i0.letvimg.com/lc06_isvrs/201710/31/13/38/589f3e66-8cea-4dc7-ab78-883c2b1d2c68.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/HalloweenVampireTuskCookie.mp4"));
        videoList.add(new Video("秘制迷迭香烤土豆",
                178000,
                "http://i3.letvimg.com/lc06_isvrs/201710/30/14/57/be8bb50a-ea48-490e-bb8b-eda08c324a89.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/RoastedPotatoesWithRosemary.mp4"));
        videoList.add(new Video("劲爽柠檬气泡水【罐头小厨】",
                450000,
                "http://i1.letvimg.com/lc04_isvrs/201705/23/14/29/6aca0dfa-69c0-47f7-88c6-66718e651984.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/JinshuangLemonBubbleWater.mp4"));
        videoList.add(new Video("降火芋圆龟苓膏",
                176000,
                "http://i1.letvimg.com/lc03_isvrs/201705/25/13/28/0da21073-31b1-4a42-a005-ff1e9e667e7c.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/JianghuoYuanguilingPaste.mp4"));
        videoList.add(new Video("告白表情包棉花糖！",
                176000,
                "http://i3.letvimg.com/lc06_isvrs/201708/28/18/20/71bb2c51-7bc7-4f2b-a8bf-42fef8ce57c4.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/ChocolateBrownieAddressExpressionCottonCandy.mp4"));
        videoList.add(new Video("电饭煲巧克力布朗尼",
                176000,
                "http://i1.letvimg.com/lc05_isvrs/201704/09/11/33/e5075ad5-2fca-401f-a9ec-06c5fd19c57e.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/Brownies.mp4"));
        videoList.add(new Video("草帽路飞鸡腿肉",
                176000,
                "http://i0.letvimg.com/lc07_isvrs/201704/07/17/33/f432694f-06fc-4254-95a2-ae7229f78899.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/FlyingChickenLegInStrawhatRoad.mp4"));
        videoList.add(new Video("鱼香肉丝",
                176000,
                "http://i3.letvimg.com/lc02_isvrs/201710/29/17/38/43a3814c-6993-4501-acfa-9d32668f4120.jpg",
                "http://101.132.32.72:8080/VideoMaterial/DeliciousFood/YuShiangShreddedPork.mp4"));
        return videoList;
    }

    //    锻炼身体
    public static List<Video> getVideoListData2() {
//        List<Video> videoList = new ArrayList<>();
        videoList2.add(new Video("瑜伽教学小视频",
                98000,
                "http://i1.letvimg.com/lc10_yunzhuanma/201711/01/10/43/dbe5fb6712090568adf2d806b86529f5_v2_MTMyOTc3MjI2/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/YogaTeaching.mp4"));
        videoList2.add(new Video("早教课堂： 7个月宝宝的瑜伽球小游戏, 克服晕车好锻炼!",
                413000,
                "http://i1.letvimg.com/lc10_yunzhuanma/201707/12/11/27/214eb5c47f436b8bfb3f9b4769081ec2_v2_MTMxNTE1MTI0/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/YogaBallGames.mp4"));
        videoList2.add(new Video("水中游泳的小宝宝，好想抱一抱",
                439000,
                "http://i3.letvimg.com/lc10_yunzhuanma/201706/30/07/09/f43b400f6bf840688f441870517e2359_v2_MTMxMjEwMjk2/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/BabySwimmingInTheWater.mp4"));
        videoList2.add(new Video("实拍深圳公园早晨，妇女们舞练太极剑锻炼身体的视频",
                178000,
                "http://i3.letvimg.com/lc05_isvrs/201705/06/10/42/21f08e58-c3dc-4d14-972c-05948058832e/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/TaiChiSwordDancingExercise.mp4"));
        videoList2.add(new Video("教你如何轻松搞定手臂上的小肥肉的瑜伽小姿势",
                450000,
                "http://i1.letvimg.com/lc08_yunzhuanma/201705/05/19/52/7d1faf2c69a4137bf7748c5d2658ef0a_v2_MTI5MjY1NzE2/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/RomanYoga.mp4"));
        videoList2.add(new Video("爸爸帮大头儿子锻炼身体，报名参加小小宇航员比赛",
                176000,
                "http://i2.letvimg.com/lc10_yunzhuanma/201707/09/03/54/3ffa0836f2cbcde5aab1b298b0bdc716_v2_MTMxNDMyODU2/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/BigHeadSon.mp4"));
        videoList2.add(new Video("Rosie的瑜伽小教室——告别“尖尖臀”",
                176000,
                "http://i2.letvimg.com/lc10_yunzhuanma/201710/24/11/07/a0e676a0044193f8ca2d369d9cf2985d_v2_MTMyODk4NjI0/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/SayGoodbyeToPointedbuttocks.mp4"));
        videoList2.add(new Video("Rosie的瑜伽小教室——缓解下腹松弛",
                176000,
                "http://i2.letvimg.com/lc09_yunzhuanma/201705/06/00/24/f76ff3865da67665869e641fad6dc8f6_v2_MTI5Mjc0NDg0/thumb/5_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/RelievingLowerAbdominalRelaxation.mp4"));
        videoList2.add(new Video("3岁体操小王子单手俯卧撑完虐王力宏",
                176000,
                "http://i1.letvimg.com/lc07_isvrs/201705/09/09/37/645edb64-4fe4-4ffd-9369-c11c4633e9d9/thumb/2_200_150.jpg",
                "http://101.132.32.72:8080/VideoMaterial/ExerciseBody/OneHandedPushUps.mp4"));

        return videoList2;
    }
}
