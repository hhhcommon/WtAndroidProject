package com.woting.ui.intercom.main.contacts.model;

import java.io.Serializable;

/**
 * 作者：xinLong on 2017/6/8 11:32
 * 邮箱：645700751@qq.com
 */
public class Contact implements Serializable {

    // 好友的字段
    public static class user implements Serializable {
        private int type = 3;
        private String avatar;
        private String sortLetters;     // 显示数据拼音的首字母
        private String id;
        private String name;
        private String gender;
        private String acc_id;
        private String signature;
        private boolean is_admin;
        private boolean is_owner;
        private String introduction;               // 简介
        private String location;                   // 地区
        private String age;                        // 年龄
        private String area;                       // 地区
        private String fans_count;                 // 关注
        private String nickName;                   // 昵称
        private String portraitMini;               // 头像
        private String alias_name;                 // 别名
        private String user_number;                // 我听号

        public void setAcc_id(String acc_id) {
            this.acc_id = acc_id;
        }

        public String getAcc_id() {
            return acc_id;
        }

        public String getUser_number() {
            return user_number;
        }

        public void setUser_number(String user_number) {
            this.user_number = user_number;
        }

        public String getAlias_name() {
            return alias_name;
        }

        public void setAlias_name(String alias_name) {
            this.alias_name = alias_name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPortraitMini() {
            return portraitMini;
        }

        public void setPortraitMini(String portraitMini) {
            this.portraitMini = portraitMini;
        }

        public boolean is_owner() {
            return is_owner;
        }

        public void setIs_owner(boolean is_owner) {
            this.is_owner = is_owner;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public boolean is_admin() {
            return is_admin;
        }

        public void setIs_admin(boolean is_admin) {
            this.is_admin = is_admin;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    // 群组的字段
    public static class group implements Serializable {

        private String created_at;                 // string	@mock=2017-06-06 11:16:31
//        private String creator_id;                 // string	@mock=00163e00693b
        private String id;                         // number	@mock=7
        private String introduction;               // string	@mock=
        private String location;                   // string	@mock=
        private String logo_url;                   // string	@mock=s
        private String member_access_mode;         // string	@mock=1
        private String owner_id;                   // string	@mock=104a0a149b20
        private String password;                   // string	@mock=1234
        private String qr_code_url;                // string	@mock=
        private String title;                      // string	@mock=测试群聊123
        private String updated_at;//
        private String channel;//
        private String group_num;//
        private String member_num;//               // 成员数
        private String roomId;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getMember_num() {
            return member_num;
        }

        public void setMember_num(String member_num) {
            this.member_num = member_num;
        }

        public String getGroup_num() {
            return group_num;
        }

        public void setGroup_num(String group_num) {
            this.group_num = group_num;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

//        public String getCreator_id() {
//            return creator_id;
//        }
//
//        public void setCreator_id(String creator_id) {
//            this.creator_id = creator_id;
//        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getMember_access_mode() {
            return member_access_mode;
        }

        public void setMember_access_mode(String member_access_mode) {
            this.member_access_mode = member_access_mode;
        }

        public String getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(String owner_id) {
            this.owner_id = owner_id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getQr_code_url() {
            return qr_code_url;
        }

        public void setQr_code_url(String qr_code_url) {
            this.qr_code_url = qr_code_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
