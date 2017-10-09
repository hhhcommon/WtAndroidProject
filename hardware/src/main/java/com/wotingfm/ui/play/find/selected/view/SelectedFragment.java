package com.wotingfm.ui.play.find.selected.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.amine.OnRefreshListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.Selected;
import com.wotingfm.ui.play.find.selected.adapter.SelectedAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/14.
 * 发现精选
 */

public class SelectedFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.mRecyclerView)
    ARecyclerView mRecyclerView;

    private View rootView;
    private SelectedAdapter selectedAdapter;
    private LoadMoreFooterView loadMoreFooterView;
    private List<Selected.DataBeanX.DataBean> datas = new ArrayList<>();

    public static SelectedFragment newInstance() {
        SelectedFragment fragment = new SelectedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_selected, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    protected void inItView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(layoutManager);

        selectedAdapter = new SelectedAdapter(getActivity(), datas, new SelectedAdapter.SelectedClick() {
            @Override
            public void click(Selected.DataBeanX.DataBean dataBean) {
                startMain(dataBean);
            }

        });
        mRecyclerView.setIAdapter(selectedAdapter);
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        refresh();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        refresh();
    }


    @Override
    public void onLoadMore(View loadMoreView) {
        if (loadMoreFooterView.canLoadMore() && selectedAdapter.getItemCount() > 0) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            loadMore();
        }
    }

    // 下拉刷新
    private void refresh() {
        RetrofitUtils.getInstance().getSelecteds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object O) {
                        delRefresh(O);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
//                        mRecyclerView.setRefreshing(false);
                        if (datas != null && datas.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showErrorView();
                        }
                    }
                });
    }

    // 加载更多
    private void loadMore() {
        RetrofitUtils.getInstance().getSelecteds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object O) {
                        delLoadMore(O);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (datas != null && datas.size() > 0) {
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showErrorView();
                        }
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    }
                });
    }


    private void delRefresh(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("刷新精选列表==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                List<Selected.DataBeanX> data = new Gson().fromJson(msg, new TypeToken<List<Selected.DataBeanX>>() {
                }.getType());
                if (data != null && data.size() > 0) {
                    List<Selected.DataBeanX.DataBean> dataBeanXes = data.get(0).data;
                    mRecyclerView.setRefreshing(false);
                    if (dataBeanXes != null && dataBeanXes.size() > 0) {
                        if (datas != null) {
                            datas.addAll(0, dataBeanXes);
                            selectedAdapter.notifyDataSetChanged();
                        } else {
                            datas = new ArrayList<>();
                            datas.addAll(0, dataBeanXes);
                            selectedAdapter.notifyDataSetChanged();
                        }
                        loadLayout.showContentView();
                    } else {
                        if (datas != null && datas.size() > 0) {
                            loadLayout.showContentView();
                            ToastUtils.show_always(BSApplication.getInstance(), "没有新的推荐了！");
                        } else {
                            loadLayout.showEmptyView();
                        }
                    }
                } else {
                    if (datas != null && datas.size() > 0) {
                        loadLayout.showContentView();
                    } else {
                        loadLayout.showErrorView();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (datas != null && datas.size() > 0) {
                loadLayout.showContentView();
            } else {
                loadLayout.showErrorView();
            }
        }
    }

    private void delLoadMore(Object o) {
        try {
            String s = new GsonBuilder().serializeNulls().create().toJson(o);
            JSONObject js = new JSONObject(s);
            int ret = js.getInt("ret");
            Log.e("加载更多精选列表==ret", String.valueOf(ret));
            if (ret == 0) {
                String msg = js.getString("data");
                List<Selected.DataBeanX> data = new Gson().fromJson(msg, new TypeToken<List<Selected.DataBeanX>>() {
                }.getType());
                if (data != null && data.size() > 0) {
                    List<Selected.DataBeanX.DataBean> dataBeanXes = data.get(0).data;
                    //                        mRecyclerView.setRefreshing(false);
                    if (dataBeanXes != null && !dataBeanXes.isEmpty()) {
                        datas.addAll(dataBeanXes);
                        selectedAdapter.notifyDataSetChanged();
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    } else {
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    }
                }else{
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                }
            } else {
                if (datas != null && datas.size() > 0) {
                    loadLayout.showContentView();
                } else {
                    loadLayout.showErrorView();
                }
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (datas != null && datas.size() > 0) {
                loadLayout.showContentView();
            } else {
                loadLayout.showErrorView();
            }
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        }
    }

}
