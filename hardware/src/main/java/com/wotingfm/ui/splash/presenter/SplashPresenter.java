package com.wotingfm.ui.splash.presenter;

import android.content.Intent;

import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.basepresenter.BasePresenter;
import com.wotingfm.common.config.GlobalUrlConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.ui.splash.model.SplashModel;
import com.wotingfm.ui.splash.view.SplashActivity;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SplashPresenter extends BasePresenter {
    private final SplashModel splashModel;
    private SplashActivity splashActivity;

    public SplashPresenter(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
        this.splashModel = new SplashModel();
    }

    // 引导页页状态，为了下次打开后是进入
    private boolean getSplashState() {
        String first = BSApplication.SharedPreferences.getString(StringConstant.FIRST, "0");// 是否是第一次登录
        if (first != null && first.equals("1")) {
            return true; // 跳转到主页
        } else {
            return false;  // 跳转到引导页
        }
    }

    // 界面跳转
    private void close() {
        if (getSplashState()) {
            splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));       // 跳转到主页
        } else {
            splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));       // 跳转到引导页  WelcomeActivity
        }
        splashActivity.finish();
    }

    // 获取每次打开应用后的数据
    private void send() {
        JSONObject js = splashModel.assemblyData(splashActivity);
    }

    private void dealLoginSuccess(JSONObject result) {
        try {
            String ReturnType = result.getString("ReturnType");
            if (ReturnType != null && ReturnType.equals("1001")) {
                try {
                    JSONObject ui = (JSONObject) new JSONTokener(result.getString("UserInfo")).nextValue();
                    if (ui != null) {
                        // 保存用户数据
                        splashModel.saveUserInfo(ui);
                    } else {
                        splashModel.unRegisterLogin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    splashModel.unRegisterLogin();
                }
            } else {
                splashModel.unRegisterLogin();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            splashModel.unRegisterLogin();
        }
    }


    /**********************对外接口************************/
    /**
     * 界面延时操作的功能
     */
    public void todo() {
        if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
            send();
        } else {
            close();    // 界面跳转
        }
    }

}
