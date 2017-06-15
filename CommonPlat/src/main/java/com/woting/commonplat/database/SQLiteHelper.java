package com.woting.commonplat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库表
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context paramContext, String dbVersionName, int dbVersionCode) {
        super(paramContext, dbVersionName, null, dbVersionCode);
    }

    public void onCreate(SQLiteDatabase db) {
        //bjuserid用户id    type对讲类型group，person   id对讲id  addtime对讲开始时间
        db.execSQL("CREATE TABLE IF NOT EXISTS talkHistory(_id Integer primary key autoincrement, "
                + "bjUserId varchar(50),type varchar(50),id varchar(50),addTime varchar(50))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS talkHistory");
        onCreate(db);
    }
}