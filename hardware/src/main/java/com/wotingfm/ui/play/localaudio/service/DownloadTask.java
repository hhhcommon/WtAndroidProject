package com.wotingfm.ui.play.localaudio.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.woting.commonplat.config.GlobalUrlConfig;
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

    private  Context mContext = null;
    private ThreadDao mDao = null;
    private FileInfoDao FID = null;

    private int mFinished = 0;
    public static boolean isPause = false;
    public static int downloadStatus = -1;

    private FileInfo mFileInfo = null;

    public DownloadTask(Context mContexts, FileInfo mFileInfo) {
        mContext = mContexts;
        this.mFileInfo = mFileInfo;
        if (mDao == null) mDao = new ThreadDao(mContext);
        if (FID == null) FID = new FileInfoDao(mContext);
    }

    public void downLoad() {
        // 读取下载数据库的线程信息，判断当前是否下载过
        List<ThreadInfo> threads = mDao.getThreads(mFileInfo.id);
        ThreadInfo threadInfo ;
        if (0 == threads.size()) {
            // 当前数据没有下载过，从头开始下载
            threadInfo = new ThreadInfo(mFileInfo.id, mFileInfo.single_file_url, 0, mFileInfo.length, 0);
        } else {
            // 当前数据下载过一部分
            threadInfo = threads.get(0);
        }
        Log.e("当前下载对象信息=====", new GsonBuilder().serializeNulls().create().toJson(threadInfo));
        downloadStatus = -1;
        // 创建子线程进行下载
        new DownloadThread(threadInfo).start();
    }

    private class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo = null;
        public DownloadThread(ThreadInfo mInfo){
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
                File file = new File(GlobalUrlConfig.DOWNLOAD_PATH1, name);
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                mFinished += mThreadInfo.getFinished();
                // 开始下载
                Log.e("开始下载=====", "开始下载");
                if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT || connection.getResponseCode() == HttpStatus.SC_OK) {
                    // 读取数据
                    inputStream = connection.getInputStream();

                    byte buf[] = new byte[1024 << 2];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = inputStream.read(buf)) != -1) {
                        // 写入文件
                        raf.write(buf, 0, len);
                        // 把下载进度发送广播给Activity
                        mFinished += len;
                        if (System.currentTimeMillis() - time > 300){
                            time = System.currentTimeMillis();
                            Log.e("执行操作", "数据发送");
                            Intent intent = new Intent();
                            intent.setAction(BroadcastConstants.ACTION_UPDATE);
                            intent.putExtra("id", mThreadInfo.getId());
                            intent.putExtra("start", mFinished);
                            intent.putExtra("end", mThreadInfo.getEnd());
                            mContext.sendBroadcast(intent);
                            Log.e("已下载：  ", mThreadInfo.getId() + "   &&&&  " + mFinished + "");
                        }

                        if (isPause) {
                            Log.e("下载暂停", isPause + "");
                            mDao.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
                            FID.upDataFileProgress(mThreadInfo.getId(), mFinished, mThreadInfo.getEnd());
                            return;
                        }
                    }

                    // 删除线程信息
                    mDao.deleteThread(mThreadInfo.getId());
                    Log.e("下载完毕=====", "下载完毕对象"+mThreadInfo.getId());
                    FID.upDataFileProgress(mThreadInfo.getId(), mFinished, mThreadInfo.getEnd());
                    FID.updataFileInfo(mFileInfo.id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                FID.upDataFileProgress(mThreadInfo.getId(), mFinished, mThreadInfo.getEnd());
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

                if (isPause) {
                    Log.e("下载完毕=====", "下载暂停");
                    mContext.sendBroadcast(new Intent(BroadcastConstants.ACTION_PAUSE));
                }else{
                    Log.e("下载完毕=====", "下载完毕");
                    mContext.sendBroadcast(new Intent(BroadcastConstants.ACTION_FINISHED_NO_DOWNLOADVIEW));
                }

            }
        }
    }


    public void destroy(){
        mFileInfo = null;
        mDao = null;
        FID =null;
    }
}

