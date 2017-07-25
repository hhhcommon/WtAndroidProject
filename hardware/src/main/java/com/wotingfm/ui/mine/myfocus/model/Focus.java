package com.wotingfm.ui.mine.myfocus.model;

/**
 * 作者：xinLong on 2017/6/12 15:20
 * 邮箱：645700751@qq.com
 */
public class Focus {
    private String avatar;// 头像
    private boolean follow_eath_other;// 是否互相关注
    private String id;// 关注用户的id
    private String name;// 名字
    private String signature;// 简介

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isFollow_eath_other() {
        return follow_eath_other;
    }

    public void setFollow_eath_other(boolean follow_eath_other) {
        this.follow_eath_other = follow_eath_other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
