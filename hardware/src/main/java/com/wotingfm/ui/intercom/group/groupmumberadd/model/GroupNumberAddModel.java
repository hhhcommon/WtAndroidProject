package com.wotingfm.ui.intercom.group.groupmumberadd.model;


import android.util.Log;

import com.google.gson.Gson;
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
public class GroupNumberAddModel {

    /**
     * 测试数据
     * @return
     */
    public List<Contact.user> getData() {
        List<Contact.user> list = GetTestData.getFriendList();
        return list;
    }

    /**
     * 组装数据
     *
     * @param src_list 群成员
     * @param list 好友列表
     */
    public List<Contact.user> assemblyData(List<Contact.user> src_list, List<Contact.user> list) {
        for(int i=0;i<src_list.size();i++){
            String src_id=src_list.get(i).getId();
            if(src_id!=null&&!src_id.trim().equals("")){
                for(int j=0;j<list.size();j++){
                    String id=list.get(i).getId();
                    if(id!=null&&!id.trim().equals("")){
                        if(id.equals(src_id)){
                            list.remove(j);
                            break;
                        }
                    }
                }
            }
        }

        return list;
    }

    /**
     * 群成员==添加
     *
     * @param listener 监听
     */
    public void loadNewsForAdd(String gid,String id,  final OnLoadInterface listener) {
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
