package com.wotingfm.ui.intercom.person.personmessage.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.add.search.net.view.SearchContactsForNetFragment;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.group.groupnews.noadd.view.GroupNewsForNoAddFragment;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.chat.presenter.ChatPresenter;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.personapply.view.PersonApplyFragment;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.intercom.person.personmessage.model.PersonMessageModel;
import com.wotingfm.ui.intercom.person.personnote.view.EditPersonNoteFragment;

import org.json.JSONException;
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
    private Contact.user user;
    private List<AlbumsBean> album;

    public PersonMessagePresenter(PersonMessageFragment activity) {
        this.activity = activity;
        this.model = new PersonMessageModel(activity);
        getArguments();
    }

    // 获取上级界面传递的数据 type用来判断是否是好友，id是该用户的id
    private void getArguments() {
        try {
            type = activity.getArguments().getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            id = activity.getArguments().getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                String sign = "我是一个文化人";
                String number = "518518";
                String address = "北京朝阳";
                String focus = "0";
                activity.setViewData("",name, sign, number, address, focus);
                List<AlbumsBean> list = model.getTestData();
                if (list != null && list.size() > 0) {
                    activity.setGridViewData(list);
                } else {
                    activity.setGridViewDataNull();
                }
                activity.isLoginView(0);
            } else {
                getPersonNews();
                if (GlobalStateConfig.test) {
                    List<AlbumsBean> list = model.getTestData();
                    if (list != null && list.size() > 0) {
                        activity.setGridViewData(list);
                    } else {
                        activity.setGridViewDataNull();
                    }
                } else {
                    getPersonSub();
                }
            }
        } else {
            activity.isLoginView(2);
        }
    }

    // 获取好友的订阅
    private void getPersonSub() {
        model.loadSubNews(id, new PersonMessageModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealSubSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
            }
        });
    }

    // 处理订阅返回数据
    private void dealSubSuccess(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                try {
                    String albums = arg1.getString("albums");
                    // 专辑
                    album = new Gson().fromJson(albums, new TypeToken<List<AlbumsBean>>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (album != null && album.size() > 0) {
                    activity.setGridViewData(album);
                } else {
                    activity.setGridViewDataNull();
                }
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.setGridViewDataNull();
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.setGridViewDataNull();
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
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String friends = arg1.getString("user");
                // 用户信息
                user = new Gson().fromJson(friends, new TypeToken<Contact.user>() {
                }.getType());
                if (user != null) {
                    assemblyData(user);
                    activity.isLoginView(0);
                } else {
                    activity.isLoginView(4);
                }
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.isLoginView(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(4);
        }
    }

    // 处理数据
    private void assemblyData(Contact.user user) {
        String name = "";
        try {
            name = user.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sign = "";
        try {
            sign = user.getSignature();
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
            address = user.getArea();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String focus = "0";
        try {
            focus = user.getFans_count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "";
        try {
            url = user.getAvatar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.setViewData(url,name, sign, number, address, focus);
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
        if (id != null && !id.trim().equals("")) {
            PersonApplyFragment fragment = new PersonApplyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }

    }

    /**
     * 跳转到添加备注界面
     */
    public void jumpNote() {
        if (id != null && !id.trim().equals("")) {
            EditPersonNoteFragment fragment = new EditPersonNoteFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            fragment.setArguments(bundle);
            InterPhoneActivity.open(fragment);

            fragment.setResultListener(new EditPersonNoteFragment.ResultListener() {
                @Override
                public void resultListener(boolean type, String name) {
                    if (type) {
                        if (name != null & !name.equals("")) {
                            activity.setViewDataForName(name);
                        }
                    }
                }
            });
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }

    }

    /**
     * Grid的点击事件
     *
     * @param list
     * @param position
     */
    public void setGridItemClick(List<AlbumsBean> list, int position) {
        ToastUtils.show_always(activity.getActivity(), "您点击了内容");
    }

    /**
     * 呼叫
     */
    public void call() {
        if (ChatPresenter.data != null) {
            // 此时有对讲状态
            String _t = ChatPresenter.data.getTyPe().trim();
            if (_t != null && !_t.equals("") && _t.equals("person")) {// 此时的对讲状态是单对单
                // 弹出选择界面
                activity.confirmDialogShow();
            } else if (_t != null && !_t.equals("") && _t.equals("group")) {// 此时的对讲状态是群组
                // 退出组
                talkOverGroup();
                // 关闭对讲页面群组数据
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_GROUP_CLOSE));
                // 进行呼叫
                callPerson();
            }
        } else {
            // 此时没有对讲状态
            // 进行呼叫
            callPerson();
        }
    }

    /**
     * 同意挂断当前对讲后的操作
     */
    public void callOk() {
        // 挂断当前会话
        talkOver();
        // 关闭对讲页面好友数据
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_PERSON_CLOSE));
        // 进行呼叫
        callPerson();
    }

    /**
     * 呼叫/入组成功后操作
     */
    public void pushCallOk() {
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE));// 跳转到对讲主页
        activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.VIEW_INTER_PHONE_CHAT_OK));// 对讲主页界面，数据更新
    }

    // 挂断当前个人对讲
    private void talkOver() {
        if (ChatPresenter.data != null && ChatPresenter.data.getID() != null) {
            ChatPresenter.data.getID();// 挂断电话
        }
    }

    // 进行呼叫
    private void callPerson() {
        if (user != null) {
            DBTalkHistory l = model.assemblyData(user);
            String id = l.getID().trim();
            if (id != null && !id.equals("")) {
                model.del(id);
                Intent intent = new Intent(activity.getActivity(), CallAlertActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    // 退出组对讲
    private void talkOverGroup() {
        if (ChatPresenter.data != null && ChatPresenter.data.getID() != null) {
            ChatPresenter.data.getID();// 退出组
        }
    }
}
