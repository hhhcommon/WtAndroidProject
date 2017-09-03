package com.wotingfm.ui.play.localaudio.model;

import java.io.Serializable;

public class FileInfo implements Serializable {
    public String id;
    public String play_time;
    public String single_title;
    public String creator_id;
    public String single_logo_url;
    public String single_seconds;
    public String single_file_url;
    public String single_file_url_base;
    public String album_title;
    public String album_lastest_news;
    public String album_logo_url;
    public String album_id;
    public String albumSize;

    public String user_id;
    public String download_type;// 0为未下载 1为下载中,2暂停状态
    public int start=-1;
    public int end=-1;
    public int length;
    public String finished;
    public String fileName;

    //集数和文件大小
    public int count = -1;//计数项
    public int sum = -1;//总和

}
