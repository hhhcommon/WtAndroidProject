package com.wotingfm.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wotingfm.ui.bean.Player;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public DBHelper(Context context) {
        super(context.getApplicationContext(), DBUtils.DB_NAME, null, DBUtils.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {

            TableUtils.createTable(connectionSource, Player.DataBean.SinglesBean.class);

//            TableUtils.createTable(connectionSource, SinglesDownload.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            if (oldVersion == 1) {
                try {
                    TableUtils.createTable(connectionSource, Player.DataBean.SinglesBean.class);
//                    TableUtils.createTable(connectionSource, SinglesDownload.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
