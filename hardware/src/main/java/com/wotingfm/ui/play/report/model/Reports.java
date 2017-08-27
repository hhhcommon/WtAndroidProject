package com.wotingfm.ui.play.report.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/12.
 */

public class Reports implements Serializable {

    /**
     * data : {"reasons":["广告","色情低俗","谣言、欺诈","政治敏感","侵权"]}
     * msg : success
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;

    public static class DataBean implements Serializable {
        public List<Reasons> reasons;

        public static class Reasons implements Serializable {
            public String title;
            public boolean isSelect;
        }
    }
}
