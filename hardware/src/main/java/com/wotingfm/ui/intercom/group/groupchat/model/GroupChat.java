package com.wotingfm.ui.intercom.group.groupchat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者：xinLong on 2017/6/12 15:20
 * 邮箱：645700751@qq.com
 */
public class GroupChat {
    private String Name;
    private String GroupNumber;

    private ArrayList<news>  person;

    public static class news implements Serializable {
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


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber = groupNumber;
    }

    public ArrayList<news> getPerson() {
        return person;
    }

    public void setPerson(ArrayList<news> person) {
        this.person = person;
    }
}
