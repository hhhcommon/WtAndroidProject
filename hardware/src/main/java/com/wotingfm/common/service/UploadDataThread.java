package com.wotingfm.common.service;

import android.text.TextUtils;
import android.util.Log;

import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 定时、定量上传数据线程
 */
public class UploadDataThread extends Thread {
    private volatile Object Lock = new Object(); // 锁

    @Override
    public void run() {
        while (true) {
            try {
                    synchronized (Lock) {
                        if(CommonUtils.isLogin())send();
                    }
                Thread.sleep(5 * 1000 );// 一定时间检查一次  如果有数据则上传
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void send(){
        try {
          if(!TextUtils.isEmpty(GlobalStateConfig.playingId)) {
              Log.e("执行数据上传",GlobalStateConfig.playingId+"   "+GlobalStateConfig.playingType+"   "+ GlobalStateConfig.listType+"   "+ GlobalStateConfig.currentTime);
              RetrofitUtils.getInstance().upDataPlayMsg(GlobalStateConfig.playingId, GlobalStateConfig.playingType, GlobalStateConfig.listType, GlobalStateConfig.currentTime)
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(new Action1<Object>() {
                          @Override
                          public void call(Object o) {

                          }
                      }, new Action1<Throwable>() {
                          @Override
                          public void call(Throwable throwable) {
                              throwable.printStackTrace();
                          }
                      });
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
