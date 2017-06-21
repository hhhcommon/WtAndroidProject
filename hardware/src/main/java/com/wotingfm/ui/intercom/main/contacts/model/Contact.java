package com.wotingfm.ui.intercom.main.contacts.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/8 11:32
 * 邮箱：645700751@qq.com
 */
public class Contact implements Serializable {

    // 下面变量的定义要与接口中的字段名字保持一致
    // 如上面的错误码字段，你就像定义为code，而服务器返回的是error_code，这个时候就应该这么写：
    @SerializedName("error_code")
    public int msg;
    public int ret;
    public List<user> friends;

    public static class user implements Serializable {
        private int type=1;
        private String avatar;
        private String sortLetters;     // 显示数据拼音的首字母
        private String id;
        private String name;
        private String NickName;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }
    }

    public static class group implements Serializable {
        private String avatar;
        private String sortLetters;     // 显示数据拼音的首字母
        private String id;
        private String name;
        private String NickName;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }
    }
}
