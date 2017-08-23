package com.wotingfm.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/12.
 */

public class UserBean implements Serializable {
    /**
     * albums : [{"id":1,"logo_url":"http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg","play_count":0,"status":0,"title":"第一个专辑"}]
     * albums_total_count : 1
     * avatar :
     * fans_count : 0
     * had_followd : false
     * id : 00163e00693b
     * idols_count : 0
     * name : 1
     * signature :
     * singles_total_count : 50
     * singles : [{"album_title":"Ms.","id":1,"published_at":"2017-06-07","single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_title":"Ms."},{"album_title":"Ms.","id":2,"published_at":"2017-06-07","single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_title":"Ms."},{"album_title":"Ms.","id":3,"published_at":"2017-06-07","single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_title":"Dr."},{"album_title":"Ms.","id":4,"published_at":"2017-06-07","single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_title":"Prof."},{"album_title":"Ms.","id":5,"published_at":"2017-06-07","single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_title":"Miss"}]
     */

    public int albums_total_count;
    public String avatar;
    public int fans_count;
    public boolean had_followd;
    public String id;
    public int idols_count;
    public String name;
    public String nick_name;
    public String signature;
    public int singles_total_count;
    public List<AlbumsBean> albums;
    public List<SinglesBean> singles;



    public static class SinglesBean implements Serializable {
        /**
         * album_title : Ms.
         * id : 1
         * published_at : 2017-06-07
         * single_file_url : http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null
         * single_logo_url : http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg
         * single_seconds : 100
         * single_title : Ms.
         */

        public String album_title;
        public int id;
        public String published_at;
        public String single_file_url;
        public String single_logo_url;
        public int single_seconds;
        public String single_title;

    }
}

