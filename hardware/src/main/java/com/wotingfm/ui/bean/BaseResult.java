package com.wotingfm.ui.bean;

import java.io.Serializable;

/**
 * Created by amine on 2017/6/21.
 */

public class BaseResult implements Serializable {
    /**
     * status : true
     * message : 验证码发送成功
     * data : []
     */

    public int ret;
    public String msg;
}
