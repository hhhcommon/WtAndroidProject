package com.wotingfm.ui.play.radio.fragment.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.ui.play.radio.fragment.adapter.RadioYesterdayAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 电台详情，昨天
 */

public class RadioInfoYesterdayFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private RadioInfo.DataBean dataBean;
    private View rootView;

    public static RadioInfoYesterdayFragment newInstance(RadioInfo.DataBean dataBean) {
        RadioInfoYesterdayFragment fragment = new RadioInfoYesterdayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataBean", dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_radio_info_yesterday, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    private void inItView() {
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
                    ChannelsBean c = new ChannelsBean();
                    c.id = singlesBean.id;
                    c.image_url = dataBean.channel.image_url;
                    c.listen_count = dataBean.channel.listen_count;
                    c.desc = dataBean.channel.desc;
                    c.radio_url = singlesBean.fileUrl;
                    c.title = singlesBean.title;
                    startMain(c);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void startMain(ChannelsBean channelsBean) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(channelsBean, 1));
    }
}


