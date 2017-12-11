package com.zhengpu.aiui.bean;

import com.zhengpu.aiuilibrary.iflytekbean.*;

import java.util.List;

/**
 * sayid ....
 * Created by wengmf on 2017/12/11.
 */

public class ZhiHuNewsBean extends com.zhengpu.aiuilibrary.iflytekbean.BaseBean {


    /**
     * date : 20171211
     * stories : [{"images":["https://pic4.zhimg.com/v2-8fbc9606e54014fbae24de33ac1e2ec7.jpg"],"type":0,"id":9660027,"ga_prefix":"121114","title":"「是否有性生活」这事，医生该怎么询问未成年人？"},{"images":["https://pic1.zhimg.com/v2-25bc326f211c729a3077298910ccc04c.jpg"],"type":0,"id":9660540,"ga_prefix":"121112","title":"大误 · 这个情节不对啊"},{"images":["https://pic3.zhimg.com/v2-b10f93761559c7bbc86135c1e63c973a.jpg"],"type":0,"id":9660025,"ga_prefix":"121110","title":"看完这篇，我赶紧摸出镜子对着嘴照了照\u2026\u2026"},{"images":["https://pic2.zhimg.com/v2-f46f186851c07de3ce09d6638a9927ed.jpg"],"type":0,"id":9659825,"ga_prefix":"121109","title":"我有爱情，却没有安全感"},{"images":["https://pic2.zhimg.com/v2-7288b5b29792745b30d6295b98516a01.jpg"],"type":0,"id":9660300,"ga_prefix":"121108","title":"柯南说，真相只有一个，但他从没说「事实」只有一个"},{"images":["https://pic4.zhimg.com/v2-78b1aef2d19046ebb933f5fdd1102963.jpg"],"type":0,"id":9660528,"ga_prefix":"121107","title":"iPhone X 真正在乎的，从来不是所谓的「全面屏风潮」"},{"images":["https://pic4.zhimg.com/v2-f17d6c585bfad15a6af9b8fe3121047f.jpg"],"type":0,"id":9660493,"ga_prefix":"121107","title":"在陆家嘴上班的金融精英和遍地的皮包公司"},{"images":["https://pic1.zhimg.com/v2-f795b56e39ca793dc853295e9db02004.jpg"],"type":0,"id":9660157,"ga_prefix":"121107","title":"说大公司不锻炼人，但每年还是有很多人抢着进是为什么？"},{"images":["https://pic1.zhimg.com/v2-12ea5a627c4c69dcea38dd1844a3dfb4.jpg"],"type":0,"id":9660531,"ga_prefix":"121106","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic4.zhimg.com/v2-c380384481b0234300ef77ec2f705bb7.jpg","type":0,"id":9659825,"ga_prefix":"121109","title":"我有爱情，却没有安全感"},{"image":"https://pic4.zhimg.com/v2-b486e6f136b9081cebf64543275923cf.jpg","type":0,"id":9660528,"ga_prefix":"121107","title":"iPhone X 真正在乎的，从来不是所谓的「全面屏风潮」"},{"image":"https://pic1.zhimg.com/v2-499e7360353264ca4637923dcf0948e0.jpg","type":0,"id":9660493,"ga_prefix":"121107","title":"在陆家嘴上班的金融精英和遍地的皮包公司"},{"image":"https://pic2.zhimg.com/v2-3118541e00795c00461c846a01bea77d.jpg","type":0,"id":9660315,"ga_prefix":"121019","title":"「姑娘，知道你想美，但你这妆容是不是有点过了\u2026\u2026」"},{"image":"https://pic1.zhimg.com/v2-f01a427e4f408a606399e32560499f88.jpg","type":0,"id":9660342,"ga_prefix":"121014","title":"该用的东西永远找不到、永远乱糟糟，其实厨房收纳有技巧"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic4.zhimg.com/v2-8fbc9606e54014fbae24de33ac1e2ec7.jpg"]
         * type : 0
         * id : 9660027
         * ga_prefix : 121114
         * title : 「是否有性生活」这事，医生该怎么询问未成年人？
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic4.zhimg.com/v2-c380384481b0234300ef77ec2f705bb7.jpg
         * type : 0
         * id : 9659825
         * ga_prefix : 121109
         * title : 我有爱情，却没有安全感
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
