package com.wotingfm.ui.play.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.woting.commonplat.player.baidu.BDPlayer;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.utils.ResourceUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.ListDataSaveUtils;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.ui.bean.Selected;
import com.wotingfm.ui.play.main.view.MenuDialog;
import com.wotingfm.ui.play.main.view.PlayerDialog;
import com.wotingfm.ui.play.main.adapter.PlayerAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.Player;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.bean.SinglesDownload;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.common.database.DBUtils.PREFERENCES_BASE_LIST_KEY;
import static com.wotingfm.common.database.DBUtils.PREFERENCES_BASE_OBJECT_KEY;


/**
 * 作者：xinLong on 2017/6/2 12:15
 * 邮箱：645700751@qq.com
 */
public class PlayerFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.txt_video_starttime)
    TextView txtVideoStarttime;
    @BindView(R.id.seekbar_video)
    SeekBar seekbarVideo;
    @BindView(R.id.txt_video_totaltime)
    TextView txtVideoTotaltime;

    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.relatiBottom)
    RelativeLayout relatiBottom;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.LoadingView)
    LinearLayout mLoadingView;
    @BindView(R.id.largeLabelSeekbar)
    RelativeLayout largeLabelSeekbar;
    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.re_img_bg)
    RelativeLayout re_img_bg;


    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public List<SinglesBase> singLesBeans = new ArrayList<>();

    private ChannelsBean channelsBean;
    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private PLMediaPlayer mMediaPlayer;
    private String mAudioPath;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;
    private SinglesBase singlesBase;
    private ListDataSaveUtils listDataSaveUtils;
private boolean isRadio=false;
    //
    @Override
    public void initView() {
        GlobalStateConfig.IS_CREATE = true;
        listDataSaveUtils = new ListDataSaveUtils(BSApplication.getInstance());
        if (mAVOptions == null)
            mAVOptions = new AVOptions();
        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // Some optimization with buffering mechanism when be set to 1
        // 1 -> hw codec enable, 0 -> disable [recommended]
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        // whether start play automatically after prepared, default value is 1

        /*AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);*/
        startTelephonyListener();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                getPlayerList(TextUtils.isEmpty(albumsId) ? "" : albumsId);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(mRecyclerView);
        mPlayerAdapter = new PlayerAdapter(BSApplication.getInstance(), singLesBeans);
        mRecyclerView.setAdapter(mPlayerAdapter);

        int with = DementionUtil.getScreenWidthInPx(this.getActivity()) - DementionUtil.dip2px(this.getActivity(), 80);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(with, with);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        img_bg.setLayoutParams(params);//将设置好的布局参数应用到控件中

        if (bdPlayer == null) bdPlayer = new BDPlayer(getActivity());
        setListener();
        historyHelper = new HistoryHelper(BSApplication.getInstance());
        Bundle bundle = getArguments();
        isDataSave = false;
        if (bundle != null) {
            singlesBeanList = (List<SinglesDownload>) bundle.getSerializable("singlesDownloads");
            albumsId = bundle.getString("albumsId");
            id = bundle.getString("id");
            singlesBase = (SinglesBase) bundle.getSerializable("singlesBase");
            channelsBean = (ChannelsBean) bundle.getSerializable("channelsBean");
            postionPlayer = 0;
            initData(albumsId, singlesBase, channelsBean, singlesBeanList,null);
        } else {
            setivPlayListView(true);
            if (listDataSaveUtils != null) {
                List<SinglesBase> singlesBases = listDataSaveUtils.getDataList(PREFERENCES_BASE_LIST_KEY, SinglesBase.class);
                SinglesBase singlesBaseww = (SinglesBase) listDataSaveUtils.getObjectFromShare(PREFERENCES_BASE_OBJECT_KEY);
                if (singlesBases != null && !singlesBases.isEmpty() && singlesBaseww != null) {
                    isDataSave = true;
                    singlesBase = singlesBaseww;
                    singLesBeans.clear();
                    boolean is_radio = singlesBaseww.is_radio;
                    singLesBeans.addAll(singlesBases);
                    mPlayerAdapter.notifyDataSetChanged();
                    postionPlayer = singlesBaseww.postionPlayer;
                    mRecyclerView.scrollToPosition(postionPlayer);
                    relatiBottom.setVisibility(View.VISIBLE);
                    if (is_radio == true) {
                        largeLabelSeekbar.setVisibility(View.INVISIBLE);
                        setivPlayListView(false);
                        mAudioPath = singlesBaseww.single_file_url;
                        prepare();
                    } else {
                        largeLabelSeekbar.setVisibility(View.VISIBLE);
                        setivPlayListView(true);
                        bdPlayer.setVideoPath(singlesBaseww.single_file_url);
                        bdPlayer.start();
                    }
                    setBeforeOrNext(singlesBaseww);
                    loadLayout.showContentView();
                } else {
                    getPlayerList(TextUtils.isEmpty(albumsId) ? "" : albumsId);
                }
            } else {
                getPlayerList(TextUtils.isEmpty(albumsId) ? "" : albumsId);
            }
        }
    }

    private boolean isDataSave;
    private List<SinglesDownload> singlesBeanList;

    private void initData(String albumsId, SinglesBase singlesBase, ChannelsBean channelsBean, List<SinglesDownload> singlesBeanList, Selected.DataBeanX.DataBean DataBean) {
        relatiBottom.setVisibility(View.VISIBLE);
        postionPlayer = 0;
        if (singlesBase != null) {
            if (singlesBase.is_radio == false) {
                loadLayout.showContentView();
                singLesBeans.clear();
                singLesBeans.add(singlesBase);
                postionPlayer = 0;
                singlesBase.postionPlayer = 0;
                bdPlayer.stopPlayback();
                bdPlayer.setVideoPath(singlesBase.single_file_url);
                bdPlayer.start();
                largeLabelSeekbar.setVisibility(View.VISIBLE);
                setivPlayListView(true);
                if (listDataSaveUtils != null)
                    listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, singLesBeans);
                setBeforeOrNext(singlesBase);
                mPlayerAdapter.notifyDataSetChanged();
            } else {
                largeLabelSeekbar.setVisibility(View.GONE);
                loadLayout.showContentView();
                singLesBeans.clear();
                mAudioPath = singlesBase.single_file_url;
                singlesBase.postionPlayer = 0;
                singLesBeans.add(singlesBase);
                if (listDataSaveUtils != null)
                    listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, singLesBeans);
                postionPlayer = 0;
                largeLabelSeekbar.setVisibility(View.INVISIBLE);
                setivPlayListView(false);
                prepare();
                setBeforeOrNext(singlesBase);
                mPlayerAdapter.notifyDataSetChanged();
            }
        } else {
            if (singlesBeanList != null) {
                if (bdPlayer != null)
                    bdPlayer.stopPlayback();
                if (singLesBeans != null)
                    singLesBeans.clear();
                singLesBeans.addAll(singlesBeanList);
                mPlayerAdapter.notifyDataSetChanged();
                largeLabelSeekbar.setVisibility(View.VISIBLE);
                setivPlayListView(true);
                if (singLesBeans != null && !singLesBeans.isEmpty()) {
                    postionPlayer = 0;
                    SinglesBase sb = singLesBeans.get(0);
                    bdPlayer.stopPlayback();
                    bdPlayer.setVideoPath(sb.single_file_url);
                    bdPlayer.start();
                    if (listDataSaveUtils != null)
                        listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, singLesBeans);
                    setBeforeOrNext(sb);
                }
            } else {
                if (channelsBean != null) {
                    largeLabelSeekbar.setVisibility(View.GONE);
                    loadLayout.showContentView();
                    singLesBeans.clear();
                    SinglesBase s = new SinglesBase();
                    s.album_title = channelsBean.title;
                    s.id = channelsBean.id;
                    mAudioPath = channelsBean.radio_url;
                    s.single_logo_url = channelsBean.image_url;
                    s.single_file_url = channelsBean.radio_url;
                    s.album_title = channelsBean.desc;
                    s.is_radio = true;
                    s.postionPlayer = 0;
                    singLesBeans.add(s);
                    if (listDataSaveUtils != null)
                        listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, singLesBeans);
                    postionPlayer = 0;
                    largeLabelSeekbar.setVisibility(View.INVISIBLE);
                    setivPlayListView(false);
                    prepare();
                    setBeforeOrNext(s);
                    mPlayerAdapter.notifyDataSetChanged();
                } else {
                    if(DataBean!=null){
                        loadLayout.showContentView();
                        singLesBeans.clear();
                        SinglesBase s = new SinglesBase();
                        s.album_title = DataBean.title;
                        s.id = DataBean.id;
                        s.single_logo_url = DataBean.single_logo_url;
                        s.single_file_url = DataBean.single_file_url;
                        s.album_title = DataBean.album_title;
                        s.is_radio = false;
                        s.postionPlayer = 0;
                        singLesBeans.add(s);
                        postionPlayer = 0;
                        bdPlayer.stopPlayback();
                        bdPlayer.setVideoPath(s.single_file_url);
                        bdPlayer.start();
                        largeLabelSeekbar.setVisibility(View.VISIBLE);
                        setivPlayListView(true);
                        if (listDataSaveUtils != null)
                            listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, singLesBeans);
                        setBeforeOrNext(s);
                        mPlayerAdapter.notifyDataSetChanged();
                    }else{
                    largeLabelSeekbar.setVisibility(View.VISIBLE);
                    setivPlayListView(true);
                    getPlayerList(TextUtils.isEmpty(albumsId) ? "" : albumsId);}
                }
            }
        }
        mRecyclerView.smoothScrollToPosition(postionPlayer);
    }


    private HistoryHelper historyHelper;
    private SinglesBase sbBase;

    private void setBeforeOrNext(SinglesBase sb) {
        if (postionPlayer != 0) {
            ivBefore.setImageResource(R.mipmap.music_play_icon_before);
        } else {
            ivBefore.setImageResource(R.mipmap.music_play_icon_before_gray);
        }
        if (postionPlayer == singLesBeans.size() - 1 || singLesBeans.size() == 1) {
            ivNext.setImageResource(R.mipmap.music_play_icon_next_gray);
        } else {
            ivNext.setImageResource(R.mipmap.music_play_icon_next);
        }
        if (sb != null) {
            sbBase = sb;
            sbBase.postionPlayer = postionPlayer;
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
            RetrofitUtils.getInstance().playSingles(sb.id)
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
            listDataSaveUtils.setObjectToShare(sb, PREFERENCES_BASE_OBJECT_KEY);
        }

    }


    private void setListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int postion = manager.findLastCompletelyVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && postion != postionPlayer && postion >= 0) {
                    seekbarVideo.setProgress(0);
                    postionPlayer = postion;
                    bdPlayer.stopPlayback();
                    bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
                    bdPlayer.start();
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                    setBeforeOrNext(singLesBeans.get(postionPlayer));
                }
            }
        });
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isChanging = false;
                bdPlayer.seekTo(seekBar.getProgress());
            }
        });
        bdPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                if (isDataSave == true && singlesBase != null) {
                    bdPlayer.seekTo(singlesBase.play_time);
                    isDataSave = false;
                } else {
                    bdPlayer.seekTo(0);
                }
                seekbarVideo.setMax(bdPlayer.getDuration());
                txtVideoTotaltime.setText(TimeUtil.formatterTime((bdPlayer.getDuration())) + "");
                setBarProgrees();
            }
        });
        bdPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (postionPlayer < singLesBeans.size() - 1) {
                    postionPlayer = postionPlayer + 1;
                    mRecyclerView.smoothScrollToPosition(postionPlayer);
                    bdPlayer.stopPlayback();
                    bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
                    bdPlayer.start();
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                    seekbarVideo.setProgress(0);
                    setBeforeOrNext(singLesBeans.get(postionPlayer));
                }
            }
        });
        ivNext.setOnClickListener(this);
        lin_PlayList.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivBefore.setOnClickListener(this);
        ivPlayerCenter.setOnClickListener(this);
        ivPlayerFind.setOnClickListener(this);
    }

    @BindView(R.id.ivPlayerCenter)
    ImageView ivPlayerCenter;
    @BindView(R.id.ivPlayerFind)
    ImageView ivPlayerFind;
    @BindView(R.id.ivBefore)
    ImageView ivBefore;
    @BindView(R.id.ivPause)
    ImageView ivPause;
    @BindView(R.id.ivNext)
    ImageView ivNext;
    @BindView(R.id.ivPlayList)
    TextView ivPlayList;
    @BindView(R.id.img_PlayList)
    ImageView img_PlayList;
    @BindView(R.id.lin_PlayList)
    LinearLayout lin_PlayList;
    @BindView(R.id.ivMore)
    ImageView ivMore;

    private void PlayerResult() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() == true) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
            }
            ivPause.setImageResource(R.mipmap.music_play_icon_play);
        } else {
            if (mIsStopped) {
                prepare();
            } else {
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();
                }
            }
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
        }
    }

    /**
     * 设置ivPlayList样式
     * @param b
     */
    private void setivPlayListView(boolean b){
        isRadio=b;
        if(b){
            // 单体节目
             img_PlayList.setImageResource(R.drawable.icon_playlists_white);
        }else{
            // 电台
            img_PlayList.setImageResource(R.mipmap.icon_playlists_white_d);
        }
    }

    private void before() {
        if (singLesBeans.size() > postionPlayer && postionPlayer > 0 && GlobalStateConfig.IS_ONE == false && channelsBean == null) {
            postionPlayer = postionPlayer - 1;
            mRecyclerView.smoothScrollToPosition(postionPlayer);
            bdPlayer.stopPlayback();
            bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
            bdPlayer.start();
            seekbarVideo.setProgress(0);
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
            setBeforeOrNext(singLesBeans.get(postionPlayer));
        }
    }

    private void next() {
        if (postionPlayer < singLesBeans.size() - 1 && GlobalStateConfig.IS_ONE == false && channelsBean == null) {
            postionPlayer = postionPlayer + 1;
            mRecyclerView.smoothScrollToPosition(postionPlayer);
            bdPlayer.stopPlayback();
            bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
            bdPlayer.start();
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
            seekbarVideo.setProgress(0);
            setBeforeOrNext(singLesBeans.get(postionPlayer));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPlayerCenter:
                GlobalStateConfig.mineFromType = 1;
                GlobalStateConfig.activityA = "C";
                GlobalStateConfig.activityB = "B";
                EventBus.getDefault().post(new MessageEvent("three"));
                //   MainActivity.changeThree();
                Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                Bundle bundle = new Bundle();
                bundle.putInt("viewType", 3);
                push.putExtras(bundle);
                getActivity().sendBroadcast(push);
                break;
            case R.id.ivPlayerFind:
                GlobalStateConfig.mineFromType = 4;
                GlobalStateConfig.activityA = "A";
                EventBus.getDefault().post(new MessageEvent("four"));
                Intent push1 = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("viewType", 4);
                push1.putExtras(bundle1);
                getActivity().sendBroadcast(push1);
                break;
            case R.id.ivBefore:
                before();
                break;
            case R.id.ivPause:
                BDPlayer.PlayerState isPause = bdPlayer.getCurrentPlayerState();
                if (channelsBean != null) {
                    PlayerResult();
                } else {
                    if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PLAYING) {
                        bdPlayer.pause();
                        ivPause.setImageResource(R.mipmap.music_play_icon_play);
                    } else if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PAUSED) {
                        bdPlayer.start();
                        ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                    }
                }
                break;
            case R.id.ivNext:
                next();
                break;
            case R.id.lin_PlayList:
                if(isRadio){
                if (playerDialog == null) {
                    playerDialog = new PlayerDialog(getActivity());
                }
                if (singLesBeans != null && !singLesBeans.isEmpty()) {
                    playerDialog.showPlayDialog(singLesBeans, singLesBeans.get(postionPlayer).id, new PlayerDialog.PopPlayCallBack() {
                        @Override
                        public void play(SinglesBase singlesBean, int postion) {
                            postionPlayer = postion;
                            mRecyclerView.scrollToPosition(postionPlayer);
                            bdPlayer.stopPlayback();
                            bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
                            bdPlayer.start();
                            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                            seekbarVideo.setProgress(0);
                            setBeforeOrNext(singLesBeans.get(postionPlayer));
                        }

                        @Override
                        public void close(SinglesBase singlesBean) {
                            singLesBeans.remove(singlesBean);
                            mPlayerAdapter.notifyDataSetChanged();
                            setBeforeOrNext(null);
                        }
                    });
                    playerDialog.show();
                }}
                break;
            case R.id.ivMore:
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getActivity());
                }
                if (singLesBeans != null && !singLesBeans.isEmpty())
                    menuDialog.setMenuData(singLesBeans.get(postionPlayer), new MenuDialog.FollowCallBack() {
                        @Override
                        public void followPlayer(SinglesBase psb) {
                            singLesBeans.set(postionPlayer, psb);
                            mPlayerAdapter.notifyDataSetChanged();
                        }
                    }, channelsBean);
                menuDialog.show();
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_player;
    }

    //控制播放下标
    private int postionPlayer = 0;
    private BDPlayer bdPlayer;
    private PlayerAdapter mPlayerAdapter;
    //数据源dialog
    private PlayerDialog playerDialog;
    //菜单dialohg
    private MenuDialog menuDialog;
    //搜索条件或者专辑
    private String albumsId, id;

    private void getPlayerList(String albums) {
        loadLayout.showLoadingView();
        RetrofitUtils.getInstance().getPlayerList(albums)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Player.DataBean.SinglesBean>>() {
                    @Override
                    public void call(List<Player.DataBean.SinglesBean> singls) {
                        if (singls != null && !singls.isEmpty()) {
                            loadLayout.showContentView();
                            singLesBeans.clear();
                            singLesBeans.addAll(singls);
                            mPlayerAdapter.notifyDataSetChanged();
                            postionPlayer = 0;
                            SinglesBase sb = singls.get(0);
                            bdPlayer.setVideoPath(sb.single_file_url);
                            bdPlayer.start();
                            relatiBottom.setVisibility(View.VISIBLE);
                            setBeforeOrNext(sb);
                            if (listDataSaveUtils != null)
                                listDataSaveUtils.setDataList(PREFERENCES_BASE_LIST_KEY, singls);
                        } else {
                            bdPlayer.stopPlayback();
                            loadLayout.showEmptyView();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                        throwable.printStackTrace();
                    }
                });
    }

    private boolean isChanging = false;//互斥变量，防止定时器与SeekBar拖动时进度冲突

    private void setBarProgrees() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (isChanging == true) {
                            return;
                        }
                        if (sbBase != null) {
                            sbBase.play_time = bdPlayer.getCurrentPosition();
                            listDataSaveUtils.setObjectToShare(sbBase, PREFERENCES_BASE_OBJECT_KEY);
                        }
                        seekbarVideo.setProgress(bdPlayer.getCurrentPosition());
                        txtVideoStarttime.setText(TimeUtil.formatterTime(bdPlayer.getCurrentPosition()) + "");
                    }
                });//每隔一秒发送数据
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEventBase(MessageEvent messageEvent) {
        String event = messageEvent.getMessage();
        int type = messageEvent.getType();
        switch (type) {
            case 0:
                if (!TextUtils.isEmpty(event) && "stop".equals(event)) {
                    if (bdPlayer != null) {
                        bdPlayer.stopPlayback();
                    }
                    release();
                } else if (!TextUtils.isEmpty(event) && event.contains("stop&")) {
                    if (bdPlayer != null) {
                        bdPlayer.stopPlayback();
                    }
                    release();
                    initData(event.split("stop&")[1], null, null, null, null);
                } else if (!TextUtils.isEmpty(event) && "pause".equals(event)) {
                    if (bdPlayer != null) {
                        bdPlayer.pause();
                    }
                    if (mMediaPlayer != null && channelsBean != null) {
                        mMediaPlayer.pause();
                    }
                    ivPause.setImageResource(R.mipmap.music_play_icon_play);
                } else if (!TextUtils.isEmpty(event) && "start".equals(event)) {
                    if (bdPlayer != null) {
                        bdPlayer.start();
                    }
                    if (mMediaPlayer != null && channelsBean != null) {
                        mMediaPlayer.start();
                    }
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                } else if (!TextUtils.isEmpty(event) && "step".equals(event)) {
                    before();
                } else if (!TextUtils.isEmpty(event) && "next".equals(event)) {
                    next();
                } else if (!TextUtils.isEmpty(event) && "stop_or_star".equals(event)) {
                    BDPlayer.PlayerState isPause = bdPlayer.getCurrentPlayerState();
                    if (channelsBean != null) {
                        PlayerResult();
                    } else {
                        if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PLAYING) {
                            bdPlayer.pause();
                            ivPause.setImageResource(R.mipmap.music_play_icon_play);
                        } else if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PAUSED) {
                            bdPlayer.start();
                            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                        }
                    }
                }
                break;
            case 1:
                if (bdPlayer != null) {
                    bdPlayer.stopPlayback();
                }
                release();
                initData(null, null, messageEvent.getChannelsBean(), null, null);
                break;
            case 2:
                if (bdPlayer != null) {
                    bdPlayer.stopPlayback();
                }
                release();
                initData(null, messageEvent.getSinglesBase(), null, null, null);
                break;
            case 3:
                if (bdPlayer != null) {
                    bdPlayer.stopPlayback();
                }
                release();
                initData(null, null, null, messageEvent.getSinglesDownloads(), null);
                break;
            case 4:
                if (bdPlayer != null) {
                    bdPlayer.stopPlayback();
                }
                release();
                initData(null,null , null, null,messageEvent.getDataBean());
                break;
        }

    }


    TelephonyManager mTelephonyManager;
    PhoneStateListener mPhoneStateListener;

    // Listen to the telephone

    private void startTelephonyListener() {
        mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            Log.e("mingku", "Failed to initialize TelephonyManager!!!");
            return;
        }

        mPhoneStateListener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                // TODO Auto-generated method stub
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("mingku", "PhoneStateListener: CALL_STATE_IDLE");
                        if (mMediaPlayer != null) {
                            mMediaPlayer.start();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d("mingku", "PhoneStateListener: CALL_STATE_OFFHOOK");
                        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("mingku", "PhoneStateListener: CALL_STATE_RINGING: " + incomingNumber);
                        break;
                }
            }
        };

        try {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopTelephonyListener() {
        if (mTelephonyManager != null && mPhoneStateListener != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            mTelephonyManager = null;
            mPhoneStateListener = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalStateConfig.IS_CREATE = false;
        EventBus.getDefault().unregister(this);
        stopTelephonyListener();
        release();
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        // mMediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPaused = true;
        // mMediaPlayer.pause();
    }


    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void prepare() {
        release();
        if (mMediaPlayer == null) {
            mMediaPlayer = new PLMediaPlayer(BSApplication.getInstance(), mAVOptions);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(BSApplication.getInstance(), PowerManager.PARTIAL_WAKE_LOCK);
        }
        try {
            mMediaPlayer.setDataSource(mAudioPath);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp, int preparedTime) {
            Log.i("minhgku", "On Prepared !");
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                mIsStopped = false;
            }
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
            Log.d("mingku", "onBufferingUpdate: " + percent + "%");
        }
    };

    /**
     * Listen the event of playing complete
     * For playing local file, it's called when reading the file EOF
     * For playing network stream, it's called when the buffered bytes played over
     * <p/>
     * If setLooping(true) is called, the player will restart automatically
     * And ｀onCompletion｀ will not be called
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            Log.e("mingku", "Error happmiened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            release();
            if (isNeedReconnect) {
                sendReconnectMessage();
            } else {
                // finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };


    private void sendReconnectMessage() {
        mLoadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            if (mIsActivityPaused || !ResourceUtil.isLiveStreamingAvailable()) {
                // finish();
                return;
            }
            if (!ResourceUtil.isNetworkAvailable(getActivity())) {
                sendReconnectMessage();
                return;
            }
            // The PLMediaPlayer has moved to the Error state, if you want to retry, must reset first !
            prepare();
        }
    };


}
