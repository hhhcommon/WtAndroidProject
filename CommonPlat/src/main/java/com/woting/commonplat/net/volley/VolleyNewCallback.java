package com.woting.commonplat.net.volley;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * 请求成功或失败的执行操作抽象类
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public abstract class VolleyNewCallback {


    /**
     * 网络请求成功监听 get
     *
     * @return
     */
    Listener<String> loadingListenerString() {
        return new Listener<String>() {

            @Override
            public void onResponse(String result) {
                requestSuccess(result);
                try {
                    Log.i("请求成功返回的数据", result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 网络请求失败监听
     *
     * @return
     */
    ErrorListener errorListener() {
        return new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                requestError(error);

                try {
                    Log.i("请求失败返回的信息", error.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     * 请求成功的回调 get
     *
     * @param result
     */
    protected abstract void requestSuccess(String result);

    /**
     * 请求失败的回调
     *
     * @param error
     */
    protected abstract void requestError(VolleyError error);
}
