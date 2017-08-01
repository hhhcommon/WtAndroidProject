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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woting.commonplat.utils.FileSizeUtil;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerListAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.ChannelsBean;
import com.wotingfm.common.bean.MessageEvent;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.bean.Radio;
import com.wotingfm.common.bean.SinglesBase;
import com.wotingfm.common.bean.SinglesDownload;
import com.wotingfm.common.config.DbConfig;
import com.wotingfm.common.database.DownloadHelper;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.SDCardUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.activity.MeSubscribeListFragment;
import com.wotingfm.ui.play.activity.PlayerHistoryFragment;
import com.wotingfm.ui.play.activity.ReportsPlayerFragment;
import com.wotingfm.ui.play.activity.albums.AlbumsInfoFragmentMain;
import com.wotingfm.ui.play.activity.download.DownloadProgramFragment;
import com.wotingfm.ui.play.radio.RadioInfoFragment;
import com.wotingfm.ui.test.PlayerActivity;
import com.wotingfm.ui.user.login.view.LoginFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.toolsfinal.io.FileUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static cn.jpush.android.api.JPushInterface.a.r;
import static cn.jpush.android.api.JPushInterface.a.t;
import static com.woting.commonplat.gather.GatherData.url;
import static com.wotingfm.R.id.mRecyclerViewList;

//首页菜单dialog
public class MenuDialog extends Dialog implements View.OnClickListener {

    private TextView tvClose, tvLike, tvAlbums, tvAnchor, tvReport, tvDownload, tvAgo, tvSubscription, tvLocal, tvRadio;
    private PlayerActivity activity;
    private MineActivity activityMain;
    private InterPhoneActivity interPhoneActivity;
    private LinearLayout largeLabel, largeLabel2;

    public MenuDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialog);
        if (context instanceof PlayerActivity)
            this.activity = (PlayerActivity) context;
        else if (context instanceof MineActivity)
            this.activityMain = (MineActivity) context;
        else if (context instanceof InterPhoneActivity)
            this.interPhoneActivity = (InterPhoneActivity) context;
        setContentView(R.layout.player_menu_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);

        tvClose = (TextView) findViewById(R.id.tvClose);
        tvLike = (TextView) findViewById(R.id.tvLike);
        tvRadio = (TextView) findViewById(R.id.tvRadio);
        tvAlbums = (TextView) findViewById(R.id.tvAlbums);
        largeLabel = (LinearLayout) findViewById(R.id.largeLabel);
        largeLabel2 = (LinearLayout) findViewById(R.id.largeLabel2);
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
        tvRadio.setOnClickListener(this);
        tvLocal.setOnClickListener(this);
    }

    private SinglesBase pdsBase;
    private FollowCallBack followCallBack;
    private ChannelsBean channelsBean;

    public void setMenuData(SinglesBase pds, FollowCallBack followCallBack, ChannelsBean channelsBean) {
        this.followCallBack = followCallBack;
        this.channelsBean = channelsBean;
        if (pds != null) {
            pdsBase = pds;
            setLikeImg(pds.had_liked);
            if (channelsBean != null || pds.is_radio == true) {
                largeLabel.setVisibility(View.GONE);
                largeLabel2.setVisibility(View.VISIBLE);
            } else {
                largeLabel.setVisibility(View.VISIBLE);
                largeLabel2.setVisibility(View.GONE);
            }
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
        BSApplication.IS_RESULT = false;
        switch (v.getId()) {
            case R.id.tvDownload:
                if (pdsBase != null) {
                    final DownloadHelper downloadHelper = new DownloadHelper(BSApplication.getInstance());
                    if (downloadHelper != null) {
                        dismiss();
                        List<SinglesDownload> singlesBeens = downloadHelper.findPlayHistoryList();
                        if (singlesBeens != null && !singlesBeens.isEmpty()) {
                            for (SinglesDownload s : singlesBeens) {
                                if (s.id.equals(pdsBase.id)) {
                                    com.wotingfm.common.utils.T.getInstance().showToast("已下载");
                                    return;
                                }
                            }
                        }
                        final File saveFile = new File(SDCardUtils.getSDPath() + DbConfig.ALBUMS + "/" + pdsBase.single_file_url);
                        HttpRequest.download(pdsBase.single_file_url, saveFile, new FileDownloadCallback() {
                            //开始下载
                            @Override
                            public void onStart() {
                                super.onStart();
                                L.i("mingku", "downloadStart");
                                if (pdsBase != null && downloadHelper != null) {
                                    com.wotingfm.common.utils.T.getInstance().showToast("开始下载");
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("id", pdsBase.id);
                                    contentValues.put("single_title", pdsBase.single_title);
                                    contentValues.put("single_logo_url", pdsBase.single_logo_url);
                                    contentValues.put("single_file_url", saveFile.getAbsolutePath());
                                    contentValues.put("album_title", pdsBase.album_title);
                                    contentValues.put("album_logo_url", pdsBase.album_logo_url);
                                    contentValues.put("had_liked", pdsBase.had_liked);
                                    contentValues.put("album_id", pdsBase.album_id);
                                    contentValues.put("creator_id", pdsBase.creator_id);
                                    contentValues.put("isDownloadOver", false);
                                    downloadHelper.insertTotable(pdsBase.id, contentValues);
                                }

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
                                L.i("mingku", "downloadFailure");
                                if (pdsBase != null && downloadHelper != null)
                                    downloadHelper.deleteTable(pdsBase.id);
                            }

                            //下载完成（下载成功）
                            @Override
                            public void onDone() {
                                super.onDone();
                                L.i("mingku", "downloadDone");
                                if (pdsBase != null && downloadHelper != null) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("id", pdsBase.id);
                                    contentValues.put("single_title", pdsBase.single_title);
                                    contentValues.put("single_logo_url", pdsBase.single_logo_url);
                                    contentValues.put("single_file_url", saveFile.getAbsolutePath());
                                    contentValues.put("album_title", pdsBase.album_title);
                                    contentValues.put("album_logo_url", pdsBase.album_logo_url);
                                    contentValues.put("had_liked", pdsBase.had_liked);
                                    contentValues.put("album_id", pdsBase.album_id);
                                    contentValues.put("creator_id", pdsBase.creator_id);
                                    contentValues.put("isDownloadOver", true);
                                    contentValues.put("albumSize", FileSizeUtil.getFileOrFilesSize(saveFile.getAbsolutePath(), FileSizeUtil.SIZETYPE_MB));
                                    downloadHelper.insertTotable(pdsBase.id, contentValues);
                                    //下载完成发送消息
                                    EventBus.getDefault().post(new MessageEvent(pdsBase.id));
                                }
                            }
                        });
                    }
                }
                break;
            case R.id.tvAnchor:
                if (pdsBase != null && activity != null) {
                    dismiss();
                    activity.open(AnchorPersonalCenterFragment.newInstance(pdsBase.creator_id));
                } else if (pdsBase != null && activityMain != null) {
                    dismiss();
                    activityMain.open(AnchorPersonalCenterFragment.newInstance(pdsBase.creator_id));
                } else if (pdsBase != null && interPhoneActivity != null) {
                    dismiss();
                    interPhoneActivity.open(AnchorPersonalCenterFragment.newInstance(pdsBase.creator_id));
                }
                break;
            case R.id.tvAlbums:
                if (pdsBase != null && activity != null) {
                    dismiss();
                    activity.open(AlbumsInfoFragmentMain.newInstance(pdsBase.album_id));
                } else if (pdsBase != null && activityMain != null) {
                    dismiss();
                    activityMain.open(AlbumsInfoFragmentMain.newInstance(pdsBase.album_id));
                } else if (pdsBase != null && interPhoneActivity != null) {
                    dismiss();
                    interPhoneActivity.open(AlbumsInfoFragmentMain.newInstance(pdsBase.album_id));
                }
                break;
            case R.id.tvSubscription:
                dismiss();
                boolean isLogin = CommonUtils.isLogin();
                if (isLogin == false && activity != null) {
                    LogoActivity.start(activity);
                    return;
                } else if (isLogin == false && activityMain != null) {
                    LogoActivity.start(activityMain);
                    return;
                } else if (isLogin == false && interPhoneActivity != null) {
                    LogoActivity.start(interPhoneActivity);
                    return;
                }
                if (activity != null)
                    activity.open(MeSubscribeListFragment.newInstance());
                else if (activityMain != null)
                    activityMain.open(MeSubscribeListFragment.newInstance());
                else if (interPhoneActivity != null)
                    interPhoneActivity.open(MeSubscribeListFragment.newInstance());
                break;
            case R.id.tvReport:
                if (pdsBase != null && activity != null) {
                    dismiss();
                    boolean isLogin1 = CommonUtils.isLogin();
                    if (isLogin1 == false) {
                        LogoActivity.start(activity);
                        return;
                    }
                    activity.open(ReportsPlayerFragment.newInstance(pdsBase.id, "REPORT_SINGLE"));
                } else if (pdsBase != null && activityMain != null) {
                    dismiss();
                    boolean isLogin1 = CommonUtils.isLogin();
                    if (isLogin1 == false) {
                        LogoActivity.start(activityMain);
                        return;
                    }
                    activityMain.open(ReportsPlayerFragment.newInstance(pdsBase.id, "REPORT_SINGLE"));
                } else if (pdsBase != null && interPhoneActivity != null) {
                    dismiss();
                    boolean isLogin1 = CommonUtils.isLogin();
                    if (isLogin1 == false) {
                        LogoActivity.start(interPhoneActivity);
                        return;
                    }
                    interPhoneActivity.open(ReportsPlayerFragment.newInstance(pdsBase.id, "REPORT_SINGLE"));
                }
                break;
            case R.id.tvAgo:
                dismiss();
                if (activity != null)
                    activity.open(PlayerHistoryFragment.newInstance());
                else if (activityMain != null)
                    activityMain.open(PlayerHistoryFragment.newInstance());
                else if (interPhoneActivity != null)
                    interPhoneActivity.open(PlayerHistoryFragment.newInstance());
                break;
            case R.id.tvClose:
                dismiss();
                break;
            case R.id.tvLike:
                if (pdsBase != null) {
                    boolean isLogin2 = CommonUtils.isLogin();
                    if (isLogin2 == false && activity != null) {
                        dismiss();
                        LogoActivity.start(activity);
                        return;
                    } else if (isLogin2 == false && activityMain != null) {
                        dismiss();
                        LogoActivity.start(activityMain);
                        return;
                    } else if (isLogin2 == false && interPhoneActivity != null) {
                        dismiss();
                        LogoActivity.start(interPhoneActivity);
                        return;
                    }
                    if (pdsBase.had_liked == true) {
                        unfollowPlayer();
                    } else {
                        followPlayer();
                    }
                }
                break;
            case R.id.tvLocal:
                dismiss();
                if (activity != null)
                    activity.open(DownloadProgramFragment.newInstance());
                else if (activityMain != null)
                    activityMain.open(DownloadProgramFragment.newInstance());
                else if (interPhoneActivity != null)
                    interPhoneActivity.open(DownloadProgramFragment.newInstance());
                break;
            case R.id.tvRadio:
                dismiss();
                if (pdsBase != null && pdsBase.is_radio == true) {
                    channelsBean = new ChannelsBean();
                    channelsBean.id = pdsBase.id;
                    channelsBean.title = pdsBase.album_title;
                }
                if (activity != null && channelsBean != null)
                    activity.open(RadioInfoFragment.newInstance(channelsBean.id, channelsBean.title));
                else if (activityMain != null && channelsBean != null)
                    activityMain.open(RadioInfoFragment.newInstance(channelsBean.id, channelsBean.title));
                else if (interPhoneActivity != null && channelsBean != null)
                    interPhoneActivity.open(RadioInfoFragment.newInstance(channelsBean.id, channelsBean.title));
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
        void followPlayer(SinglesBase psb);
    }

}
