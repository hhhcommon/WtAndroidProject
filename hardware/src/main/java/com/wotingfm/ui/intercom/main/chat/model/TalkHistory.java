package com.wotingfm.ui.intercom.main.chat.model;

import java.io.Serializable;

public class TalkHistory implements Serializable {
	private String TyPe;		// 类别 User，Group
	private String ID;			// 用户id，组id
	private String AddTime;		// 添加时间
	private String URL;		    // 头像
	private String Name;		// 昵称
	private String GroupNum;    // 昵称

	public String getGroupNum() {
		return GroupNum;
	}

	public void setGroupNum(String groupNum) {
		GroupNum = groupNum;
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
