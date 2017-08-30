package com.wotingfm.ui.play.radio.fragment.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.ui.play.radio.fragment.adapter.RadioTomorrowAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 电台详情，明天
 */

public class RadioInfoTomorrowFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private RadioInfo.DataBean dataBean;
    private RadioTomorrowAdapter radioTomorrowAdapter;
    private View rootView;
    private Dialog dialog;

    public static RadioInfoTomorrowFragment newInstance(RadioInfo.DataBean dataBean) {
        RadioInfoTomorrowFragment fragment = new RadioInfoTomorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataBean", dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_radio_info_tomorrow, container, false);
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
        dialogShow();
        RetrofitUtils.getInstance().reservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        todayBean.had_subscribed = true;
                        dataBean.tomorrow.set(postion, todayBean);
                        radioTomorrowAdapter.notifyDataSetChanged();
                        dialogCancel();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("预约失败");
                        dialogCancel();
                    }
                });
    }

    private void deleteReservations(final RadioInfo.DataBean.TomorrowBean todayBean, final int postion) {
        dialogShow();
        RetrofitUtils.getInstance().deleteReservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        todayBean.had_subscribed = false;
                        dataBean.tomorrow.set(postion, todayBean);
                        radioTomorrowAdapter.notifyDataSetChanged();
                        dialogCancel();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消预约失败");
                        dialogCancel();
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }
}


