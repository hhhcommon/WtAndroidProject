package com.wotingfm.ui.mine.feedback.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personnote.model.EditPersonNoteModel;
import com.wotingfm.ui.mine.feedback.model.FeedbackModel;
import com.wotingfm.ui.mine.feedback.view.FeedbackFragment;
import com.wotingfm.ui.mine.fm.model.FMInfo;
import com.wotingfm.ui.mine.fm.model.FMSetModel;
import com.wotingfm.ui.mine.fm.view.FMSetFragment;
import com.wotingfm.ui.mine.main.MineActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class FeedbackPresenter {

    private FeedbackFragment activity;
    private FeedbackModel model;

    public FeedbackPresenter(FeedbackFragment activity) {
        this.activity = activity;
        this.model = new FeedbackModel();
    }

    /**
     * 发送申请
     *
     * @param information
     * @param feedback
     */
    public void send(String information, String feedback) {
        if (checkData(feedback)) {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (GlobalStateConfig.test) {
                    ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
                    activity.closeFragment();
                } else {
                    activity.dialogShow();
                    model.loadNews(information, feedback, new FeedbackModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o);
                        }

                        @Override
                        public void onFailure(String msg) {
                            activity.dialogCancel();
                            ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
                            activity.closeFragment();
                        }
                    });
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "意见内容不能为空，谢谢！");
        }
    }

    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                activity.closeFragment();
                ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
            } else {
                ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "感谢您的反馈！");
        }
    }

    /**
     * 判断意见反馈是否为空
     *
     * @param feedback
     * @return
     */
    private boolean checkData(String feedback) {
        if (feedback != null && !feedback.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
