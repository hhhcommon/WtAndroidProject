package com.woting.commonplat.net.volley;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;

/**
 * Volley 网络请求类
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class VolleyRequest {
    //volley请求超时 时间
    public static  int HTTP_CONNECTION_TIMEOUT = 0 * 1000;
    private static RequestQueue Queues;

    public static void set(RequestQueue queues){
        Queues=queues;
    }

    /**
     * post网络请求  自定义标签  用于取消网络请求
     *
     * @param tag        标签
     * @param url        网络请求地址
     * @param jsonObject 请求参数
     * @param callback   返回值
     */
    public static void requestPost(String url, String tag, JSONObject jsonObject, VolleyCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Method.POST, url, jsonObject, callback.loadingListener(), callback.errorListener());

        jsonObjectRequest.setTag(tag);// 设置标签
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(HTTP_CONNECTION_TIMEOUT, 1, 1.0f));
        Queues.add(jsonObjectRequest);// 加入队列
        long a = System.currentTimeMillis();
        Log.i("请求服务器时间", "--- > > >  " + a);
        Log.i("请求服务器地址", "--- > > >  " + url);
        Log.i("请求服务器提交的参数", "--- > > >  " + jsonObject.toString());
    }

    /**
     * get网络请求  自定义标签  用于取消网络请求
     *
     * @param tag      标签
     * @param url      网络请求地址
     * @param callback 返回值
     */
    public static void requestGet(String url, String tag, VolleyNewCallback callback) {
        StringRequest jsonObjectRequest = new StringRequest(
                Method.GET, url, callback.loadingListenerString(), callback.errorListener());
        jsonObjectRequest.setTag(tag);// 设置标签
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(HTTP_CONNECTION_TIMEOUT, 1, 1.0f));
        Queues.add(jsonObjectRequest);// 加入队列
        long a = System.currentTimeMillis();
        Log.i("请求服务器时间", "--- > > >  " + a);
        Log.i("请求服务器地址", "--- > > >  " + url);
    }

    /**
     * 取消自定义标签的网络请求
     */
    public static boolean cancelRequest(String tag) {
        Queues.cancelAll(tag);
        Log.i("取消网络请求", "--- > > >" + "\t" + tag);
        return true;
    }

}