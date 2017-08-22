package com.woting.ui.mine.myfocus.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.common.application.BSApplication;
import com.woting.common.constant.StringConstant;
import com.woting.common.utils.ToastUtils;
import com.woting.ui.mine.myfocus.model.Focus;
import com.woting.ui.mine.myfocus.model.MyFocusModel;
import com.woting.ui.mine.myfocus.view.MyFocusFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class MyFocusPresenter {

    private MyFocusFragment activity;
    private MyFocusModel model;

    public MyFocusPresenter(MyFocusFragment activity) {
        this.activity = activity;
        this.model = new MyFocusModel();
    }

    /**
     * 获取我关注的人的列表
     */
    public void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
            model.loadNews(id, new MyFocusModel.OnLoadInterface() {
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

    // 获取我关注的人的列表
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
                String focus = arg1.getString("idols");
                // 关注的人
                List<Focus> list = new Gson().fromJson(focus, new TypeToken<List<Focus>>() {
                }.getType());
                if (list != null && list.size() > 0) {
                    activity.updateUI(list);
                    activity.isLoginView(0);
                } else {
                    activity.isLoginView(1);
                }
            } else {
                activity.isLoginView(4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.isLoginView(4);
        }
    }

    /**
     * 取消关注的点击事件
     *
     * @param position
     */
    public void del(final List<Focus> list, final int position) {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            String idol_id = list.get(position).getId();
            String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
            if (idol_id == null || idol_id.trim().equals("")) {
                return;
            }
            activity.dialogShow();
            model.loadNewsDel(idol_id, id, new MyFocusModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    dealDelSuccess(o, list, position);
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

    // 取消关注返回数据
    private void dealDelSuccess(Object o, List<Focus> list, int position) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                list.remove(position);
                if (list != null && list.size() > 0) {
                    activity.updateUI(list);
                    activity.isLoginView(0);
                } else {
                    activity.isLoginView(1);
                }
            } else {
                ToastUtils.show_always(activity.getActivity(), "取消关注失败，请稍后再试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show_always(activity.getActivity(), "取消关注失败，请稍后再试！");
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
