package com.wotingfm.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
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
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.common.view.MenuDialog;
import com.wotingfm.common.view.PlayerDialog;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.main.view.MainActivity;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public void initView() {

        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                getPlayerList();
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
        bdPlayer = new BDPlayer(getApplicationContext());
        setListener();

        getPlayerList();

    }

    private void setBeforeOrNext() {
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
    }

    private void setListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int postion = manager.findLastCompletelyVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && postion != postionPlayer) {
                    seekbarVideo.setProgress(0);
                    postionPlayer = postion;
                    bdPlayer.stopPlayback();
                    bdPlayer.setVideoPath(singLesBeans.get(postionPlayer).single_file_url);
                    bdPlayer.start();
                    setBeforeOrNext();
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
                    setBeforeOrNext();
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
                GlobalStateConfig.mineFromType=1;
                GlobalStateConfig.activityA="C";
                GlobalStateConfig.activityB="B";
                MainActivity.changeThree();
                Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                Bundle bundle = new Bundle();
                bundle.putInt("viewType", 3);
                push.putExtras(bundle);
                sendBroadcast(push);
                break;
            case R.id.ivPlayerFind:

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
                    setBeforeOrNext();
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
                    setBeforeOrNext();
                }
                break;
            case R.id.ivPlayList:
                if (playerDialog == null) {
                    playerDialog = new PlayerDialog(this);
                }
                if (singLesBeans != null && !singLesBeans.isEmpty()) {
                    playerDialog.showPlayDialo(singLesBeans, singLesBeans.get(postionPlayer).id, new PlayerDialog.PopPlayCallBack() {
                        @Override
                        public void play(Player.DataBean.SinglesBean singlesBean, int postion) {
                            bdPlayer.stopPlayback();
                            bdPlayer.setVideoPath(singlesBean.single_file_url);
                            postionPlayer = postion;
                            bdPlayer.start();
                        }

                        @Override
                        public void close(Player.DataBean.SinglesBean singlesBean) {
                            singLesBeans.remove(singlesBean);
                            mPlayerAdapter.notifyDataSetChanged();
                            setBeforeOrNext();
                        }
                    });
                    playerDialog.show();
                }
                break;
            case R.id.ivMore:
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(this);
                }
                menuDialog.show();
                break;
        }
    }

    //控制播放下标
    private int postionPlayer = 0;
    private BDPlayer bdPlayer;
    private PlayerAdapter mPlayerAdapter;
    private List<Player.DataBean.SinglesBean> singLesBeans = new ArrayList<>();
    //数据源dialog
    private PlayerDialog playerDialog;
    //菜单dialohg
    private MenuDialog menuDialog;

    private void getPlayerList() {
        RetrofitUtils.getInstance().getPlayerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Player.DataBean.SinglesBean>>() {
                    @Override
                    public void call(List<Player.DataBean.SinglesBean> singls) {
                        if (singls != null && !singls.isEmpty()) {
//                            loadLayout.showContentView();
//                            singLesBeans.clear();
//                            singLesBeans.addAll(singls);
//                            mPlayerAdapter.notifyDataSetChanged();
//                            postionPlayer = 0;
//                            bdPlayer.setVideoPath(singls.get(0).single_file_url);
//                            bdPlayer.start();
//                            relatiBottom.setVisibility(View.VISIBLE);
//                            setBarProgrees();
//                            setBeforeOrNext();
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
                        txtVideoStarttime.setText(TimeUtil.formatterTime(bdPlayer.getCurrentPosition()) + "");
                    }
                });//每隔一秒发送数据
    }
}
