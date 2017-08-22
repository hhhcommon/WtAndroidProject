package com.woting.ui.play.radio.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.woting.R;
import com.woting.common.adapter.radioAdapter.RadioTodayAdapter;
import com.woting.common.bean.ChannelsBean;
import com.woting.common.bean.RadioInfo;
import com.woting.common.net.RetrofitUtils;
import com.woting.common.utils.T;
import com.woting.ui.base.basefragment.BaseFragment;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 电台详情，今天
 */

public class RadioInfoTodayFragment extends BaseFragment {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_radio_info_today;
    }

    public static RadioInfoTodayFragment newInstance(RadioInfo.DataBean dataBean) {
        RadioInfoTodayFragment fragment = new RadioInfoTodayFragment();
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
        if (dataBean != null && dataBean.today != null && !dataBean.today.isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new RadioTodayAdapter(getActivity(), dataBean.today, new RadioTodayAdapter.TodayBeanClick() {
                @Override
                public void clickAlbums(RadioInfo.DataBean.TodayBean singlesBean) {
                    ChannelsBean c = new ChannelsBean();
                    c.id = singlesBean.id;
                    c.image_url = dataBean.channel.image_url;
                    c.listen_count = dataBean.channel.listen_count;
                    c.desc = dataBean.channel.desc;
                    c.radio_url = singlesBean.fileUrl;
                    c.title = singlesBean.title;
                    startMain(c);
                }

                @Override
                public void follow(RadioInfo.DataBean.TodayBean singlesBean, int postion) {
                    if (singlesBean.had_subscribed == false)
                        reservations(singlesBean, postion);
                    else
                        deleteReservations(singlesBean, postion);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private RadioTodayAdapter mAdapter;

    private void reservations(final RadioInfo.DataBean.TodayBean todayBean, final int postion) {
        showLodingDialog();
        RetrofitUtils.getInstance().reservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        todayBean.had_subscribed = true;
                        dataBean.today.set(postion, todayBean);
                        mAdapter.notifyDataSetChanged();
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("预约失败");
                        dissmisDialog();
                    }
                });
    }

    private void deleteReservations(final RadioInfo.DataBean.TodayBean todayBean, final int postion) {
        showLodingDialog();
        RetrofitUtils.getInstance().deleteReservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        todayBean.had_subscribed = false;
                        dataBean.today.set(postion, todayBean);
                        mAdapter.notifyDataSetChanged();
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消预约失败");
                        dissmisDialog();
                    }
                });
    }

}


