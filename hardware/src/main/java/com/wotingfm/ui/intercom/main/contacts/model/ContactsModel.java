package com.wotingfm.ui.intercom.main.contacts.model;

import com.wotingfm.common.net.RetrofitUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ContactsModel  {

    /**
     * 进行数据交互
     *
     * @param s
     */
    public void loadNews(String s, final OnLoadInterface listener) {
//        RetrofitUtils.getInstance().retrofitService.getFriends(s)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Contact>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        listener.onFailure("");
//                    }
//
//                    @Override
//                    public void onNext(Contact contact) {
//                        //填充UI
//                        listener.onSuccess(contact);
//                    }
//                });
    }

    public interface OnLoadInterface {
        void onSuccess(Contact contact);

        void onFailure(String msg);
    }

}
