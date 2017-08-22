package com.woting.ui.main.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.common.net.RetrofitUtils;
import com.woting.ui.base.model.UserInfo;
import com.woting.ui.main.view.MainActivity;

import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class MainModel extends UserInfo  {
    private final MainActivity mainActivity;

    public MainModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        loadNews();
    }
    //

    /**
     * 极光id绑定
     */
    private void loadNews() {
        try {
            String id= JPushInterface.getRegistrationID(mainActivity);
            RetrofitUtils.getInstance().bingJG(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("极光ID绑定返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最新版本号
     * @param listener 监听
     */
    public void getVersion( final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getVersion()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取版本号返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);
        void onFailure(String msg);
    }
}
