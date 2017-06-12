package com.wotingfm.ui.user.guide.presenter;

import android.content.Intent;

import com.wotingfm.common.config.GlobalUrlConfig;
import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.user.guide.model.GuideModel;
import com.wotingfm.ui.user.guide.view.GuideActivity;
import com.wotingfm.ui.user.preference.PreferenceFragment;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GuidePresenter extends BasePresenter {
    private final GuideModel model;
    private GuideActivity activity;

    public GuidePresenter(GuideActivity activity) {
        this.activity = activity;
        this.model = new GuideModel();
    }

    // 界面跳转
    private void close() {
        activity.startActivity(new Intent(activity, MainActivity.class));       // 跳转到引导页  PreferenceActivity
        activity.finish();
    }

    // 获取每次打开应用后的数据
    private void send() {
        JSONObject js = model.assemblyData(activity);
        model.loadNews(GlobalUrlConfig.splashUrl, activity.getTag(), js, new OnLoadInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                dealLoginSuccess(result);
                close();    // 界面跳转
            }

            @Override
            public void onFailure(String msg) {
                close();    // 界面跳转
            }
        });
    }

    private void dealLoginSuccess(JSONObject result) {
        try {
            String ReturnType = result.getString("ReturnType");
            if (ReturnType != null && ReturnType.equals("1001")) {
                try {
                    JSONObject ui = (JSONObject) new JSONTokener(result.getString("UserInfo")).nextValue();
                    if (ui != null) {
                        // 保存用户数据
                        model.saveUserInfo(ui);
                    } else {
                        model.unRegisterLogin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    model.unRegisterLogin();
                }
            } else {
                model.unRegisterLogin();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            model.unRegisterLogin();
        }
    }

    /**********************对外接口************************/
    /**
     * 界面延时操作的功能
     */
    public void todo() {
//        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
//            send();
//        } else {
//            close();    // 界面跳转
//        }
        close();    // 界面跳转
    }

}
