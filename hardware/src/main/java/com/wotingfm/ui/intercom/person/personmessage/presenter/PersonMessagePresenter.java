package com.wotingfm.ui.intercom.person.personmessage.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.add.search.net.view.SearchContactsForNetFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personapply.view.PersonApplyFragment;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.intercom.person.personmessage.model.PersonMessageModel;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 好友信息的处理器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PersonMessagePresenter {

    private final PersonMessageFragment activity;
    private final PersonMessageModel model;
    private String type = "true";// 界面类型 未添加好友=false，已添加好友=true
    private boolean headViewShow = false;// 界面是否展示
    private String id;// 好友id


    public PersonMessagePresenter(PersonMessageFragment activity) {
        this.activity = activity;
        this.model = new PersonMessageModel();
        Bundle bundle = activity.getArguments();
        type = bundle.getString("type");
        id = bundle.getString("id");
    }

    /**
     * 数据适配
     */
    public void getData() {
        if (type != null && !type.trim().equals("") && type.trim().equals("true")) {
            activity.setView(true);
        } else {
            activity.setView(false);
        }
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                String name = "辛龙";
                String introduce = "我是一个文化人";
                String number = "518518";
                String address = "北京朝阳";
                String sign = "我是一个文化人";
                activity.setViewData(name, introduce, number, address, sign);
            } else {
                getPersonNews();
            }
        } else {
            activity.isLoginView(2);
        }
    }

    // 获取好友的请求
    private void getPersonNews() {
        model.loadNews(id, new PersonMessageModel.OnLoadInterface() {
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
    }

    // 处理返回数据
    private void dealSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                // 此处字段待对接
                String friends = arg1.getString("friends");
                // 用户信息
                Contact.user user = new Gson().fromJson(friends, new TypeToken<Contact.user>() {
                }.getType());
                assemblyData(user);

            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.isLoginView(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.isLoginView(4);
    }

    private void assemblyData(Contact.user user) {
        String name = "";
        try {
            name = user.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String introduce = "";
        try {
            introduce = user.getIntroduction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String number = "";
        try {
            number = user.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String address = "";
        try {
            address = user.getLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sign = "";
        try {
            sign = user.getSignature();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.setViewData(name, introduce, number, address, sign);
        activity.isLoginView(0);
    }

    /**
     * 判断界面展示
     */
    public void headViewShow() {
        if (headViewShow) {
            activity.imageShow(false);
            headViewShow = false;
        } else {
            activity.imageShow(true);
            headViewShow = true;
        }
    }

    /**
     * 删除好友的请求
     */
    public void delFriend() {
        dealDelFriend(false);
    }

    // 请求后台数据删除好友是否成功
    private void dealDelFriend(boolean b) {
        if (b) {
            activity.close();
        } else {
            ToastUtils.show_always(activity.getActivity(), "删除好友失败，请稍后再试");
        }
    }

    /**
     * 举报的请求
     */
    public void reportFriend() {
        dealReportFriend(true);
    }

    // 请求后台数据举报是否成功
    private void dealReportFriend(boolean b) {
        if (b) {
            ToastUtils.show_always(activity.getActivity(), "举报成功");
        } else {
            ToastUtils.show_always(activity.getActivity(), "举报失败，请稍后再试");
        }
    }

    /**
     * 异常按钮的操作
     *
     * @param type
     */
    public void tipClick(int type) {
        if (type == 2) {
            getData();
        }
    }

    /**
     * 添加到申请好友界面
     */
    public void apply() {
        PersonApplyFragment fragment = new PersonApplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        InterPhoneActivity.open(fragment);
    }

}
