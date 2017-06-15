package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/12.
 */

public class Subscrible implements Serializable {

    /**
     * data : {"albums":[{"id":1,"lastest_news":"更新至: Dr.","logo_url":"http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg","play_count":0,"title":"第一个专辑"}]}
     * msg : success
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {
        public List<AlbumsBean> albums;

        public static class AlbumsBean implements Serializable {

            public String id;
            public String lastest_news;
            public String logo_url;
            public String play_count;
            public String title;

        }
    }
}
