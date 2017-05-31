package com.woting.commonplat.manager;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.woting.commonplat.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 软件更新下载安装
 * author：辛龙 (xinLong)
 * 2016/12/28 11:21
 * 邮箱：645700751@qq.com
 */
public class UpdateManager {

    private Context mContext;
    private String updateMsg = "我听FM有最新版本，亲快下载吧~"; // 提示语
    private Dialog noticeDialog;

    private String apkUrl;                                    // 返回的安装包 url
    private String savePath;
    private final String saveFileName;

    private ProgressDialog pd;
    private TextView textProgress;
    private ProgressBar progressBar;

    // 进度条与通知 ui 刷新的 handler 和 msg 常量
    private final int DOWN_UPDATE = 1;
    private final int DOWN_OVER = 2;
    private int progress;
    private boolean interceptFlag = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    textProgress.setText("正在更新版本 " + progress + "%");
                    progressBar.setProgress(progress);
                    break;
                case DOWN_OVER:
                    pd.dismiss();
                    installApk();
                    break;
            }
        }
    };

    private Runnable mDownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                ///////////////////////////////////////////////////////////////////////////////////
                //  String state = Environment.getExternalStorageState();
                //  if (!state.equals(Environment.MEDIA_MOUNTED)) {
                //  Log.i("update1", "SD Card is not mounted,It is  " + state + ".");
                //  }
                //  File directory = new File(savePath).getParentFile();
                //  if (!directory.exists() && !directory.mkdirs()) {
                //  Log.i("update2", "Path to file could not be created");
                //  }
                ///////////////////////////////////////////////////////////////////////////////////
                File file = new File(savePath);
                if (!file.exists()) if (!file.mkdirs()) Log.v("TAG", "mk fail");
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numRead = is.read(buf);
                    count += numRead;
                    progress = (int) (((float) count / length) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);// 更新进度
                    if (numRead <= 0) {
                        mHandler.sendEmptyMessage(DOWN_OVER);// 下载完成通知安装
                        break;
                    }
                    fos.write(buf, 0, numRead);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 老版本样式
    private void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });

        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    // 新版本样式
    private void showDownloadDialog() {
        View progressView = LayoutInflater.from(mContext).inflate(R.layout.progress_dialog_view, null);
        textProgress = (TextView) progressView.findViewById(R.id.text_progress);
        progressBar = (ProgressBar) progressView.findViewById(R.id.pb_progressbar);
        pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.show();
        pd.setContentView(progressView);
        downloadApk();
    }

    // 下载 apk
    private void downloadApk() {
        Thread downLoadThread = new Thread(mDownApkRunnable);
        downLoadThread.start();
    }

    // 安装 apk
    private void installApk() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 下载功能
     *
     * @param context
     * @param apkUrl      apk下载路径
     * @param savePath    apk保存的本地路径
     * @param packageName 包名称  比如：woTing.apk 传参 woTing
     */
    public UpdateManager(Context context, String apkUrl, String savePath, String packageName) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.savePath = savePath;
        this.saveFileName = savePath + packageName + ".apk";
    }

    /**
     * 老版本样式
     */
    public void checkUpdateInfo1() {
        showNoticeDialog();
    }

    /**
     * 新版本样式
     */
    public void checkUpdateInfo() {
        showDownloadDialog();
    }

}
