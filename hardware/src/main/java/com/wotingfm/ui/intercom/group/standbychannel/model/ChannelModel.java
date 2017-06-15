package com.wotingfm.ui.intercom.group.standbychannel.model;

import com.wotingfm.common.utils.FrequencyUtil;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.login.model.Login;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ChannelModel extends UserInfo {

    public List<String> getData(){
        List<String> list = FrequencyUtil.getFrequencyList();
        return list;
    }

    /**
     * 进行数据交互
     *
     * @param userName
     * @param password
     * @param listener 监听
     */
    public void loadNews(String userName, String password, final OnLoadInterface listener) {
        getRetrofitService().login(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Login>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure("");
                    }

                    @Override
                    public void onNext(Login login) {
                        //填充UI
                        listener.onSuccess(login);
                    }
                });
    }

    public interface OnLoadInterface {
        void onSuccess(Login login);

        void onFailure(String msg);
    }

}
