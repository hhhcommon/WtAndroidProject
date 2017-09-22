package com.woting.commonplat.nim.entertainment.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.woting.commonplat.R;

/**
 * Dialog提示框
 */
public class EditDialog {

    private static Dialog editDialog;

    /**
     * Dialog提示框消失方法
     */
    public static void dialogShow() {
        if (editDialog!=null) {
            editDialog.show();
        }
    }
    /**
    * Dialog提示框消失方法
    */
    public static void dialogDismiss() {
        if (editDialog!=null) {
            editDialog.dismiss();
        }
    }

    /**
     * 显示确认安全码提示框
     *
     * @param activity              当前Activity
     */
    public static void showSecurityCodeInputDialog(final Activity activity,  InputActivityProxy proxy ) {
        InputActivityProxyManager.getInstance().setProxy(proxy);
        final View dialog = LayoutInflater.from(activity).inflate(R.layout.dialog_live_edit, null);
        ImageView ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
        final EditText etSearchLike = (EditText) dialog.findViewById(R.id.et_searchlike);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etSearchLike.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    InputActivityProxy proxy = InputActivityProxyManager.getInstance().getProxy();
                    if (proxy != null) {
                        proxy.onSendMessage(content);
                    }
                    etSearchLike.setText("");
                }

            }
        });
        editDialog = new Dialog(activity, R.style.SampleTheme);
        editDialog.setContentView(dialog);
        editDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = editDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        editDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // 获取焦点
                etSearchLike.requestFocus();
                // 显示软键盘
                SoftInputUtils.showSoftInput(activity);
                Log.e("软键盘","打开");
            }
        });
        editDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // 隐藏软键盘
                SoftInputUtils.hideSoftInput(activity);

                Log.e("软键盘","关闭");
            }
        });

    }

    // callback
    public interface InputActivityProxy {
        void onSendMessage(String text);
    }

    private static class InputActivityProxyManager {
        private InputActivityProxy proxy;

        public static InputActivityProxyManager getInstance() {
            return InstanceHolder.instance;
        }

        public void setProxy(InputActivityProxy proxy) {
            this.proxy = proxy;
        }

        public InputActivityProxy getProxy() {
            return proxy;
        }

        public void clearProxy() {
            this.proxy = null;
        }

        static class InstanceHolder {
            final static InputActivityProxyManager instance = new InputActivityProxyManager();
        }
    }
}
