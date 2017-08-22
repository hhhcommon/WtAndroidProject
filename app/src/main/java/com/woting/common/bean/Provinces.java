package com.woting.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/7/6.
 */

public class Provinces implements Serializable {

    /**
     * data : {"provinces":[{"id":3,"title":"北京","type":2},{"id":5,"title":"天津","type":2},{"id":7,"title":"河北","type":2},{"id":19,"title":"山西","type":2},{"id":31,"title":"内蒙古","type":2},{"id":44,"title":"辽宁","type":2},{"id":59,"title":"吉林","type":2},{"id":69,"title":"黑龙江","type":2},{"id":83,"title":"上海","type":2},{"id":85,"title":"江苏","type":2},{"id":99,"title":"浙江","type":2},{"id":111,"title":"安徽","type":2},{"id":129,"title":"福建","type":2},{"id":139,"title":"江西","type":2},{"id":151,"title":"山东","type":2},{"id":169,"title":"河南","type":2},{"id":187,"title":"湖北","type":2},{"id":202,"title":"湖南","type":2},{"id":217,"title":"广东","type":2},{"id":239,"title":"广西","type":2},{"id":254,"title":"海南","type":2},{"id":257,"title":"重庆","type":2},{"id":259,"title":"四川","type":2},{"id":281,"title":"贵州","type":2},{"id":291,"title":"云南","type":2},{"id":308,"title":"西藏","type":2},{"id":316,"title":"陕西","type":2},{"id":327,"title":"甘肃","type":2},{"id":342,"title":"青海","type":2},{"id":351,"title":"宁夏","type":2},{"id":357,"title":"新疆","type":2},{"id":407,"title":"网络台","type":2}]}
     * msg : success
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {
        public List<ProvincesBean> provinces;


        public static class ProvincesBean implements Serializable {
            /**
             * id : 3
             * title : 北京
             * type : 2
             */

            public String id;
            public String title;
            public int type;


        }
    }
}
