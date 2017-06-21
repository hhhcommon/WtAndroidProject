package com.wotingfm.ui.intercom.group.groupintroduce.presenter;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.groupintroduce.model.EditGroupIntroduceModel;
import com.wotingfm.ui.intercom.group.groupintroduce.view.EditGroupIntroduceFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditGroupIntroducePresenter {

    private final EditGroupIntroduceFragment activity;
    private final EditGroupIntroduceModel model;


    public EditGroupIntroducePresenter(EditGroupIntroduceFragment activity) {
        this.activity = activity;
        this.model = new EditGroupIntroduceModel();
    }

    /**
     * 发送申请
     * @param s
     */
    public void send(String s) {
        if(s!=null&&!s.trim().equals("")){
            model.loadNews(s, new EditGroupIntroduceModel.OnLoadInterface() {
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
