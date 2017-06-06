package com.woting.commonplat.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.woting.commonplat.R;


/**
 * 正在加载提示图
 */
public class LoadingDialog extends ProgressDialog {
    private LayoutInflater mLayoutInflater;


    public LoadingDialog(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);

        setMessage("加载中");
        setIndeterminate(true);
        setCancelable(true);
    }

    public void show() {
        super.show();

        LinearLayout layout = (LinearLayout) mLayoutInflater.inflate(
                R.layout.loading_dialog, null);
        this.setContentView(layout);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawable(new ColorDrawable());
   /*     ProgressBar iv = (ImageView) layout
                .findViewById(R.id.progress_image_loading);
        BaseUtils.setLoadingImageAnimation(iv);
*/
    }
}
