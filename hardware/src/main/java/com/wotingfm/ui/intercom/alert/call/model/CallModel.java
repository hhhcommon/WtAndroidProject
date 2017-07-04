package com.wotingfm.ui.intercom.alert.call.model;

import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.intercom.alert.call.view.CallAlertActivity;
import com.wotingfm.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.wotingfm.ui.intercom.main.chat.model.DBTalkHistory;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class CallModel {
    private final CallAlertActivity activity;
    private SearchTalkHistoryDao dao;

    public CallModel(CallAlertActivity activity) {
        this.activity = activity;
        initDao();
    }

    // 初始化数据库
    private void initDao() {
        dao = new SearchTalkHistoryDao(activity);
    }

    /**
     * 获取好友数据
     *
     * @param id
     */
    public Contact.user getUser(String id) {
        Contact.user user=null;
        if (id != null && !id.equals("")) {
           List<Contact.user> list= GlobalStateConfig.list_person;
            if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    String _id=list.get(i).getId();
                    if (_id != null && !_id.equals("")) {
                        if(id.equals(_id)){
                            user=list.get(i);
                        }
                    }
                }
            }

        }
        return user;
    }

    /**
     * 在数据库中插入一条数据
     * @param h
     */
    public void add(DBTalkHistory h){
        if(dao==null){
            initDao();
            dao.addTalkHistory(h);
        }else{
            dao.addTalkHistory(h);
        }
    }

    /**
     * 组装数据库数据
     * @param s
     * @return
     */
    public DBTalkHistory assemblyData(Contact.user s) {
        String id = s.getId();
        String type = "person";
        String addTime = Long.toString(System.currentTimeMillis());
        String bjUserId = CommonUtils.getUserId();
        DBTalkHistory h = new DBTalkHistory(bjUserId, type, id, addTime);
        return h;
    }

    public void destroy(){
        if (dao != null) {
            dao = null;
        }
    }
}
