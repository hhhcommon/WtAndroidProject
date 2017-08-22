package com.woting.ui.play.radio.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.woting.R;
import com.woting.common.adapter.radioAdapter.RadioYesterdayAdapter;
import com.woting.common.bean.RadioInfo;
import com.woting.ui.base.basefragment.BaseFragment;

import butterknife.BindView;


/**
 * 电台详情，昨天
 */

public class RadioInfoYesterdayFragment extends BaseFragment {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_radio_info_yesterday;
    }

    public static RadioInfoYesterdayFragment newInstance(RadioInfo.DataBean dataBean) {
        RadioInfoYesterdayFragment fragment = new RadioInfoYesterdayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataBean", dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RadioInfo.DataBean dataBean;


    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null)
            dataBean = (RadioInfo.DataBean) bundle.getSerializable("dataBean");
        if (dataBean != null && dataBean.yesterday != null && !dataBean.yesterday.isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(layoutManager);
            RadioYesterdayAdapter mAdapter = new RadioYesterdayAdapter(getActivity(), dataBean.yesterday, new RadioYesterdayAdapter.YesterdayBeanClick() {
                @Override
                public void clickAlbums(RadioInfo.DataBean.YesterdayBean singlesBean) {

                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}


