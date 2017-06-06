package com.wotingfm.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;

import com.woting.commonplat.widget.LoadingDialog;


/**
 * Created by amine on 16/4/5.
 */
public class ProgressDialogUtils {
    private final static ProgressDialogUtils instance = new ProgressDialogUtils();
    private Context context;
    private LoadingDialog progressDialog;

    private ProgressDialogUtils() {
    }

    public static ProgressDialogUtils instance(Context context) {
        if (instance.context != context) {
            instance.context = context;
            instance.progressDialog = new LoadingDialog(context);
        }

        return instance;
    }

    public static LoadingDialog getLoadingDialog() {
        return instance.progressDialog;
    }

    /**
     * 单例的progressDialog显示
     *
     * @param title
     */
    public void show(String title) {
        ProgressDialogUtils.show(title, progressDialog);
    }

    /**
     * 单例的progressDialog隐藏
     */
    public void dismiss() {
        ProgressDialogUtils.dismiss(progressDialog);
    }

    /**
     * 单例的progressDialog影藏，在线程中使用
     */
    public void dismiss(Handler handler) {
        ProgressDialogUtils.dismiss(handler, progressDialog);
    }

    /**
     * 显示（在UI线程中使用）
     *
     * @param title
     */
    public static void show(String title, Dialog progressDialog) {
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 隐藏（在线程中使用）
     *
     * @param handler
     */
    public static void dismiss(Handler handler, final Dialog progressDialog) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 隐藏（在UI线程中使用）
     */
    public static void dismiss(Dialog progressDialog) {
        progressDialog.dismiss();
    }

}
