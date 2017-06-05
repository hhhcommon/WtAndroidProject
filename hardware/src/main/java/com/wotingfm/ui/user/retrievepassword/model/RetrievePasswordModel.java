package com.wotingfm.ui.user.retrievepassword.model;

import com.android.volley.VolleyError;
import com.woting.commonplat.net.volley.VolleyCallback;
import com.woting.commonplat.net.volley.VolleyRequest;
import com.wotingfm.common.config.GlobalUrlConfig;
import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.model.CommonModel;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.register.view.RegisterActivity;
import com.wotingfm.ui.user.retrievepassword.view.RetrievePassWordActivity;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class RetrievePasswordModel extends UserInfo {

    /**
     * 组装数据
     *
     * @param activity
     * @return
     */
    public JSONObject assemblyData(RetrievePassWordActivity activity) {
        JSONObject jsonObject = CommonModel.getJsonObject(activity);
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
    public void loadNews(String url, String tag, JSONObject js, final OnLoadInterface listener) {
        VolleyRequest.requestPost(GlobalUrlConfig.baseUrl + url, tag, js, new VolleyCallback() {
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
