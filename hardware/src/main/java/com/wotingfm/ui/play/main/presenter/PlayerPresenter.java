package com.wotingfm.ui.play.main.presenter;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.service.PlayerService;
import com.wotingfm.common.utils.ListDataSaveUtils;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.bean.PlayBill;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.main.PlayerFragment;
import com.wotingfm.ui.play.main.model.PlayerModel;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.common.database.DBUtils.PREFERENCES_BASE_LIST_KEY;
import static com.wotingfm.common.database.DBUtils.PREFERENCES_BASE_OBJECT_KEY;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class PlayerPresenter {

    private PlayerModel model;
    private PlayerFragment activity;
    private ListDataSaveUtils listDataSaveUtils;
    private HistoryHelper historyHelper;
    public static int playerType = 1; //1 百度播放器，2 大牛播放器
    private PlayBill data;
    private String endTime;
    private String startTime;

    public PlayerPresenter(PlayerFragment activity) {
        this.activity = activity;
        this.model = new PlayerModel();
        create();
    }

    private void create() {
        listDataSaveUtils = new ListDataSaveUtils(BSApplication.getInstance());// 本地数据
        historyHelper = new HistoryHelper(BSApplication.getInstance());
    }

    public SinglesBase getData() {
        if (listDataSaveUtils != null) {
            SinglesBase s = (SinglesBase) listDataSaveUtils.getObjectFromShare(PREFERENCES_BASE_OBJECT_KEY);
            if (s != null) {
                return s;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<SinglesBase> getDataList() {
        if (listDataSaveUtils != null) {
            List<SinglesBase> s = listDataSaveUtils.getDataList(PREFERENCES_BASE_LIST_KEY, SinglesBase.class);
            if (s != null) {
                return s;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取推荐数据
     */
    public void getRecommendedList(String s) {
        model.getRecommendedList(s, new PlayerModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                List<SinglesBase> singLesBeans = (List<SinglesBase>) o;
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        singLesBeans.get(i).isAlbumList = false;
                    }
                    activity.setData(singLesBeans, 1);
                } else {
                    activity.setData(null, 1);
                }
            }

            @Override
            public void onFailure(String msg) {
                activity.setData(null, 1);
            }
        });
    }

    /**
     * 获取语音搜索数据
     */
    public void getVoiceSearchList(String s) {
        model.getRecommendedList(s, new PlayerModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                List<SinglesBase> singLesBeans = (List<SinglesBase>) o;
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        singLesBeans.get(i).isAlbumList = false;
                    }
                    activity.setData(singLesBeans, 1);
                } else {
                    activity.setData(null, 1);
                }
            }

            @Override
            public void onFailure(String msg) {
                activity.setData(null, 1);
            }
        });
    }

    /**
     * 获取专辑列表
     *
     * @param id
     */
    public void getPlayerList(String id) {
        model.getAlbumList(id, new PlayerModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                List<SinglesBase> singLesBeans = (List<SinglesBase>) o;
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        singLesBeans.get(i).isAlbumList = true;
                    }
                    activity.setData(singLesBeans, 2);
                } else {
                    activity.setData(null, 2);
                }
            }

            @Override
            public void onFailure(String msg) {
                activity.setData(null, 2);
            }
        });
    }

    /**
     * 保存数据
     *
     * @param sb
     */
    public void saveData(SinglesBase sb) {
        if (sb != null) {
            saveHistory(sb);
            saveUtil(sb);
            sendPlay(sb.id);
        }
    }

    /**
     * 保存播放历史
     *
     * @param sb
     */
    public void saveHistory(SinglesBase sb) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", sb.id);
        contentValues.put("isPlay", sb.isPlay);
        contentValues.put("is_radio", sb.is_radio);
        contentValues.put("single_title", sb.single_title);
        contentValues.put("play_time", System.currentTimeMillis());
        contentValues.put("single_logo_url", sb.single_logo_url);
        contentValues.put("single_file_url", sb.single_file_url);
        contentValues.put("album_title", sb.album_title);
        historyHelper.insertTotable(sb.id, contentValues);
    }

    /**
     * 保存当前播放数据
     *
     * @param sb
     */
    public void saveUtil(SinglesBase sb) {
        listDataSaveUtils.setObjectToShare(sb, PREFERENCES_BASE_OBJECT_KEY);
    }

    /**
     * 保存列表数据
     *
     * @param list
     */
    public void saveUtilList(List<SinglesBase> list) {
        listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, list);
    }

    // 提交到后台当前播放内容
    private void sendPlay(String id) {
        RetrofitUtils.getInstance().playSingles(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    /**
     * 播放
     *
     * @param url
     */
    public void play(String url) {
        PlayerService.play(playerType, url);
    }

    /**
     * 暂停
     */
    public void playPause() {
        PlayerService.playPause();
    }

    /**
     * 继续播放
     */
    public void start() {
        PlayerService.start(playerType);
    }

    /**
     * @return
     */
    public Object getCurrentPlayerState() {
        return PlayerService.getCurrentPlayerState(playerType);
    }

    /**
     * @return
     */
    public int getCurrentPosition() {
        return PlayerService.getCurrentPosition(playerType);
    }

    /**
     * @return
     */
    public int getDuration() {
        return PlayerService.getDuration(playerType);
    }

    /**
     * 设置播放进度
     *
     * @param progress
     */
    public void seekTo(long progress) {
        PlayerService.seekTo(playerType, progress);
    }

    /**
     * 获取最大进度值
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public int getMax(String startTime, String endTime) {
        return model.getMax(startTime, endTime);
    }

    public int getIng() {
        return model.getIng(startTime);
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

    public void getSeek(String id) {
        try {
            RetrofitUtils.getInstance().getSeek(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                Log.e("电台直播返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
                                //填充UI
                                data = (PlayBill) o;
                                endTime = data.end_time;
                                startTime = data.start_time;
                                activity.setMax(getMax(startTime,endTime),endTime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEndTime(){
        return endTime;
    }

    public String getStartTime(){
        return startTime;
    }

}
