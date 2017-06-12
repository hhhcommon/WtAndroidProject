package com.wotingfm.ui.user.retrievepassword.model;


import com.wotingfm.ui.base.baseinterface.OnLoadInterface;
import com.wotingfm.ui.base.model.CommonModel;
import com.wotingfm.ui.base.model.UserInfo;
import com.wotingfm.ui.user.login.model.Login;
import com.wotingfm.ui.user.retrievepassword.view.RetrievePassWordFragment;

import org.json.JSONObject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class RetrievePasswordModel extends UserInfo {

    /**
     * 组装数据
     *
     * @param activity
     * @return
     */
    public JSONObject assemblyData(RetrievePassWordFragment activity) {
        JSONObject jsonObject = CommonModel.getJsonObject(activity.getActivity());
        return jsonObject;
    }

    /**
     * 进行数据交互
     *
     * @param userName
     * @param password
     * @param yzm
     * @param listener 监听
     */
    public void loadNews(String userName, String password, String yzm, final OnLoadInterface listener) {
        getRetrofitService().resetPasswords(userName, password, yzm)
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
