package com.wotingfm.ui.play.album.main.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wotingfm.R;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.play.anchor.view.AnchorPersonalCenterFragment;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.report.view.ReportFragment;
import com.wotingfm.ui.user.logo.LogoActivity;

// 专辑首页菜单dialog
public class AlbumMenuDialog extends Dialog implements View.OnClickListener {

    private TextView tvClose,  tvAnchor, tvReport;
    private PlayerActivity activity;
    private AlbumInfo info;

    public AlbumMenuDialog(Activity context) {
        super(context, R.style.BottomDialog);
        this.activity = (PlayerActivity) context;
        setContentView(R.layout.album_menu_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        inItView();
        inItListener();
    }

    private void inItView() {
        tvClose = (TextView) findViewById(R.id.tvClose);
        tvAnchor = (TextView) findViewById(R.id.tvAnchor);
        tvReport = (TextView) findViewById(R.id.tvReport);
    }

    private void inItListener() {
        tvClose.setOnClickListener(this);
        tvAnchor.setOnClickListener(this);
        tvReport.setOnClickListener(this);
    }

    public void setMenuData(AlbumInfo album_info) {
        if (album_info != null) {
            info = album_info;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAnchor:
                String id=null;
                try {
                     id=info.data.album.owner.id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (id != null && activity != null) {
                    dismiss();
                    activity.open(AnchorPersonalCenterFragment.newInstance(info.data.album.owner.id));
                }
                break;
            case R.id.tvReport:
                String album_id=null;
                try {
                    album_id=info.data.album.owner.id;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (info != null && activity != null) {
                    dismiss();
                    boolean isLogin1 = CommonUtils.isLogin();
                    if (isLogin1 == false) {
                        LogoActivity.start(activity);
                        return;
                    }
                    activity.open(ReportFragment.newInstance(album_id, "REPORT_ALBUM"));
                }
                break;
            case R.id.tvClose:
                dismiss();
                break;
        }
    }

}