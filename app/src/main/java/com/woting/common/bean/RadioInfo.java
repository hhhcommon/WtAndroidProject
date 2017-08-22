package com.woting.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/7/7.
 */

public class RadioInfo implements Serializable {


    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {

        public ChannelBean channel;
        public List<TodayBean> today;
        public List<TomorrowBean> tomorrow;
        public List<YesterdayBean> yesterday;


        public static class ChannelBean implements Serializable {

            public String desc;
            public boolean had_subscribed;
            public String id;
            public String image_url;
            public String listen_count;
            public String radio_url;
            public int subscribed_count;
            public String title;

        }

        public static class TodayBean implements Serializable {

            public String channel_id;
            public String duration;
            public String end_time;
            public String fileUrl;
            public boolean had_subscribed;
            public String id;
            public boolean is_playing;
            public int order;
            public int program_id;
            public String start_time;
            public String title;
            public boolean can_reserved;
            public int week;


        }

        public static class TomorrowBean implements Serializable {

            public String channel_id;
            public String duration;
            public String end_time;
            public String fileUrl;
            public boolean had_subscribed;
            public String id;
            public boolean is_playing;
            public int order;
            public int program_id;
            public String start_time;
            public String title;
            public int week;


        }

        public static class YesterdayBean implements Serializable {

            public String channel_id;
            public String duration;
            public String end_time;
            public String fileUrl;
            public boolean had_subscribed;
            public String id;
            public boolean is_playing;
            public int order;
            public int program_id;
            public String start_time;
            public String title;
            public int week;

        }
    }
}
