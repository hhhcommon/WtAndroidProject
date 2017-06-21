package com.wotingfm.ui.intercom.group.groupname.presenter;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupname.view.EditGroupNameFragment;
import com.wotingfm.ui.intercom.group.groupname.model.EditGroupNameModel;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditGroupNamePresenter {

    private final EditGroupNameFragment activity;
    private final EditGroupNameModel model;


    public EditGroupNamePresenter(EditGroupNameFragment activity) {
        this.activity = activity;
        this.model = new EditGroupNameModel();
    }

    /**
     * 发送申请
     * @param s
     */
    public void send(String s) {
        if(s!=null&&!s.trim().equals("")){
            model.loadNews(s, new EditGroupNameModel.OnLoadInterface() {
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
