package com.wotingfm.ui.intercom.person.personnote.presenter;

import android.os.Bundle;

import com.woting.commonplat.config.GlobalNetWorkConfig;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.person.personnote.model.EditPersonNoteModel;
import com.wotingfm.ui.intercom.person.personnote.view.EditPersonNoteFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditPersonNotePresenter {

    private final EditPersonNoteFragment activity;
    private final EditPersonNoteModel model;
    private final String id;


    public EditPersonNotePresenter(EditPersonNoteFragment activity) {
        this.activity = activity;
        this.model = new EditPersonNoteModel();
       Bundle bundle= activity.getArguments();
        id= bundle.getString("id");
    }

    /**
     * 发送申请
     * @param s
     */
    public void send(String s) {
        if(s!=null&&!s.trim().equals("")){
            if (GlobalNetWorkConfig.CURRENT_NETWORK_STATE_TYPE != -1) {
                activity.dialogShow();
            model.loadNews(id,s, new EditPersonNoteModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();                    dealSuccess(o);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                }
            });
            }else{
                ToastUtils.show_always(activity.getActivity(), "网络连接失败，请稍后再试！");
            }
        }else{
            ToastUtils.show_always(activity.getActivity(),"提交数据不能为空");
        }
    }

    private void dealSuccess(Object o) {
    }


}
