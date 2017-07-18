package com.wotingfm.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.Player;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.play.activity.ReportsPlayerFragment;
import com.wotingfm.ui.test.PlayerActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.R.id.tvAgo;
import static com.wotingfm.R.id.tvAlbums;
import static com.wotingfm.R.id.tvAnchor;
import static com.wotingfm.R.id.tvDownload;
import static com.wotingfm.R.id.tvLike;
import static com.wotingfm.R.id.tvLocal;
import static com.wotingfm.R.id.tvSubscription;

//举报个人dialog
public class ReportsDialog extends Dialog implements View.OnClickListener {

    private TextView tvClose, tvReport;
    private PlayerActivity activity;

    public ReportsDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialog);
        if (context instanceof PlayerActivity)
            activity = (PlayerActivity) context;
        setContentView(R.layout.reports_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);

        tvClose = (TextView) findViewById(R.id.tvClose);
        tvReport = (TextView) findViewById(R.id.tvReport);
        tvReport.setOnClickListener(this);
        tvClose.setOnClickListener(this);
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                dismiss();
                break;
            case R.id.tvReport:
                dismiss();
                if (activity != null)
                    ReportsPlayerFragment.newInstance(userId, "REPORT_USER");
                break;
        }
    }
}