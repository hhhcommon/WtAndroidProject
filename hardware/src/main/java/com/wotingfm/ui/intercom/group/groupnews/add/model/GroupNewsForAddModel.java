package com.wotingfm.ui.intercom.group.groupnews.add.model;

import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.user.login.model.Login;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNewsForAddModel extends UserInfo {

    public List<Contact.user> getPersonList() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

    public void getData() {

    }

    /**
     * 进行数据交互
     *
     * @param userName
     * @param password
     * @param listener 监听
     */
    public void loadNews(String userName, String password, final OnLoadInterface listener) {
//        RetrofitUtils.getInstance().retrofitService.login(userName, password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Login>() {
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
//                    public void onNext(Login login) {
//                        //填充UI
//                        listener.onSuccess(login);
//                    }
//                });
    }

    public interface OnLoadInterface {
        void onSuccess(Login login);

        void onFailure(String msg);
    }

}