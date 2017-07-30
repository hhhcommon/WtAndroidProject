package com.wotingfm.ui.test;

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
import android.widget.Toast;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.woting.commonplat.player.baidu.BDPlayer;
import com.woting.commonplat.utils.ResourceUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.bean.SinglesDownload;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.common.view.MenuDialog;
import com.wotingfm.common.view.PlayerDialog;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.look.activity.LookListFragment;
import com.wotingfm.ui.play.look.activity.classification.fragment.MinorClassificationFragment;
import com.wotingfm.ui.play.look.activity.classification.fragment.SubcategoryFragment;
import com.wotingfm.ui.play.look.activity.serch.SerchFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.AlbumsListFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.AnchorListFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.ProgramListFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.RadioStationListFragment;
import com.wotingfm.ui.play.look.fragment.ClassificationFragment;
import com.wotingfm.ui.play.look.fragment.LiveFragment;
import com.wotingfm.ui.play.look.fragment.RadioStationFragment;
import com.wotingfm.ui.play.look.fragment.SelectedFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    LinearLayout largeLabelSeekbar;


    public static PlayerFragment newInstance(String albumsId) {
        BSApplication.IS_RESULT = true;
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsId", albumsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstanceSerch(String albumsId, String id, String title) {
        BSApplication.IS_RESULT = true;
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsId", albumsId);
        bundle.putString("id", id);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance(String albumsId, String q) {
        BSApplication.IS_RESULT = true;
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumsId", albumsId);
        bundle.putString("q", q);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance(ChannelsBean singlesBase, String q) {
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("q", q);
        bundle.putSerializable("channelsBean", singlesBase);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance(ChannelsBean singlesBase) {
        BSApplication.IS_RESULT = true;
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("channelsBean", singlesBase);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance(SinglesBase singlesBase) {
        BSApplication.IS_RESULT = true;
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("singlesBase", singlesBase);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance(SinglesBase singlesBase, String q) {
        BSApplication.IS_RESULT = true;
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("q", q);
        bundle.putSerializable("singlesBase", singlesBase);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance(List<SinglesDownload> singlesDownloads) {
        EventBus.getDefault().postSticky("stop");
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("singlesDownloads", (Serializable) singlesDownloads);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlayerFragment newInstance() {
        BSApplication.IS_RESULT = true;
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    private ChannelsBean channelsBean;
    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private PLMediaPlayer mMediaPlayer;
    private String mAudioPath;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;
    private String q, title;

    @Override
    public void initView() {
        mAVOptions = new AVOptions();
        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);
        // Some optimization with buffering mechanism when be set to 1
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);

        // whether start play automatically after prepared, default value is 1
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        /*AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);*/
        startTelephonyListener();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                getPlayerList("");
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
        bdPlayer = new BDPlayer(getActivity());
        setListener();
        historyHelper = new HistoryHelper(BSApplication.getInstance());
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<SinglesDownload> singlesBeanList = (List<SinglesDownload>) bundle.getSerializable("singlesDownloads");
            albumsId = bundle.getString("albumsId");
            q = bundle.getString("q");
            id = bundle.getString("id");
            title = bundle.getString("title");
            SinglesBase singlesBase = (SinglesBase) bundle.getSerializable("singlesBase");
            channelsBean = (ChannelsBean) bundle.getSerializable("channelsBean");
            relatiBottom.setVisibility(View.VISIBLE);
            if (singlesBase != null) {
                loadLayout.showContentView();
                singLesBeans.clear();
                singLesBeans.add(singlesBase);
                postionPlayer = 0;
                bdPlayer.setVideoPath(singlesBase.single_file_url);
                bdPlayer.start();
                largeLabelSeekbar.setVisibility(View.VISIBLE);
                ivPlayList.setVisibility(View.VISIBLE);
                setBeforeOrNext(singlesBase);
                mPlayerAdapter.notifyDataSetChanged();
            } else {
                if (singlesBeanList != null) {
                    if (bdPlayer != null)
                        bdPlayer.stopPlayback();
                    if (singLesBeans != null)
                        singLesBeans.clear();
                    singLesBeans.addAll(singlesBeanList);
                    mPlayerAdapter.notifyDataSetChanged();
                    largeLabelSeekbar.setVisibility(View.VISIBLE);
                    ivPlayList.setVisibility(View.VISIBLE);
                    if (singLesBeans != null && !singLesBeans.isEmpty()) {
                        postionPlayer = 0;
                        SinglesBase sb = singLesBeans.get(0);
                        bdPlayer.stopPlayback();
                        bdPlayer.setVideoPath(sb.single_file_url);
                        bdPlayer.start();
                        setBeforeOrNext(sb);
                    }
                } else {
                    if (channelsBean != null) {
                        largeLabelSeekbar.setVisibility(View.GONE);
                        loadLayout.showContentView();
                        singLesBeans.clear();
                        SinglesBase s = new SinglesBase();
                        s.album_title = channelsBean.title;
                        mAudioPath = channelsBean.radio_url;
                        s.single_logo_url = channelsBean.image_url;
                        s.single_file_url = channelsBean.radio_url;
                        s.album_title = channelsBean.desc;
                        singLesBeans.add(s);
                        postionPlayer = 0;
                        largeLabelSeekbar.setVisibility(View.INVISIBLE);
                        ivPlayList.setVisibility(View.INVISIBLE);
                        prepare();
                        setBeforeOrNext(s);
                        mPlayerAdapter.notifyDataSetChanged();
                    } else {
                        largeLabelSeekbar.setVisibility(View.VISIBLE);
                        ivPlayList.setVisibility(View.VISIBLE);
                        getPlayerList(TextUtils.isEmpty(albumsId) ? "" : albumsId);
                    }
                }
            }
        } else {
            ivPlayList.setVisibility(View.VISIBLE);
            getPlayerList(TextUtils.isEmpty(albumsId) ? "" : albumsId);
        }

    }

    private HistoryHelper historyHelper;

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
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", sb.id);
            contentValues.put("isPlay", sb.isPlay);
            contentValues.put("single_title", sb.single_title);
            contentValues.put("play_time", System.currentTimeMillis());
            contentValues.put("single_logo_url", sb.single_logo_url);
            contentValues.put("single_file_url", sb.single_file_url);
            contentValues.put("album_title", sb.album_title);
            historyHelper.insertTotable(sb.id, contentValues);
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
        ivPlayList.setOnClickListener(this);
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
                if (getActivity() instanceof PlayerActivity) {
                    PlayerActivity playerActivity = (PlayerActivity) getActivity();
                    if (BSApplication.fragmentBase == null) {
                        playerActivity.open(LookListFragment.newInstance(0));
                    } else {
                        BSApplication.isIS_BACK = true;
                        if (BSApplication.fragmentBase instanceof SelectedFragment) {
                            playerActivity.open(LookListFragment.newInstance(0));
                        } else if (BSApplication.fragmentBase instanceof ClassificationFragment) {
                            playerActivity.open(LookListFragment.newInstance(1));
                        } else if (BSApplication.fragmentBase instanceof RadioStationFragment) {
                            playerActivity.open(LookListFragment.newInstance(2));
                        } else if (BSApplication.fragmentBase instanceof LiveFragment) {
                            playerActivity.open(LookListFragment.newInstance(3));
                        } else if (BSApplication.fragmentBase instanceof AlbumsListFragment) {
                            playerActivity.open(SerchFragment.newInstance(q, 0));
                        } else if (BSApplication.fragmentBase instanceof ProgramListFragment) {
                            playerActivity.open(SerchFragment.newInstance(q, 1));
                        } else if (BSApplication.fragmentBase instanceof AnchorListFragment) {
                            playerActivity.open(SerchFragment.newInstance(q, 2));
                        } else if (BSApplication.fragmentBase instanceof RadioStationListFragment) {
                            playerActivity.open(SerchFragment.newInstance(q, 3));
                        } else if (BSApplication.fragmentBase instanceof RadioStationListFragment) {
                            playerActivity.open(SerchFragment.newInstance(q, 3));
                        } else if (BSApplication.fragmentBase instanceof SubcategoryFragment) {
                            playerActivity.open(MinorClassificationFragment.newInstance(id, title));
                        } else {
                            playerActivity.open(BSApplication.fragmentBase);
                        }
                        BSApplication.fragmentBase = null;
                    }
                }
                //  LookListActivity.start(this);
                break;
            case R.id.ivBefore:
                if (singLesBeans.size() > postionPlayer && postionPlayer > 0) {
                    postionPlayer = postionPlayer - 1;
                    mRecyclerView.smoothScrollToPosition(postionPlayer);
                    bdPlayer.stopPlayback();
                    bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
                    bdPlayer.start();
                    seekbarVideo.setProgress(0);
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                    setBeforeOrNext(singLesBeans.get(postionPlayer));
                }
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
                break;
            case R.id.ivPlayList:
                if (playerDialog == null) {
                    playerDialog = new PlayerDialog(getActivity());
                }
                if (singLesBeans != null && !singLesBeans.isEmpty()) {
                    playerDialog.showPlayDialo(singLesBeans, singLesBeans.get(postionPlayer).id, new PlayerDialog.PopPlayCallBack() {
                        @Override
                        public void play(SinglesBase singlesBean, int postion) {
                            bdPlayer.stopPlayback();
                            bdPlayer.setVideoPath(singlesBean.single_file_url);
                            postionPlayer = postion;
                            bdPlayer.start();
                        }

                        @Override
                        public void close(SinglesBase singlesBean) {
                            singLesBeans.remove(singlesBean);
                            mPlayerAdapter.notifyDataSetChanged();
                            setBeforeOrNext(null);
                        }
                    });
                    playerDialog.show();
                }
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
    private List<SinglesBase> singLesBeans = new ArrayList<>();
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
                        seekbarVideo.setProgress(bdPlayer.getCurrentPosition());
                        txtVideoStarttime.setText(TimeUtil.formatterTime(bdPlayer.getCurrentPosition()) + "");
                    }
                });//每隔一秒发送数据
    }


    //接受到下载完成的通知
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEventMainThread(String event) {
        Log.i("mingku", "event=" + event);
        if (!TextUtils.isEmpty(event) && "stop".equals(event)) {
            if (bdPlayer != null) {
                bdPlayer.stopPlayback();
            }
            release();
        } else if (!TextUtils.isEmpty(event) && "pause".equals(event)) {
            if (bdPlayer != null) {
                bdPlayer.pause();
            }
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
            }
        } else if (!TextUtils.isEmpty(event) && "start".equals(event)) {
            if (bdPlayer != null) {
                bdPlayer.start();
            }
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
    }


    private Toast mToast = null;

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
            mMediaPlayer.start();
            mIsStopped = false;
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            Log.i("mingku", "OnInfo, what = " + what + ", extra = " + extra);
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
     * <p>
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
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToastTips("Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToastTips("404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToastTips("Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    showToastTips("Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToastTips("Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToastTips("Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToastTips("Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    showToastTips("unknown error !");
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

    private void showToastTips(final String tips) {
     /*   if (mIsActivityPaused) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getActivity(), tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });*/
    }

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
