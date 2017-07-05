package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/27.
 */

public class LiveBean implements Serializable {


    public String msg;
    public int ret;
    public List<DataBean> data;


    public static class DataBean implements Serializable {

        public OwnerBean owner;
        public int audience_count;
        public String begin_at;
        public String cover;
        public int id;
        public String live_number;
        public String title;
        public String type;
        public RtmpPushPullUrlJsonBean rtmp_push_pull_url_json;

        public static class RtmpPushPullUrlJsonBean implements Serializable {

            public String cid;
            public long ctime;
            public String hlsPullUrl;
            public String httpPullUrl;
            public String name;
            public String pushUrl;
            public String rtmpPullUrl;


        }

        public static class OwnerBean implements Serializable {
            /**
             * avatar : ##userimg##user_42c0.jpg
             * fans_count : 0
             * had_real_name_cert : true
             * id : 8053ae905e71
             * idols_count : 0
             * name : 查理
             */

            public String avatar;
            public int fans_count;
            public boolean had_real_name_cert;
            public String id;
            public int idols_count;
            public String name;

        }
    }
}
