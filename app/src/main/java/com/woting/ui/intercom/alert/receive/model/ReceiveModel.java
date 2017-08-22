package com.woting.ui.intercom.alert.receive.model;

import com.woting.common.config.GlobalStateConfig;
import com.woting.common.utils.CommonUtils;
import com.woting.ui.intercom.alert.receive.view.ReceiveAlertActivity;
import com.woting.ui.intercom.main.chat.dao.SearchTalkHistoryDao;
import com.woting.ui.intercom.main.chat.model.DBTalkHistory;
import com.woting.ui.intercom.main.contacts.model.Contact;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ReceiveModel {

    private  ReceiveAlertActivity activity;
    private SearchTalkHistoryDao dbDao;

    public ReceiveModel(ReceiveAlertActivity activity) {
        this.activity = activity;
        initDao();      // 初始化数据库
    }

    // 初始化数据库
    private void initDao() {
        dbDao = new SearchTalkHistoryDao(activity);
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
}
