package com.wotingfm.ui.intercom.group.groupapply.presenter;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.editgroupmessage.model.EditGroupMessageModel;
import com.wotingfm.ui.intercom.group.groupapply.model.GroupApplyModel;
import com.wotingfm.ui.intercom.group.groupapply.view.GroupApplyFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class GroupApplyPresenter {

    private final GroupApplyFragment activity;
    private final GroupApplyModel model;


    public GroupApplyPresenter(GroupApplyFragment activity) {
        this.activity = activity;
        this.model = new GroupApplyModel();
    }

    /**
     * 发送申请
     * @param s
     */
    public void send(String s) {
        if(s!=null&&!s.trim().equals("")){
            model.loadNews(s, new GroupApplyModel.OnLoadInterface() {
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

    /**
     * 字数更改之后变化
     * @param src
     */
    public void textChange(String src) {

        activity.setTextViewChange("");
    }
}
