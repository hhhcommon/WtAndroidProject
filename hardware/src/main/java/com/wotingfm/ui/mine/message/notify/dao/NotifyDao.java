package com.wotingfm.ui.mine.message.notify.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wotingfm.common.database.SQLiteHelper;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.mine.message.notify.model.DBNotifyMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * 对消息中心列表的操作
 *
 * @author 辛龙
 *         2016年1月15日
 */
public class NotifyDao {
    private SQLiteHelper helper;
    private Context context;

    //构造方法
    public NotifyDao(Context context) {
        helper = new SQLiteHelper(context);
        this.context = context;
    }

    /**
     * 插入消息中心表一条数据
     *
     * @param msg
     */
    public void addNotifyMsg(DBNotifyMsg msg) {
        //通过helper的实现对象获取可操作的数据库db
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into Notify(bjUserId,msgId,msgType,dealType,linkUrl,applyAvatar,applyId,applyName,applyMessage,groupAvatar,groupId,groupName,showTime,addTime)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{msg.getBJUserId(), msg.getMsg_id(), msg.getMsg_type(), msg.getDeal_type(),
                        msg.getHtml(), msg.getApply_avatar(), msg.getApply_id(), msg.getApply_name(),
                        msg.getApply_message(), msg.getGroup_avatar(), msg.getGroup_id(), msg.getGroup_name(), msg.getShow_time(), msg.getAdd_time()});//sql语句
        db.close();//关闭数据库对象
    }


    /**
     * 查询数据库里的数据，无参查询语句 供特定使用
     *
     * @return
     */
    public List<DBNotifyMsg> queryHistory() {
        List<DBNotifyMsg> myList = new ArrayList<DBNotifyMsg>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String userId = CommonUtils.getUserId();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("Select * from Notify  where bjUserId=? order by addTime desc", new String[]{userId});
            while (cursor.moveToNext()) {
                String _BJUserId = cursor.getString(1);
                String _msg_id = cursor.getString(2);
                String _msg_type = cursor.getString(3);
                String _deal_type = cursor.getString(4);
                String _html = cursor.getString(5);
                String _apply_avatar = cursor.getString(6);
                String _apply_id = cursor.getString(7);
                String _apply_name = cursor.getString(8);
                String _apply_message = cursor.getString(9);
                String _group_avatar = cursor.getString(10);
                String _group_id = cursor.getString(11);
                String _group_name = cursor.getString(12);
                String _show_time = cursor.getString(13);
                String _add_time = cursor.getString(14);

                //把每个对象都放到DBNotifyMsg对象里
                DBNotifyMsg h = new DBNotifyMsg(_BJUserId, _msg_id, _msg_type, _deal_type, _html, _apply_avatar,
                        _apply_id, _apply_name, _apply_message, _group_avatar, _group_id, _group_name, _show_time, _add_time);
                myList.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return myList;
    }

    /**
     * 删除数据库表中的数据
     *
     * @param id
     * @return
     */
    public void deleteHistory(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String userId = CommonUtils.getUserId();
        String uid = id;
        db.execSQL("Delete from Notify where msgId=? and bjUserId=?",
                new String[]{uid, userId});
        db.close();
    }
}
