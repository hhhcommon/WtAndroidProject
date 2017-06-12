package com.wotingfm.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerListAdapter;
import com.wotingfm.common.bean.Player;
import com.wotingfm.ui.play.activity.PlayerHistoryActivity;

import java.util.ArrayList;
import java.util.List;

import static com.wotingfm.R.id.mRecyclerViewList;


public class MenuDialog extends Dialog implements View.OnClickListener {

    private View tvClose, tvLike, tvAlbums, tvAnchor, tvReport, tvDownload, tvAgo, tvSubscription, tvLocal;
    private Activity activity;

    public MenuDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialog);
        this.activity = context;
        setContentView(R.layout.player_menu_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);

        tvClose = findViewById(R.id.tvClose);
        tvLike = findViewById(R.id.tvLike);
        tvAlbums = findViewById(R.id.tvAlbums);
        tvAnchor = findViewById(R.id.tvAnchor);
        tvReport = findViewById(R.id.tvReport);
        tvDownload = findViewById(R.id.tvDownload);
        tvAgo = findViewById(R.id.tvAgo);
        tvSubscription = findViewById(R.id.tvSubscription);
        tvLocal = findViewById(R.id.tvLocal);
        tvClose.setOnClickListener(this);
        tvLike.setOnClickListener(this);
        tvAlbums.setOnClickListener(this);
        tvAnchor.setOnClickListener(this);
        tvReport.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
        tvAgo.setOnClickListener(this);
        tvSubscription.setOnClickListener(this);
        tvLocal.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.tvAgo:
                PlayerHistoryActivity.start(activity);
                break;
            case R.id.tvClose:
                break;
        }
    }

}
