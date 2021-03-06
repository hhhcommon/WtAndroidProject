package com.wotingfm.ui.intercom.person.personnote.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 修改备注信息
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class EditPersonNoteModel extends UserInfo {


    /**
     * 进行数据交互
     * @param s
     * @param listener 监听
     */
    public void loadNews(String id, String s, final OnLoadInterface listener) {
        try {
            String pid= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
            RetrofitUtils.getInstance().editPersonNote(pid,id,s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("修改备注返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
