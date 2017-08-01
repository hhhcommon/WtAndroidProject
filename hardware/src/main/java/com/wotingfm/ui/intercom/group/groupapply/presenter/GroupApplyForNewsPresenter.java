package com.wotingfm.ui.intercom.group.groupapply.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupapply.model.GroupApplyForNewsModel;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyForNewsFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupApplyForNewsPresenter {

    private final GroupApplyForNewsFragment activity;
    private final GroupApplyForNewsModel model;
    private String gid;// 组id


    public GroupApplyForNewsPresenter(GroupApplyForNewsFragment activity) {
        this.activity = activity;
        this.model = new GroupApplyForNewsModel();
        try {
            gid = activity.getArguments().getString("gid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送申请
     *
     * @param s
     */
    public void send(String s) {
        if (s != null && !s.trim().equals("")) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
                model.loadNews(gid, s, new GroupApplyForNewsModel.OnLoadInterface() {
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
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }

        } else {
            ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
        }
    }

    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("加入群:消息==ret", String.valueOf(ret));
            if (ret == 0) {
                ToastUtils.show_always(activity.getActivity(),"申请成功！");
                InterPhoneActivity.close();
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(),"添加失败，请稍后再试！");
        }
    }

    /**
     * 字数更改之后变化
     *
     * @param src
     */
    public void textChange(String src) {
        if (src != null &&! src.trim().equals("")) {
            int l = src.length();
            activity.setTextViewChange(String.valueOf(30 - l));
        } else {
            activity.setTextViewChange("30");
        }
    }
}
