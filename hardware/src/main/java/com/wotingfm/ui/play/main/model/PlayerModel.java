package com.wotingfm.ui.play.main.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SerchList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 数据中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class PlayerModel {

    /**
     * 获取推荐数据
     */
    public void getRecommendedList(final OnLoadInterface listener) {
        try {
            Log.e("执行操作", "获取推荐数据");
            RetrofitUtils.getInstance().getPlayerList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Player.DataBean.SinglesBean>>() {
                        @Override
                        public void call(List<Player.DataBean.SinglesBean> o) {
                            try {
                                Log.e("获取推荐返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            listener.onFailure("");
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    public void getAlbumList(String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().getPlayerList(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Player.DataBean.SinglesBean>>() {
                        @Override
                        public void call(List<Player.DataBean.SinglesBean> o) {
                            try {
                                Log.e("获取专辑返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            listener.onFailure("");
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    /**
     * 获取续播返回数据
     * @param listener
     */
    public void getOnPlay( final OnLoadInterface listener) {
        try {
            Log.e("执行操作","获取续播返回数据");
            RetrofitUtils.getInstance().getOnPlay()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("获取续播返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                listener.onSuccess(o);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            listener.onFailure("");
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }

    /**
     * 获取语音搜索数据
     * @param s
     * @param listener
     */
    public void getVoiceSearchList(String s,final OnLoadInterface listener) {
        try {
            Log.e("查询数据", "" + s);
            RetrofitUtils.getInstance().serchList("singles", s, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SerchList>() {
                        @Override
                        public void call(SerchList o) {
                            if (o != null && o.ret == 0 && o.data != null && o.data.singles != null && !o.data.singles.isEmpty()) {
                                try {
                                    Log.e("获取语音搜索返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                    //填充UI
                                    listener.onSuccess(o.data.singles);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    listener.onFailure("");
                                }
                            } else {
                                listener.onFailure("");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            listener.onFailure("");
                            throwable.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure("");
        }
    }
    /**
     * start_time: 15:00:00
     * end_time: 16:30:00
     * <p/>
     * 1.获取时间差，根据时间差来设置max_pos
     * 2.获取当前时间 ing_time: 15:45:36
     * 3.当前时间减去开始时间为此时pos
     */
    public int getMax(String startTime, String endTime) {
        int start = getTime(startTime);
        int end = getTime(endTime);
        return end - start;
    }

    public int getIng(String startTime) {
        int start = getTime(startTime);
        int end = getIngTimes();
        Log.e("start", "" + start);
        Log.e("end", "" + end);
        return end - start;
    }

    public String getIngTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String s = format.format(new Date(System.currentTimeMillis()));
        return s;
    }

    private int getTime(String s) {
        Log.e("当前时间", "" + s);
        if (!TextUtils.isEmpty(s)) {
            if (s.contains(":")) {
                String[] strArray = s.split(":");
                String hh = strArray[0];
                String mm = strArray[1];
                String ss = strArray[2];
                int time = Integer.parseInt(hh) * 60 * 60 + Integer.parseInt(mm) * 60  +Integer.parseInt(ss);
                Log.e("当前时间结果", "" + time);
                return time;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取当前时间
     */
    private int getIngTimes() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String s = format.format(new Date(System.currentTimeMillis()));
        int ing = getTime(s);
        return ing;
    }



    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
