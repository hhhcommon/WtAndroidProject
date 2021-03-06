package com.wotingfm.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wotingfm.common.config.GlobalStateConfig;

/**
 * 创建数据库表
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context paramContext) {
        super(paramContext, GlobalStateConfig.dbVersionName, null, GlobalStateConfig.dbVersionCode);
    }

    // 该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabase对象的时候，才会调用这个方法
    public void onCreate(SQLiteDatabase db) {
        //bjUserIid用户id    type对讲类型group，person   id对讲id  addTime对讲开始时间 callType 是否接听：ok/no accId
        db.execSQL("CREATE TABLE IF NOT EXISTS talkHistory(_id Integer primary key autoincrement, "
                + "bjUserId varchar(50),type varchar(50),id varchar(50),addTime varchar(50),callType varchar(50),callTypeM varchar(50),accId varchar(50))");

        // 本机userId,消息id,消息类型,群消息更改类型,跳转路径,头像,申请人id,申请人昵称,申请信息,组头像,组id,组昵称,展示时间,添加时间
        db.execSQL("CREATE TABLE IF NOT EXISTS Notify(_id Integer primary key autoincrement, "
                + "bjUserId varchar(50),msgId varchar(50),msgType varchar(50),dealType varchar(50),"
                + "linkUrl varchar(100),applyAvatar varchar(100),applyId varchar(50),applyName varchar(50),"
                + "applyMessage varchar(100),groupAvatar varchar(100),groupId varchar(50),groupName varchar(50),showTime varchar(50),addTime varchar(50))");

        // 线程表
        db.execSQL("create table IF NOT EXISTS thread_info(_id integer primary key autoincrement,"
                + "thread_id varchar(100), url varchar(100), start integer, end integer, finished integer)");

        // 文件数据
        db.execSQL("create table IF NOT EXISTS file_info(_id integer primary key autoincrement," +
                "start integer," +
                "end integer," +
                "user_id varchar(50)," +
                "download_type varchar(10)," +
                "finished varchar(10)," +
                "id varchar(50)," +
                "single_title varchar(200)," +
                "single_logo_url varchar(200)," +
                "single_file_url varchar(200)," +
                "album_title varchar(200)," +
                "file_name varchar(200)," +
                "album_logo_url varchar(200)," +
                "had_liked varchar(50)," +
                "album_id varchar(50)," +
                "creator_id varchar(10)," +
                "single_seconds varchar(50)," +
                "albumSize varchar(20))");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS talkHistory");
        db.execSQL("DROP TABLE IF EXISTS Notify");
        db.execSQL("DROP TABLE IF EXISTS thread_info");
        db.execSQL("DROP TABLE IF EXISTS file_info");
        onCreate(db);
    }
}