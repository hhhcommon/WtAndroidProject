package com.wotingfm.ui.intercom.person.personmessage.presenter;

import android.app.Activity;
import android.os.Bundle;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.person.personmessage.PersonMessageFragment;
import com.wotingfm.ui.intercom.person.personmessage.model.PersonMessageModel;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonMessagePresenter {

    private final PersonMessageFragment activity;
    private final PersonMessageModel model;
    private String type = "true";// 界面类型 未添加好友=false，已添加好友=true
    private boolean headViewShow = false;// 界面是否展示


    public PersonMessagePresenter(PersonMessageFragment activity) {
        this.activity = activity;
        this.model = new PersonMessageModel();
    }

    public void getData() {
        Bundle bundle = activity.getArguments();
        type = bundle.getString("type");

        if(type!=null&&!type.trim().equals("")&&type.trim().equals("true")){
            activity.setView(true);
        }else{
            activity.setView(false);
        }
        String name="辛龙";
        String introduce="我是一个文化人";
        String number="518518";
        String address="北京朝阳";
        String sign="我是一个文化人";
        activity.setViewData( name, introduce, number, address, sign);
    }

    /**
     * 判断界面展示
     */
    public void headViewShow() {
        if (headViewShow) {
            activity.imageShow(false);
            headViewShow = false;
        } else {
            activity.imageShow(true);
            headViewShow = true;
        }
    }

    /**
     * 删除好友的请求
     */
    public void delFriend(){
        dealDelFriend(false);
    }

    // 请求后台数据删除好友是否成功
    private void dealDelFriend(boolean b){
        if(b){
            activity.close();
        }else{
            ToastUtils.show_always(activity.getActivity(),"删除好友失败，请稍后再试");
        }
    }

    /**
     * 举报的请求
     */
    public void reportFriend(){
        dealReportFriend(true);
    }

    // 请求后台数据举报是否成功
    private void dealReportFriend(boolean b){
        if(b){
            ToastUtils.show_always(activity.getActivity(),"举报成功");
        }else{
            ToastUtils.show_always(activity.getActivity(),"举报失败，请稍后再试");
        }
    }

}
