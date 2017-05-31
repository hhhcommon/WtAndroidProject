package com.wotingfm.splash.model;

import com.wotingfm.base.baseinterface.OnLoadInterface;
import com.wotingfm.base.model.UserInfo;
import com.wotingfm.splash.view.SplashActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class SplashModel extends UserInfo {

    /**
     * 组装数据
     *
     * @param splashActivity
     * @return
     */
    public JSONObject assemblyData( SplashActivity splashActivity) {
        JSONObject jsonObject = VolleyRequest.getJsonObject(splashActivity);
        return jsonObject;
    }

    /**
     * 进行数据交互
     *
     * @param url      请求地址
     * @param tag      地址标签
     * @param js       请求参数
     * @param listener 监听
     */
    @Override
    public void loadNews(String url, String tag, JSONObject js, final OnLoadInterface listener) {
        VolleyRequest.requestPost(url, tag, js, new VolleyCallback() {
            @Override
            protected void requestSuccess(JSONObject result) {
                listener.onSuccess(result);
            }

            @Override
            protected void requestError(VolleyError error) {
                listener.onFailure("");
            }
        });
    }

}
