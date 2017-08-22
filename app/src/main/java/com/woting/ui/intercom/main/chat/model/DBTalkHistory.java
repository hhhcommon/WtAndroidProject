package com.woting.ui.intercom.main.chat.model;

import java.io.Serializable;

public class DBTalkHistory implements Serializable {
	private String BJUserId;	// 本机userid
	private String TyPe;		// 类别 person，group
	private String ID;			//
	private String AddTime;		// 添加时间
	private String CallType;    // 是否接听
	private String CallTypeM;   //
	private String ACCID;

	public DBTalkHistory(String bjUserId, String type, String id, String addTime, String callType, String callTypeM,String accId) {
		super();
		BJUserId = bjUserId;
		TyPe = type;
		ID = id;
		AddTime = addTime;
		CallType = callType;
		CallTypeM = callTypeM;
		ACCID = accId;
	}

	public void setACC_ID(String ACC_ID) {
		this.ACCID = ACC_ID;
	}

	public String getACC_ID() {
		return ACCID;
	}

	public String getCallTypeM() {
		return CallTypeM;
	}

	public void setCallTypeM(String callTypeM) {
		CallTypeM = callTypeM;
	}
	public String getBJUserId() {
		return BJUserId;
	}
	public void setBJUserId(String bJUserId) {
		BJUserId = bJUserId;
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

	public String getCallType() {
		return CallType;
	}

	public void setCallType(String callType) {
		CallType = callType;
	}

	@Override
	public String toString(){
		return "添加时间：" + getAddTime();
	}
}
