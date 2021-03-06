package com.wotingfm.ui.user.splash.presenter;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.user.splash.model.SplashModel;
import com.wotingfm.ui.user.splash.view.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SplashPresenter extends BasePresenter {
    private SplashModel model;
    private SplashActivity activity;

    public SplashPresenter(SplashActivity activity) {
        this.activity = activity;
        this.model = new SplashModel();
    }

    // 界面跳转
    private void close() {
        activity.startActivity(new Intent(activity, MainActivity.class));       // 跳转到主页
        activity.finish();
    }

    /**
     * 界面延时操作的功能
     */
    public void todo() {
        // 测试代码
        if (GlobalStateConfig.test) {
            // 界面跳转
            model.saveTestLogin();
            close();
        } else {
            // 正式代码
            if (CommonUtils.isLogin()) {
                if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                    send();
                } else {
                    close();    // 界面跳转
                }
            } else {
                close();    // 界面跳转
            }
        }
    }

    // 获取每次打开应用后的数据
    private void send() {
        model.loadNews(new SplashModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                dealSuccess(o);
            }

            @Override
            public void onFailure(String msg) {
                close();    // 界面跳转
            }
        });
    }

    // 处理返回数据
    private void dealSuccess(Object o) {
        try {
            Gson g = new GsonBuilder().serializeNulls().create();
            String s = g.toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                try {
                    String  token = arg1.getString("token");
                    // 保存后台获取到的token
                    if (token != null && !token.trim().equals("")) {
                        model.saveToken(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject ui = (JSONObject) new JSONTokener(arg1.getString("user")).nextValue();
                // 保存用户数据
                if (ui != null) {
                    model.saveUserInfo(ui);
                }
                close();    // 界面跳转
            } else {
                String msg = js.getString("msg");
                if (msg != null && !msg.trim().equals("")) {
                    ToastUtils.show_always(activity, msg);
                }
                close();    // 界面跳转
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();    // 界面跳转
        }
    }

    /**
     * 数据销毁
     */
    public void destroy(){
        model=null;
    }
}
