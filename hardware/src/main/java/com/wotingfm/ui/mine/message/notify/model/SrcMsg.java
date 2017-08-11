package com.wotingfm.ui.mine.message.notify.model;

import java.io.Serializable;
import java.util.List;

/**
 * 消息的存储
 * 作者：xinLong on 2017/6/23 15:28
 * 邮箱：645700751@qq.com
 */
public class SrcMsg implements Serializable {

    private List<groupApplyMes> group_apply_mes;        //
    private List<groupApproveMes> group_approve_mes;    //
    private List<friendApplyMes> friend_apply_mes;      //
    private List<groupAdminMes> group_admin_mes;       //
    private List<inviteeMes> invitee_mes;           //

    public List<groupAdminMes> getGroup_admin_mes() {
        return group_admin_mes;
    }

    public void setGroup_admin_mes(List<groupAdminMes> group_admin_mes) {
        this.group_admin_mes = group_admin_mes;
    }

    public List<inviteeMes> getInvitee_mes() {
        return invitee_mes;
    }

    public void setInvitee_mes(List<inviteeMes> invitee_mes) {
        this.invitee_mes = invitee_mes;
    }

    public List<groupApplyMes> getGroup_apply_mes() {
        return group_apply_mes;
    }

    public void setGroup_apply_mes(List<groupApplyMes> group_apply_mes) {
        this.group_apply_mes = group_apply_mes;
    }

    public List<groupApproveMes> getGroup_approve_mes() {
        return group_approve_mes;
    }

    public void setGroup_approve_mes(List<groupApproveMes> group_approve_mes) {
        this.group_approve_mes = group_approve_mes;
    }

    public List<friendApplyMes> getFriend_apply_mes() {
        return friend_apply_mes;
    }

    public void setFriend_apply_mes(List<friendApplyMes> friend_apply_mes) {
        this.friend_apply_mes = friend_apply_mes;
    }

    public static class groupApplyMes implements Serializable {

        private String group_id;        // 申请群的id(x)
        private String group_name;      // 申请群的名字
        private String status;          // 申请状态	number	（0 未通过 · ·1通过 2 等待审核中）
        private String user_id;         // 当前用户的id(x)

        private String id;              // 消息id
        private String approve_at;      // 时间
        private String avatar;          // 头像

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApprove_at() {
            return approve_at;
        }

        public void setApprove_at(String approve_at) {
            this.approve_at = approve_at;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public static class groupApproveMes implements Serializable {
        private String group_id;        // 被申请群的id
        private String group_name;      // 被申请群的名字
        private String status;          // 申请状态	number
        private String user_id;         // 申请者的id
        private String applier_name;    // 申请者的昵称
        private String id;              // 消息id
        private String approve_at;      // 时间
        private String avatar;          // 头像
        private String content;         // 验证消息

        public String getApplier_name() {
            return applier_name;
        }

        public void setApplier_name(String applier_name) {
            this.applier_name = applier_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApprove_at() {
            return approve_at;
        }

        public void setApprove_at(String approve_at) {
            this.approve_at = approve_at;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public static class friendApplyMes implements Serializable {
        // applier_id 暂无用
        private String id;              // 消息id
        private String status;          // 状态
        private String approved_at;     // 时间
        private String receiver_avatar; // 头像
        private String receiver_name;   // 消息处理者昵称
        private String receiver_id;     // 消息处理者id

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getApproved_at() {
            return approved_at;
        }

        public void setApproved_at(String approved_at) {
            this.approved_at = approved_at;
        }

        public String getReceiver_avatar() {
            return receiver_avatar;
        }

        public void setReceiver_avatar(String receiver_avatar) {
            this.receiver_avatar = receiver_avatar;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
        }

        public String getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(String receiver_id) {
            this.receiver_id = receiver_id;
        }
    }

    public static class groupAdminMes implements Serializable {
        private String group_id;        // 被申请群的id
        private String group_name;      // 被申请群的名字
        private String status;          // 申请状态	number
        private String user_id;         // 申请者的id
        private String applier_name;    // 申请者的昵称
        private String id;              // 消息id
        private String approve_at;      // 时间
        private String avatar;          // 头像
        private String content;         // 验证消息

        public String getApplier_name() {
            return applier_name;
        }

        public void setApplier_name(String applier_name) {
            this.applier_name = applier_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApprove_at() {
            return approve_at;
        }

        public void setApprove_at(String approve_at) {
            this.approve_at = approve_at;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    /**
     * 邀请消息
     */
    public static class inviteeMes implements Serializable {
        private String id;              // 消息id
        private String group_id;        // 组id
        private String group_name;      // 组名称
        private String avatar;          // 头像
        private String status;          // 状态
        private String user_id;         // 被邀请者id
        private String invitee_agreed;  // 消息类型
        private String applier_name;    // 被邀请者名称
        private String approve_at;      //

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getInvitee_agreed() {
            return invitee_agreed;
        }

        public void setInvitee_agreed(String invitee_agreed) {
            this.invitee_agreed = invitee_agreed;
        }

        public String getApplier_name() {
            return applier_name;
        }

        public void setApplier_name(String applier_name) {
            this.applier_name = applier_name;
        }

        public String getApprove_at() {
            return approve_at;
        }

        public void setApprove_at(String approve_at) {
            this.approve_at = approve_at;
        }
    }

    public static class MesNotify implements Serializable {
        private String id;              // 消息id
        private String approved_at;     // 时间
        private String avatar;          // 头像
        private String message;         // 展示消息
        private String html;            // 跳转路径

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApproved_at() {
            return approved_at;
        }

        public void setApproved_at(String approved_at) {
            this.approved_at = approved_at;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }
    }

}