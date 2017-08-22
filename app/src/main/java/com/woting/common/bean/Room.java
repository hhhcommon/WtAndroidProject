package com.woting.common.bean;

import java.io.Serializable;

/**
 * Created by amine on 2017/6/15.
 */

public class Room implements Serializable {


    public DataBean data;
    public String msg;
    public int ret;

    public static class DataBean implements Serializable {
        public String room_number;

    }
}
