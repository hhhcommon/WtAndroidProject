package com.wotingfm.ui.intercom.group.groupapply.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupapply.model.GroupApplyForPasswordModel;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyForPasswordFragment;
import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupApplyForPasswordPresenter {

    private final GroupApplyForPasswordFragment activity;
    private final GroupApplyForPasswordModel model;
    private  String gid;// 组id


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
                model.loadNews(gid,  s, new GroupApplyForPasswordModel.OnLoadInterface() {
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
            Log.e("加入群：密码==ret", String.valueOf(ret));
            if (ret == 0) {

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
        }
    }

}
