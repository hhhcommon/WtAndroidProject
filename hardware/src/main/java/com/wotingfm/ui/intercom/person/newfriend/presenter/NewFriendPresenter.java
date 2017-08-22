package com.wotingfm.ui.intercom.person.newfriend.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriend;
import com.wotingfm.ui.intercom.person.newfriend.model.NewFriendModel;
import com.wotingfm.ui.intercom.person.newfriend.view.NewFriendFragment;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 新的好友申请控制器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class NewFriendPresenter {

    private NewFriendFragment activity;
    private NewFriendModel model;
    private List<NewFriend> list;
    private final InnerHandler mHandler;

    public NewFriendPresenter(NewFriendFragment activity) {
        this.activity = activity;
        this.model = new NewFriendModel();
        mHandler = new InnerHandler(activity);
        mHandler.sendEmptyMessageDelayed(0, 300); // 延时启动
    }

    // 获取数据
    private void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据
                list = model.getFriendList();
                if (list != null && list.size() > 0) {
                    activity.updateUI(list);
                    activity.isLoginView(0);
                } else {
                    activity.isLoginView(1);
                }
            } else {
                // 获取网络数据
                send();
            }
        } else {
            activity.isLoginView(3);
        }
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
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String applies = arg1.getString("applies");
                list = new Gson().fromJson(applies, new TypeToken<List<NewFriend>>() {
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

    /**
     * item的点击事件
     *
     * @param position
     */
    public void onClick(int position) {
        String id = null;
        try {
            id = list.get(position).getApply_user().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id != null && !id.equals("")) {
            boolean b = false;
            try {
                b = list.get(position).isHad_approved();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!b) {
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "false");
                bundle.putString("id", id);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            } else {
                PersonMessageFragment fragment = new PersonMessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "true");
                bundle.putString("id", id);
                fragment.setArguments(bundle);
                InterPhoneActivity.open(fragment);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "数据出错了，请稍后再试！");
        }
    }

    /**
     * 删除该条数据
     *
     * @param position
     */
    public void del(int position) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据==同意
                list.remove(position);
                activity.updateUI(list);
            } else {
                // 获取网络数据
                sendDel(position);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    /**
     * 同意
     *
     * @param position
     */
    public void apply(int position) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            if (GlobalStateConfig.test) {
                // 测试数据==同意
                list.get(position).setHad_approved(true);
                activity.updateUI(list);
            } else {
                // 获取网络数据
                sendApply(position);
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
        }
    }

    // 新的好友申请==同意
    private void sendApply(final int position) {
        activity.dialogShow();
        String s = list.get(position).getApply_id();
        model.loadNewsForApply(s, new NewFriendModel.OnLoadInterface() {
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

    // 新的好友申请==删除
    private void sendDel(final int position) {
        boolean approve = list.get(position).isHad_approved();
        if (approve) {
            // 执行删除操作
            activity.dialogShow();
            String s = list.get(position).getApply_id();
            model.loadNewsForDel(s, new NewFriendModel.OnLoadInterface() {
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
            // 执行拒绝操作
            activity.dialogShow();
            String s = list.get(position).getApply_id();
            model.loadNewsForRefuse(s, new NewFriendModel.OnLoadInterface() {
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

    // 处理新的好友申请==同意=返回数据
    private void dealApplySuccess(Object o, int position) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                list.get(position).setHad_approved(true);
                activity.updateUI(list);
                activity.getActivity().sendBroadcast(new Intent(BroadcastConstants.PERSON_GET));
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                } else {
                    ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
        }
    }

    // 处理新的好友申请==删除=返回数据
    private void dealDelSuccess(Object o, int position) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                list.remove(position);
                activity.updateUI(list);
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                } else {
                    ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
        }
    }

    // 处理新的好友申请==拒绝=返回数据
    private void dealRefuseSuccess(Object o, int position) {
        try {
            String s = new Gson().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                list.remove(position);
                activity.updateUI(list);
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity.getActivity(), msg);
                } else {
                    ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "出错了，请您稍后再试！");
        }
    }

    // 防止内存泄漏
    private class InnerHandler extends Handler {
        private final WeakReference<NewFriendFragment> mActivity;
        public InnerHandler(NewFriendFragment activity) {
            mActivity = new WeakReference<NewFriendFragment>(activity);
        }
        public void handleMessage(Message msg) {
            NewFriendFragment activity = mActivity.get();
            if (activity == null) {
                return;
            }
            getData();// 获取数据
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
        model = null;
    }
}
