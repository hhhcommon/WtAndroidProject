package com.wotingfm.ui.intercom.group.groupmumberdel.model;


import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 删除群成员
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNumberDelModel {

    /**
     * 测试数据
     *
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> list = GetTestData.getFriendList();
        try {
            list.get(0).setType(1);
            list.get(1).setType(2);
            list.get(2).setType(2);
            list.get(3).setType(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断是不是自己好友
     *
     * @param list
     * @param position
     * @return
     */
    public boolean isFriend(List<Contact.user> list, int position) {
        boolean b = false;
        if (list != null && list.size() > 0) {
            Contact.user u = list.get(position);
            if (u != null) {
                String id = u.getId();
                if (id != null && !id.trim().equals("")) {
                    List<Contact.user> _list = GlobalStateConfig.list_person;
                    if (_list != null && _list.size() > 0) {
                        for (int i = 0; i < _list.size(); i++) {
                            String _id = _list.get(i).getId();
                            if (_id.equals(id)) {
                                b = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return b;
    }

    /**
     * 组装数据
     *
     * @param list
     * @param id
     */
    public List<Contact.user> assemblyData(List<Contact.user> list, String id) {
        List<Contact.user> _list = new ArrayList<>();
        // 有群主
        if (id != null && !id.trim().equals("")) {
            // 添加群主
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_owner()  ;
                if (b) {
                    String _id = list.get(i).getId();
                    if (_id != null && !_id.trim().equals("")) {
                        if (id.equals(_id)) {
                            list.get(i).setType(1);
                            _list.add(list.get(i));
                        }
                    }
                }
            }

            // 添加管理员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    String _id = list.get(i).getId();
                    if (_id != null && !_id.trim().equals("")) {
                        if (!id.equals(_id)) {
                            list.get(i).setType(2);
                            _list.add(list.get(i));
                        }
                    }
                }
            }

            // 添加成员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                boolean b1 = list.get(i).is_owner();
                if (!b&&!b1) {
                    list.get(i).setType(3);
                    _list.add(list.get(i));
                }
            }
        } else {
            // 添加管理员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                if (b) {
                    list.get(i).setType(2);
                    _list.add(list.get(i));
                }
            }
            // 添加成员
            for (int i = 0; i < list.size(); i++) {
                boolean b = list.get(i).is_admin();
                boolean b1 = list.get(i).is_owner();
                if (!b&&!b1) {
                    list.get(i).setType(3);
                    _list.add(list.get(i));
                }
            }
        }

        return _list;
    }

    /**
     * 群成员==删除
     *
     * @param listener 监听
     */
    public void loadNewsForDel(String gid, String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().groupNumDel(gid, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("群成员删除==返回数据", new Gson().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
