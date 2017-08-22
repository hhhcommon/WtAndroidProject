package com.woting.ui.user.guide.presenter;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.woting.common.config.GlobalStateConfig;
import com.woting.common.utils.CommonUtils;
import com.woting.common.utils.ToastUtils;
import com.woting.ui.base.basepresenter.BasePresenter;
import com.woting.ui.main.view.MainActivity;
import com.woting.ui.user.guide.model.GuideModel;
import com.woting.ui.user.guide.view.GuideActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GuidePresenter extends BasePresenter {
    private GuideModel model;
    private GuideActivity activity;

    public GuidePresenter(GuideActivity activity) {
        this.activity = activity;
        this.model = new GuideModel();
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
        model.loadNews(new GuideModel.OnLoadInterface() {
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
