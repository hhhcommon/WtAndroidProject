package com.wotingfm.ui.mine.feedback.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.mine.fm.model.FMInfo;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class FeedbackModel {

    /**
     * 进行数据交互
     * @param information
     * @param feedback
     * @param listener 监听
     */
    public void loadNews(String information,String feedback, final OnLoadInterface listener) {
        String id= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
        RetrofitUtils.getInstance().feedback(id,information,feedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        try {
                            Log.e("提交意见反馈返回数据",new Gson().toJson(o));
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
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
