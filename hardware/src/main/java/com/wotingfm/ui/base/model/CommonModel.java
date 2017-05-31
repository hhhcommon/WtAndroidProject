package com.wotingfm.ui.base.model;

import android.content.Context;

import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/31 15:13
 * 邮箱：645700751@qq.com
 */
public class CommonModel {

    /**
     * 获取网络请求公共请求属性
     */
    public static JSONObject getJsonObject(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MobileClass", PhoneMsgManager.model + "::" + PhoneMsgManager.productor);
            jsonObject.put("ScreenSize", PhoneMsgManager.ScreenWidth + "x" + PhoneMsgManager.ScreenHeight);
            jsonObject.put("IMEI", PhoneMsgManager.imei);
            jsonObject.put("GPS-longitude", GlobalAddressConfig.longitude);
            jsonObject.put("GPS-latitude ", GlobalAddressConfig.latitude);
            jsonObject.put("PCDType", GlobalStateConfig.PCDType);
            String userId = CommonUtils.getSocketUserId();
            if (userId != null && !userId.trim().equals("")) {
                jsonObject.put("UserId", userId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
