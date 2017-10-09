package com.wotingfm.ui.play.main.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.BeanCloneUtil;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.album.main.view.AlbumsInfoMainFragment;
import com.wotingfm.ui.play.anchor.view.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.local.view.AlbumsFragment;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.localaudio.service.DownloadClient;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.mysubscribelist.view.MeSubscribeListFragment;
import com.wotingfm.ui.play.playhistory.view.PlayerHistoryFragment;
import com.wotingfm.ui.play.radio.radioinfo.view.RadioInfoFragment;
import com.wotingfm.ui.play.report.view.ReportFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//首页菜单dialog
public class MenuDialog extends Dialog implements View.OnClickListener {

    private TextView tvClose, tvLike, tvAlbums, tvAnchor, tvReport, tvDownload, tvAgo, tvSubscription, tvLocal, tvRadio;
    private PlayerActivity activity;

    private SinglesBase pdsBase;
    private FollowCallBack followCallBack;
    private FileInfoDao mFileDao;// 文件相关数据库

    public MenuDialog(Activity context) {
        super(context, R.style.BottomDialog);
        this.activity = (PlayerActivity) context;
        setContentView(R.layout.player_menu_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);

        inItView();
        inItListener();
        mFileDao = new FileInfoDao(context);
    }

    private void inItView() {
        tvClose = (TextView) findViewById(R.id.tvClose);
        tvLike = (TextView) findViewById(R.id.tvLike);
        tvRadio = (TextView) findViewById(R.id.tvRadio);
        tvAlbums = (TextView) findViewById(R.id.tvAlbums);
        tvAnchor = (TextView) findViewById(R.id.tvAnchor);
        tvReport = (TextView) findViewById(R.id.tvReport);
        tvDownload = (TextView) findViewById(R.id.tvDownload);
        tvAgo = (TextView) findViewById(R.id.tvAgo);
        tvSubscription = (TextView) findViewById(R.id.tvSubscription);
        tvLocal = (TextView) findViewById(R.id.tvLocal);
    }

    private void inItListener() {
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

    public void setMenuData(SinglesBase pds, FollowCallBack followCallBack ) {
        this.followCallBack = followCallBack;
        if (pds != null) {
            pdsBase = pds;
            setLikeImg(pds.had_liked);
            if (pds.is_radio == true) {
                tvRadio.setVisibility(View.VISIBLE);
                tvAlbums.setVisibility(View.INVISIBLE);
                tvAnchor.setVisibility(View.INVISIBLE);
                tvReport.setVisibility(View.GONE);
                Drawable top = activity.getResources().getDrawable(R.mipmap.music_play_icon_download);
                tvDownload.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            } else {
                tvAlbums.setVisibility(View.VISIBLE);
                tvAnchor.setVisibility(View.VISIBLE);
                tvReport.setVisibility(View.VISIBLE);
                tvRadio.setVisibility(View.GONE);
                Drawable top = activity.getResources().getDrawable(R.mipmap.music_play_icon_download);
                tvDownload.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
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
        switch (v.getId()) {
            case R.id.tvDownload:
                if (pdsBase != null && pdsBase.is_radio == false) {
                    dismiss();
                    download(pdsBase);
                }
                break;
            case R.id.tvAnchor:
                if (pdsBase != null && activity != null) {
                    dismiss();
                    activity.open(AnchorPersonalCenterFragment.newInstance(pdsBase.creator_id));
                }
                break;
            case R.id.tvAlbums:
                if (pdsBase != null) {
                    dismiss();
                    activity.open(AlbumsInfoMainFragment.newInstance(pdsBase.album_id));
                }
                break;
            case R.id.tvSubscription:
                dismiss();
                boolean isLogin = CommonUtils.isLogin();
                if (isLogin == false) {
                    LogoActivity.start(activity);
                    return;
                }
                activity.open(MeSubscribeListFragment.newInstance());
                break;
            case R.id.tvReport:
                if (pdsBase != null && activity != null) {
                    dismiss();
                    boolean isLogin1 = CommonUtils.isLogin();
                    if (isLogin1 == false) {
                        LogoActivity.start(activity);
                        return;
                    }
                    activity.open(ReportFragment.newInstance(pdsBase.id, "REPORT_SINGLE"));
                }
                break;
            case R.id.tvAgo:
                dismiss();
                activity.open(PlayerHistoryFragment.newInstance());
                break;
            case R.id.tvClose:
                dismiss();
                break;
            case R.id.tvLike:
                if (pdsBase != null) {
                    boolean isLogin2 = CommonUtils.isLogin();
                    if (isLogin2 == false) {
                        dismiss();
                        LogoActivity.start(activity);
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
                activity.open(AlbumsFragment.newInstance());
                break;
            case R.id.tvRadio:
                dismiss();
                if (pdsBase != null && pdsBase.is_radio == true) {
                    ChannelsBean  channelsBean = new ChannelsBean();
                    channelsBean.id = pdsBase.id;
                    channelsBean.title = pdsBase.album_title;
                    activity.open(RadioInfoFragment.newInstance(channelsBean.id, channelsBean.title));
                }
                break;
        }
    }

    private void followPlayer() {
        String type;
        if(pdsBase.is_radio){
            type="radio";
        }else{
            type="single";
        }
        RetrofitUtils.getInstance().postFollowUser(pdsBase.id,type)
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
        String type;
        if(pdsBase.is_radio){
            type="radio";
        }else{
            type="single";
        }
        RetrofitUtils.getInstance().postUnfollowUser(pdsBase.id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        if (followCallBack != null) {
                            pdsBase.had_liked = false;
                            Toast.makeText(BSApplication.getInstance(), "取消收藏成功", Toast.LENGTH_LONG).show();
                            followCallBack.followPlayer(pdsBase);
                            setLikeImg(pdsBase.had_liked);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(BSApplication.getInstance(), "取消收藏失败", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // 内容的下载
    private void download(SinglesBase pdsBase) {
        List<FileInfo> fileDataList = mFileDao.queryFileInfoAll(CommonUtils.getUserId());
        if (fileDataList.size() != 0) {
            // 此时有下载数据
            if (!getDownload(pdsBase)) {
                List<FileInfo>  dataList= getList(pdsBase);
                List<FileInfo> list= BeanCloneUtil.cloneTo(dataList);
                mFileDao.insertFileInfo(list);
                ToastUtils.show_always(activity, "开始下载");
                List<FileInfo> fileUnDownLoadList = mFileDao.queryFileInfo("false", CommonUtils.getUserId());// 未下载列表
                for (int kk = 0; kk < fileUnDownLoadList.size(); kk++) {
                    if (fileUnDownLoadList.get(kk).download_type.trim().equals("1")) {
                        DownloadClient.workStop(fileUnDownLoadList.get(kk));
                        mFileDao.upDataDownloadStatus(fileUnDownLoadList.get(kk).id, "2");
                    }
                }
                for (int k = 0; k < fileUnDownLoadList.size(); k++) {
                    if (fileUnDownLoadList.get(k).id.equals(dataList.get(0).id)) {
                        FileInfo file = fileUnDownLoadList.get(k);
                        mFileDao.upDataDownloadStatus(dataList.get(0).id, "1");
                        DownloadClient.workStart(file);
                        break;
                    }
                }
            }else{
                ToastUtils.show_always(activity, "已经下载过");
            }
        }else {// 此时库里没数据
            List<FileInfo>  dataList= getList(pdsBase);
            List<FileInfo> list= BeanCloneUtil.cloneTo(dataList);
            mFileDao.insertFileInfo(list);
            ToastUtils.show_always(activity, "已经加入下载列表");
            List<FileInfo> fileUnDownloadList = mFileDao.queryFileInfo("false", CommonUtils.getUserId());// 未下载列表
            for (int k = 0; k < fileUnDownloadList.size(); k++) {
                if (fileUnDownloadList.get(k).id.equals(dataList.get(0).id)) {
                    FileInfo file = fileUnDownloadList.get(k);
                    mFileDao.upDataDownloadStatus(dataList.get(0).id, "1");
                    DownloadClient.workStart(file);
                    break;
                }
            }
        }
    }

    // 判断当前节目是否下载过
    private boolean getDownload(SinglesBase pdsBase) {
        boolean t = false;
        // 检查是否重复,如果不重复插入数据库，并且开始下载，重复了提示
        List<FileInfo> fileDataList = mFileDao.queryFileInfoAll(CommonUtils.getUserId());
        if (fileDataList.size() != 0) {// 此时有下载数据
            for (int j = 0; j < fileDataList.size(); j++) {
                if (fileDataList.get(j).id.equals(pdsBase.id)) {
                    if (fileDataList.get(j).single_file_url != null) {
                        t = true;
                        break;
                    }
                }
            }
        }
        return t;
    }

    private List<FileInfo> getList(SinglesBase pdsBase) {
        // 对数据进行转换
        List<FileInfo> dataList = new ArrayList<>();
        FileInfo m = new FileInfo();
        m.id = pdsBase.id;
        m.play_time = String.valueOf(pdsBase.play_time);
        m.single_title = pdsBase.single_title;
        m.creator_id = pdsBase.creator_id;
        m.single_logo_url = pdsBase.single_logo_url;
        m.single_seconds = pdsBase.single_seconds;
        m.single_file_url = pdsBase.single_file_url;
        m.single_file_url_base = pdsBase.single_file_url;
        m.album_title = pdsBase.album_title;
        m.album_lastest_news = pdsBase.album_lastest_news;
        m.album_logo_url = pdsBase.album_logo_url;
        m.album_id = pdsBase.album_id;
        m.albumSize = String.valueOf(pdsBase.albumSize);
        m.user_id = CommonUtils.getUserId();
        m.download_type = "0";
        m.start = 0;
        m.end = 0;
        m.finished = "false";
        dataList.add(m);
        return dataList;
    }

    public interface FollowCallBack {
        void followPlayer(SinglesBase psb);
    }
}