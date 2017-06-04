package com.woting.commonplat.application;

import android.app.Application;
import android.content.Context;

/**
 * Application
 * Created by Administrator on 2017/5/25.
 */
public class WtApp extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
