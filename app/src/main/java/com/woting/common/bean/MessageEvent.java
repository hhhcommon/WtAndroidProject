package com.woting.common.bean;

import java.util.List;

/**
 * Created by amine on 2017/7/7.
 */


public class MessageEvent {
    public String message;
    public ChannelsBean channelsBean;
    public SinglesBase singlesBase;
    public int type;
    public String roomid;
    public List<SinglesDownload> singlesDownloads;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(ChannelsBean channelsBean, int type) {
        this.channelsBean = channelsBean;
        this.type = type;
    }

    public MessageEvent(SinglesBase singlesBase, int type) {
        this.singlesBase = singlesBase;
        this.type = type;
    }
    public MessageEvent(List<SinglesDownload> singlesDownloads, int type) {
        this.singlesDownloads = singlesDownloads;
        this.type = type;
    }
    public MessageEvent(String roomid, int type) {
        this.roomid = roomid;
        this.type = type;
    }
    public ChannelsBean getChannelsBean() {
        return channelsBean;
    }

    public int getType() {
        return type;
    }

    public SinglesBase getSinglesBase() {
        return singlesBase;
    }

    public List<SinglesDownload> getSinglesDownloads() {
        return singlesDownloads;
    }

    public String getMessage() {
        return message;
    }

    public String getRoomid() {
        return roomid;
    }

}