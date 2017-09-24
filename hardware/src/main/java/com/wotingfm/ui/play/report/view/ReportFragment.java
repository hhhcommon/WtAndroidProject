package com.wotingfm.ui.play.report.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.report.adapter.ReportAdapter;
import com.wotingfm.ui.play.report.model.Reports;
import com.wotingfm.ui.play.report.presenter.ReportPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 举报
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class ReportFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.edContent)
    EditText edContent;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.head_left_btn)
    ImageView head_left_btn;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private ReportAdapter reportAdapter;
    private View rootView;
    private ReportPresenter presenter;


    /**
     * @param playerId 举报的id(节目的id或者个人的userid)
     * @param type     REPORT_USER:举报用户; REPORT_ALBUM:举报专辑;REPORT_CHAT_GROUP:举报群聊;REPORT_SINGLE:举报节目
     */
    public static ReportFragment newInstance(String playerId, String type) {
        ReportFragment fragment = new ReportFragment();
        Bundle bundle = new Bundle();
        bundle.putString("playerId", playerId);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_play_reports, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new ReportPresenter(this);
        }
        return rootView;
    }

    public void inItView() {
        head_left_btn.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getReport();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setData(List<Reports.DataBean.Reasons> reports) {
        if (reportAdapter == null) {
            reportAdapter = new ReportAdapter(getActivity(), reports, new ReportAdapter.ReportsSelect() {
                @Override
                public void select(Reports.DataBean.Reasons reasons) {
                    if (reasons != null) {
                        presenter.select(reasons);
                    }
                }
            });
            mRecyclerView.setAdapter(reportAdapter);
        } else {
            reportAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                String content = edContent.getText().toString().trim();
                presenter.send(content);
                break;
            case R.id.head_left_btn:
                closeFragment();
                break;
        }
    }

    public void showContentView() {
        loadLayout.showContentView();
    }

    public void showEmptyView() {
        loadLayout.showEmptyView();
    }

    public void showLoadingView() {
        loadLayout.showLoadingView();
    }

    public void showErrorView() {
        loadLayout.showErrorView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter=null;
    }
}
