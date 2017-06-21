package com.wotingfm.ui.intercom.person.personnote.presenter;

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


    public EditPersonNotePresenter(EditPersonNoteFragment activity) {
        this.activity = activity;
        this.model = new EditPersonNoteModel();
    }

    /**
     * 发送申请
     * @param s
     */
    public void send(String s) {
        if(s!=null&&!s.trim().equals("")){
            model.loadNews(s, new EditPersonNoteModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
//                loginView.removeDialog();
                    dealSuccess(o);
                }

                @Override
                public void onFailure(String msg) {
//                loginView.removeDialog();
//                ToastUtils.showVolleyError(loginView);
                }
            });
        }else{
            ToastUtils.show_always(activity.getActivity(),"提交数据不能为空");
        }
    }

    private void dealSuccess(Object o) {
    }


}
