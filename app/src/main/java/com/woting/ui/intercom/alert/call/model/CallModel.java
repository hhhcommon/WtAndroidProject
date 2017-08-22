package com.woting.ui.intercom.alert.call.model;

import com.woting.common.config.GlobalStateConfig;
import com.woting.ui.intercom.alert.call.view.CallAlertActivity;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class CallModel {
    private final CallAlertActivity activity;

    public CallModel(CallAlertActivity activity) {
        this.activity = activity;
    }

    /**
     * 获取好友数据
     *
     * @param id
     */
    public Contact.user getUser(String id) {
        Contact.user user = null;
        if (id != null && !id.equals("")) {
            List<Contact.user> list = GlobalStateConfig.list_person;
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String _id = list.get(i).getId();
                    if (_id != null && !_id.equals("")) {
                        if (id.equals(_id)) {
                            user = list.get(i);
                        }
                    }
                }
            }

        }
        return user;
    }
}
