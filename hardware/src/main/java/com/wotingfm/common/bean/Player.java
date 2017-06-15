package com.wotingfm.common.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.wotingfm.common.database.DBUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/7.
 */

public class Player implements Serializable {

    public int ret;
    public String msg;
    public DataBean data;


    public static class DataBean implements Serializable {
        public List<SinglesBean> singles;

        @DatabaseTable(tableName = DBUtils.TB_HISTORICAL_PLAY)
        public static class SinglesBean extends SinglesBase implements Serializable {
        }

    }
}
