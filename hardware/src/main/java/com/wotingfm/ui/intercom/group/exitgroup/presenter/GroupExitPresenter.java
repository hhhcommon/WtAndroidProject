package com.wotingfm.ui.intercom.group.exitgroup.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.exitgroup.model.GroupExitModel;
import com.wotingfm.ui.intercom.group.exitgroup.view.GroupExitFragment;
import com.wotingfm.ui.intercom.group.groupapply.model.GroupApplyForNewsModel;
import com.wotingfm.ui.intercom.group.transfergroup.view.TransferGroupFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupExitPresenter {

    private GroupExitFragment activity;
    private GroupExitModel model;
    private Contact.group group;
    private List<Contact.user> list;

    public GroupExitPresenter(GroupExitFragment activity) {
        this.activity = activity;
        this.model = new GroupExitModel();
        getData();
    }

    private void getData() {
        try {
            group = (Contact.group) activity.getArguments().getSerializable("group");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            list = (List<Contact.user>) activity.getArguments().getSerializable("list");// 成员列表
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送解散群的申请
     */
    public void exit() {
        if (group != null && group.getId() != null && !group.getId().trim().equals("")) {
            String gid = group.getId().trim();
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
                model.loadNews(gid, new GroupExitModel.OnLoadInterface() {
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
            ToastUtils.show_always(activity.getActivity(), "数据出错了,请您稍后再试！");
        }
    }

    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("解散群 ret", String.valueOf(ret));
            if (ret == 0) {
                // 通知上层界面关闭群详情界面
                activity.setResult(true, 0);
                // 发送重新获取群列表的广播
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                InterPhoneActivity.close();
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "解散失败，请稍后再试！");
        }
    }

    /**
     * 跳转到转让群主界面
     */
    public void transfer() {
        if (group != null && list != null && list.size() > 0 && group.getOwner_id() != null && group.getId() != null) {
            TransferGroupFragment fragment = new TransferGroupFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable) list);
            bundle.putString("gid", group.getId());
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
            fragment.setResultListener(new TransferGroupFragment.ResultListener() {
                @Override
                public void resultListener(boolean type) {
                    if (type) {
                        // 已经不是群主,更改上层界面数据
                        activity.setResult(true, 1);
                        InterPhoneActivity.close();
                    }
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请您稍后再试！");
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
