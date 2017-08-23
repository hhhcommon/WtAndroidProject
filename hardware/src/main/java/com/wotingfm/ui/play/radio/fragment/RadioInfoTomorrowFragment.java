package com.wotingfm.ui.play.radio.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wotingfm.R;
import com.wotingfm.ui.adapter.radioAdapter.RadioTomorrowAdapter;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 电台详情，明天
 */

public class RadioInfoTomorrowFragment extends BaseFragment {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_radio_info_tomorrow;
    }

    public static RadioInfoTomorrowFragment newInstance(RadioInfo.DataBean dataBean) {
        RadioInfoTomorrowFragment fragment = new RadioInfoTomorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataBean", dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RadioInfo.DataBean dataBean;
    private RadioTomorrowAdapter radioTomorrowAdapter;

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null)
            dataBean = (RadioInfo.DataBean) bundle.getSerializable("dataBean");
        if (dataBean != null && dataBean.tomorrow != null && !dataBean.tomorrow.isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(layoutManager);
            radioTomorrowAdapter = new RadioTomorrowAdapter(getActivity(), dataBean.tomorrow, new RadioTomorrowAdapter.TomorrowBeanClick() {
                @Override
                public void follow(RadioInfo.DataBean.TomorrowBean singlesBean, int position) {
                    if (singlesBean.had_subscribed == true)
                        deleteReservations(singlesBean, position);
                    else
                        reservations(singlesBean, position);
                }

            });
            mRecyclerView.setAdapter(radioTomorrowAdapter);
        }
    }

    private void reservations(final RadioInfo.DataBean.TomorrowBean todayBean, final int postion) {
        showLodingDialog();
        RetrofitUtils.getInstance().reservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        todayBean.had_subscribed = true;
                        dataBean.tomorrow.set(postion, todayBean);
                        radioTomorrowAdapter.notifyDataSetChanged();
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

    private void deleteReservations(final RadioInfo.DataBean.TomorrowBean todayBean, final int postion) {
        showLodingDialog();
        RetrofitUtils.getInstance().deleteReservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        todayBean.had_subscribed = false;
                        dataBean.tomorrow.set(postion, todayBean);
                        radioTomorrowAdapter.notifyDataSetChanged();
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


