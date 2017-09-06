package com.wotingfm.ui.play.main.presenter;

import android.content.ContentValues;
import android.util.Log;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.pili.pldroid.player.PLMediaPlayer;
import com.woting.commonplat.player.baidu.BDPlayer;
import com.woting.commonplat.player.qiniu.QNPlayer;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.ListDataSaveUtils;
import com.wotingfm.ui.bean.BaseResult;
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
    private BDPlayer bdPlayer;
    private QNPlayer QNPlayer;
    public static int playerType = 1; //1 百度播放器，2 大牛播放器

    public PlayerPresenter(PlayerFragment activity) {
        this.activity = activity;
        this.model = new PlayerModel();
        create();
        setListener();
    }

    private void create() {
        listDataSaveUtils = new ListDataSaveUtils(BSApplication.getInstance());// 本地数据
        if (bdPlayer == null) bdPlayer = new BDPlayer(activity.getActivity());
        if (QNPlayer == null) QNPlayer = new QNPlayer(activity.getActivity());
        historyHelper = new HistoryHelper(BSApplication.getInstance());
    }

    private void setListener() {
            bdPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    Log.e("百度监听", "播放准备好");
                    activity.playerOnPrepared();
                }
            });
            bdPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    Log.e("百度监听", "播放完成");
                    activity.playerOnCompletion();
                }
            });
            QNPlayer.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(PLMediaPlayer mp, int preparedTime) {
                    Log.e("大牛监听", "播放准备好");
                    activity.playerOnPrepared();
                }
            });
            QNPlayer.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(PLMediaPlayer mp) {
                    Log.e("大牛监听", "播放完成");
                    activity.playerOnCompletion();
                }
            });
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

    public void play(String url) {
        if (playerType == 1) {
            bdPlayer.setVideoPath(url);
            bdPlayer.start();
        } else {
            QNPlayer.onClickPlay(url);
        }
    }

    public void playPause() {
            bdPlayer.pause();
            QNPlayer.onClickPause();
    }

    public void start() {
        if (playerType == 1) {
            bdPlayer.start();
        } else {
            QNPlayer.onClickResume();
        }
    }

    public Object getCurrentPlayerState() {
        if (playerType == 1) {
            return bdPlayer.getCurrentPlayerState();
        } else {
            return QNPlayer.getCurrentPlayerState();
        }
    }

    public int getCurrentPosition() {
        if (playerType == 1) {
            return bdPlayer.getCurrentPosition();
        } else {
            return QNPlayer.getCurrentPosition();
        }
    }

    public int getDuration() {
        if (playerType == 1) {
            return bdPlayer.getDuration();
        } else {
            return QNPlayer.getDuration();
        }
    }

//    public void stopPlayback() {
//        if (playerType == 1) {
//            bdPlayer.stopPlayback();
//        } else {
//            QNPlayer.onClickStop();
//        }
//    }

    public void seekTo(int progress) {
        if (playerType == 1) {
            bdPlayer.seekTo(progress);
        } else {
            QNPlayer.seekTo(progress);
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {

        QNPlayer.destroy();
        model = null;

    }

}
