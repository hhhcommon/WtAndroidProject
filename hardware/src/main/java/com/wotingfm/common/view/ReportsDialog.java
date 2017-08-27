package com.wotingfm.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.report.view.ReportFragment;

//举报个人dialog
public class ReportsDialog extends Dialog implements View.OnClickListener {

    private TextView tvClose, tvReport;
    private PlayerActivity activity;
    private InterPhoneActivity interPhoneActivity;
    private MineActivity activityMain;

    public ReportsDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialog);
        if (context instanceof PlayerActivity)
            activity = (PlayerActivity) context;
        else if (context instanceof MineActivity)
            this.activityMain = (MineActivity) context;
        else if (context instanceof InterPhoneActivity)
            this.interPhoneActivity = (InterPhoneActivity) context;
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
                    activity.open(ReportFragment.newInstance(userId, "REPORT_USER"));
                else if (activityMain != null)
                    activityMain.open(ReportFragment.newInstance(userId, "REPORT_USER"));
                else if (interPhoneActivity != null)
                    interPhoneActivity.open(ReportFragment.newInstance(userId, "REPORT_USER"));
                break;
        }
    }
}