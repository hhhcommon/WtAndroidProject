package com.woting.ui.intercom.person.personnote.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.common.config.GlobalStateConfig;
import com.woting.common.utils.ToastUtils;
import com.woting.ui.intercom.main.view.InterPhoneActivity;
import com.woting.ui.intercom.person.personnote.model.EditPersonNoteModel;
import com.woting.ui.intercom.person.personnote.view.EditPersonNoteFragment;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditPersonNotePresenter {

    private EditPersonNoteFragment activity;
    private EditPersonNoteModel model;
    private String id;

    public EditPersonNotePresenter(EditPersonNoteFragment activity) {
        this.activity = activity;
        this.model = new EditPersonNoteModel();
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
    public void send(final String s) {
        if (GlobalStateConfig.test) {
            if (s != null && !s.trim().equals("")) {
                activity.setResult(true, s);
                InterPhoneActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
            }
        } else {
            if (s != null && !s.trim().equals("")) {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    activity.dialogShow();
                    model.loadNews(id, s, new EditPersonNoteModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o, s);
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
    }

    private void dealSuccess(Object o, String name) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                activity.setResult(true, name);
                InterPhoneActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
