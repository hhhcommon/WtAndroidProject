package com.wotingfm.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.wotingfm.common.application.BSApplication;


/**
 * Created by amine on 16/5/18.
 */
public class T {

    private static volatile T sToastUtil = null;

    private Toast mToast = null;

    /**
     * 获取实例
     *
     * @return
     */
    public static T getInstance() {
        if (sToastUtil == null) {
            synchronized (T.class) {
                if (sToastUtil == null) {
                    sToastUtil = new T();
                }
            }
        }
        return sToastUtil;
    }

    protected Handler handler = new Handler(Looper.getMainLooper());

    public void showToast(final String tips) {
        showToast(tips, Toast.LENGTH_SHORT);
    }

    public void showToast(final int tips) {
        showToast(tips, Toast.LENGTH_SHORT);
    }

    public void showToast(final String tips, final int duration) {
        if (android.text.TextUtils.isEmpty(tips)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(BSApplication.getInstance(), tips, duration);
                    mToast.show();
                } else {
                    //mToast.cancel();
                    //mToast.setView(mToast.getView());
                    mToast.setText(tips);
                    mToast.setDuration(duration);
                    mToast.show();
                }
            }
        });
    }

    public void showToast(final int tips, final int duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(BSApplication.getInstance(), tips, duration);
                    mToast.show();
                } else {
                    //mToast.cancel();
                    //mToast.setView(mToast.getView());
                    mToast.setText(tips);
                    mToast.setDuration(duration);
                    mToast.show();
                }
            }
        });
    }

}
