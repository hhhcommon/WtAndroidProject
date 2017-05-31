package com.wotingfm.common.database;

import android.content.Context;

import com.woting.commonplat.database.SQLiteHelper;
import com.wotingfm.common.config.GlobalStateConfig;

/**
 * 数据库
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class SQLite {

    public static SQLiteHelper helper;
    /**
     * 单例模式
     *
     * @return
     */
    public static SQLiteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new SQLiteHelper(context, GlobalStateConfig.dbVersionName, GlobalStateConfig.dbVersionCode);
        }
        return helper;
    }

}