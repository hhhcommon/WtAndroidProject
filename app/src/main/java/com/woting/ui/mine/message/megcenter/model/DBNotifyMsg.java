package com.woting.ui.mine.message.megcenter.model;

/**
 * 消息的存储
 * 作者：xinLong on 2017/6/23 15:28
 * 邮箱：645700751@qq.com
 */
public class DBNotifyMsg {

    private String BJUserId;	    // 本机userid
    private String msg_id;          // 消息id
    private String msg_type;        // 消息类型 0 添加好友同意；1 通知消息；2群申请消息；3群同意消息；4 群消息更改；
    private String deal_type;       // 群消息更改类型；
    private String html;            // 跳转路径

    private String apply_avatar;    // 头像
    private String apply_id;        // 申请人id
    private String apply_name;      // 申请人昵称
    private String apply_message;   // 申请信息

    private String group_avatar;    // 组头像
    private String group_id;        // 组id
    private String group_name;      // 组昵称
    private String show_time;		// 展示时间
    private String add_time;		// 添加时间


    public DBNotifyMsg(String _BJUserId, String _msg_id, String _msg_type,
                       String _deal_type, String _html, String _apply_avatar,
                       String _apply_id, String _apply_name, String _apply_message,
                       String _group_avatar, String _group_id, String _group_name, String _show_time, String _add_time) {
        super();
        BJUserId = _BJUserId;
        msg_id = _msg_id;
        msg_type = _msg_type;
        deal_type = _deal_type;
        html = _html;
        apply_avatar = _apply_avatar;
        apply_id = _apply_id;
        apply_name = _apply_name;
        apply_message = _apply_message;
        group_avatar = _group_avatar;
        group_id = _group_id;
        group_name = _group_name;
        show_time = _show_time;
        add_time = _add_time;
    }

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    public String getBJUserId() {
        return BJUserId;
    }

    public void setBJUserId(String BJUserId) {
        this.BJUserId = BJUserId;
    }

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

    public String getDeal_type() {
        return deal_type;
    }

    public void setDeal_type(String deal_type) {
        this.deal_type = deal_type;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getApply_avatar() {
        return apply_avatar;
    }

    public void setApply_avatar(String apply_avatar) {
        this.apply_avatar = apply_avatar;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getApply_name() {
        return apply_name;
    }

    public void setApply_name(String apply_name) {
        this.apply_name = apply_name;
    }

    public String getApply_message() {
        return apply_message;
    }

    public void setApply_message(String apply_message) {
        this.apply_message = apply_message;
    }

    public String getGroup_avatar() {
        return group_avatar;
    }

    public void setGroup_avatar(String group_avatar) {
        this.group_avatar = group_avatar;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
