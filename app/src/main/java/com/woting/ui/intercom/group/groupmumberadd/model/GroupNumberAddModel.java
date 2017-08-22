package com.woting.ui.intercom.group.groupmumberadd.model;


import android.util.Log;

import com.google.gson.Gson;
import com.woting.common.net.RetrofitUtils;
import com.woting.common.utils.GetTestData;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class GroupNumberAddModel {

    /**
     * 测试数据
     *
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

    /**
     * 组装数据,去除已经是群成员的好友
     *
     * @param src_list 群成员
     * @param list     好友列表
     */
    public List<Contact.user> assemblyData(List<Contact.user> src_list, List<Contact.user> list) {
        List<Contact.user> _list = new ArrayList<>();
        // 遍历好友列表
        for (int i = 0; i < list.size(); i++) {
            String src_id = list.get(i).getId();
            if (src_id != null && !src_id.trim().equals("")) {
                boolean b = false;
                for (int j = 0; j < src_list.size(); j++) {
                    String id = src_list.get(j).getId();
                    if (id != null && !id.trim().equals("")) {
                        if (id.equals(src_id)) {
                            b = true;
                            break;
                        }
                    }
                }
                if (!b) _list.add(list.get(i));
            }
        }

        for(int j=0;j<_list.size();j++){
            _list.get(j).setIs_admin(false);
        }
        return _list;
    }

    /**
     * 获取提交数据
     *
     * @return
     */
    public String getString(List<Contact.user> list) {
        StringBuffer s = new StringBuffer();
        String S = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).is_admin()) {
                    String id = list.get(i).getId();
                    s.append(id + ",");
                }
            }
        }
        // 去掉最后一个逗号
        if (s.length() > 0) {
            S = s.substring(0, s.length() - 1);
        }
        return S;
    }

    /**
     * 群成员==添加
     *
     * @param listener 监听
     */
    public void loadNewsForAdd(String gid, String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().groupNumAdd(gid, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("群成员添加==返回数据", new Gson().toJson(o));
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
