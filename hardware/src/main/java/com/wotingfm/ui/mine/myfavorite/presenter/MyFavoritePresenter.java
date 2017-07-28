package com.wotingfm.ui.mine.myfavorite.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.mine.myfavorite.model.Favorite;
import com.wotingfm.ui.mine.myfavorite.model.MyFavoriteModel;
import com.wotingfm.ui.mine.myfavorite.view.MyFavoriteFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * 新的好友申请控制器
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class MyFavoritePresenter {

    private final MyFavoriteFragment activity;
    private final MyFavoriteModel model;

    public MyFavoritePresenter(MyFavoriteFragment activity) {
        this.activity = activity;
        this.model = new MyFavoriteModel();
    }

    /**
     * 获取我喜欢的节目的列表
     */
    public void getData() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            activity.dialogShow();
            String id = BSApplication.SharedPreferences.getString(StringConstant.USER_ID, "");
            model.loadNews(id, new MyFavoriteModel.OnLoadInterface() {
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

    // 获取我喜欢的节目的列表=返回数据
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
                String favorite = arg1.getString("likes");
                // 我喜欢的列表
                List<Favorite> list = new Gson().fromJson(favorite, new TypeToken<List<Favorite>>() {
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
     * item的点击事件
     *
     * @param position
     */
    public void onItemClick(List<Favorite> list, int position) {

    }

}
