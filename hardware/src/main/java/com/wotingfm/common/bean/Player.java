package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/7.
 */

public class Player implements Serializable {

    public int ret;
    public String msg;
    public DataBean data;


    public static class DataBean implements Serializable {
        public List<SinglesBean> singles;

        public static class SinglesBean implements Serializable {
            /**
             * id : 15
             * single_title : Dr.
             * single_logo_url : http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg
             * single_seconds : 100
             * single_file_url : http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null
             * album_title : Ms.
             */

            public String id;
            public boolean isPlay;
            public String single_title;
            public String single_logo_url;
            public long single_seconds;
            public String single_file_url;
            public String album_title;

        }
    }
}
