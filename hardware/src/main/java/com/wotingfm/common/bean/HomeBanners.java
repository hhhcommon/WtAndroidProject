package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/5/17.
 */

public class HomeBanners {


    public DataBean data;
    public String msg;
    public int ret;

    public static class DataBean implements Serializable {
        public List<BannersBean> banners;


        public static class BannersBean  implements Serializable{
            /**
             * created_at : 2017-06-21 13:52:12
             * id : 6
             * jump_url : 2
             * logo_url : 2
             * sort : 3
             * type : LIVE
             * updated_at : 2017-06-21 13:52:17
             */

            public String created_at;
            public String id;
            public String jump_url;
            public String logo_url;
            public int sort;
            public String type;
            public String updated_at;

        }
    }
}
