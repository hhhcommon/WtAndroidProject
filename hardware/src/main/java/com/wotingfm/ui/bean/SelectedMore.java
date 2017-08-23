package com.wotingfm.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/22.
 */

public class SelectedMore implements Serializable {

    /**
     * data : {"albums":[{"id":2,"lastest_news":"暂无更新","logo_url":"http://lorempixel.com/640/480/?94119","play_count":0,"title":"Ms."},{"id":2,"lastest_news":"暂无更新","logo_url":"http://lorempixel.com/640/480/?94119","play_count":0,"title":"Ms."},{"id":3,"lastest_news":"暂无更新","logo_url":"http://lorempixel.com/640/480/?93206","play_count":0,"title":"Ms."}]}
     * msg : success
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {
        public List<AlbumsBean> albums;

    }
}
