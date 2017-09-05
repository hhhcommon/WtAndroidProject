package com.wotingfm.ui.play.main.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SerchList;

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
    public void getRecommendedList(String s, final OnLoadInterface listener) {
        try {
            Log.e("查询数据",""+s);
            Log.e("实际查询数据","郭德纲");
            RetrofitUtils.getInstance().serchList("singles", "郭德纲", 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SerchList>() {
                        @Override
                        public void call(SerchList o) {
                            if (o != null && o.ret == 0 && o.data != null && o.data.singles != null && !o.data.singles.isEmpty()) {
                                try {
                                    Log.e("获取推荐返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
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


//            RetrofitUtils.getInstance().getPlayerList(id)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<List<Player.DataBean.SinglesBean>>() {
//                        @Override
//                        public void call(List<Player.DataBean.SinglesBean> o) {
//                            try {
//                                Log.e("获取推荐返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
//                                //填充UI
//                                listener.onSuccess(o);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                listener.onFailure("");
//                            }
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            listener.onFailure("");
//                            throwable.printStackTrace();
//                        }
//                    });
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

    public interface OnLoadInterface {
        void onSuccess(Object o);

        void onFailure(String msg);
    }

}
