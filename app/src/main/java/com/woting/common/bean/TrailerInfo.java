package com.woting.common.bean;

import java.io.Serializable;

/**
 * Created by amine on 2017/7/11.
 */

public class TrailerInfo implements Serializable {


    public int ret;
    public String msg;
    public DataBean data;


    public static class DataBean implements Serializable {

        public VoiceLiveBean voice_live;


        public static class VoiceLiveBean implements Serializable {

            public String id;
            public String user_id;
            public String title;
            public String cover;
            public String begin_at;
            public boolean had_started;
            public int audience_count;
            public String created_at;
            public String updated_at;
            public boolean had_ended;
            public int enable_save;
            public String audience_ids;
            public String background;
            public int enable_push;
            public String real_begin_at;
            public int lives_count;
            public String live_number;
            public Object rtmp_push_pull_url_json;
            public String description;
            public int diff_from_real_begin;
            public int reserved_count;
            public boolean had_reserved;
            public OwnerBean owner;

        }
    }
}
