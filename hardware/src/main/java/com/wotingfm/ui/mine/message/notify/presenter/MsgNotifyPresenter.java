package com.wotingfm.ui.mine.message.notify.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.ui.mine.message.notify.model.Msg;
import com.wotingfm.ui.mine.message.notify.model.SrcMsg;
import com.wotingfm.ui.mine.message.notify.model.MsgNotifyModel;
import com.wotingfm.ui.mine.message.notify.view.MsgNotifyFragment;

import org.json.JSONObject;

import java.util.List;

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
        send();
    }

    // 获取消息
    private void send() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            model.loadNews(new MsgNotifyModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealSuccess(o);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                    activity.isLoginView(4);
                }
            });
        } else {
            activity.isLoginView(2);
        }
    }

    private void dealSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String data = js.getString("data");
                SrcMsg s_msg = new Gson().fromJson(data, new TypeToken<SrcMsg>() {
                }.getType());
                if (s_msg != null) {
                    List<Msg> msg = model.assemblyData(s_msg);
                    if (msg != null && msg.size() > 0) {
                        activity.updateUI(msg);
                    } else {
                        activity.isLoginView(1);
                    }
                } else {
                    activity.isLoginView(1);
                }
            } else {
                activity.isLoginView(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(4);
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
