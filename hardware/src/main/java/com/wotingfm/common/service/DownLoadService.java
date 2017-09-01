//package com.wotingfm.common.service;
//
//import android.app.Service;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.os.IBinder;
//
//import com.woting.commonplat.utils.FileSizeUtil;
//import com.wotingfm.common.application.BSApplication;
//import com.wotingfm.common.database.DbConfig;
//import com.wotingfm.common.database.DownloadHelper;
//import com.wotingfm.common.utils.L;
//import com.wotingfm.common.utils.SDCardUtils;
//import com.wotingfm.ui.bean.MessageEvent;
//import com.wotingfm.ui.bean.SinglesBase;
//import com.wotingfm.ui.bean.SinglesDownload;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.File;
//import java.util.List;
//
//import cn.finalteam.okhttpfinal.FileDownloadCallback;
//import cn.finalteam.okhttpfinal.HttpRequest;
//
///**
// * 下载服务
// * 作者：xinLong on 2017/8/20 18:26
// * 邮箱：645700751@qq.com
// */
//public class DownLoadService extends Service {
//
//    private DownloadHelper downloadHelper;
//    private File saveFile;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        downloadHelper = new DownloadHelper(BSApplication.getInstance());
//    }
//
//    public void download(final SinglesBase pdsBase) {
//        boolean isDownload = getDownload(pdsBase);// 是否下载过
//        if (!isDownload) {
//            saveFile = new File(SDCardUtils.getSDPath() + DbConfig.ALBUMS + "/" + pdsBase.single_file_url);// 本地保存地址
//            HttpRequest.download(pdsBase.single_file_url, saveFile, new FileDownloadCallback() {
//                //开始下载
//                @Override
//                public void onStart() {
//                    super.onStart();
//                    saveStartData(pdsBase);
//                }
//
//                //下载进度
//                @Override
//                public void onProgress(int progress, long networkSpeed) {
//                    super.onProgress(progress, networkSpeed);
//                    //String speed = FileUtils.generateFileSize(networkSpeed);
//                }
//
//                //下载失败
//                @Override
//                public void onFailure() {
//                    super.onFailure();
//                    saveFailureData(pdsBase);
//                }
//
//                //下载完成（下载成功）
//                @Override
//                public void onDone() {
//                    super.onDone();
//                    saveDoneData(pdsBase);
//                }
//            });
//        }
//    }
//
//    // 判断当前节目是否下载过
//    private boolean getDownload(SinglesBase pdsBase) {
//        boolean t = false;
//        if (downloadHelper != null && pdsBase != null) {
//            List<SinglesDownload> singlesBeans = downloadHelper.findDownloadHistoryList();
//            if (singlesBeans != null && !singlesBeans.isEmpty()) {
//                for (SinglesDownload s : singlesBeans) {
//                    if (s.id.equals(pdsBase.id)) {
//                        t = true;
//                        break;
//                    }
//                }
//            }
//        }
//        return t;
//    }
//
//    // 开始下载
//    private void saveStartData(SinglesBase pdsBase) {
//        L.e("下载", "    开始下载");
//        if (pdsBase != null && downloadHelper != null) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("id", pdsBase.id);
//            contentValues.put("single_title", pdsBase.single_title);
//            contentValues.put("single_logo_url", pdsBase.single_logo_url);
//            contentValues.put("single_file_url", saveFile.getAbsolutePath());
//            contentValues.put("album_title", pdsBase.album_title);
//            contentValues.put("album_logo_url", pdsBase.album_logo_url);
//            contentValues.put("had_liked", pdsBase.had_liked);
//            contentValues.put("album_id", pdsBase.album_id);
//            contentValues.put("creator_id", pdsBase.creator_id);
//            contentValues.put("isDownloadOver", false);
//            downloadHelper.insertTotable(pdsBase.id, contentValues);
//        }
//    }
//
//    // 下载完成（下载成功）
//    private void saveDoneData(SinglesBase pdsBase) {
//        L.e("下载", "    下载完成");
//        if (pdsBase != null && downloadHelper != null) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("id", pdsBase.id);
//            contentValues.put("single_title", pdsBase.single_title);
//            contentValues.put("single_logo_url", pdsBase.single_logo_url);
//            contentValues.put("single_file_url", saveFile.getAbsolutePath());
//            contentValues.put("album_title", pdsBase.album_title);
//            contentValues.put("album_logo_url", pdsBase.album_logo_url);
//            contentValues.put("had_liked", pdsBase.had_liked);
//            contentValues.put("album_id", pdsBase.album_id);
//            contentValues.put("creator_id", pdsBase.creator_id);
//            contentValues.put("isDownloadOver", true);
//            contentValues.put("albumSize", FileSizeUtil.getFileOrFilesSize(saveFile.getAbsolutePath(), FileSizeUtil.SIZETYPE_MB));
//            downloadHelper.insertTotable(pdsBase.id, contentValues);
//            //下载完成发送消息
//            EventBus.getDefault().post(new MessageEvent(pdsBase.id));
//        }
//    }
//
//    // 保存下载失败
//    private void saveFailureData(SinglesBase pdsBase) {
//        L.e("下载", "    下载失败");
//        if (pdsBase != null && downloadHelper != null)
//            downloadHelper.deleteTable(pdsBase.id);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}
