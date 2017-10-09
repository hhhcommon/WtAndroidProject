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

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.TimeUtil;
import com.wotingfm.common.utils.ToastUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.img_bg_head)// 下一首
            ImageView img_bg_head;


    public static SinglesBase singlesBase;
    public static List<SinglesBase> singLesBeans = new ArrayList<>();
    private int positionPlayer = 0; //控制播放下标
    private boolean isFirst = true;//第一次进入

    private View rootView;
    private PlayerPresenter presenter;
    private PlayerAdapter mPlayerAdapter;
    private PlayerDialog playerDialog; //数据源dialog
    private MenuDialog menuDialog;//菜单dialog

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_player, container, false);
            rootView.setOnClickListener(this);
            EventBus.getDefault().register(this);
            ButterKnife.bind(this, rootView);
            inItView();// 设置界面
            setListener();
            presenter = new PlayerPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    public void inItView() {
        // 重新获取数据
//        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLoadingView();
//                presenter.getRecommendedList("郭德纲");
//            }
//        });

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

    public void setDataForLocal(SinglesBase s, int type) {
        if (s != null) {
            showContentView();
            singLesBeans.clear();
            singLesBeans.add(s);
            mPlayerAdapter.notifyDataSetChanged();
            String id = s.id;
            if (!TextUtils.isEmpty(id)) {
                for (int i = 0; i < singLesBeans.size(); i++) {
                    if (!TextUtils.isEmpty(singLesBeans.get(i).id) && singLesBeans.get(i).id.equals(id)) {
                        singLesBeans.get(i).isPlay = true;
                        singlesBase = singLesBeans.get(i);
                    }
                }
            }
            positionPlayer = s.postionPlayer;
            setDataView();
            playMusic();
            if (type == 1) {
                presenter.getRecommendedList(1);
            } else {
                if (!TextUtils.isEmpty(s.album_id)) {
                    presenter.getPlayerList(s.album_id);
                } else {
                    presenter.getRecommendedList(1);
                }
            }
        } else {
            // 获取网络推荐数据
            showLoadingView();
            isFirst = true;
            presenter.getRecommendedList(1);
        }
    }

    // 获取推荐返回数据/获取专辑返回数据
    public void setData(List<SinglesBase> list, int type) {
        if (type == 0) {
            // 剔除当前重复数据
            if (list != null && list.size() > 0) {
                singLesBeans.addAll(0, list);
                mPlayerAdapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(singlesBase.id)) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        if (!TextUtils.isEmpty(singLesBeans.get(i).id) && singLesBeans.get(i).id.equals(singlesBase.id)) {
                            singLesBeans.get(i).isPlay = true;
                            singlesBase = singLesBeans.get(i);
                            positionPlayer = i;
                        }
                    }
                }
                mPlayerAdapter.notifyDataSetChanged();
                setDataView();
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                EventBus.getDefault().post(new MessageEvent(1100));
            }
        } else if (type == 1) {
            // 推荐返回数据
            if (list != null && list.size() > 0) {
                singLesBeans.addAll(list);
                mPlayerAdapter.notifyDataSetChanged();
                showContentView();
                setDataView();
                if (isFirst) {
                    singlesBase = singLesBeans.get(0);
                    positionPlayer = 0;
                    setDataView();
                    playMusic();
                    isFirst = false;
                }
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

                if (!TextUtils.isEmpty(singlesBase.id)) {
                    for (int i = 0; i < singLesBeans.size(); i++) {
                        if (!TextUtils.isEmpty(singLesBeans.get(i).id) && singLesBeans.get(i).id.equals(singlesBase.id)) {
                            singLesBeans.get(i).isPlay = true;
                            singlesBase = singLesBeans.get(i);
                            positionPlayer = i;
                        }
                    }
                }
                mPlayerAdapter.notifyDataSetChanged();
                smoothMoveToPosition(mRecyclerView, positionPlayer);
                showContentView();
                setDataView();
                if (isFirst) {
                    singlesBase = singLesBeans.get(0);
                    positionPlayer = 0;
                    setDataView();
                    playMusic();
                    isFirst = false;
                }
            } else {
                if (singLesBeans != null && singLesBeans.size() > 0) {
                    showContentView();
                } else {
                    showErrorView();
                }
            }
        }
    }

    // 设置按钮样式
    private void setDataView() {
        if (singLesBeans != null && singLesBeans.size() > 0) {
            // 此时有数据
            if (singLesBeans.size() == 1) {
                // 只有一条数据
                ivBefore.setImageResource(R.mipmap.music_play_icon_before_d);
                ivNext.setImageResource(R.mipmap.music_play_icon_next_d);
            } else {
                if (positionPlayer == 0) {
                    // 第一条数据
                    ivBefore.setImageResource(R.mipmap.music_play_icon_before_d);
                    ivNext.setImageResource(R.drawable.icon_play_next);
                } else {
                    if (positionPlayer == singLesBeans.size() - 1) {
                        // 最后一条数据
                        ivBefore.setImageResource(R.drawable.icon_play_before);
                        ivNext.setImageResource(R.mipmap.music_play_icon_next_d);
                    } else {
                        ivBefore.setImageResource(R.drawable.icon_play_before);
                        ivNext.setImageResource(R.drawable.icon_play_next);
                    }
                }
            }
        } else {
            ivBefore.setImageResource(R.mipmap.music_play_icon_before_d);
            ivNext.setImageResource(R.mipmap.music_play_icon_next_d);
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
                    positionPlayer = position;
                    setDataView();
                    singlesBase = singLesBeans.get(positionPlayer);
                    playMusic();
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
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (singlesBase != null && !singlesBase.is_radio) {
                    presenter.seekTo(seekBar.getProgress());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPlayerCenter:
//                GlobalStateConfig.mineFromType = 1;
//                GlobalStateConfig.activityA = "C";
//                GlobalStateConfig.activityB = "B";
                EventBus.getDefault().post(new MessageEvent("three"));
                //   MainActivity.changeThree();
                Intent push = new Intent(BroadcastConstants.MINE_ACTIVITY_CHANGE);
                Bundle bundle = new Bundle();
                bundle.putInt("viewType", 3);
                push.putExtras(bundle);
                getActivity().sendBroadcast(push);
                break;
            case R.id.ivPlayerFind:
//                GlobalStateConfig.mineFromType = 4;
//                GlobalStateConfig.activityA = "A";
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
                setPlayerListDialog();
                break;
            case R.id.ivMore:
                setMenuDialog();
                break;
        }
    }

    // 设置播放列表
    private void setPlayerListDialog() {
        if (playerDialog == null) {
            playerDialog = new PlayerDialog(getActivity());
        }
        if (singLesBeans != null && !singLesBeans.isEmpty()) {
            playerDialog.showPlayDialog(new PlayerDialog.PopPlayCallBack() {
                @Override
                public void play(int position) {
                    positionPlayer = position;
                    setDataView();
                    singlesBase = singLesBeans.get(positionPlayer);
                    mPlayerAdapter.notifyDataSetChanged();
                    playMusic();
                }

                @Override
                public void close(SinglesBase singlesBean) {
                    singLesBeans.remove(singlesBean);
                    mPlayerAdapter.notifyDataSetChanged();
                }

                @Override
                public void getList(int position) {
                    isFirst = true;
                    presenter.getPlayerList(singLesBeans.get(position).album_id);
                }
            });
            playerDialog.show();
        }
    }

    // 功能按钮
    private void setMenuDialog() {
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
                if (!TextUtils.isEmpty(event) && "START".equals(event)) {
                    presenter.playPause();
                } else if (!TextUtils.isEmpty(event) && event.contains("stop&")) {
                    presenter.playPause();
                    initData(event.split("stop&")[1], null, null, null, null);
                } else if (!TextUtils.isEmpty(event) && "PAUSE".equals(event)) {
                    presenter.playPause();
                    ivPause.setImageResource(R.mipmap.music_play_icon_play);
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
            case 2001:// 监听==播放准备好
                playerOnPrepared();
                break;
            case 2002:// 监听==播放完成
                playerOnCompletion();
                break;
            case 2003:// 监听==进度条更改
                setBarProgress();
                break;
            case 2004:// 监听==进度条缓存进度
                Log.e("设置缓存进度", "" + messageEvent.getPercentsAvailable());
                setBarCacheProgress(messageEvent.getPercentsAvailable());
                break;
            case 2005:// 下拉刷新
                presenter.getRecommendedList(0);
                break;
        }
    }

    // 别的界面的跳转数据处理
    private void initData(String albumsId, SinglesBase _singlesBase, ChannelsBean channelsBean, List<SinglesDownload> singlesBeanList, Selected.DataBeanX.DataBean DataBean) {
        if (_singlesBase != null) {
            showContentView();
            singLesBeans.clear();
            singLesBeans.add(_singlesBase);
            positionPlayer = 0;
            setDataView();
            singlesBase = _singlesBase;
            singlesBase.postionPlayer = 0;
            mPlayerAdapter.notifyDataSetChanged();
            playMusic();
            presenter.getRecommendedList(1);
        } else {
            if (singlesBeanList != null) {
                presenter.playPause();
                singLesBeans.clear();
                singLesBeans.addAll(singlesBeanList);
                mPlayerAdapter.notifyDataSetChanged();
                positionPlayer = 0;
                setDataView();
                singlesBase = singLesBeans.get(0);
                playMusic();
            } else {
                if (channelsBean != null) {

                    SinglesBase s = new SinglesBase();
                    s.single_title = channelsBean.title;
                    s.id = channelsBean.id;
                    s.single_logo_url = channelsBean.image_url;
                    s.single_file_url = channelsBean.radio_url;
                    s.had_liked = channelsBean.had_liked;
                    try {
                        s.album_title = channelsBean.play_bill.title;
                    } catch (Exception e) {
                        e.printStackTrace();
                        s.album_title = "直播中";
                    }

                    s.is_radio = true;
                    s.postionPlayer = 0;
                    singLesBeans.clear();
                    singLesBeans.add(s);
                    positionPlayer = 0;
                    setDataView();
                    singlesBase = singLesBeans.get(0);
                    mPlayerAdapter.notifyDataSetChanged();
                    playMusic();
                    presenter.getRecommendedList(1);
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
                        mPlayerAdapter.notifyDataSetChanged();
                        positionPlayer = 0;
                        setDataView();
                        singlesBase = singLesBeans.get(0);
                        playMusic();
                        presenter.getRecommendedList(1);
                    } else {
                        showLoadingView();
                        isFirst = true;
                        presenter.getPlayerList(albumsId);
                    }
                }
            }
        }
    }

    // 设置播放器类型
    private void setPlayer() {
        if (singlesBase != null) {
            if (singlesBase.is_radio) {
                presenter.playerType = 2;
                seekbarVideo.setEnabled(false);
            } else {
                presenter.playerType = 1;
                seekbarVideo.setEnabled(true);
            }
        } else {
            presenter.playerType = 1;
            seekbarVideo.setEnabled(true);
        }
    }

    // 设置当前的播放数据
    private void setIsPlay() {
        for (int i = 0; i < singLesBeans.size(); i++) {
            singLesBeans.get(i).isPlay = false;
        }
        singlesBase.isPlay = true;
    }

    // 执行播放
    private void playMusic() {
        Glide.with(BSApplication.mContext).load(singlesBase.single_logo_url).error(R.mipmap.oval_defut_photo).crossFade(300).into(img_bg_head);
        if (singlesBase.is_radio) {
            presenter.getSeek(singlesBase.id);// 获取节目单
        }

        setPlayer();
        setIsPlay();
        smoothMoveToPosition(mRecyclerView, positionPlayer);
        presenter.playPause();
        presenter.play(singlesBase.single_file_url);
        seekbarVideo.setProgress(0);
        ivPause.setImageResource(R.mipmap.music_play_icon_pause);
        presenter.saveHistory(singlesBase);
        presenter.sendPlay(singlesBase.id);
    }

    // 播放上一首
    private void before() {
        if (singLesBeans.size() > positionPlayer && positionPlayer > 0) {
            positionPlayer = positionPlayer - 1;
            setDataView();
            singlesBase = singLesBeans.get(positionPlayer);
            playMusic();
        }
    }

    // 暂停播放
    private void pause() {
        if (presenter.isPlaying()) {
            presenter.playPause();
            ivPause.setImageResource(R.mipmap.music_play_icon_play);
        } else {
            presenter.start();
            ivPause.setImageResource(R.mipmap.music_play_icon_pause);
        }
    }

    // 播放下一首
    private void next() {
        if (positionPlayer < singLesBeans.size() - 1) {
            positionPlayer = positionPlayer + 1;
            setDataView();
            singlesBase = singLesBeans.get(positionPlayer);
            playMusic();
        } else {
            // 播放完成推荐逻辑，待实现
            ToastUtils.show_always(this.getActivity(), "播放完成推荐逻辑，待实现");
        }
    }

    /**
     * 播放器准备好
     */
    public void playerOnPrepared() {
        // 设置内容部分进度条
        if (singlesBase != null && !singlesBase.is_radio) {
            presenter.seekTo(singlesBase.play_time);
            setMaxForSingle(presenter.getDuration(), TimeUtil.formatterTime(presenter.getCurrentPosition()), TimeUtil.formatterTime((presenter.getDuration())));
            setBarProgress();
        }
    }

    // 设置内容进度条展示数据
    public void setMaxForSingle(int max, String start, String end) {
        seekbarVideo.setMax(max);
        Log.e("设置内容进度条最大值", "" + max);
        txtVideoStarttime.setText(start + "");
        txtVideoTotaltime.setText(end + "");
    }

    // 设置电台进度条展示数据
    public void setMaxForRadio( String title,  String start, String end) {
        seekbarVideo.setMax(seekbarVideo.getMax());
        Log.e("设置电台进度条最大值", "" + seekbarVideo.getMax());
        txtVideoStarttime.setText(start + "");
        txtVideoTotaltime.setText(end + "");
    }

    // 设置进度条以及数据保存
    private void setBarProgress() {
        if (singlesBase != null && !singlesBase.is_radio) {
            setBarProgressForSingle();
        } else if (singlesBase != null && singlesBase.is_radio) {
            setBarProgressForRadio();
        }
    }

    // 内容进度条缓存进度
    private void setBarCacheProgress(int percentsAvailable) {
        if (percentsAvailable == -1) {
            seekbarVideo.setSecondaryProgress(seekbarVideo.getMax());
        } else if (percentsAvailable == -100) {
            seekbarVideo.setSecondaryProgress(seekbarVideo.getMax());
        } else {
            double i = percentsAvailable * seekbarVideo.getMax() / 100;
            int b = (int) i;
            seekbarVideo.setSecondaryProgress(b);
            Log.e("设置缓存进度", "已设置： " + b);
        }
    }

    // 设置电台进度条
    private void setBarProgressForRadio() {
        if (singlesBase != null && singlesBase.is_radio) {
            singlesBase.play_time = 0;
            presenter.save(singlesBase);
            int pro = presenter.getIng() * seekbarVideo.getMax() / presenter.getMax(presenter.getStartTime(), presenter.getEndTime());
            if(pro==100) presenter.getSeek(singlesBase.id);
            seekbarVideo.setProgress(pro);
            txtVideoStarttime.setText(presenter.getIngTime() + "");
            Log.e("电台播放进度", "" + pro);
        }
    }

    // 设置内容进度条
    private void setBarProgressForSingle() {
        if (singlesBase != null && !singlesBase.is_radio) {
            singlesBase.play_time = presenter.getCurrentPosition();
            presenter.save(singlesBase);
            seekbarVideo.setProgress(presenter.getCurrentPosition());
            txtVideoStarttime.setText(TimeUtil.formatterTime(presenter.getCurrentPosition()) + "");
            Log.e("内容播放进度", "" + presenter.getCurrentPosition());
        }
    }

    /**
     * 播放完成，播放下一首
     */
    public void playerOnCompletion() {
        if(singlesBase!=null&&!singlesBase.is_radio){
            next();
        }
    }

    public void showContentView() {
//        loadLayout.showContentView();
    }

    public void showEmptyView() {
//        loadLayout.showEmptyView();
    }

    public void showLoadingView() {
//        loadLayout.showLoadingView();
    }

    public void showErrorView() {
//        loadLayout.showErrorView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        playerDialog.destroy();
    }

}
