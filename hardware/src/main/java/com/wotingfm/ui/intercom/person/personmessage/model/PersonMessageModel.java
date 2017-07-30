package com.wotingfm.ui.intercom.person.personmessage.model;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.GetTestData;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.contacts.fragment.ContactsFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.contacts.view.CharacterParser;
import com.wotingfm.ui.intercom.main.contacts.view.PinyinComparator;
import com.wotingfm.ui.intercom.person.personmessage.view.PersonMessageFragment;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 好友信息的数据中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class PersonMessageModel  {
    private final PersonMessageFragment activity;
    private SearchTalkHistoryDao dbDao;

    public PersonMessageModel(PersonMessageFragment activity) {
        this.activity = activity;
        initDao();      // 初始化数据库
    }

    // 初始化数据库
    private void initDao() {
        dbDao = new SearchTalkHistoryDao(activity.getActivity());
    }

    /**
     * 获取订阅数据
     * @return
     */
    public List<AlbumsBean>  getTestData(){
        List<AlbumsBean> list= GetTestData.getAlbumsList();
        return list;
    }
    /**
     * 组装数据库数据
     * @param s
     * @return
     */
    public DBTalkHistory assemblyData(Contact.user s,String callType, String CallTypeM) {
        String id = s.getId();
        String type = "person";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime,callType,CallTypeM,s.getAcc_id());
        return h;
    }

    /**
     * 从数据库中删除一条数据
     * @param id
     */
    public void del(String id){
        if(dbDao==null){
            initDao();
            dbDao.deleteHistory(id);
        }else{
            dbDao.deleteHistory(id);
        }
    }

    /**
     * 在数据库中插入一条数据
     * @param h
     */
    public void add(DBTalkHistory h){
        if(dbDao==null){
            initDao();
            dbDao.addTalkHistory(h);
        }else{
            dbDao.addTalkHistory(h);
        }
    }

    /**
     * 获取好友信息
     * @param listener 监听
     */
    public void loadNews(String id,final OnLoadInterface listener) {
        String token= BSApplication.SharedPreferences.getString(StringConstant.TOKEN,"000");
        try {
            RetrofitUtils.getInstance().getPersonNews(id, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取好友信息返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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

    /**
     * 获取好友订阅的专辑
     * @param listener 监听
     */
    public void loadSubNews(String id,final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getPersonSub(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取好友订阅专辑返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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

    /**
     * 删除好友
     * @param listener 监听
     */
    public void loadNewsDel(String id,final OnLoadInterface listener) {
        try {
            String pid= BSApplication.SharedPreferences.getString(StringConstant.USER_ID,"");
            RetrofitUtils.getInstance().delPerson(pid,id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("删除好友返回数据",new GsonBuilder().serializeNulls().create().toJson(o));
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
