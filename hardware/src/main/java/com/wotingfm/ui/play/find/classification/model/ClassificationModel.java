package com.wotingfm.ui.play.find.classification.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ClassificationModel extends UserInfo {

    /**
     * 进行数据交互
     * @param listener 监听
     */
    public void loadNews(final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getClassifications()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Classification.DataBeanX>>() {
                        @Override
                        public void call(List<Classification.DataBeanX> o) {
                            Log.e("获取分类返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
                            listener.onSuccess(o);
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
        void onSuccess(List<Classification.DataBeanX>  o);

        void onFailure(String msg);
    }

}
