package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/7/6.
 */

public class Radio implements Serializable {


    public int ret;
    public String msg;
    public DataBean data;


    public static class DataBean implements Serializable {
        public List<ChannelsBean> channels;

        public static class ChannelsBean implements Serializable {

            public String id;
            public String title;
            public String desc;
            public String image_url;
            public String radio_url;
            public String listen_count;
            public PivotBean pivot;


            public static class PivotBean implements Serializable {
                /**
                 * category_id : 409
                 * channel_id : 386
                 */

                public int category_id;
                public int channel_id;


            }
        }
    }
}
