package com.wotingfm.ui.mine.message.notify.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.mine.feedback.model.FeedbackModel;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.mine.message.notify.model.MsgNotifyModel;
import com.wotingfm.ui.mine.message.notify.view.MsgNotifyFragment;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class MsgNotifyPresenter {

    private final MsgNotifyFragment activity;
    private final MsgNotifyModel model;

    public MsgNotifyPresenter(MsgNotifyFragment activity) {
        this.activity = activity;
        this.model = new MsgNotifyModel();
    }

    /**
     * 获取申请消息
     *
     */
    public void send() {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (GlobalStateConfig.test) {
                } else {
                    activity.dialogShow();
                    model.loadNews( new MsgNotifyModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o);
                        }

                        @Override
                        public void onFailure(String msg) {
                            activity.dialogCancel();
                            ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
                        }
                    });
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }

    }

    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                MineActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
        }
    }



    public void apply(int position) {

    }
    public void del(int position) {

    }
    public void onClick(int position) {

    }
    public void tipClick(int position) {

    }


}
