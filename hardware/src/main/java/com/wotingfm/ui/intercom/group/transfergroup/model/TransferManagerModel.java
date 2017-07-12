package com.wotingfm.ui.intercom.group.transfergroup.model;

import android.util.Log;

import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class TransferManagerModel {

    /**
     * 测试数据
     *
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> data = GetTestData.getFriendList();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setType(3);
            }
        }
        return data;
    }

    /**
     * 组装数据
     *
     * @param list
     * @param id
     */
    public List<Contact.user> assemblyData(List<Contact.user> list, String id) {
        // 有群主
        if (id != null && !id.trim().equals("")) {
            for(int i=0;i<list.size();i++){
              String _id=  list.get(i).getId();
                if (_id != null && !_id.trim().equals("")) {
                    if(id.equals(_id)){
                        list.remove(i);
                    }
                }
            }
        }
        return list;
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
                if (list.get(i).getType() == 2) {
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
     * 设置管理员
     *
     * @param s
     * @param listener 监听
     */
    public void loadNews(String gid, String s, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().transferManager(gid, s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("移交群主返回数据", o.toString());
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
