package com.woting.commonplat.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Application
 * Created by Administrator on 2017/5/25.
 */
public class WtApp extends Application {
    public static Context mContext;
    private static RequestQueue queues;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        queues = Volley.newRequestQueue(this);
    }

    //volley
    public static RequestQueue getHttpQueues() {
        return queues;
    }
}
