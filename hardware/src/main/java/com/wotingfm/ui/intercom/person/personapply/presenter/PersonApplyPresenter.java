package com.wotingfm.ui.intercom.person.personapply.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personapply.model.PersonApplyModel;
import com.wotingfm.ui.intercom.person.personapply.view.PersonApplyFragment;

import org.json.JSONObject;

/**
 * 申请添加好友中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonApplyPresenter {

    private final PersonApplyFragment activity;
    private final PersonApplyModel model;
    private String id;

    public PersonApplyPresenter(PersonApplyFragment activity) {
        this.activity = activity;
        this.model = new PersonApplyModel();
        try {
            id = activity.getArguments().getString("id");
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
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (id != null && !id.trim().equals("")) {
                if (s != null && !s.trim().equals("")) {
                    activity.dialogShow();
                    model.loadNews(id, s, new PersonApplyModel.OnLoadInterface() {
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
                    ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }

    }

    // 处理返回数据
    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                ToastUtils.show_always(activity.getActivity(), "申请消息发送成功！");
                InterPhoneActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "申请添加好友失败，请您稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
        }

    }

    /**
     * 字数更改之后变化
     *
     * @param src
     */
    public void textChange(String src) {
        if (src != null &&!src.trim().equals("")) {
            int l = src.length();
            activity.setTextViewChange(String.valueOf(30 - l));
        } else {
            activity.setTextViewChange("30");
        }
    }
}
