package com.wotingfm.ui.play.radio.radioinfo.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.play.radio.radioinfo.view.RadioInfoFragment;

import java.lang.ref.WeakReference;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class RadioInfoPresenter {

    private RadioInfoFragment activity;
    private AlbumInfo s;
    private String rid;
    private InnerHandler mHandler;

    public RadioInfoPresenter(RadioInfoFragment activity) {
        this.activity = activity;
        getData();
    }

    // 获取传递数据
    private void getData() {
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            activity.setTitle(title);
            rid = bundle.getString("rId");
            mHandler = new InnerHandler(activity);
            mHandler.sendEmptyMessageDelayed(0, 300); // 延时启动
        } else {
            activity.showErrorView();
        }
    }

    // 防止内存泄漏
    private class InnerHandler extends Handler {
        private final WeakReference<RadioInfoFragment> mActivity;

        public InnerHandler(RadioInfoFragment activity) {
            mActivity = new WeakReference<RadioInfoFragment>(activity);
        }

        public void handleMessage(Message msg) {
            RadioInfoFragment activity = mActivity.get();
            if (activity == null) {
                return;
            }
            activity.refresh();
        }
    }

    public String getRid(){
        return rid;
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
