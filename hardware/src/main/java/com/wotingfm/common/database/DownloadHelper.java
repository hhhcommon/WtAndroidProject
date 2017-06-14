package com.wotingfm.common.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.stmt.QueryBuilder;
import com.wotingfm.common.bean.Player;

import java.util.List;

public class DownloadHelper extends DBHelper {
    private SQLiteDatabase sqLiteDatabase;

    public DownloadHelper(Context context) {
        super(context);
        sqLiteDatabase = getWritableDatabase();
    }

    /**
     * 取得一个列表
     *
     * @return
     */
    public List<Player.DataBean.SinglesBean> findPlayHistoryList() {
        List<Player.DataBean.SinglesBean> chooseCarBeanList = null;
        QueryBuilder<Player.DataBean.SinglesBean, ?> builder = getRuntimeExceptionDao(Player.DataBean.SinglesBean.class).queryBuilder();
        builder.orderBy("_id", false);
      //  builder.limit(10);
        try {
            chooseCarBeanList = builder.query();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return chooseCarBeanList;
    }

    /**
     * 插入数据
     *
     * @param initialValues
     * @return
     */
    public long insertTotable(String uid, ContentValues initialValues) {
        deleteTable(uid);
        return sqLiteDatabase.insert(DBUtils.TB_DOWNLOAD, null, initialValues);
    }

    public void deleteTable(String uid) {
        sqLiteDatabase.delete(DBUtils.TB_DOWNLOAD, "id=" + uid, null);
    }

    /**
     * 清空表数据
     */
    public void clearTable() {
        sqLiteDatabase.delete(DBUtils.TB_DOWNLOAD, null, null);
    }

    /**
     * 清空表数据
     */
    public void clearTableIds(List<String> ids) {
        sqLiteDatabase.delete(DBUtils.TB_DOWNLOAD, null, null);
    }

}
