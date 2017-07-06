package com.wotingfm.ui.intercom.group.groupintroduce.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupintroduce.model.EditGroupIntroduceModel;
import com.wotingfm.ui.intercom.group.groupintroduce.view.EditGroupIntroduceFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditGroupIntroducePresenter {

    private final EditGroupIntroduceFragment activity;
    private final EditGroupIntroduceModel model;
    private Contact.group group;


    public EditGroupIntroducePresenter(EditGroupIntroduceFragment activity) {
        this.activity = activity;
        this.model = new EditGroupIntroduceModel();
        try {
            group = (Contact.group) activity.getArguments().getSerializable("group");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (group == null) {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
            InterPhoneActivity.close();
        }
    }

    /**
     * 发送申请
     *
     * @param s
     */
    public void send(final String s) {
        if (s != null && !s.trim().equals("")) {
            if (GlobalStateConfig.test) {
                activity.setResult(s);
                InterPhoneActivity.close();
            } else {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    activity.dialogShow();
                    String id = group.getId();
                    model.loadNews(id, s, new EditGroupIntroduceModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o, s);
                        }

                        @Override
                        public void onFailure(String msg) {
                            activity.dialogCancel();
                            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
                        }
                    });
                } else {
                    ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
                }
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
        }
    }

    private void dealSuccess(Object o, String name) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("修改群介绍==ret", String.valueOf(ret));
            if (ret == 0) {
                // 设置回调监听返回值
                activity.setResult(name);
                InterPhoneActivity.close();
                ToastUtils.show_always(activity.getActivity(), "修改成功");
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
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
