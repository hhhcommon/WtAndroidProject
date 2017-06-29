package com.wotingfm.ui.intercom.group.groupapply.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.EditGroupMessageModel;
import com.wotingfm.ui.intercom.group.groupapply.model.GroupApplyModel;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyFragment;
import com.wotingfm.ui.intercom.group.groupintroduce.model.EditGroupIntroduceModel;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupApplyPresenter {

    private final GroupApplyFragment activity;
    private final GroupApplyModel model;
    private final String gid;// 组id


    public GroupApplyPresenter(GroupApplyFragment activity) {
        this.activity = activity;
        this.model = new GroupApplyModel();
        Bundle bundle = activity.getArguments();
        gid = bundle.getString("gid");
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
                model.loadNews(gid, s, new GroupApplyModel.OnLoadInterface() {
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
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改群介绍==ret", String.valueOf(ret));
            if (ret == 0) {

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
        }
    }

    /**
     * 字数更改之后变化
     *
     * @param src
     */
    public void textChange(String src) {

        activity.setTextViewChange("");
    }
}
