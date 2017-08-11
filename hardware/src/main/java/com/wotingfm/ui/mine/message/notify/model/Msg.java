package com.wotingfm.ui.mine.message.notify.model;

/**
 * 消息的存储
 * 作者：xinLong on 2017/6/23 15:28
 * 邮箱：645700751@qq.com
 */
public class Msg {

    private String msg_id;          // 消息id
    private String msg_type;        // 消息类型 1 通知消息；2 好友消息friend_apply_msg；3 加群消息被处理group_apply_msg；
                                    //          4 群申请消息（群主、管理员）；5 群主邀请好友入群消息invitee_msg；
    private String avatar;          // 头像
    private String title;           // 标题
    private String news;            // 展示消息
    private String time;            // 时间

    private String html;            // 跳转路径

    private String status;          //
    private String introduce;       //
    private String group_id;        //
    private String apply_id;        //

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }
}
