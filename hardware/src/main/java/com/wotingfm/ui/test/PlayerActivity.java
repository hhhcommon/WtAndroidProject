package com.wotingfm.ui.test;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.woting.commonplat.player.baidu.BDPlayer;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerAdapter;
import com.wotingfm.common.bean.Player;
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

    public static void start(Context activity, String albumsId, String serchQ) {
        EventBus.getDefault().postSticky("stop");
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra("albumsId", albumsId);
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

    @Override
    public void initView() {
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
        if (singlesBase != null) {
            loadLayout.showContentView();
            singLesBeans.clear();
            singLesBeans.add(singlesBase);
            postionPlayer = 0;
            bdPlayer.pause();
            bdPlayer.setVideoPath(singlesBase.single_file_url);
            bdPlayer.start();
            relatiBottom.setVisibility(View.VISIBLE);
            setBeforeOrNext(singlesBase);
            mPlayerAdapter.notifyDataSetChanged();
        } else {
            getPlayerList(albumsId);
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
                    ivPause.setImageResource(R.mipmap.music_play_icon_play);
                } else if (bdPlayer.getCurrentPlayerState() == isPause.STATE_PAUSED) {
                    bdPlayer.start();
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
                    });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
