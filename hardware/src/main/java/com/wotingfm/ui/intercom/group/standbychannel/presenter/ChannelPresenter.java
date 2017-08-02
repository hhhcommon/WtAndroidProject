package com.wotingfm.ui.intercom.group.standbychannel.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupnews.add.view.GroupNewsForAddFragment;
import com.wotingfm.ui.intercom.group.groupnews.noadd.model.GroupNewsForNoAddModel;
import com.wotingfm.ui.intercom.group.standbychannel.model.ChannelModel;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import rx.internal.operators.OperatorReplay;

/**
 * 设置备用频道的处理中心
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class ChannelPresenter {

    private final StandbyChannelFragment activity;
    private final ChannelModel model;
    private String fromType, groupId;
    private int type=1;
    private List<String> list;
    private String s;

    public ChannelPresenter(StandbyChannelFragment activity) {
        this.activity = activity;
        this.model = new ChannelModel();
        getArguments();// 获取上层界面数据
    }

    // 获取上层界面数据
    private void getArguments() {
        try {
            fromType = activity.getArguments().getString("fromType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            groupId = activity.getArguments().getString("groupId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fromType != null && fromType.trim().equals("create")) {
            // 从创建界面来，不需要设置默认界面
        } else if (fromType != null && fromType.trim().equals("message")) {
            // 从群详情界面来
            String channel1 = null;
            try {
                channel1 = activity.getArguments().getString("channel1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (channel1 != null && !channel1.trim().equals("")) {
                activity.setChannel(1, channel1);
            }
            String channel2 = null;
            try {
                channel2 = activity.getArguments().getString("channel2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (channel2 != null && !channel2.trim().equals("")) {
                activity.setChannel(2, channel2);
            }
        }
    }

    /**
     * 获取数据
     */
    public void getData() {
        list = model.getData();
        activity.setDialog(list);
    }

    /**
     * 按钮点击事件
     *
     * @param type
     */
    public void setChannel(int type) {
        this.type = type;
        if (type == 1) {
            activity.showDialog(true);
        } else {
            activity.showDialog(true);
        }
    }

    /**
     * 完成按钮的操作
     */
    public void over(String channel1, String channel2) {
        if (GlobalStateConfig.test) {
            jump();
        } else {
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                if (model.checkData(channel1, channel2)) {
                    activity.dialogShow();
                    s = model.assemblyData(channel1, channel2);
                    model.loadNews(s, groupId, new ChannelModel.OnLoadInterface() {
                        @Override
                        public void onSuccess(Object o) {
                            activity.dialogCancel();
                            dealSuccess(o);
                        }

                        @Override
                        public void onFailure(String msg) {
                            activity.dialogCancel();
                            ToastUtils.show_always(activity.getActivity(), "设置失败，请稍后再试！");
                        }
                    });
                } else {
                    ToastUtils.show_always(activity.getActivity(), "频道不能设置为空！");
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }
    }

    // 处理返回的数据
    private void dealSuccess(Object o) {
        try {
            String s =new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("设置群备用频道==ret", String.valueOf(ret));
            if (ret == 0) {
                ToastUtils.show_always(activity.getActivity(), "设置成功！");
                jump();// 业务逻辑跳转；群详情还是关闭当前页面
            } else {
                ToastUtils.show_always(activity.getActivity(), "设置失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "设置失败，请稍后再试！");
        }
    }

    /**
     * 确定按钮的操作
     */
    public void ok(int channelIndex) {
        if (list != null && list.size() > 0) {
            String s = list.get(channelIndex);
            if (s != null && !s.trim().equals("")) {
                activity.setChannel(type, s);
            }
        }
    }

    /**
     * 业务逻辑跳转；群详情还是关闭当前页面
     */
    public void jump() {
        if (fromType != null && fromType.trim().equals("create")) {
            InterPhoneActivity.close();
            GroupNewsForAddFragment fragment = new GroupNewsForAddFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", groupId);
            bundle.putString("type", "true");
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else if (fromType != null && fromType.trim().equals("message")) {
            if(s!=null&&!s.trim().equals(""))activity.setResult(true,s);
            InterPhoneActivity.close();
        }
    }
}
