package com.wotingfm.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amine on 2017/5/17.
 */

public class HomeBanners {
    public List<Banner> banners;
    public String message;
    public int status;

    public static class Banner {
        public int id;
        public String name;
        public String image;
        public String url;
        public int status;
        public int sort;
        @SerializedName("create_at")
        public String createTime;
        @SerializedName("images_hover")
        public String hoverImage;
        public BannerType type;
    }

    public enum BannerType {
        VIDEO, GIF, IMAGE
    }
}
