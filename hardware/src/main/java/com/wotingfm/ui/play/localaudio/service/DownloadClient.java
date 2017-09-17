package com.wotingfm.ui.play.localaudio.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.woting.commonplat.config.GlobalUrlConfig;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.model.FileInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 类注释
 */
public class DownloadClient {


    public static final int MSG_INIT = 0;
    private static Context context;
    private static DownloadTask mTask;
    private static FileInfo fileTemp = null;
    private static FileInfoDao FID;
    private MessageReceiver Receiver;

    public DownloadClient(Context context) {
        DownloadClient.context = context;
        if (FID == null) {
            FID = new FileInfoDao(context);
        }
        if (Receiver == null) {
            Receiver = new MessageReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.ACTION_FINISHED_NO_DOWNLOADVIEW);
            context.registerReceiver(Receiver, filter);
        }
    }

    /**
     * 开始下载
     *
     * @param fileInfo
     */
    public static void workStart(FileInfo fileInfo) {
        new InitThread(fileInfo).start();// 开启一个下载线程
    }

    public static void workStop(FileInfo fileInfo) {
        if (mTask != null) {
            DownloadTask.isPause = true;
        }
    }

    // 下载线程
    private static class InitThread extends Thread {
        private FileInfo mFileInfo = null;

        public InitThread(FileInfo mFileInfos) {
            mFileInfo = mFileInfos;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile raf = null;
            try {
                // 连接网络文件
                Log.e("当前下载文件名称：", mFileInfo.single_file_url + "");
                URL url = new URL(mFileInfo.single_file_url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int length = -1;
                if (connection.getResponseCode() == HttpStatus.SC_OK) {
                    // 获得文件的长度
                    length = connection.getContentLength();
                }
                if (length <= 0) {
                    Log.e("获取文件长度====", "失败");
                    Log.e("执行操作", "删除该文件");
                    FID.deleteFileInfo(mFileInfo.id, CommonUtils.getUserId());
                    Log.e("执行操作", "继续下载另外文件");
                    context.sendBroadcast(new Intent(BroadcastConstants.ACTION_FINISHED_NO_DOWNLOADVIEW));
                    return;
                }
                File dir = new File(GlobalUrlConfig.DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                String dir_path = dir.getPath();
                Log.e("dir_path", "" + dir_path);

                File dir1 = new File(GlobalUrlConfig.DOWNLOAD_PATH1);
                if (!dir1.exists()) {
                    dir1.mkdir();
                }
                String dir1_path = dir1.getPath();
                Log.e("dir1_path", "" + dir1_path);

                // 在本地创建文件
                String name = GlobalUrlConfig.DOWNLOAD_PATH1 + mFileInfo.fileName;
                File file = new File(name);
                if (!file.exists()) {
                    file.createNewFile();
                }
                String path = file.getPath();
                Log.e("path", "" + path);
                raf = new RandomAccessFile(file, "rwd");
                // 设置文件长度
                raf.setLength(length);
                mFileInfo.length = length;
                Log.e("length====", "" + length);
                mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("执行操作", "获取文件长度数据流关闭");
            }
        }
    }

    //
    private static Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    // 启动下载任务
                    Log.e("开启下载事务====", fileInfo.single_file_url + "");
                    DownloadTask.isPause = false;
                    if (fileTemp == null) {
                        fileTemp = fileInfo;
                        mTask = new DownloadTask(context, fileInfo);
                        mTask.downLoad();
                    } else {
                        if (!fileTemp.single_file_url.equals(fileInfo.single_file_url)) {
                            mTask = new DownloadTask(context, fileInfo);
                            DownloadTask.isPause = false;
                            mTask.downLoad();
                        }
                    }
                    break;
            }
        }

        ;
    };

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.ACTION_FINISHED_NO_DOWNLOADVIEW)) {
                Log.e("执行操作", "下载完毕后继续下载");
                context.sendBroadcast(new Intent(BroadcastConstants.ACTION_FINISHED));

                // 开始下载新的节目
                List<FileInfo> fileInfoList = FID.queryFileInfo("false", CommonUtils.getUserId());
                if (fileInfoList != null && fileInfoList.size() > 0) {
                    fileInfoList.get(0).download_type = "1";
                    FID.upDataDownloadStatus(fileInfoList.get(0).id, "1");
                    workStart(fileInfoList.get(0));
                }
            }
        }
    }

    /**
     * 注销广播
     */
    public void unregister() {
        context.unregisterReceiver(Receiver);
    }
}