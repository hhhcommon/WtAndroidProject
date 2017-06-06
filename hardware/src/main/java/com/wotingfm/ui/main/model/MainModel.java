package com.wotingfm.ui.main.model;

import com.wotingfm.common.config.GlobalUrlConfig;
import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.model.CommonModel;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.splash.view.SplashActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MainModel extends UserInfo  {

    /**
     * 组装数据
     *
     * @param splashActivity
     * @return
     */
    public JSONObject assemblyData( SplashActivity splashActivity) {
        JSONObject jsonObject = CommonModel.getJsonObject(splashActivity);
        return jsonObject;
    }


}
