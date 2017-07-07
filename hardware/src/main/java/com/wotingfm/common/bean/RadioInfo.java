package com.wotingfm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amine on 2017/7/7.
 */

public class RadioInfo implements Serializable {

    /**
     * data : {"today":[{"channel_id":386,"duration":"7140","end_time":"23:59:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_220000_235900_24_0.m4a","id":563703,"order":19,"program_id":1913994,"start_time":"22:00:00","title":"央广夜新闻","week":4},{"channel_id":386,"duration":"3600","end_time":"22:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_210000_220000_24_0.m4a","id":734443,"order":18,"program_id":4302448,"start_time":"21:00:00","title":"政务直通","week":4},{"channel_id":386,"duration":"1800","end_time":"21:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_203000_210000_24_0.m4a","id":734434,"order":17,"program_id":4302408,"start_time":"20:30:00","title":"直播中国","week":4},{"channel_id":386,"duration":"1800","end_time":"20:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_200000_203000_24_0.m4a","id":563702,"order":16,"program_id":1913992,"start_time":"20:00:00","title":"小喇叭","week":4},{"channel_id":386,"duration":"3600","end_time":"20:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_190000_200000_24_0.m4a","id":563701,"order":15,"program_id":1913990,"start_time":"19:00:00","title":"央广新闻晚高峰","week":4},{"channel_id":386,"duration":"1800","end_time":"19:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_183000_190000_24_0.m4a","id":563700,"order":14,"program_id":1913991,"start_time":"18:30:00","title":"全国新闻联播","week":4},{"channel_id":386,"duration":"7200","end_time":"18:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_163000_183000_24_0.m4a","id":563699,"order":13,"program_id":1913990,"start_time":"16:30:00","title":"央广新闻晚高峰","week":4},{"channel_id":386,"duration":"12600","end_time":"16:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_130000_163000_24_0.m4a","id":563698,"order":12,"program_id":1913988,"start_time":"13:00:00","title":"央广新闻","week":4},{"channel_id":386,"duration":"3600","end_time":"13:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_120000_130000_24_0.m4a","id":563697,"order":11,"program_id":1913989,"start_time":"12:00:00","title":"全球华语广播网","week":4},{"channel_id":386,"duration":"10800","end_time":"12:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_090000_120000_24_0.m4a","id":563696,"order":10,"program_id":1913988,"start_time":"09:00:00","title":"央广新闻","week":4},{"channel_id":386,"duration":"7200","end_time":"09:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_070000_090000_24_0.m4a","id":563695,"order":9,"program_id":1913987,"start_time":"07:00:00","title":"新闻纵横","week":4},{"channel_id":386,"duration":"1800","end_time":"07:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_063000_070000_24_0.m4a","id":563694,"order":8,"program_id":1913986,"start_time":"06:30:00","title":"新闻和报纸摘要","week":4},{"channel_id":386,"duration":"1800","end_time":"06:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_060000_063000_24_0.m4a","id":563693,"order":7,"program_id":1913985,"start_time":"06:00:00","title":"国防时空","week":4},{"channel_id":386,"duration":"1800","end_time":"06:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_053000_060000_24_0.m4a","id":563692,"order":6,"program_id":3729931,"start_time":"05:30:00","title":"专题广告","week":4},{"channel_id":386,"duration":"1800","end_time":"05:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_050000_053000_24_0.m4a","id":563691,"order":5,"program_id":3729929,"start_time":"05:00:00","title":"祥康健康早班车","week":4},{"channel_id":386,"duration":"1800","end_time":"05:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_043000_050000_24_0.m4a","id":563690,"order":4,"program_id":1913997,"start_time":"04:30:00","title":"中央农业广播学校","week":4},{"channel_id":386,"duration":"1800","end_time":"04:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_040000_043000_24_0.m4a","id":563689,"order":3,"program_id":1913982,"start_time":"04:00:00","title":"养生大讲堂","week":4},{"channel_id":386,"duration":"5400","end_time":"04:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_023000_040000_24_0.m4a","id":563688,"order":2,"program_id":1913981,"start_time":"02:30:00","title":"昨日新闻重现","week":4},{"channel_id":386,"duration":"1800","end_time":"02:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_020000_023000_24_0.m4a","id":734440,"order":1,"program_id":4302406,"start_time":"02:00:00","title":"纪录中国","week":4},{"channel_id":386,"duration":"7200","end_time":"02:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170706/386/386_20170706_000000_020000_24_0.m4a","id":563687,"order":0,"program_id":1913980,"start_time":"00:00:00","title":"千里共良宵","week":4}],"tomorrow":[{"channel_id":386,"duration":"10740","end_time":"23:59:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_210000_235900_24_0.m4a","id":563720,"order":17,"program_id":1913994,"start_time":"21:00:00","title":"央广夜新闻","week":5},{"channel_id":386,"duration":"1800","end_time":"21:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_203000_210000_24_0.m4a","id":734435,"order":16,"program_id":4302408,"start_time":"20:30:00","title":"直播中国","week":5},{"channel_id":386,"duration":"1800","end_time":"20:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_200000_203000_24_0.m4a","id":563719,"order":15,"program_id":1913992,"start_time":"20:00:00","title":"小喇叭","week":5},{"channel_id":386,"duration":"3600","end_time":"20:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_190000_200000_24_0.m4a","id":563718,"order":14,"program_id":1913990,"start_time":"19:00:00","title":"央广新闻晚高峰","week":5},{"channel_id":386,"duration":"1800","end_time":"19:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_183000_190000_24_0.m4a","id":563717,"order":13,"program_id":1913991,"start_time":"18:30:00","title":"全国新闻联播","week":5},{"channel_id":386,"duration":"7200","end_time":"18:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_163000_183000_24_0.m4a","id":563716,"order":12,"program_id":1913990,"start_time":"16:30:00","title":"央广新闻晚高峰","week":5},{"channel_id":386,"duration":"12600","end_time":"16:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_130000_163000_24_0.m4a","id":563715,"order":11,"program_id":1913988,"start_time":"13:00:00","title":"央广新闻","week":5},{"channel_id":386,"duration":"3600","end_time":"13:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_120000_130000_24_0.m4a","id":563714,"order":10,"program_id":1913989,"start_time":"12:00:00","title":"全球华语广播网","week":5},{"channel_id":386,"duration":"10800","end_time":"12:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_090000_120000_24_0.m4a","id":563713,"order":9,"program_id":1913988,"start_time":"09:00:00","title":"央广新闻","week":5},{"channel_id":386,"duration":"7200","end_time":"09:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_070000_090000_24_0.m4a","id":563712,"order":8,"program_id":1913987,"start_time":"07:00:00","title":"新闻纵横","week":5},{"channel_id":386,"duration":"1800","end_time":"07:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_063000_070000_24_0.m4a","id":563711,"order":7,"program_id":1913986,"start_time":"06:30:00","title":"新闻和报纸摘要","week":5},{"channel_id":386,"duration":"1800","end_time":"06:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_060000_063000_24_0.m4a","id":563710,"order":6,"program_id":1913985,"start_time":"06:00:00","title":"国防时空","week":5},{"channel_id":386,"duration":"3600","end_time":"06:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_050000_060000_24_0.m4a","id":734422,"order":5,"program_id":4302407,"start_time":"05:00:00","title":"悦动清晨","week":5},{"channel_id":386,"duration":"1800","end_time":"05:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_043000_050000_24_0.m4a","id":563707,"order":4,"program_id":1913997,"start_time":"04:30:00","title":"中央农业广播学校","week":5},{"channel_id":386,"duration":"1800","end_time":"04:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_040000_043000_24_0.m4a","id":563706,"order":3,"program_id":1913982,"start_time":"04:00:00","title":"养生大讲堂","week":5},{"channel_id":386,"duration":"5400","end_time":"04:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_023000_040000_24_0.m4a","id":734419,"order":2,"program_id":1913981,"start_time":"02:30:00","title":"昨日新闻重现","week":5},{"channel_id":386,"duration":"1800","end_time":"02:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_020000_023000_24_0.m4a","id":734438,"order":1,"program_id":4302406,"start_time":"02:00:00","title":"纪录中国","week":5},{"channel_id":386,"duration":"7200","end_time":"02:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170707/386/386_20170707_000000_020000_24_0.m4a","id":563704,"order":0,"program_id":1913980,"start_time":"00:00:00","title":"千里共良宵","week":5}],"yesterday":[{"channel_id":386,"duration":"10740","end_time":"23:59:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_210000_235900_24_0.m4a","id":563686,"order":16,"program_id":1913994,"start_time":"21:00:00","title":"央广夜新闻","week":3},{"channel_id":386,"duration":"1800","end_time":"21:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_203000_210000_24_0.m4a","id":734431,"order":15,"program_id":4302408,"start_time":"20:30:00","title":"直播中国","week":3},{"channel_id":386,"duration":"1800","end_time":"20:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_200000_203000_24_0.m4a","id":563685,"order":14,"program_id":1913992,"start_time":"20:00:00","title":"小喇叭","week":3},{"channel_id":386,"duration":"3600","end_time":"20:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_190000_200000_24_0.m4a","id":563684,"order":13,"program_id":1913990,"start_time":"19:00:00","title":"央广新闻晚高峰","week":3},{"channel_id":386,"duration":"7200","end_time":"18:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_163000_183000_24_0.m4a","id":563682,"order":12,"program_id":1913990,"start_time":"16:30:00","title":"央广新闻晚高峰","week":3},{"channel_id":386,"duration":"12600","end_time":"16:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_130000_163000_24_0.m4a","id":563681,"order":11,"program_id":1913988,"start_time":"13:00:00","title":"央广新闻","week":3},{"channel_id":386,"duration":"3600","end_time":"13:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_120000_130000_24_0.m4a","id":563680,"order":10,"program_id":1913989,"start_time":"12:00:00","title":"全球华语广播网","week":3},{"channel_id":386,"duration":"10800","end_time":"12:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_090000_120000_24_0.m4a","id":563679,"order":9,"program_id":1913988,"start_time":"09:00:00","title":"央广新闻","week":3},{"channel_id":386,"duration":"7200","end_time":"09:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_070000_090000_24_0.m4a","id":563678,"order":8,"program_id":1913987,"start_time":"07:00:00","title":"新闻纵横","week":3},{"channel_id":386,"duration":"1800","end_time":"07:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_063000_070000_24_0.m4a","id":563677,"order":7,"program_id":1913986,"start_time":"06:30:00","title":"新闻和报纸摘要","week":3},{"channel_id":386,"duration":"1800","end_time":"06:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_060000_063000_24_0.m4a","id":563676,"order":6,"program_id":1913985,"start_time":"06:00:00","title":"国防时空","week":3},{"channel_id":386,"duration":"3600","end_time":"06:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_050000_060000_24_0.m4a","id":734430,"order":5,"program_id":4302407,"start_time":"05:00:00","title":"悦动清晨","week":3},{"channel_id":386,"duration":"1800","end_time":"05:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_043000_050000_24_0.m4a","id":563673,"order":4,"program_id":1913997,"start_time":"04:30:00","title":"中央农业广播学校","week":3},{"channel_id":386,"duration":"300","end_time":"04:30:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_042500_043000_24_0.m4a","id":734429,"order":3,"program_id":4302447,"start_time":"04:25:00","title":"开始曲","week":3},{"channel_id":386,"duration":"8400","end_time":"04:25:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_020500_042500_24_0.m4a","id":734428,"order":2,"program_id":4302445,"start_time":"02:05:00","title":"停机检修","week":3},{"channel_id":386,"duration":"300","end_time":"02:05:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_020000_020500_24_0.m4a","id":734427,"order":1,"program_id":4302444,"start_time":"02:00:00","title":"结束曲","week":3},{"channel_id":386,"duration":"7200","end_time":"02:00:00","fileUrl":"http://lcache.qingting.fm/cache/20170705/386/386_20170705_000000_020000_24_0.m4a","id":563670,"order":0,"program_id":1913980,"start_time":"00:00:00","title":"千里共良宵","week":3}]}
     * msg : success
     * ret : 0
     */

    public DataBean data;
    public String msg;
    public int ret;


    public static class DataBean implements Serializable {

        public List<TodayBean> today;
        public List<TomorrowBean> tomorrow;
        public List<YesterdayBean> yesterday;
        public ChannelBean channel;

        public static class ChannelBean implements Serializable {
            /**
             * desc :       交通台全天节目由以下内容构成：新闻、服务信息、知识性、服务性、娱乐性专题。新闻节目立足北京，通过新闻网络，全方位关注世界范围内的与交通相关的动态。整点、半点、播报路况信息，重要信息随时插播，整点报告天气情况。专题节目为听众介绍衣食住行方面的知识，提供娱乐信息。
             * had_subscribed : false
             * id : 336
             * image_url : http://pic.qingting.fm/2015/0514/20150514183903578.jpg
             * listen_count : 0
             * radio_url : http://ls.qingting.fm/live/336/24k.m3u8
             * title : 北京交通广播
             */

            public String desc;
            public boolean had_subscribed;
            public int id;
            public String image_url;
            public int subscribed_count;
            public int listen_count;
            public String radio_url;
            public String title;


        }

        public static class TodayBean implements Serializable {
            /**
             * channel_id : 386
             * duration : 7140
             * end_time : 23:59:00
             * fileUrl : http://lcache.qingting.fm/cache/20170706/386/386_20170706_220000_235900_24_0.m4a
             * id : 563703
             * order : 19
             * program_id : 1913994
             * start_time : 22:00:00
             * title : 央广夜新闻
             * week : 4
             */

            public int channel_id;
            public String duration;
            public String end_time;
            public String fileUrl;
            public int id;
            public int order;
            public int program_id;
            public String start_time;
            public String title;
            public int week;


        }

        public static class TomorrowBean implements Serializable {
            /**
             * channel_id : 386
             * duration : 10740
             * end_time : 23:59:00
             * fileUrl : http://lcache.qingting.fm/cache/20170707/386/386_20170707_210000_235900_24_0.m4a
             * id : 563720
             * order : 17
             * program_id : 1913994
             * start_time : 21:00:00
             * title : 央广夜新闻
             * week : 5
             */

            public int channel_id;
            public String duration;
            public String end_time;
            public String fileUrl;
            public int id;
            public int order;
            public int program_id;
            public String start_time;
            public String title;
            public int week;


        }

        public static class YesterdayBean implements Serializable {

            public int channel_id;
            public String duration;
            public String end_time;
            public String fileUrl;
            public int id;
            public int order;
            public int program_id;
            public String start_time;
            public String title;
            public int week;


        }
    }
}
