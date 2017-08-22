package com.woting.ui.base.baseinterface;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/31 13:42
 * 邮箱：645700751@qq.com
 */
public interface OnLoadInterface {
        void onSuccess(JSONObject msg);

        void onFailure(String msg);

}
