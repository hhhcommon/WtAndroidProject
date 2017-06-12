package com.wotingfm.ui.play.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerReportsListAdapter;
import com.wotingfm.common.bean.Reports;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.baseactivity.BaseToolBarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.loc.e.l;

/**
 * Created by amine on 2017/6/9.
 * 举报节目
 */

public class ReportsPlayerActivity extends BaseToolBarActivity {
    @BindView(R.id.edContent)
    EditText edContent;

    public static void start(Context activity, String playerId) {
        Intent intent = new Intent(activity, ReportsPlayerActivity.class);
        intent.putExtra("playerId", playerId);
        activity.startActivity(intent);
    }

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private PlayerReportsListAdapter playerReportsListAdapter;
    private String playerId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_play_reports;
    }

    @Override
    public void initView() {
        setTitle("举报");
        tvSubmit.setText("提交");
        playerId = getIntent().getStringExtra("playerId");
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        playerReportsListAdapter = new PlayerReportsListAdapter(this, reports, new PlayerReportsListAdapter.ReportsSelect() {
            @Override
            public void select(Reports.DataBean.Reasons reasons) {
                if (reasons != null) {
                    reasonsBase = reasons;
                }

            }
        });
        mRecyclerView.setAdapter(playerReportsListAdapter);
        loadLayout.showLoadingView();
        getPlayerReports();
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportsPlayer();
            }
        });
    }

    private List<Reports.DataBean.Reasons> reports = new ArrayList<>();
    private Reports.DataBean.Reasons reasonsBase;

    private void getPlayerReports() {
        RetrofitUtils.getInstance().getPlayerReports()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Reports.DataBean.Reasons>>() {
                    @Override
                    public void call(List<Reports.DataBean.Reasons> albumsBeen) {
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            reports.addAll(albumsBeen);
                            loadLayout.showContentView();
                            playerReportsListAdapter.notifyDataSetChanged();
                        } else {
                            loadLayout.showEmptyView();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                        throwable.printStackTrace();
                    }
                });
    }

    private void reportsPlayer() {
        String report_reason = null;
        if (reasonsBase != null)
            report_reason = reasonsBase.title;
        String content = edContent.getText().toString().trim();
        if (TextUtils.isEmpty(report_reason) && TextUtils.isEmpty(content)) {
            T.getInstance().equals("请选择举报原因");
            return;
        }
        showLodingDialog();
        RetrofitUtils.getInstance().reportsPlayer(playerId, report_reason, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        dissmisDialog();
                        T.getInstance().equals("举报成功");
                        finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dissmisDialog();
                        T.getInstance().equals("举报失败");
                    }
                });
    }

}
