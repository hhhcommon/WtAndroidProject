package com.wotingfm.ui.base.model;

import com.wotingfm.ui.base.baseinterface.OnLoadInterface;

import org.json.JSONObject;

/**
 * 作者：xinLong on 2017/5/16 14:46
 * 邮箱：645700751@qq.com
 */
public interface NetModelInterface {
    void loadNews(String url, String type, JSONObject js, OnLoadInterface listener);
}
