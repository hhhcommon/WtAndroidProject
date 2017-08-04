package com.wotingfm.ui.mine.editusermessage.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.editusermessage.model.EditUserModel;
import com.wotingfm.ui.mine.editusermessage.view.EditUserFragment;
import com.wotingfm.ui.mine.main.MineActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditUserPresenter {

    private EditUserFragment activity;
    private EditUserModel model;
    private int type = 1;

    public EditUserPresenter(EditUserFragment activity) {
        this.activity = activity;
        this.model = new EditUserModel();
        getData();
    }

    private void getData() {
        try {
            type = activity.getArguments().getInt("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.setView(type);
    }

    /**
     * 发送申请
     *
     * @param name
     * @param introduce
     */
    public void send(final String name, final String introduce) {
        if (checkData(name, introduce)) {
            if (GlobalStateConfig.test) {
                setResult(name, introduce);
                MineActivity.close();
            } else {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    activity.dialogShow();
                    String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");

                    // 设置提交数据
                    String news = "";
                    if (type == 1) {
                        news = name;
                    } else if (type == 2) {
                        news = introduce;
                    }
                    model.loadNews(id, news, type, new EditUserModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o, name, introduce);
                        }

                        @Override
                        public void onFailure(String msg) {
                            activity.dialogCancel();
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

    private void dealSuccess(Object o, String name, String introduce) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                setResult(name, introduce);
                MineActivity.close();
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
        }
    }

    // 数据验证
    private boolean checkData(String name, String introduce) {
        if (type == 1) {
            if (name != null && !name.trim().equals("")) {
                return true;
            } else {
                return false;
            }
        } else if (type == 2) {
            if (introduce != null && !introduce.trim().equals("")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 字数更改之后变化
     *
     * @param src
     */
    public void textChange(String src) {
        if (src != null && !src.trim().equals("")) {
            int l = src.length();
            activity.setTextViewChange(String.valueOf(90 - l));
        } else {
            activity.setTextViewChange("90");
        }
    }

    // 设置返回监听参数
    private void setResult(String name, String introduce) {
        if (type == 1) {
            activity.setResult(true, name);
            model.saveName(name);
            activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.MINE_CHANGE));
        } else if (type == 2) {
            activity.setResult(true, introduce);
            model.saveSign(introduce);
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
