package com.wotingfm.ui.intercom.group.groupapply.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupapply.model.GroupApplyForPasswordModel;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyForPasswordFragment;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;

import retrofit2.http.POST;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupApplyForPasswordPresenter {

    private GroupApplyForPasswordFragment activity;
    private GroupApplyForPasswordModel model;
    private String gid;// 组id


    public GroupApplyForPasswordPresenter(GroupApplyForPasswordFragment activity) {
        this.activity = activity;
        this.model = new GroupApplyForPasswordModel();
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
                model.loadNews(gid, s, new GroupApplyForPasswordModel.OnLoadInterface() {
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
            Log.e("加入群：密码==ret", String.valueOf(ret));
            if (ret == 0) {
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.GROUP_GET));
                ToastUtils.show_always(activity.getActivity(), "加入成功！");
                InterPhoneActivity.closeAll();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", gid);
                        bundle.putString("type", "false");
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                    }
                }, 300);//3秒后执行Runnable中的run方法
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "添加失败，请稍后再试！");
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
