package com.wotingfm.ui.play.localaudio.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.utils.L;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.ui.play.localaudio.dao.FileInfoDao;
import com.wotingfm.ui.play.localaudio.dao.ThreadDao;
import com.wotingfm.ui.play.localaudio.model.FileInfo;
import com.wotingfm.ui.play.localaudio.model.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * 下载任务类
 */
public class DownloadTask {

    public static int downloadStatus = -1;
    public static Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDao mDao = null;
    private FileInfoDao FID = null;
    private int mFinished = 0;
    public static boolean isPause = false;

    public DownloadTask(Context mContexts, FileInfo mFileInfo) {
        mContext = mContexts;
        this.mFileInfo = mFileInfo;
        mDao = new ThreadDao(mContext);
        FID = new FileInfoDao(mContext);
    }

    public void downLoad() {
        // 读取数据库的线程信息
        List<ThreadInfo> threads = mDao.getThreads(mFileInfo.id);
        ThreadInfo threadInfo;
        if (0 == threads.size()) {
            // 初始化线程信息对象
            threadInfo = new ThreadInfo(mFileInfo.id, mFileInfo.single_file_url, 0, mFileInfo.length, 0);
        } else {
            threadInfo = threads.get(0);
        }
        Log.e("数据库的线程信息=====", new GsonBuilder().serializeNulls().create().toJson(threadInfo));
        downloadStatus = -1;
        // 创建子线程进行下载
        new DownloadThread(threadInfo).start();
    }

    private class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo = null;

        public DownloadThread(ThreadInfo mInfo) {
            this.mThreadInfo = mInfo;
        }

        @Override
        public void run() {
            // 向数据库插入线程信息
            if (!mDao.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
                mDao.insertThread(mThreadInfo);
            }
            HttpURLConnection connection = null;
            RandomAccessFile raf = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                downloadStatus = 1;
                // 设置下载位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                connection.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
                // 设置文件写入位置
                String name = mFileInfo.fileName;
                File file = new File(DownloadClient.DOWNLOAD_PATH1, name);
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                mFinished += mThreadInfo.getFinished();
                // 开始下载
                Log.e("开始下载=====", "开始下载");
                if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT || connection.getResponseCode() == HttpStatus.SC_OK) {
                    // 读取数据
                    inputStream = connection.getInputStream();

                    byte buf[] = new byte[1024 << 2];
                    int len;
                    long time = System.currentTimeMillis();
                    while ((len = inputStream.read(buf)) != -1) {
                        // 写入文件
                        raf.write(buf, 0, len);
                        // 把下载进度发送广播给Activity
                        mFinished += len;
                        if (System.currentTimeMillis() - time > 100) {
                            time = System.currentTimeMillis();
                            Intent intent = new Intent();
                            intent.setAction(BroadcastConstants.ACTION_UPDATE);
                            intent.putExtra("id", mThreadInfo.getId());
                            intent.putExtra("start", mFinished);
                            intent.putExtra("end", mThreadInfo.getEnd());
                            FID.upDataFileProgress(mThreadInfo.getId(), mFinished, mThreadInfo.getEnd());
                            L.w("TAG", mFinished + "");
                            mContext.sendBroadcast(intent);
                        }
                        // 在下载暂停时，保存下载进度
                        if (isPause) {
                            Log.e("下载暂停", isPause + "");
                            mDao.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
                            FID.upDataFileProgress(mThreadInfo.getId(), mFinished, mThreadInfo.getEnd());
                            Log.e("下载==mFinised", mFinished + "");
                            Log.e("下载==Start", start + "");
                            Log.e("下载==End", mThreadInfo.getEnd() + "");
                            return;
                        }
                    }
                    // 删除线程信息
                    mDao.deleteThread(mThreadInfo.getId());
                    FID.updataFileInfo(mFileInfo.id);

                    Log.e("下载完毕=====", "下载完毕");

                    Intent intent = new Intent();
                    intent.setAction(BroadcastConstants.ACTION_FINISHED_NO_DOWNLOADVIEW);
                    mContext.sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setAction(BroadcastConstants.ACTION_FINISHED_NO_DOWNLOADVIEW);
                mContext.sendBroadcast(intent);
            }
        }
    }
}

