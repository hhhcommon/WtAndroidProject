package com.woting.ui.intercom.main.chat.model;

import java.io.Serializable;

public class TalkHistory implements Serializable {
    private String TyPe;        // 类别 User，Group
    private String ID;          // 用户id，组id
    private String AddTime;     // 添加时间
    private String URL;         // 头像
    private String Name;        // 昵称
    private String GroupNum;    // 展示消息
    private String CallType;    // 呼叫类型
    private String CallTypeM;   // 呼叫消息 已拒绝 已被好友拒绝
    private String ACC_ID;

    public void setACC_ID(String ACC_ID) {
        this.ACC_ID = ACC_ID;
    }

    public String getACC_ID() {
        return ACC_ID;
    }

    public String getCallTypeM() {
        return CallTypeM;
    }

    public void setCallTypeM(String callTypeM) {
        CallTypeM = callTypeM;
    }

    public String getGroupNum() {
        return GroupNum;
    }

    public void setGroupNum(String groupNum) {
        GroupNum = groupNum;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTyPe() {
        return TyPe;
    }

    public void setTyPe(String tyPe) {
        TyPe = tyPe;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }


}
