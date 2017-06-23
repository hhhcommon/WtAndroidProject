package com.wotingfm.ui.intercom.person.newfriend.presenter;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriend;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;
import com.wotingfm.ui.user.login.model.LoginModel;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 新的好友申请控制器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class NewFriendPresenter {

    private final NewFriendFragment activity;
    private final NewFriendModel model;

    public NewFriendPresenter(NewFriendFragment activity) {
        this.activity = activity;
        this.model = new NewFriendModel();
    }

    // 新的好友申请请求
    private void send() {
        activity.dialogShow();
        model.loadNews(new NewFriendModel.OnLoadInterface() {
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
                String applies = arg1.getString("applies");
                List<NewFriend> list = new Gson().fromJson(applies, new TypeToken<List<NewFriend>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    activity.updateUI(list);
                    activity.isLoginView(0);
                } else {
                    activity.isLoginView(1);
                }
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.isLoginView(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(1);
        }
    }

    /**
     * 获取数据
     */
    public void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                List<NewFriend> list = model.getFriendList();
                activity.updateUI(list);
                activity.isLoginView(0);
            } else {
                // 获取网络数据
                send();
            }
        } else {
            activity.isLoginView(3);
        }
    }

    /**
     * 提示事件的点击事件
     *
     * @param type
     */
    public void tipClick(int type) {
        if (type == 3) {
            // 跳转到登录界面
            activity.getActivity().startActivity(new Intent(activity.getActivity(), LogoActivity.class));
        } else if (type == 4) {
            // 重新获取数据
            getData();
        }
    }

    public void del(int num){

    }

    public void apply(int num){
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                List<NewFriend> list = model.getFriendList();
                activity.updateUI(list);
                activity.isLoginView(0);
            } else {
                // 获取网络数据
                send();
            }
        } else {
            activity.isLoginView(3);
        }
    }

    // 新的好友申请==同意
    private void sendApply() {
        activity.dialogShow();
        model.loadNewsForApply(new NewFriendModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealApplySuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                activity.isLoginView(4);
            }
        });
    }

    // 处理新的好友申请==同意=返回数据
    private void dealApplySuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {

            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.isLoginView(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(1);
        }
    }

    // 新的好友申请==删除
    private void sendDel() {
        activity.dialogShow();
        model.loadNewsForDel(new NewFriendModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealDelSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                activity.isLoginView(4);
            }
        });
    }

    // 处理新的好友申请==删除=返回数据
    private void dealDelSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {

            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.isLoginView(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(1);
        }
    }

    // 新的好友申请==拒绝
    private void sendRefuse() {
        activity.dialogShow();
        model.loadNewsForRefuse(new NewFriendModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealRefuseSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                activity.isLoginView(4);
            }
        });
    }

    // 处理新的好友申请==拒绝=返回数据
    private void dealRefuseSuccess(Object o) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {

            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                }
                activity.isLoginView(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(1);
        }
    }


}
