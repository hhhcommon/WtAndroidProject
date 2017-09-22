package com.wotingfm.common.live;
import com.woting.commonplat.nim.DemoCache;
import com.wotingfm.ui.bean.CLive;
import com.wotingfm.ui.bean.TrailerInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 直播管理器
 * 作者：xinlong on 2017/8/23 21:18
 * 邮箱：645700751@qq.com
 */

public class LiveManger {
    public static LiveManger INSTANCE;
    private boolean open_audio = true;
    private boolean open_video = false;
    private boolean useFilter = false; //默认开启滤镜
    private boolean faceBeauty = false; //默认关闭美颜
    private String quality = "SD";
    private String push_url;

    private LiveManger() {
        // 创建网络连接
    }

    public static LiveManger getInstance() {
        if (INSTANCE == null) {
            synchronized (LiveManger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveManger();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 开始直播
     * @param userId
     * @param callBack
     */
    public void startLive(String userId, final LiveCallBack callBack) {
        RetrofitUtils.getInstance().carteLive(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CLive.DataBean.VoiceLiveBean>() {
                    @Override
                    public void call(CLive.DataBean.VoiceLiveBean cLive) {
//                        if (cLive != null && cLive.rtmp_push_pull_url_json != null) {
//                            RoomInfoEntity roomInfoEntity = new RoomInfoEntity();
//                            roomInfoEntity.cid = cLive.rtmp_push_pull_url_json.cid;
//                            push_url = cLive.rtmp_push_pull_url_json.pushUrl;
//                            roomInfoEntity.hlsPullUrl = cLive.rtmp_push_pull_url_json.hlsPullUrl;
//                            roomInfoEntity.pushUrl = cLive.rtmp_push_pull_url_json.pushUrl;
//                            roomInfoEntity.roomid = cLive.live_number;
//                            roomInfoEntity.httpPullUrl = cLive.rtmp_push_pull_url_json.httpPullUrl;
//                            roomInfoEntity.rtmpPullUrl = cLive.rtmp_push_pull_url_json.rtmpPullUrl;
//                            roomInfoEntity.owner = cLive.rtmp_push_pull_url_json.name;
//
//                            DemoCache.setRoomInfoEntity(roomInfoEntity);
//
//                            PublishParam publishParam = new PublishParam();
//                            publishParam.pushUrl = push_url;
//                            publishParam.definition = quality;
//                            publishParam.openVideo = open_video;
//                            publishParam.openAudio = open_audio;
//                            publishParam.useFilter = useFilter;
//                            publishParam.faceBeauty = faceBeauty;
//                            if (callBack != null)
//                                callBack.liveStatus(true, publishParam, cLive.live_number);
//                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("创建失败");
//                        if (callBack != null)
//                            callBack.liveStatus(false, null, 0);
                    }
                });
    }

    /**
     * 预告信息
     * @param userId
     * @param callBack
     */
    public void trailerInfo(String userId, final TrailerInfoCallBack callBack) {
        RetrofitUtils.getInstance().getTrailerInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TrailerInfo.DataBean.VoiceLiveBean>() {
                    @Override
                    public void call(TrailerInfo.DataBean.VoiceLiveBean cLive) {
                        if (callBack != null)
                            callBack.trailerInfo(cLive);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callBack != null)
                            callBack.trailerInfo(null);
                    }
                });
    }

    /**
     * 进入直播间返回监听
     */
    public interface LiveCallBack {
//        void liveStatus(boolean status, PublishParam publishParam, int roomId);
    }

    /**
     * 获取节目预告返回监听
     */
    public interface TrailerInfoCallBack {
        void trailerInfo(TrailerInfo.DataBean.VoiceLiveBean voiceLiveBean);
    }
}
