package com.wotingfm.ui.intercom.group.standbychannel.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.FrequencyUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ChannelModel {

    public List<String> getData() {
        List<String> list = FrequencyUtil.getFrequencyList();
        return list;
    }

    /**
     * 判断备用频道是不是为空
     *
     * @param channel1
     * @param channel2
     * @return
     */
    public boolean checkData(String channel1, String channel2) {
        if ((channel1 != null && !channel1.trim().equals("")) || (channel2 != null && !channel2.trim().equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 组装提交数据
     * @param channel1
     * @param channel2
     * @return
     */
    public String assemblyData(String channel1, String channel2) {
        if (channel1 != null && !channel1.trim().equals("")&&!channel1.trim().equals("备用频道一")) {
            if (channel2 != null && !channel2.trim().equals("")&&!channel2.trim().equals("备用频道二")) {
                String s =channel1+ ","+channel2;
                return s;
            } else {
                String s = channel1;
                return s;
            }
        } else {
            if (channel2 != null && !channel2.trim().equals("")) {
                String s = channel2;
                return s;
            } else {
                String s = "";
                return s;
            }
        }
    }

    /**
     * 设置组备用频道
     *
     * @param channel
     * @param id       组id
     * @param listener 监听
     */
    public void loadNews(String channel, String id, final OnLoadInterface listener) {
        try {
            RetrofitUtils.getInstance().setChannel(id, channel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            Log.e("设置备用频道返回数据", new Gson().toJson(o));
                            //填充UI
                            listener.onSuccess(o);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            listener.onFailure("");
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
