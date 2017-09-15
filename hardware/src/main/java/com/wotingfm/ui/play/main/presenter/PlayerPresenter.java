package com.wotingfm.ui.play.main.presenter;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.service.PlayerService;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ListDataSaveUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.main.PlayerFragment;
import com.wotingfm.ui.play.main.model.PlayerModel;
import com.wotingfm.ui.play.main.model.shoot;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    public static int playerType = 1;
    private String endTime;
    private String startTime;

    public PlayerPresenter(PlayerFragment activity) {
        this.activity = activity;
        this.model = new PlayerModel();
        create();

    }

    private void create() {
        listDataSaveUtils = new ListDataSaveUtils(BSApplication.getInstance());// 本地数据
        historyHelper = new HistoryHelper(BSApplication.getInstance());// 播放历史
    }

    // 获取数据
    public void getData() {
        if (CommonUtils.isLogin()) {
            getOnPlay();// 获取保存的续播对象数据
        } else {
            // 获取本地数据
            SinglesBase s = getSingle();
            if (s != null) {
                if (s.isAlbumList) {
                    activity.setDataForLocal(s, 2);// 获取专辑列表
                } else {
                    activity.setDataForLocal(s, 1);// 获取推荐列表
                }
            } else {
                activity.setDataForLocal(null, 1);// 此时没有对象，1不起作用
            }
        }
    }

    // 获取播放对象
    private SinglesBase getSingle() {
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

    // 获取播放列表
//    private List<SinglesBase> getDataList() {
//        if (listDataSaveUtils != null) {
//            List<SinglesBase> s = listDataSaveUtils.getDataList(PREFERENCES_BASE_LIST_KEY, SinglesBase.class);
//            if (s != null) {
//                return s;
//            } else {
//                return null;
//            }
//        } else {
//            return null;
//        }
//    }

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

//    /**
//     * 保存列表数据
//     *
//     * @param list
//     */
//    public void saveUtilList(List<SinglesBase> list) {
//        listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, list);
//    }

    /**
     * 播放
     *
     * @param url
     */
    public void play(String url) {
        if (!TextUtils.isEmpty(url) && !url.contains("duotin")) {
            PlayerService.play(playerType, url);
        } else {
            ToastUtils.show_always(activity.getActivity(), "当前节目播放地址出错");
        }

    }

    /**
     * 暂停
     */
    public void playPause() {
        if (PlayerService.isPlaying(playerType)) PlayerService.playPause();
    }

    /**
     * 继续播放
     */
    public void start() {
        if (!PlayerService.isPlaying(playerType)) PlayerService.start(playerType);
    }

    /**
     * @return
     */
    public boolean isPlaying() {
        return PlayerService.isPlaying(playerType);
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

    public String getIngTime() {
        return model.getIngTime();
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    // 保存续播数据
    private void saveUP(SinglesBase singlesBase) {
        GlobalStateConfig.currentTime = String.valueOf(singlesBase.play_time);
        if (singlesBase.isAlbumList) {
            GlobalStateConfig.listType = "2";
        } else {
            GlobalStateConfig.listType = "1";
        }
        GlobalStateConfig.playingId = singlesBase.id;
        if (singlesBase.is_radio) {
            GlobalStateConfig.playingType = "2";
        } else {
            GlobalStateConfig.playingType = "1";
        }
    }

    /**
     * 保存续播数据
     *
     * @param singlesBase
     */
    public void save(SinglesBase singlesBase) {
        if (CommonUtils.isLogin()) {
            saveUP(singlesBase);
        } else {
            saveUtil(singlesBase);
        }
    }

    /**
     * 获取续播数据
     */
    public void getOnPlay() {
        model.getOnPlay(new PlayerModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                dealShoot(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.setDataForLocal(null, 0);// 获取推荐数据
            }
        });
    }

    // 处理返回续播数据
    private void dealShoot(Object o) {
        try {
            String ss = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(ss);
            int ret = js.getInt("ret");
            Log.e("获取续播数据==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                JSONTokener jsonParser = new JSONTokener(msg);
                JSONObject arg1 = (JSONObject) jsonParser.nextValue();
                String onPlay = arg1.getString("onplay");
                // 续播提交的数据
                shoot _s = new Gson().fromJson(onPlay, new TypeToken<shoot>() {
                }.getType());

                if (_s != null) {
                    if (_s.playingType.equals("2")) {
                        // 电台
                        String radio = arg1.getString("radio");
                        ChannelsBean _channel = new Gson().fromJson(radio, new TypeToken<ChannelsBean>() {
                        }.getType());
                        if (_channel != null) {
                            SinglesBase s = new SinglesBase();
                            s.single_title = _channel.title;
                            s.id = _channel.id;
                            s.single_logo_url = _channel.image_url;
                            s.single_file_url = _channel.radio_url;
                            s.album_title = "直播中";
                            s.is_radio = true;
                            if (!TextUtils.isEmpty(_s.currentTime)) {
                                s.play_time = Integer.valueOf(_s.currentTime);
                            } else {
                                s.play_time = 0;
                            }
                            s.postionPlayer = 0;
                            if (!TextUtils.isEmpty(_s.listType)) {
                                if (_s.listType.equals("1")) {
                                    activity.setDataForLocal(s, 1);// 获取推荐数据
                                } else {
                                    activity.setDataForLocal(s, 2);// 获取专辑数据
                                }
                            } else {
                                activity.setDataForLocal(s, 1);// 获取推荐数据
                            }
                        } else {
                            activity.setDataForLocal(null, 0);// 获取推荐数据
                        }
                    } else {
                        // 节目
                        String single = arg1.getString("single");
                        SinglesBase _single = new Gson().fromJson(single, new TypeToken<SinglesBase>() {
                        }.getType());
                        if (!TextUtils.isEmpty(_s.currentTime)) {
                            _single.play_time = Integer.valueOf(_s.currentTime);
                        } else {
                            _single.play_time = 0;
                        }
                        _single.postionPlayer = 0;

                        if (!TextUtils.isEmpty(_s.listType)) {
                            if (_s.listType.equals("1")) {
                                activity.setDataForLocal(_single, 1);// 获取推荐数据
                            } else {
                                activity.setDataForLocal(_single, 2);// 获取专辑数据
                            }
                        } else {
                            activity.setDataForLocal(_single, 1);// 获取推荐数据
                        }
                    }
                } else {
                    activity.setDataForLocal(null, 0);// 获取推荐数据
                }
            } else {
                activity.setDataForLocal(null, 0);// 获取推荐数据
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.setDataForLocal(null, 0);// 获取推荐数据
        }
    }

    /**
     * 获取电台节目单
     *
     * @param id
     */
    public void getSeek(String id) {
        try {
            RetrofitUtils.getInstance().getSeek(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            try {
                                String s = new GsonBuilder().serializeNulls().create().toJson(o);
                                Log.e("电台直播返回数据", s);
                                //填充UI
                                JSONObject js = new JSONObject(s);
                                try {
                                    endTime = js.getString("end_time");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    endTime = "24:00:00";
                                }
                                try {
                                    startTime = js.getString("start_time");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    startTime = "00:00:00";
                                }
                                String title = "直播中";
                                try {
                                    title = js.getString("title");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                activity.setMaxForRadio(title, startTime, endTime);
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

    /**
     * 获取推荐数据
     * type 0,列表刷新
     */
    public void getRecommendedList(final int type) {
        model.getRecommendedList(new PlayerModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                List<SinglesBase> singLesBeans = (List<SinglesBase>) o;
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        singLesBeans.get(i).isAlbumList = false;
                    }
                    activity.setData(singLesBeans, type);
                } else {
                    activity.setData(null, type);
                }
            }

            @Override
            public void onFailure(String msg) {
                activity.setData(null, type);
            }
        });
    }

    /**
     * 获取语音搜索数据
     */
    public void getVoiceSearchList(String s) {
        model.getVoiceSearchList(s, new PlayerModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                List<SinglesBase> singLesBeans = (List<SinglesBase>) o;
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        singLesBeans.get(i).isAlbumList = false;
                    }
                    activity.setData(singLesBeans, 1);
                } else {
                    ToastUtils.show_always(activity.getActivity(), "抱歉，没有查询到您想要的内容");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show_always(activity.getActivity(), "抱歉，没有查询到您想要的内容");
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

    // 提交到后台当前播放内容
    public void sendPlay(String id) {
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
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

}
