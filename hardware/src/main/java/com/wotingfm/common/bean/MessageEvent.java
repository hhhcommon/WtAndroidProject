package com.wotingfm.common.bean;

/**
 * Created by amine on 2017/7/7.
 */


public class MessageEvent {
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}