package com.wotingfm.ui.play.find.main.view;

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
import android.widget.TextView;

import com.wotingfm.R;

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
     * @param dialogOnClickListener 确定按钮点击事件
     */
    public static void showSecurityCodeInputDialog(final Activity activity,final DialogOnClickListener dialogOnClickListener) {
        final View dialog = LayoutInflater.from(activity).inflate(R.layout.dialog_video_search, null);
        ImageView ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
        final EditText etSearchLike = (EditText) dialog.findViewById(R.id.et_searchlike);
        TextView tvSubmitSearch = (TextView) dialog.findViewById(R.id.tvSubmitSerch);

        tvSubmitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etSearchLike.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    dialogOnClickListener.searchClick(content);
                    etSearchLike.setText("");
                    editDialog.dismiss();
                }

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOnClickListener.backClick();
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SoftInputUtils.hideSoftInput(activity);
                    }
                }, 300);

                Log.e("软键盘","关闭");
            }
        });

    }

    public interface DialogOnClickListener {
        /**
         * 点击事件
         *
         * @param str 回调参数
         */
         void searchClick(String str);
         void backClick();
    }
}
