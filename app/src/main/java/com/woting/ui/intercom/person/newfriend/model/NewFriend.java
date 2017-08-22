package com.woting.ui.intercom.person.newfriend.model;

import java.io.Serializable;

/**
 * 好友申请消息的对象
 * 作者：xinLong on 2017/6/23 15:28
 * 邮箱：645700751@qq.com
 */
public class NewFriend implements Serializable {
    private String apply_id;        // 好友申请的id	number	@mock=1
    private String apply_message;   // 申请信息	string	@mock=qsd
    private apply apply_user;       // 申请用户信息	object
    private create created_at;      // 申请创建时间	object
    private boolean had_approved;  // 好友申请的类型 1未同意 2同意

    public apply getApply_user() {
        return apply_user;
    }

    public void setApply_user(apply apply_user) {
        this.apply_user = apply_user;
    }

    public create getCreated_at() {
        return created_at;
    }

    public void setCreated_at(create created_at) {
        this.created_at = created_at;
    }

    public boolean isHad_approved() {
        return had_approved;
    }

    public void setHad_approved(boolean had_approved) {
        this.had_approved = had_approved;
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

    public class apply implements Serializable {
        private String avatar;          // string	@mock=
        private String id;              // string	@mock=0579efbaf9a9
        private String name;            // string	@mock=W003

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
    }

    public class create implements Serializable {
        private String date;            // string	@mock=2017-06-05 18:18:04.000000
        private String timezone;        // string	@mock=Asia/Shanghai
        private String timezone_type;   //

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


}
