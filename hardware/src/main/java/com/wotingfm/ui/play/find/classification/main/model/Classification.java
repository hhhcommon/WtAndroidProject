package com.wotingfm.ui.play.find.classification.main.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分类的对象
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */

public class Classification implements Serializable {

    /**
     * data : [{"data":[{"id":"cn32","logo":"http://www.wotingfm.com/dataCenter/contentimg/80abcbe42ada4574be5e28323f73cef1.100_100.png","title":"游戏动漫"}],"title":"常用分类"},{"data":[{"id":"cn34","logo":"http://www.wotingfm.com/dataCenter/contentimg/ee5b96e963324fd1bd8a243b98be2446.100_100.png","title":"综艺娱乐"}],"title":"其它分类"}]
     * msg : success
     * ret : 0
     */

    public String msg;
    public int ret;
    public List<DataBeanX> data;

    public static class DataBeanX implements Serializable {
        /**
         * data : [{"id":"cn32","logo":"http://www.wotingfm.com/dataCenter/contentimg/80abcbe42ada4574be5e28323f73cef1.100_100.png","title":"游戏动漫"}]
         * title : 常用分类
         */

        public String id;
        public String logo;
        public String title;
    }
}
