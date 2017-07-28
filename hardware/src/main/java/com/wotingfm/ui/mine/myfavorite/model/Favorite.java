package com.wotingfm.ui.mine.myfavorite.model;

/**
 * 作者：xinLong on 2017/6/12 15:20
 * 邮箱：645700751@qq.com
 */
public class Favorite {
    private String id;//
    private String liked_type;//
    private String liked_id;// 1
    private String title;// 一口气读完美国史 第23集
    private String logo_url;//
    private String file_url;// http://aliod.qingting.fm/vod/00/00/0000000000000000000002808616_24.m4a
    private String album_id;// 一口气读完美国史
    private String play_count;//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiked_type() {
        return liked_type;
    }

    public void setLiked_type(String liked_type) {
        this.liked_type = liked_type;
    }

    public String getLiked_id() {
        return liked_id;
    }

    public void setLiked_id(String liked_id) {
        this.liked_id = liked_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }
}
