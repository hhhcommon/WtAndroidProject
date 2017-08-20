package com.wotingfm.common.bean;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by amine on 2017/6/14.
 */

public class SinglesBase implements Serializable {
    @DatabaseField(generatedId = true)
    public int _id;
    @DatabaseField
    public String id;
    @DatabaseField
    public long play_time;
    @DatabaseField
    public boolean isPlay;
    @DatabaseField
    public String single_title;
    @DatabaseField
    public String creator_id;
    @DatabaseField
    public String single_logo_url;
    @DatabaseField
    public String single_seconds;
    @DatabaseField
    public String single_file_url;
    @DatabaseField
    public String single_file_url_base;
    @DatabaseField
    public String album_title;
    @DatabaseField
    public boolean is_radio;
    @DatabaseField
    public String album_lastest_news;
    @DatabaseField
    public String album_logo_url;
    @DatabaseField
    public int album_play_count;
    @DatabaseField
    public String album_id;
    @DatabaseField
    public int count;
    @DatabaseField
    public int postionPlayer;
    @DatabaseField
    public double albumSize;
    @DatabaseField
    public boolean had_liked;
    //是否下载完成
    @DatabaseField
    public boolean isDownloadOver;
    @DatabaseField
    public boolean isSelect;
}
