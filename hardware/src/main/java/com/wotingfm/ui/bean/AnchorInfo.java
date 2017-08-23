package com.wotingfm.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/6/12.
 */

public class AnchorInfo implements Serializable {

    /**
     * ret : 0
     * msg : success
     * data : {"user":{"id":"00163e00693b","avatar":"","name":"暂未填写昵称","signature":"","fans_count":0,"idols_count":0,"had_followd":false,"data":[{"type":"lives","total_count":1,"data":[{"id":127,"title":"风风光光","cover":"","audience_count":1,"live_number":"10000127","begin_at":"2017-05-12 14:16:20","begin_at_timestamp":1494569780,"owner":{"id":"00163e00693b","avatar":"","name":"暂未填写昵称","fans_count":0,"idols_count":0,"had_real_name_cert":false},"channel":{}}]},{"type":"albums","total_count":1,"data":[{"id":1,"title":"第一个专辑","logo_url":"http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg","play_count":0,"lastest_news":"更新至: Dr."}]},{"type":"singles","total_count":50,"data":[{"id":1,"single_title":"Ms.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Alan%20Walker%20-%20Fade.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":true},{"id":2,"single_title":"Ms.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":true},{"id":3,"single_title":"Dr.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/BAAD%20-%20%E5%90%9B%E3%81%8B%E3%82%99%E5%A5%BD%E3%81%8D%E3%81%9F%E3%82%99%E3%81%A8%E5%8F%AB%E3%81%B2%E3%82%99%E3%81%9F%E3%81%84.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false},{"id":4,"single_title":"Prof.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Avril%20Lavigne%20-%20Innocence.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false},{"id":5,"single_title":"Miss","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Carly%20Rae%20Jepsen%20-%20I%20Really%20Like%20You.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false}]}]}}
     */

    public int ret;
    public String msg;
    public DataBeanXX data;


    public static class DataBeanXX implements Serializable {
        /**
         * user : {"id":"00163e00693b","avatar":"","name":"暂未填写昵称","signature":"","fans_count":0,"idols_count":0,"had_followd":false,"data":[{"type":"lives","total_count":1,"data":[{"id":127,"title":"风风光光","cover":"","audience_count":1,"live_number":"10000127","begin_at":"2017-05-12 14:16:20","begin_at_timestamp":1494569780,"owner":{"id":"00163e00693b","avatar":"","name":"暂未填写昵称","fans_count":0,"idols_count":0,"had_real_name_cert":false},"channel":{}}]},{"type":"albums","total_count":1,"data":[{"id":1,"title":"第一个专辑","logo_url":"http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg","play_count":0,"lastest_news":"更新至: Dr."}]},{"type":"singles","total_count":50,"data":[{"id":1,"single_title":"Ms.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Alan%20Walker%20-%20Fade.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":true},{"id":2,"single_title":"Ms.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":true},{"id":3,"single_title":"Dr.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/BAAD%20-%20%E5%90%9B%E3%81%8B%E3%82%99%E5%A5%BD%E3%81%8D%E3%81%9F%E3%82%99%E3%81%A8%E5%8F%AB%E3%81%B2%E3%82%99%E3%81%9F%E3%81%84.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false},{"id":4,"single_title":"Prof.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Avril%20Lavigne%20-%20Innocence.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false},{"id":5,"single_title":"Miss","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Carly%20Rae%20Jepsen%20-%20I%20Really%20Like%20You.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false}]}]}
         */

        public UserBean user;

        public static class UserBean implements Serializable {
            /**
             * id : 00163e00693b
             * avatar :
             * name : 暂未填写昵称
             * signature :
             * fans_count : 0
             * idols_count : 0
             * had_followd : false
             * data : [{"type":"lives","total_count":1,"data":[{"id":127,"title":"风风光光","cover":"","audience_count":1,"live_number":"10000127","begin_at":"2017-05-12 14:16:20","begin_at_timestamp":1494569780,"owner":{"id":"00163e00693b","avatar":"","name":"暂未填写昵称","fans_count":0,"idols_count":0,"had_real_name_cert":false},"channel":{}}]},{"type":"albums","total_count":1,"data":[{"id":1,"title":"第一个专辑","logo_url":"http://i.ebayimg.com/images/g/UQsAAOxy4t1SkeRh/s-l300.jpg","play_count":0,"lastest_news":"更新至: Dr."}]},{"type":"singles","total_count":50,"data":[{"id":1,"single_title":"Ms.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Alan%20Walker%20-%20Fade.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":true},{"id":2,"single_title":"Ms.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://om5.alicdn.com/46/15046/130555/1326590_2360054_l.mp3?auth_key=406b8dcbb4fa2b46fd6d3e316b630943-1497409200-0-null","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":true},{"id":3,"single_title":"Dr.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/BAAD%20-%20%E5%90%9B%E3%81%8B%E3%82%99%E5%A5%BD%E3%81%8D%E3%81%9F%E3%82%99%E3%81%A8%E5%8F%AB%E3%81%B2%E3%82%99%E3%81%9F%E3%81%84.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false},{"id":4,"single_title":"Prof.","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Avril%20Lavigne%20-%20Innocence.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false},{"id":5,"single_title":"Miss","single_logo_url":"http://img0.imgtn.bdimg.com/it/u=2966114667,2205691231&fm=23&gp=0.jpg","single_seconds":100,"single_file_url":"http://nanzhu.oss-cn-shanghai.aliyuncs.com/Carly%20Rae%20Jepsen%20-%20I%20Really%20Like%20You.mp3","album_title":"Ms.","album_id":1,"published_at":"2017-06-07","had_liked":false}]}]
             */

            public String id;
            public String avatar;
            public String name;
            public String signature;
            public int fans_count;
            public int idols_count;
            public boolean had_followd;
            public List<DataBeanX> data;


            public static class DataBeanX implements Serializable{
                /**
                 * type : lives
                 * total_count : 1
                 * data : [{"id":127,"title":"风风光光","cover":"","audience_count":1,"live_number":"10000127","begin_at":"2017-05-12 14:16:20","begin_at_timestamp":1494569780,"owner":{"id":"00163e00693b","avatar":"","name":"暂未填写昵称","fans_count":0,"idols_count":0,"had_real_name_cert":false},"channel":{}}]
                 */

                public String type;
                public int total_count;
                public List<DataBean> data;


                public static class DataBean implements Serializable{
                    /**
                     * id : 127
                     * title : 风风光光
                     * cover :
                     * audience_count : 1
                     * live_number : 10000127
                     * begin_at : 2017-05-12 14:16:20
                     * begin_at_timestamp : 1494569780
                     * owner : {"id":"00163e00693b","avatar":"","name":"暂未填写昵称","fans_count":0,"idols_count":0,"had_real_name_cert":false}
                     * channel : {}
                     */

                    public String id;
                    public String title;
                    public String cover;
                    public String logo_url;
                    public String play_count;
                    public String lastest_news;
                    public String album_id;
                    public String single_logo_url;
                    public String single_title;
                    public String album_title;
                    public String published_at;
                    public String single_file_url;
                    public int audience_count;
                    public String live_number;
                    public String begin_at;
                    public int begin_at_timestamp;
                }
            }
        }
    }
}
