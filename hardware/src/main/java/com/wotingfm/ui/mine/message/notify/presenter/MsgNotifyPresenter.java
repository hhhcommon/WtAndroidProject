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

    private MsgNotifyFragment activity;
    private MsgNotifyModel model;
    private List<Msg> msg;

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

    /**
     * 同意的操作
     *
     * @param position
     */
    public void apply(final int position) {
        String type = msg.get(position).getMsg_type();
        if (type != null && type.equals("4")) {
            String gid = msg.get(position).getGroup_id();
            String pid = msg.get(position).getApply_id();
            activity.dialogShow();
            model.loadNewsForApply(gid, pid, new MsgNotifyModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealApplySuccess(o, position);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                }
            });
        }
    }

    /**
     * 删除该条消息
     *
     * @param position
     */
    public void del(final int position) {
        String type = msg.get(position).getMsg_type();
        if (type != null && !type.equals("4")) {
            String id = msg.get(position).getMsg_id();
            if (id != null && !id.equals("")) {
                activity.dialogShow();
                String dType = "";
                if (type.equals("2")) {
                    dType = "friend";
                } else if (type.equals("3")) {
                    dType = "group";
                }
                model.loadNewsForDel(id, dType, new MsgNotifyModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealDelSuccess(o, position);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                    }
                });
            }
        } else if (type != null && type.equals("4")) {
            String status = msg.get(position).getStatus();
            if (status.equals("1")) {
                activity.dialogShow();
                String dType = "group";
                String id = msg.get(position).getMsg_id();
                model.loadNewsForDel(id, dType, new MsgNotifyModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealDelSuccess(o, position);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                    }
                });
            } else {
                String gid = msg.get(position).getGroup_id();
                String pid = msg.get(position).getApply_id();
                activity.dialogShow();
                model.loadNewsForRefuse(gid, pid, new MsgNotifyModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealRefuseSuccess(o, position);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                    }
                });
            }
        }
    }

    // 处理获取数据的消息
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
                    msg = model.assemblyData(s_msg);
                    if (msg != null && msg.size() > 0) {
                        activity.updateUI(msg);
                        activity.isLoginView(0);
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

    // 处理同意消息
    private void dealApplySuccess(Object o, int position) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                msg.get(position).setStatus("1");
                activity.updateUI(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理拒绝消息
    private void dealRefuseSuccess(Object o, int position) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                msg.remove(position);
                activity.updateUI(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理删除消息
    private void dealDelSuccess(Object o, int position) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                msg.remove(position);
                activity.updateUI(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(int position) {

    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

}
