package com.wotingfm.ui.message.notify.model;

/**
 *
 * 作者：xinLong on 2017/6/23 15:28
 * 邮箱：645700751@qq.com
 */
public class NotifyMsg {

    private String apply_id;        // 好友申请的id	number	@mock=1
    private String apply_message;   // 申请信息	string	@mock=qsd
    private String apply_user;      // 申请用户信息	object
    private String avatar;          // string	@mock=
    private String id;              // string	@mock=0579efbaf9a9
    private String name;            // string	@mock=W003
    private String created_at;      // 申请创建时间	object
    private String date;            // string	@mock=2017-06-05 18:18:04.000000
    private String timezone;        // string	@mock=Asia/Shanghai
    private String timezone_type;   //
    private String apply_type="1";  // 好友申请的类型 1未同意 2同意

    public String getApply_type() {
        return apply_type;
    }

    public void setApply_type(String apply_type) {
        this.apply_type = apply_type;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getApply_message() {
        return apply_message;
    }

    public void setApply_message(String apply_message) {
        this.apply_message = apply_message;
    }

    public String getApply_user() {
        return apply_user;
    }

    public void setApply_user(String apply_user) {
        this.apply_user = apply_user;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_type() {
        return timezone_type;
    }

    public void setTimezone_type(String timezone_type) {
        this.timezone_type = timezone_type;
    }
}
