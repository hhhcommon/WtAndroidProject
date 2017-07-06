package com.wotingfm.ui.test;

import android.app.Activity;
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
import com.netease.nim.uikit.common.util.C;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.woting.commonplat.player.baidu.BDPlayer;
import com.woting.commonplat.utils.ResourceUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerAdapter;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Radio;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.bean.SinglesDownload;
import com.wotingfm.common.config.DbConfig;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.TimeUtils;
import com.wotingfm.common.view.MenuDialog;
import com.wotingfm.common.view.PlayerDialog;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.main.view.MainActivity;
import com.wotingfm.ui.play.activity.albums.AlbumsInfoActivity;
import com.wotingfm.ui.play.look.activity.LookListActivity;

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

/**
 * 作者：xinLong on 2017/6/2 12:15
 * 邮箱：645700751@qq.com
 */
public class PlayerActivity extends NoTitleBarBaseActivity implements View.OnClickListener {


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

    public static void start(Context activity, String albumsId, String serchQ) {
        EventBus.getDefault().postSticky("stop");
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra("albumsId", albumsId);
        intent.putExtra("serchQ", serchQ);
        activity.startActivity(intent);
    }

    public static void start(Context activity, Radio.DataBean.ChannelsBean channelsBean, String serchQ) {
        EventBus.getDefault().postSticky("stop");
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra("channelsBean", channelsBean);
        intent.putExtra("serchQ", serchQ);
        activity.startActivity(intent);
    }

    public static void start(Context activity, SinglesBase singlesBase, String serchQ) {
        EventBus.getDefault().postSticky("stop");
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra("singlesBase", singlesBase);
        intent.putExtra("serchQ", serchQ);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    private Radio.DataBean.ChannelsBean channelsBean;
    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private PLMediaPlayer mMediaPlayer;
    private String mAudioPath;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;

    @Override
    public void initView() {
        mAVOptions = new AVOptions();

        int isLiveStreaming = getIntent().getIntExtra("liveStreaming", 1);
        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);
        // Some optimization with buffering mechanism when be set to 1
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
        if (isLiveStreaming == 1) {
            mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", 0);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        // whether start play automatically after prepared, default value is 1
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        startTelephonyListener();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                getPlayerList("");
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mPlayerAdapter = new PlayerAdapter(getApplicationContext(), singLesBeans);
        mRecyclerView.setAdapter(mPlayerAdapter);
        if (bdPlayer == null)
            bdPlayer = new BDPlayer(getApplicationContext());
        bdPlayer.stopPlayback();
        setListener();
        historyHelper = new HistoryHelper(this);
        Intent intent = getIntent();
        serchQ = intent.getStringExtra("serchQ");
        albumsId = intent.getStringExtra("albumsId");
        loadLayout.showLoadingView();
        SinglesBase singlesBase = (SinglesBase) intent.getSerializableExtra("singlesBase");
        channelsBean = (Radio.DataBean.ChannelsBean) intent.getSerializableExtra("channelsBean");
        if (singlesBase != null) {
            loadLayout.showContentView();
            singLesBeans.clear();
            singLesBeans.add(singlesBase);
            postionPlayer = 0;
            bdPlayer.setVideoPath(singlesBase.single_file_url);
            bdPlayer.start();
            relatiBottom.setVisibility(View.VISIBLE);
            setBeforeOrNext(singlesBase);
            mPlayerAdapter.notifyDataSetChanged();
        } else {
            if (channelsBean != null) {
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
                relatiBottom.setVisibility(View.VISIBLE);
                prepare();
                setBeforeOrNext(s);
                mPlayerAdapter.notifyDataSetChanged();
            } else {
                getPlayerList(albumsId);
            }
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
                txtVideoTotaltime.setText(TimeUtils.formatterTime((bdPlayer.getDuration())) + "");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPlayerCenter:
                GlobalStateConfig.mineFromType = 1;
                GlobalStateConfig.activityA = "C";
                GlobalStateConfig.activityB = "B";
                MainActivity.changeThree();
                Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                Bundle bundle = new Bundle();
                bundle.putInt("viewType", 3);
                push.putExtras(bundle);
                sendBroadcast(push);
                break;
            case R.id.ivPlayerFind:
                LookListActivity.start(this);
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
                if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PLAYING) {
                    bdPlayer.pause();
                    if (mMediaPlayer != null) {
                        mMediaPlayer.pause();
                    }
                    ivPause.setImageResource(R.mipmap.music_play_icon_play);
                } else if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PAUSED) {
                    bdPlayer.start();
                    if (mIsStopped) {
                        prepare();
                    } else {
                        if (mMediaPlayer != null) {
                            mMediaPlayer.start();
                        }
                    }
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
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
                    playerDialog = new PlayerDialog(this);
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
                    menuDialog = new MenuDialog(this);
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
    private String serchQ, albumsId;

    private void getPlayerList(String albums) {
        RetrofitUtils.getInstance().getPlayerList(albums, serchQ)
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
                            bdPlayer.pause();
                            bdPlayer.setVideoPath(sb.single_file_url);
                            bdPlayer.start();
                            relatiBottom.setVisibility(View.VISIBLE);

                            setBeforeOrNext(sb);
                        } else {
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
                        txtVideoStarttime.setText(TimeUtils.formatterTime(bdPlayer.getCurrentPosition()) + "");
                    }
                });//每隔一秒发送数据
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //播放历史
        if (requestCode == 9090 && resultCode == RESULT_OK && data != null) {
            Player.DataBean.SinglesBean sb = (Player.DataBean.SinglesBean) data.getSerializableExtra("singlesBean");
            if (sb != null && singLesBeans != null && !singLesBeans.isEmpty()) {
                for (int i = 0, size = singLesBeans.size(); i < size; i++) {
                    if (sb.id.equals(singLesBeans.get(i).id)) {
                        postionPlayer = i;
                        seekbarVideo.setProgress(0);
                        bdPlayer.stopPlayback();
                        bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
                        bdPlayer.start();
                        mRecyclerView.smoothScrollToPosition(postionPlayer);
                        setBeforeOrNext(singLesBeans.get(postionPlayer));
                        return;
                    }
                }
            }
        }
        //选择专辑
        else if (requestCode == 8080 && resultCode == RESULT_OK && data != null) {
            String albumsId = data.getStringExtra("albumsId");
            if (bdPlayer != null)
                bdPlayer.stopPlayback();
            loadLayout.showLoadingView();
            getPlayerList(albumsId);
        }
        //专辑详情一系列，resultCode =RESULT_OK 代表是选择详情所有节目
        else if (requestCode == 8088 && resultCode == RESULT_OK && data != null) {
            String albumsId = data.getStringExtra("albumsId");
            if (bdPlayer != null)
                bdPlayer.stopPlayback();
            loadLayout.showLoadingView();
            getPlayerList(albumsId);
        }
        //缓存数据 专辑
        else if (requestCode == 6060 && resultCode == RESULT_OK && data != null) {
            if (bdPlayer != null)
                bdPlayer.stopPlayback();
            if (singLesBeans != null)
                singLesBeans.clear();
            List<SinglesDownload> singlesBeanList = (List<SinglesDownload>) data.getSerializableExtra("singles");
            singLesBeans.addAll(singlesBeanList);
            mPlayerAdapter.notifyDataSetChanged();
            if (singLesBeans != null && !singLesBeans.isEmpty()) {
                postionPlayer = 0;
                SinglesBase sb = singLesBeans.get(0);
                bdPlayer.stopPlayback();
                bdPlayer.setVideoPath(sb.single_file_url);
                bdPlayer.start();
                setBeforeOrNext(sb);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bdPlayer != null) {
            bdPlayer.stopPlayback();
        }
    }

    //接受到下载完成的通知
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEventMainThread(String event) {
        L.i("mingku", "event=" + event);
        if (!TextUtils.isEmpty(event) && "stop".equals(event)) {
            if (bdPlayer != null) {
                bdPlayer.stopPlayback();
            }
        } else if (!TextUtils.isEmpty(event) && "pause".equals(event)) {
            if (bdPlayer != null) {
                bdPlayer.pause();
            }
        } else if (!TextUtils.isEmpty(event) && "start".equals(event)) {
            if (bdPlayer != null) {
                bdPlayer.start();
            }
        }
    }


    private Toast mToast = null;

    TelephonyManager mTelephonyManager;
    PhoneStateListener mPhoneStateListener;

    // Listen to the telephone
    private void startTelephonyListener() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopTelephonyListener();
        release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        // mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
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
            mMediaPlayer = new PLMediaPlayer(getApplicationContext(), mAVOptions);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
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
                finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    private void showToastTips(final String tips) {
        if (mIsActivityPaused) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PlayerActivity.this, tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void sendReconnectMessage() {
        showToastTips("缓存中...");
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
                finish();
                return;
            }
            if (!ResourceUtil.isNetworkAvailable(PlayerActivity.this)) {
                sendReconnectMessage();
                return;
            }
            // The PLMediaPlayer has moved to the Error state, if you want to retry, must reset first !
            prepare();
        }
    };
}
