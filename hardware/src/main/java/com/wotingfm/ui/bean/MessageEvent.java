package com.wotingfm.ui.bean;

import java.util.List;

public class MessageEvent {
    public String message;
    public ChannelsBean channelsBean;
    public SinglesBase singlesBase;
    public Selected.DataBeanX.DataBean  DataBean;
    public int type;
    public int percentsAvailable;
    public boolean isBottom;
    public String roomid;
    public List<SinglesDownload> singlesDownloads;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(boolean type) {
        this.isBottom = type;
    }

    public MessageEvent(ChannelsBean channelsBean, int type) {
        this.channelsBean = channelsBean;
        this.type = type;
    }

    public MessageEvent(SinglesBase singlesBase, int type) {
        this.singlesBase = singlesBase;
        this.type = type;
    }

    public MessageEvent(Selected.DataBeanX.DataBean  DataBean, int type) {
        this.DataBean = DataBean;
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

    public MessageEvent(int type,int percentsAvailable) {
        this.type = type;
        this.percentsAvailable = percentsAvailable;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public ChannelsBean getChannelsBean() {
        return channelsBean;
    }

    public int getType() {
        return type;
    }

    public int getPercentsAvailable() {
        return percentsAvailable;
    }

    public boolean getIsBottom() {
        return isBottom;
    }
    public SinglesBase getSinglesBase() {
        return singlesBase;
    }

    public Selected.DataBeanX.DataBean  getDataBean() {
        return DataBean;
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