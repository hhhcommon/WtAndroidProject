package com.wotingfm.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.live.view.LiveRoomActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.report.view.ReportFragment;

//举报个人dialog
public class ReportsDialog extends Dialog implements View.OnClickListener {

    private Activity context;
    private TextView tvClose, tvReport;

    public ReportsDialog(@NonNull Activity contexts) {
        super(contexts, R.style.BottomDialog);
        context=contexts;
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
                openFragment(ReportFragment.newInstance(userId, "REPORT_USER"));
                break;
        }
    }


    public void openFragment(Fragment fragment) {
        if (context instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) context;
            playerActivity.open(fragment);
        } else if (context instanceof MineActivity) {
            MineActivity mineActivity = (MineActivity) context;
            mineActivity.open(fragment);
        } else if (context instanceof InterPhoneActivity) {
            InterPhoneActivity interPhoneActivity = (InterPhoneActivity) context;
            interPhoneActivity.open(fragment);
        } else if (context instanceof LookListActivity) {
            LookListActivity lookListActivity = (LookListActivity) context;
            lookListActivity.open(fragment);
        }else if (context instanceof LiveRoomActivity) {
            LiveRoomActivity liveRoomActivity = (LiveRoomActivity) context;
            liveRoomActivity.open(fragment);
        }
    }
}