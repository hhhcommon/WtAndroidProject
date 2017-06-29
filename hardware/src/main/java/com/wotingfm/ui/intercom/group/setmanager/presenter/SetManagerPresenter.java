package com.wotingfm.ui.intercom.group.setmanager.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.setmanager.view.SetManagerFragment;
import com.wotingfm.ui.intercom.group.setmanager.model.SetManagerModel;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SetManagerPresenter {

    private final SetManagerFragment activity;
    private final SetManagerModel model;
    private List<Contact.user> list;
    private String gId;// 组Id

    public SetManagerPresenter(SetManagerFragment activity) {
        this.activity = activity;
        this.model = new SetManagerModel();

    }

    /**
     * 获取展示数据
     */
    public void getData() {
        if (GlobalStateConfig.test) {
            // 测试数据
            list = model.getData();
        } else {
            // 实际数据
            list = getList();

        }

        activity.setView(list);
    }

    private List<Contact.user> getList() {
        // 从来源界面传递群成员列表数据
        Bundle bundle = activity.getArguments();
        gId = bundle.getString("gid");// 群id
        String id = bundle.getString("id");// 群创建者id
        List<Contact.user> list = (List<Contact.user>) bundle.getSerializable("list");// 群成员列表

        List<Contact.user> _list = new ArrayList<>();
        // 有群主
        if (id != null && !id.trim().equals("")) {
            // 添加管理员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    String _id = list.get(i).getId();
                    if (_id != null && !_id.trim().equals("")) {
                        if (!id.equals(_id)) {
                            list.get(i).setType(2);
                            _list.add(list.get(i));
                        }
                    }
                }
            }
            // 添加成员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (!b) {

                    list.get(i).setType(3);
                    _list.add(list.get(i));
                }
            }
        } else {
            // 添加管理员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    list.get(i).setType(2);
                    _list.add(list.get(i));
                }
            }
            // 添加成员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (!b) {

                    list.get(i).setType(3);
                    _list.add(list.get(i));
                }
            }
        }
        return _list;
    }

    /**
     * 点击按钮的数据更改
     *
     * @param position
     */
    public void changeData(int position) {
        int type = list.get(position).getType();
        if (type == 1) {
            list.get(position).setType(2);
        } else {
            list.get(position).setType(1);
        }
        activity.setView(list);
    }

    /**
     * 发送申请
     */
    public void send() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            String s = getString();

            if (s != null && !s.equals("")) {
                model.loadNews(gId, s, new SetManagerModel.OnLoadInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        activity.dialogCancel();
                        dealSuccess(o);
                    }

                    @Override
                    public void onFailure(String msg) {
                        activity.dialogCancel();
                        ToastUtils.show_always(activity.getActivity(), "设置管理员失败，请稍后再试！");
                    }
                });
            } else {
                ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }

    }

    // 处理返回的数据
    private void dealSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("设置管理员==ret", String.valueOf(ret));
            if (ret == 0) {
                InterPhoneActivity.close();
                ToastUtils.show_always(activity.getActivity(), "设置成功");
            } else {
                ToastUtils.show_always(activity.getActivity(), "设置管理员失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 设置数据出错界面
            ToastUtils.show_always(activity.getActivity(), "设置管理员失败，请稍后再试！");
        }
    }

    /**
     * 获取提交数据
     *
     * @return
     */
    private String getString() {
        StringBuffer s = new StringBuffer();
        String S = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == 2) {
                    String id = list.get(i).getId();
                    s.append(id + ",");
                }
            }
        }
        // 去掉最后一个逗号
        if (s.length() > 0) {
            S = s.substring(0, s.length() - 1);
        }
        return S;
    }
}
