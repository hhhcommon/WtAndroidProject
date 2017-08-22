package com.woting.common.utils;

import android.content.ContentValues;

import com.woting.commonplat.utils.FileSizeUtil;
import com.woting.common.application.BSApplication;
import com.woting.common.bean.MessageEvent;
import com.woting.common.bean.SinglesBase;
import com.woting.common.bean.SinglesDownload;
import com.woting.common.config.DbConfig;
import com.woting.common.database.DownloadHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

/**
 * Created by amine on 2017/6/20.
 */

public class DownloadUtils {
    public static void downloadManger(final SinglesBase pdsBase) {
        final DownloadHelper downloadHelper = new DownloadHelper(BSApplication.getInstance());
        if (downloadHelper != null && pdsBase != null) {
            List<SinglesDownload> singlesBeens = downloadHelper.findPlayHistoryList();
            if (singlesBeens != null && !singlesBeens.isEmpty()) {
                for (SinglesDownload s : singlesBeens) {
                    if (s.id.equals(pdsBase.id)) {
                        continue;
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
                        com.woting.common.utils.T.getInstance().showToast("开始下载");
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
}
