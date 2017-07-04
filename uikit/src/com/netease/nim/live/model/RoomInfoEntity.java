package com.netease.nim.live.model;

import java.io.Serializable;

/**
 * Created by zhukkun on 1/13/17.
 */
public class RoomInfoEntity implements Serializable {

    public int roomid;
    public String owner;
    public String pushUrl;
    public String rtmpPullUrl;
    public String hlsPullUrl;
    public String httpPullUrl;
    public String cid;
    public int status;

    public int getRoomid() {
        return roomid;
    }

    public String getCid() {
        return cid;
    }

    public String getHlsPullUrl() {
        return hlsPullUrl;
    }

    public String getOwner() {
        return owner;
    }

    public String getHttpPullUrl() {
        return httpPullUrl;
    }

    public String getRtmpPullUrl() {
        return rtmpPullUrl;
    }

    public String getPushUrl() {
        return pushUrl;
    }
}
