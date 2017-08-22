package com.woting.common.bean;

import java.io.Serializable;

/**
 * Created by amine on 2017/7/3.
 */

public class CLive implements Serializable {


    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {
        /**
         * voiceLive : {"audience_count":1,"audience_ids":",104a0a149b20","background":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/voice-live-background.jpg","begin_at":"2017-07-04","channel":{},"cover":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/voice-live-background.jpg","created_at":"2017-07-04 14:22:01","diff_from_real_begin":1000,"had_started":true,"id":165,"keywords":{},"live_number":9737375,"owner":{"avatar":"##userimg##user_4cfc.jpg","fans_count":11,"had_real_name_cert":true,"id":"104a0a149b20","idols_count":10,"name":"名酷123tyhhb"},"real_begin_at":{"date":"2017-07-04 14:22:01.000000","timezone":"Asia/Shanghai","timezone_type":3},"rtmp_push_pull_url_json":{"cid":"f3ecc8dc0a4b4b6398066be174296133","ctime":1499149321881,"hlsPullUrl":"http://pullhlsc7bdc021.live.126.net/live/f3ecc8dc0a4b4b6398066be174296133/playlist.m3u8","httpPullUrl":"http://flvc7bdc021.live.126.net/live/f3ecc8dc0a4b4b6398066be174296133.flv?netease=flvc7bdc021.live.126.net","name":"名酷123tyhhb的直播1499149321","pushUrl":"rtmp://pc7bdc021.live.126.net/live/f3ecc8dc0a4b4b6398066be174296133?wsSecret=9ab0bccbfcd1500d2a9b7f556e822510&wsTime=1499149321","rtmpPullUrl":"rtmp://vc7bdc021.live.126.net/live/f3ecc8dc0a4b4b6398066be174296133"},"title":"","updated_at":"2017-07-04 14:22:02","user_id":"104a0a149b20"}
         */

        public VoiceLiveBean voice_live;


        public static class VoiceLiveBean implements Serializable {

            public int audience_count;
            public String audience_ids;
            public String background;
            public String begin_at;
            public String cover;
            public String created_at;
            public int diff_from_real_begin;
            public boolean had_started;
            public int id;
            public int live_number;
            public OwnerBean owner;
            public RealBeginAtBean real_begin_at;
            public RtmpPushPullUrlJsonBean rtmp_push_pull_url_json;
            public String title;
            public String updated_at;
            public String user_id;


            public static class OwnerBean implements Serializable {
                /**
                 * avatar : ##userimg##user_4cfc.jpg
                 * fans_count : 11
                 * had_real_name_cert : true
                 * id : 104a0a149b20
                 * idols_count : 10
                 * name : 名酷123tyhhb
                 */

                public String avatar;
                public int fans_count;
                public boolean had_real_name_cert;
                public String id;
                public int idols_count;
                public String name;


            }

            public static class RealBeginAtBean implements Serializable {

                public String date;
                public String timezone;
                public int timezone_type;

            }

            public static class RtmpPushPullUrlJsonBean implements Serializable {

                public String cid;
                public long ctime;
                public String hlsPullUrl;
                public String httpPullUrl;
                public String name;
                public String pushUrl;
                public String rtmpPullUrl;


            }
        }
    }
}
