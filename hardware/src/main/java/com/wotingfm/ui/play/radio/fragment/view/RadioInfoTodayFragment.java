package com.wotingfm.ui.play.radio.fragment.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.RadioInfo;
import com.wotingfm.ui.play.radio.fragment.adapter.RadioTodayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 电台详情，今天
 */

public class RadioInfoTodayFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private RadioInfo.DataBean dataBean;
    private RadioTodayAdapter mAdapter;
    private View rootView;
    private Dialog dialog;

    public static RadioInfoTodayFragment newInstance(RadioInfo.DataBean dataBean) {
        RadioInfoTodayFragment fragment = new RadioInfoTodayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataBean", dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_radio_info_today, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    protected void inItView() {
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
                    c.had_liked = dataBean.channel.had_liked;
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

    private void reservations(final RadioInfo.DataBean.TodayBean todayBean, final int postion) {
        dialogShow();
        RetrofitUtils.getInstance().reservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        todayBean.had_subscribed = true;
                        dataBean.today.set(postion, todayBean);
                        mAdapter.notifyDataSetChanged();
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

    private void deleteReservations(final RadioInfo.DataBean.TodayBean todayBean, final int postion) {
        dialogShow();
        RetrofitUtils.getInstance().deleteReservationsRadio(todayBean.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        todayBean.had_subscribed = false;
                        dataBean.today.set(postion, todayBean);
                        mAdapter.notifyDataSetChanged();
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


