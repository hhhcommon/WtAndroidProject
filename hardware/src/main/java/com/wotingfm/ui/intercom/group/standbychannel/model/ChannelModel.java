package com.wotingfm.ui.intercom.group.standbychannel.model;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.FrequencyUtil;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.login.model.Login;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
//        RetrofitUtils.getInstance().register(userName, password,yzm)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Object>() {
//                    @Override
//                    public void call(Object o) {
//                        //填充UI
//                        listener.onSuccess(o);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                        listener.onFailure("");
//                    }
//                });
    }

    public interface OnLoadInterface {
        void onSuccess(Login login);

        void onFailure(String msg);
    }

}
