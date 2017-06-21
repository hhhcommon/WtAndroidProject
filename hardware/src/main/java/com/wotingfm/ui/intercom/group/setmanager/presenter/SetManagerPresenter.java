package com.wotingfm.ui.intercom.group.setmanager.presenter;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.setmanager.view.SetManagerFragment;
import com.wotingfm.ui.intercom.group.setmanager.model.SetManagerModel;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class SetManagerPresenter {

    private final SetManagerFragment activity;
    private final SetManagerModel model;
    private List<Contact.user> list;


    public SetManagerPresenter(SetManagerFragment activity) {
        this.activity = activity;
        this.model = new SetManagerModel();
    }

    /**
     * 获取展示数据
     */
    public void getData() {
        list = model.getData();
        if (list != null && list.size() > 0) {
            activity.setView(list);
        }
    }

    /**
     * 点击按钮的数据更改
     * @param position
     */
    public void changeData(int position) {
        int type = list.get(position).getType();
        if (type == 1) {
            list.get(position).setType(2);
        } else {
            list.get(position).setType(1);
        }
        activity.setView(list);
    }

    /**
     * 发送申请
     */
    public void send() {

        String s="";
        if (s != null && !s.trim().equals("")) {
            model.loadNews(s, new SetManagerModel.OnLoadInterface() {
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
        } else {
            ToastUtils.show_always(activity.getActivity(), "提交数据不能为空");
        }
    }

    private void dealSuccess(Object o) {
    }


}
