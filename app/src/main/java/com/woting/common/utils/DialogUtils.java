package com.woting.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.woting.R;

/**
 * 等待提示
 *
 * @author 辛龙
 *         2016年8月5日
 */
public class DialogUtils {

    /**
     * 等待提示
     * @param context
     * @return
     */
    public static Dialog Dialog(Context context) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = dialog.getWindow();
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // lp.width = 300; // 宽度
        // lp.height = 300; // 高度
        // dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawableResource(R.color.transparent_background);
        dialog.show();
        return dialog;
    }



}
