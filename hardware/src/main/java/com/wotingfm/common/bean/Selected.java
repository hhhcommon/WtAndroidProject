package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/21.
 */

public class Selected implements Serializable {

    /**
     * data : [{"data":[{"id":2,"lastest_news":"暂无更新","logo_url":"http://lorempixel.com/640/480/?94119","play_count":0,"title":"Ms."}],"title":"每日听单","type":"DAILY_LISTENINGS"},{"data":[{"id":1,"lastest_news":"更新至: Dr.","logo_url":"http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg","play_count":0,"title":"第一个专辑"}],"title":"编辑精选","type":"EDITOR_SELECTIONS"},{"data":[{"id":2,"lastest_news":"暂无更新","logo_url":"http://lorempixel.com/640/480/?94119","play_count":0,"title":"Ms."}],"title":"热门点击","type":"HOT_ALBUMS"}]
     * msg : success
     * ret : 0
     */

    public String msg;
    public int ret;
    public List<DataBeanX> data;


    public static class DataBeanX implements Serializable {
        /**
         * data : [{"id":2,"lastest_news":"暂无更新","logo_url":"http://lorempixel.com/640/480/?94119","play_count":0,"title":"Ms."}]
         * title : 每日听单
         * type : DAILY_LISTENINGS
         */

        public String title;
        public String type;
        public List<DataBean> data;


        public static class DataBean implements Serializable {
            /**
             * id : 2
             * lastest_news : 暂无更新
             * logo_url : http://lorempixel.com/640/480/?94119
             * play_count : 0
             * title : Ms.
             */

            public String id;
            public String lastest_news;
            public String logo_url;
            public int play_count;
            public String title;

        }
    }
}
