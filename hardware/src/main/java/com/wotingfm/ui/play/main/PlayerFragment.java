package com.wotingfm.ui.play.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.pili.pldroid.player.PlayerState;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.Selected;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.bean.SinglesDownload;
import com.wotingfm.ui.play.main.adapter.PlayerAdapter;
import com.wotingfm.ui.play.main.presenter.PlayerPresenter;
import com.wotingfm.ui.play.main.view.MenuDialog;
import com.wotingfm.ui.play.main.view.PlayerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：xinLong on 2017/6/2 12:15
 * 邮箱：645700751@qq.com
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.ivPlayerCenter)// 个人中心
            ImageView ivPlayerCenter;
    @BindView(R.id.ivPlayerFind)// 发现按钮
            ImageView ivPlayerFind;
    @BindView(R.id.relatiBottom)// 底部按钮
            RelativeLayout relatiBottom;
    @BindView(R.id.lin_PlayList)// 播放列表
            LinearLayout lin_PlayList;
    @BindView(R.id.ivMore)// 功能按钮
            ImageView ivMore;
    @BindView(R.id.img_bg)// 图片背景
            ImageView img_bg;
    @BindView(R.id.seekbar_video)// 进度条
            SeekBar seekbarVideo;
    @BindView(R.id.txt_video_starttime)// 当前时长
            TextView txtVideoStarttime;
    @BindView(R.id.txt_video_totaltime)// 总时长
            TextView txtVideoTotaltime;

    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.LoadingView)
    LinearLayout mLoadingView;

    @BindView(R.id.ivBefore)// 上一首
            ImageView ivBefore;
    @BindView(R.id.ivPause)// 暂停
            ImageView ivPause;
    @BindView(R.id.ivNext)// 下一首
            ImageView ivNext;

    private SinglesBase singlesBase;
    public static List<SinglesBase> singLesBeans = new ArrayList<>();
    private int positionPlayer = 0; //控制播放下标

    private View rootView;
    private PlayerPresenter presenter;
    private PlayerAdapter mPlayerAdapter;
    private PlayerDialog playerDialog; //数据源dialog
    private MenuDialog menuDialog;//菜单dialog

    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_player, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            EventBus.getDefault().register(this);
            inItView();// 设置界面
            presenter = new PlayerPresenter(this);
            getData();// 获取数据
            setListener();
        }
        return rootView;
    }

    private void getData() {
        SinglesBase s = presenter.getData();
        List<SinglesBase> list = presenter.getDataList();
        if (list != null && list.size() > 0) {
            showContentView();
            singLesBeans.clear();
            singLesBeans.addAll(list);
            mPlayerAdapter.notifyDataSetChanged();

            if (s != null) {
                String id=s.id;
                if(!TextUtils.isEmpty(id)){
                    for(int i=0;i<singLesBeans.size();i++){
                        if(!TextUtils.isEmpty(singLesBeans.get(i).id)&&singLesBeans.get(i).id.equals(id)){
                            singLesBeans.get(i).isPlay=true;
                            singlesBase = singLesBeans.get(i);
                        }
                    }
                }
                positionPlayer = s.postionPlayer;
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                presenter.play(s.single_file_url);
            } else {
                singlesBase = list.get(0);
                positionPlayer = 0;
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                presenter.play(singlesBase.single_file_url);
            }
            setDataView();
            setDataListView();
        } else {
            if (s != null) {
                showContentView();
                singLesBeans.clear();
                singLesBeans.add(s);
                mPlayerAdapter.notifyDataSetChanged();
                String id=s.id;
                if(!TextUtils.isEmpty(id)){
                    for(int i=0;i<singLesBeans.size();i++){
                        if(!TextUtils.isEmpty(singLesBeans.get(i).id)&&singLesBeans.get(i).id.equals(id)){
                            singLesBeans.get(i).isPlay=true;
                            singlesBase = singLesBeans.get(i);
                        }
                    }
                }
                positionPlayer = s.postionPlayer;
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                presenter.play(s.single_file_url);
                setDataView();
                setDataListView();
            } else {
                // 获取网络推荐数据
                showLoadingView();
                presenter.getRecommendedList("郭德纲");
            }
        }
    }

    public void inItView() {
        // 重新获取数据
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                presenter.getRecommendedList("郭德纲");
            }
        });

        // 设置list界面
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(mRecyclerView);
        mPlayerAdapter = new PlayerAdapter(BSApplication.getInstance(), singLesBeans);
        mRecyclerView.setAdapter(mPlayerAdapter);

        // 设置背景大小
        int with = DementionUtil.getScreenWidthInPx(this.getActivity()) - DementionUtil.dip2px(this.getActivity(), 80);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(with, with);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        img_bg.setLayoutParams(params);//将设置好的布局参数应用到控件中
    }

    // 获取推荐返回数据/获取专辑返回数据
    public void setData(List<SinglesBase> list, int type) {
        if (type == 1) {
            // 推荐返回数据
            // 剔除当前重复数据
            if (list != null && list.size() > 0) {
                if (singlesBase != null && singlesBase.id != null) {
                    for (int i = 0; i < list.size(); i++) {
                        String id = list.get(i).id;
                        if (!TextUtils.isEmpty(id) && id.equals(singlesBase.id)) {
                            list.remove(i);
                            break;
                        }
                    }
                }
                singLesBeans.addAll(list);

                presenter.saveUtilList(singLesBeans);
                mPlayerAdapter.notifyDataSetChanged();
                showContentView();
                singlesBase = singLesBeans.get(0);
                positionPlayer = 0;
                setIsPlay();
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                setDataView();
                setDataListView();
            } else {
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    showContentView();
                } else {
                    showErrorView();
                }
            }
        } else {
            // 专辑返回数据
            if (list != null && list.size() > 0) {
                singLesBeans.clear();
                singLesBeans.addAll(list);
                presenter.saveUtilList(singLesBeans);
                mPlayerAdapter.notifyDataSetChanged();
                showContentView();
                singlesBase = singLesBeans.get(0);
                positionPlayer = 0;
                setIsPlay();
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                setDataView();
                setDataListView();
            } else {
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    showContentView();
                } else {
                    showErrorView();
                }
            }
        }
    }

    private void setDataView() {
        if (singlesBase != null) {

        }
    }

    private void setDataListView() {
    }

    private void initData(String albumsId, SinglesBase _singlesBase, ChannelsBean channelsBean, List<SinglesDownload> singlesBeanList, Selected.DataBeanX.DataBean DataBean) {
        if (_singlesBase != null) {
            loadLayout.showContentView();
            singLesBeans.clear();
            singLesBeans.add(_singlesBase);
            presenter.saveUtilList(singLesBeans);
            positionPlayer = 0;
            singlesBase = _singlesBase;
            singlesBase.postionPlayer = 0;
            mPlayerAdapter.notifyDataSetChanged();
            smoothMoveToPosition(mRecyclerView, positionPlayer);
            presenter.playPause();
            presenter.play(singlesBase.single_file_url);
            presenter.getRecommendedList(singlesBase.single_title);
        } else {
            if (singlesBeanList != null) {
                presenter.playPause();
                singLesBeans.clear();
                singLesBeans.addAll(singlesBeanList);
                presenter.saveUtilList(singLesBeans);
                mPlayerAdapter.notifyDataSetChanged();
                positionPlayer = 0;
                singlesBase = singLesBeans.get(0);
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                presenter.playPause();
                presenter.play(singlesBase.single_file_url);
            } else {
                if (channelsBean != null) {

                    SinglesBase s = new SinglesBase();
                    s.single_title = channelsBean.title;
                    s.id = channelsBean.id;
                    s.single_logo_url = channelsBean.image_url;
                    s.single_file_url = channelsBean.radio_url;
                    s.album_title = channelsBean.desc;
                    s.is_radio = true;
                    s.postionPlayer = 0;
                    singLesBeans.clear();
                    singLesBeans.add(s);
                    presenter.saveUtilList(singLesBeans);
                    positionPlayer = 0;
                    singlesBase = singLesBeans.get(0);
                    mPlayerAdapter.notifyDataSetChanged();
                    smoothMoveToPosition(mRecyclerView, positionPlayer);
                    presenter.playPause();
                    presenter.play(singlesBase.single_file_url);
                    presenter.getRecommendedList(singlesBase.single_title);
                } else {
                    if (DataBean != null) {
                        singLesBeans.clear();
                        SinglesBase s = new SinglesBase();
                        s.single_title = DataBean.single_title;
                        s.id = DataBean.id;
                        s.album_id = DataBean.album_id;
                        s.single_logo_url = DataBean.single_logo_url;
                        s.single_file_url = DataBean.single_file_url;
                        s.album_title = DataBean.album_title;
                        s.creator_id = DataBean.creator_id;
                        s.is_radio = false;
                        s.postionPlayer = 0;
                        singLesBeans.add(s);
                        presenter.saveUtilList(singLesBeans);
                        mPlayerAdapter.notifyDataSetChanged();
                        positionPlayer = 0;
                        singlesBase = singLesBeans.get(0);
                        smoothMoveToPosition(mRecyclerView, positionPlayer);
                        presenter.playPause();
                        presenter.play(singlesBase.single_file_url);
                        presenter.getRecommendedList(singlesBase.single_title);
                    } else {
                        showLoadingView();
                        presenter.getPlayerList(albumsId);
                    }
                }
            }
        }
    }

    private void setListener() {
        // 左右滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = manager.findLastCompletelyVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && position != positionPlayer && position >= 0) {
                    Log.e("mRecyclerView", "滚动");
                    seekbarVideo.setProgress(0);
                    positionPlayer = position;
                    singlesBase = singLesBeans.get(positionPlayer);
                    setIsPlay();
                    smoothMoveToPosition(mRecyclerView, positionPlayer);
                    presenter.playPause();
                    presenter.play(singlesBase.single_file_url);
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                }
            }
        });
        // 进度条监听
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
                presenter.seekTo(seekBar.getProgress());
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

    private void setIsPlay() {
        for (int i = 0; i < singLesBeans.size(); i++) {
            singLesBeans.get(i).isPlay = false;
        }
        singlesBase.isPlay = true;
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
                pause();
                break;
            case R.id.ivNext:
                next();
                break;
            case R.id.lin_PlayList:
                if (playerDialog == null) {
                    playerDialog = new PlayerDialog(getActivity());
                }
                if (singLesBeans != null && !singLesBeans.isEmpty()) {
                    playerDialog.showPlayDialog( new PlayerDialog.PopPlayCallBack() {
                        @Override
                        public void play(int position) {
                            positionPlayer = position;
                            mPlayerAdapter.notifyDataSetChanged();
                            smoothMoveToPosition(mRecyclerView, positionPlayer);
                            singlesBase = singLesBeans.get(positionPlayer);
                            presenter.playPause();
                            presenter.play(singlesBase.single_file_url);
                            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                            seekbarVideo.setProgress(0);
                        }

                        @Override
                        public void close(SinglesBase singlesBean) {
                            singLesBeans.remove(singlesBean);
                            mPlayerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void getList(int position) {
                            showLoadingView();
                            presenter.getPlayerList(singLesBeans.get(position).album_id);
                        }
                    });
                    playerDialog.show();
                }
                break;
            case R.id.ivMore:
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getActivity());
                }
                if (singlesBase != null)
                    menuDialog.setMenuData(singlesBase, new MenuDialog.FollowCallBack() {
                        @Override
                        public void followPlayer(SinglesBase psb) {
                            singLesBeans.set(positionPlayer, psb);
                            mPlayerAdapter.notifyDataSetChanged();
                        }
                    });
                menuDialog.show();
                break;
        }
    }


    private boolean isChanging = false;//互斥变量，防止定时器与SeekBar拖动时进度冲突

    public void setBarProgress() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (isChanging == true) {
                            return;
                        }
//                        if (singlesBase != null) {
//                            singlesBase.play_time = presenter.getCurrentPosition();
//                            singlesBase.postionPlayer = positionPlayer;
//                            presenter.saveUtil(singlesBase);
//                        }
//                        seekbarVideo.setProgress(presenter.getCurrentPosition());
//                        txtVideoStarttime.setText(TimeUtil.formatterTime(presenter.getCurrentPosition()) + "");
                    }
                });//每隔一秒发送数据
    }

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
//        // 第一个可见位置
//        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
//        // 最后一个可见位置
//        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
//
//        Log.e("位置：","开始执行移动命令");
//        Log.e("位置：","firstItem=="+firstItem);
//        Log.e("位置：","lastItem=="+lastItem);
//        Log.e("位置：","position=="+position);
//
//        if (position < firstItem) {
//            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
//            mRecyclerView.smoothScrollToPosition(position);
//            Log.e("位置：","移动命令《1》");
//        } else if (position <= lastItem) {
//            // 跳转位置在第一个可见项之后，最后一个可见项之前
//            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
//            int movePosition = position - firstItem;
//            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
//                int top = mRecyclerView.getChildAt(movePosition).getTop();
//                mRecyclerView.smoothScrollBy(0, top);
//            }
//            Log.e("位置：","移动命令《2》");
//        } else {
//            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
//            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
//            mRecyclerView.smoothScrollToPosition(position);
//            Log.e("位置：","移动命令《3》");
//        }
//        Log.e("位置：","移动命令结束");
        mRecyclerView.smoothScrollToPosition(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEventBase(MessageEvent messageEvent) {
        String event = messageEvent.getMessage();
        int type = messageEvent.getType();
        switch (type) {
            case 0:
                if (!TextUtils.isEmpty(event) && "stop".equals(event)) {
                    presenter.playPause();
                } else if (!TextUtils.isEmpty(event) && event.contains("stop&")) {
                    presenter.playPause();
                    initData(event.split("stop&")[1], null, null, null, null);
                } else if (!TextUtils.isEmpty(event) && "pause".equals(event)) {
                    presenter.playPause();
                    ivPause.setImageResource(R.mipmap.music_play_icon_play);
                } else if (!TextUtils.isEmpty(event) && "start".equals(event)) {
                    presenter.start();
                    ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                } else if (!TextUtils.isEmpty(event) && "step".equals(event)) {
                    before();
                } else if (!TextUtils.isEmpty(event) && "next".equals(event)) {
                    next();
                } else if (!TextUtils.isEmpty(event) && "stop_or_star".equals(event)) {
//                    BDPlayer.PlayerState isPause = presenter.getCurrentPlayerState();
//                    if (isPause == isPause.STATE_PLAYING) {
//                        presenter.playPause();
//                        ivPause.setImageResource(R.mipmap.music_play_icon_play);
//                    } else if (isPause == isPause.STATE_PAUSED) {
//                        presenter.start();
//                        ivPause.setImageResource(R.mipmap.music_play_icon_pause);
//                    }

                    PlayerState isPause = (PlayerState) presenter.getCurrentPlayerState();
                    if (isPause == isPause.PLAYING) {
                        presenter.playPause();
                        ivPause.setImageResource(R.mipmap.music_play_icon_play);
                    } else if (isPause ==isPause.PAUSED) {
                        presenter.start();
                        ivPause.setImageResource(R.mipmap.music_play_icon_pause);
                    }

                }
                break;
            case 1:
                initData(null, null, messageEvent.getChannelsBean(), null, null);
                break;
            case 2:
                initData(null, messageEvent.getSinglesBase(), null, null, null);
                break;
            case 3:
                initData(null, null, null, messageEvent.getSinglesDownloads(), null);
                break;
            case 4:
                initData(null, null, null, null, messageEvent.getDataBean());
                break;
        }
    }

    public void before() {
        if (singLesBeans.size() > positionPlayer && positionPlayer > 0) {
            positionPlayer = positionPlayer - 1;
            singlesBase = singLesBeans.get(positionPlayer);
            setIsPlay();
            smoothMoveToPosition(mRecyclerView,positionPlayer);
            presenter.playPause();
            presenter.play(singlesBase.single_file_url);
            seekbarVideo.setProgress(0);
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
        }
    }

    public void pause() {
//        BDPlayer.PlayerState isPause = presenter.getCurrentPlayerState();
//        if (isPause == isPause.STATE_PLAYING) {
//            presenter.playPause();
//            ivPause.setImageResource(R.mipmap.music_play_icon_play);
//        } else if (isPause == isPause.STATE_PAUSED) {
//            presenter.start();
//            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
//        }

        PlayerState isPause = (PlayerState) presenter.getCurrentPlayerState();
        if (isPause == isPause.PLAYING) {
            presenter.playPause();
            ivPause.setImageResource(R.mipmap.music_play_icon_play);
        } else if (isPause ==isPause.PAUSED) {
            presenter.start();
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
        }
    }

    public void next() {
        if (positionPlayer < singLesBeans.size() - 1) {
            positionPlayer = positionPlayer + 1;
            singlesBase = singLesBeans.get(positionPlayer);
            setIsPlay();
            smoothMoveToPosition(mRecyclerView,positionPlayer);
            presenter.playPause();
            presenter.play(singlesBase.single_file_url);
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
            seekbarVideo.setProgress(0);
        }
    }

    /**
     * 播放器准备好
     */
    public void playerOnPrepared() {
        if (singlesBase != null) {
            presenter.seekTo((int) singlesBase.play_time);
        } else {
            presenter.seekTo(0);
        }
        seekbarVideo.setMax(presenter.getDuration());
        txtVideoTotaltime.setText(TimeUtil.formatterTime((presenter.getDuration())) + "");
        setBarProgress();
    }

    public void playerOnCompletion() {
        next();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showContentView() {
        loadLayout.showContentView();
    }

    public void showEmptyView() {
        loadLayout.showEmptyView();
    }

    public void showLoadingView() {
        loadLayout.showLoadingView();
    }

    public void showErrorView() {
        loadLayout.showErrorView();
    }
}
