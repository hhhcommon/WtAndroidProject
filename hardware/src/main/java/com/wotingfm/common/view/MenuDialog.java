package com.wotingfm.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerListAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.config.DbConfig;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.database.HistoryHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.SDCardUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterActivity;
import com.wotingfm.ui.play.activity.MeSubscribeListActivity;
import com.wotingfm.ui.play.activity.PlayerHistoryActivity;
import com.wotingfm.ui.play.activity.ReportsPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.woting.commonplat.gather.GatherData.url;
import static com.wotingfm.R.id.mRecyclerViewList;

//首页菜单dialog
public class MenuDialog extends Dialog implements View.OnClickListener {

    private TextView tvClose, tvLike, tvAlbums, tvAnchor, tvReport, tvDownload, tvAgo, tvSubscription, tvLocal;
    private Activity activity;

    public MenuDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialog);
        this.activity = context;
        setContentView(R.layout.player_menu_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);

        tvClose = (TextView) findViewById(R.id.tvClose);
        tvLike = (TextView) findViewById(R.id.tvLike);
        tvAlbums = (TextView) findViewById(R.id.tvAlbums);
        tvAnchor = (TextView) findViewById(R.id.tvAnchor);
        tvReport = (TextView) findViewById(R.id.tvReport);
        tvDownload = (TextView) findViewById(R.id.tvDownload);
        tvAgo = (TextView) findViewById(R.id.tvAgo);
        tvSubscription = (TextView) findViewById(R.id.tvSubscription);
        tvLocal = (TextView) findViewById(R.id.tvLocal);
        tvClose.setOnClickListener(this);
        tvLike.setOnClickListener(this);
        tvAlbums.setOnClickListener(this);
        tvAnchor.setOnClickListener(this);
        tvReport.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
        tvAgo.setOnClickListener(this);
        tvSubscription.setOnClickListener(this);
        tvLocal.setOnClickListener(this);
    }

    private Player.DataBean.SinglesBean pdsBase;
    private FollowCallBack followCallBack;

    public void setMenuData(Player.DataBean.SinglesBean pds, FollowCallBack followCallBack) {
        this.followCallBack = followCallBack;
        if (pds != null) {
            pdsBase = pds;
            setLikeImg(pds.had_liked);
        }
    }

    private void setLikeImg(boolean hadLike) {
        if (hadLike == true) {
            Drawable drawable = getContext().getResources().getDrawable(R.mipmap
                    .music_play_icon_like_s);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getContext().getResources().getDrawable(R.mipmap
                    .music_play_icon_like_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(null, drawable, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDownload:
                if (pdsBase != null) {
                    final DownloadHelper downloadHelper = new DownloadHelper(BSApplication.getInstance());
                    if (downloadHelper != null) {
                        List<Player.DataBean.SinglesBean> singlesBeens = downloadHelper.findPlayHistoryList();
                        if (singlesBeens != null && !singlesBeens.isEmpty()) {
                            for (Player.DataBean.SinglesBean s : singlesBeens) {
                                if (s.id.equals(pdsBase.id)) {
                                    com.wotingfm.common.utils.T.getInstance().equals("已下载");
                                    return;
                                }
                            }
                        }
                        File saveFile = new File(SDCardUtils.getSDPath() + DbConfig.ALBUMS + "/" + pdsBase.single_file_url);
                        HttpRequest.download(pdsBase.single_file_url, saveFile, new FileDownloadCallback() {
                            //开始下载
                            @Override
                            public void onStart() {
                                super.onStart();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("id", pdsBase.id);
                                contentValues.put("isPlay", pdsBase.isPlay);
                                contentValues.put("single_title", pdsBase.single_title);
                                contentValues.put("play_time", System.currentTimeMillis());
                                contentValues.put("single_logo_url", pdsBase.single_logo_url);
                                contentValues.put("single_file_url", pdsBase.single_file_url);
                                contentValues.put("album_title", pdsBase.album_title);
                                downloadHelper.insertTotable(pdsBase.id, contentValues);

                            }

                            //下载进度
                            @Override
                            public void onProgress(int progress, long networkSpeed) {
                                super.onProgress(progress, networkSpeed);
                                //String speed = FileUtils.generateFileSize(networkSpeed);
                            }

                            //下载失败
                            @Override
                            public void onFailure() {
                                super.onFailure();
                                com.wotingfm.common.utils.T.getInstance().showToast("下载失败");
                            }

                            //下载完成（下载成功）
                            @Override
                            public void onDone() {
                                super.onDone();
                                com.wotingfm.common.utils.T.getInstance().showToast("下载成功");
                            }
                        });
                    }
                }
                break;
            case R.id.tvAnchor:
                //测试userid
                if (pdsBase != null) {
                    dismiss();
                    AnchorPersonalCenterActivity.start(activity, pdsBase.creator_id);
                }
                break;
            case R.id.tvSubscription:
                dismiss();
                MeSubscribeListActivity.start(activity);
                break;
            case R.id.tvReport:
                if (pdsBase != null) {
                    dismiss();
                    ReportsPlayerActivity.start(activity, pdsBase.id, "REPORT_SINGLE");
                }
                break;
            case R.id.tvAgo:
                dismiss();
                PlayerHistoryActivity.start(activity);
                break;
            case R.id.tvClose:
                dismiss();
                break;
            case R.id.tvLike:
                if (pdsBase != null) {
                    if (pdsBase.had_liked == true) {
                        unfollowPlayer();
                    } else {
                        followPlayer();
                    }
                }
                break;
        }
    }

    private void followPlayer() {
        RetrofitUtils.getInstance().postFollowUser(pdsBase.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        if (followCallBack != null) {
                            pdsBase.had_liked = true;
                            Toast.makeText(BSApplication.getInstance(), "收藏成功", Toast.LENGTH_LONG)
                                    .show();
                            followCallBack.followPlayer(pdsBase);
                            setLikeImg(pdsBase.had_liked);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(BSApplication.getInstance(), "收藏失败", Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void unfollowPlayer() {
        RetrofitUtils.getInstance().postUnfollowUser(pdsBase.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        if (followCallBack != null) {
                            pdsBase.had_liked = false;
                            Toast.makeText(BSApplication.getInstance(), "取消收藏成功", Toast.LENGTH_LONG)
                                    .show();
                            followCallBack.followPlayer(pdsBase);
                            setLikeImg(pdsBase.had_liked);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(BSApplication.getInstance(), "取消收藏失败", Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    public interface FollowCallBack {
        void followPlayer(Player.DataBean.SinglesBean psb);
    }

}
