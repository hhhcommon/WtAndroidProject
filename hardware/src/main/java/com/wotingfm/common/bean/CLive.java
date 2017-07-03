package com.wotingfm.common.bean;

import java.io.Serializable;

/**
 * Created by amine on 2017/7/3.
 */

public class CLive implements Serializable {

    /**
     * data : {"voiceLive":{"begin_at":"2017-04-12 12:12:12","cover":"we","created_at":"2017-04-25 14:30:21","enable_save":"0","id":12,"live_number":"1000012","title":"上天","updated_at":"2017-04-25 14:30:21","user_id":"123"}}
     * msg : succss
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;

    public static class DataBean implements Serializable {
        /**
         * voiceLive : {"begin_at":"2017-04-12 12:12:12","cover":"we","created_at":"2017-04-25 14:30:21","enable_save":"0","id":12,"live_number":"1000012","title":"上天","updated_at":"2017-04-25 14:30:21","user_id":"123"}
         */

        public VoiceLiveBean voiceLive;

        public static class VoiceLiveBean implements Serializable {
            /**
             * begin_at : 2017-04-12 12:12:12
             * cover : we
             * created_at : 2017-04-25 14:30:21
             * enable_save : 0
             * id : 12
             * live_number : 1000012
             * title : 上天
             * updated_at : 2017-04-25 14:30:21
             * user_id : 123
             */

            public String begin_at;
            public String cover;
            public String created_at;
            public String enable_save;
            public int id;
            public String live_number;
            public String title;
            public String updated_at;
            public String user_id;


        }
    }
}
