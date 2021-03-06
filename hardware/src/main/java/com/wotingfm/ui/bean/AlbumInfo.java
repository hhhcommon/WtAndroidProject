package com.wotingfm.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/15.
 */

public class AlbumInfo implements Serializable {

    public DataBean data;
    public String msg;
    public int ret;

    public static class DataBean implements Serializable {


        public AlbumBean album;


        public static class AlbumBean implements Serializable {

            public boolean had_subscibed;
            public String id;
            public String introduction;
            public String lastest_news;
            public String logo_url;
            public OwnerBean owner;
            public int subscriptions_count;
            public String title;
            public int total_single_count;
            public List<Channels> channels;

            public static class Channels implements Serializable {
                public String id;
                public String title;
                public String logo;

            }

        }

    }
}
