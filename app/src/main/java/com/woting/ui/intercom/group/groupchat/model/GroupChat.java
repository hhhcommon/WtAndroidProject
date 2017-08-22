package com.woting.ui.intercom.group.groupchat.model;

import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;

/**
 * 作者：xinLong on 2017/6/12 15:20
 * 邮箱：645700751@qq.com
 */
public class GroupChat {
    private String Name;
    private String GroupNumber;

    private ArrayList<Contact.group>  person;

    public ArrayList<Contact.group> getPerson() {
        return person;
    }

    public void setPerson(ArrayList<Contact.group> person) {
        this.person = person;
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

}
