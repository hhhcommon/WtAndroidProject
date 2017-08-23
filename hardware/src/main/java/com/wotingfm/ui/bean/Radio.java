package com.wotingfm.ui.bean;

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

    }
}
