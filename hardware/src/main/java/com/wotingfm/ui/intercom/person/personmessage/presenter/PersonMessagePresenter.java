package com.wotingfm.ui.intercom.person.personmessage.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.intercom.person.personmessage.model.PersonMessageModel;
import com.wotingfm.ui.user.login.model.LoginModel;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 好友信息的处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonMessagePresenter {

    private final PersonMessageFragment activity;
    private final PersonMessageModel model;
    private String type = "true";// 界面类型 未添加好友=false，已添加好友=true
    private boolean headViewShow = false;// 界面是否展示
    private String id;// 好友id


    public PersonMessagePresenter(PersonMessageFragment activity) {
        this.activity = activity;
        this.model = new PersonMessageModel();
    }

    public void getData() {
        Bundle bundle = activity.getArguments();
        type = bundle.getString("type");
        id = bundle.getString("id");

        if(type!=null&&!type.trim().equals("")&&type.trim().equals("true")){
            activity.setView(true);
        }else{
            activity.setView(false);
        }

        if(GlobalStateConfig.test){
            String name="辛龙";
            String introduce="我是一个文化人";
            String number="518518";
            String address="北京朝阳";
            String sign="我是一个文化人";
            activity.setViewData( name, introduce, number, address, sign);
        }else{
            getPersonNews();
        }
    }

    // 获取好友的请求
    private void getPersonNews(){
        model.loadNews( new PersonMessageModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
            }
        });
    }

    // 处理返回数据
    private void dealSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
//                String msg = js.getString("data");
//                JSONTokener jsonParser = new JSONTokener(msg);
//                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
//                String token = arg1.getString("token");
//
//                // 保存后台获取到的token
//                if (token != null && !token.trim().equals("")) {
//                    model.saveToken(token);
//                    ToastUtils.show_always(activity.getActivity(), token);
//                }
//
//                JSONObject ui = (JSONObject) new JSONTokener(arg1.getString("user")).nextValue();
                String name="辛龙";
                String introduce="我是一个文化人";
                String number="518518";
                String address="北京朝阳";
                String sign="我是一个文化人";
                activity.setViewData( name, introduce, number, address, sign);
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
