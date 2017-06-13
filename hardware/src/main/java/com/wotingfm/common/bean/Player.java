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
        public static class SinglesBean implements Serializable {
            /**
             * id : 15
             * single_title : Dr.
             * single_logo_url : http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg
             * single_seconds : 100
             * single_file_url : http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null
             * album_title : Ms.
             */
            @DatabaseField(generatedId = true)
            public int _id;
            @DatabaseField
            public String creator_id;
            @DatabaseField
            public String id;
            @DatabaseField
            public boolean isPlay;
            @DatabaseField
            public boolean had_liked;
            @DatabaseField
            public String single_title;
            @DatabaseField
            public long play_time;
            @DatabaseField
            public String single_logo_url;
            @DatabaseField
            public String single_file_url;
            @DatabaseField
            public String album_title;
            @DatabaseField
            public String album_logo_url;

        }
    }
}
