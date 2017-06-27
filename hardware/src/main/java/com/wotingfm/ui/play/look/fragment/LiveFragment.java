package com.wotingfm.ui.play.look.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.LoadMoreFooterView;
import com.woting.commonplat.amine.OnLoadMoreListener;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.findHome.LiveListAdapter;
import com.wotingfm.common.bean.AlbumsBean;
import com.wotingfm.common.bean.HomeBanners;
import com.wotingfm.common.bean.LiveBean;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.BannerView;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/14.
 * 发现直播
 */

public class LiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_live_list;
    }

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    private View headview;
    private TextView tvTitle;
    private LiveListAdapter selectedAdapter;

    @Override
    protected void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //拿到最后一条的position
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int endCompletelyPosition = manager.findLastCompletelyVisibleItemPosition();
                if (selectedAdapter.getItemCount() > 10 && endCompletelyPosition == selectedAdapter.getItemCount() - 1) {
                    //执行加载更多的方法，无论是用接口还是别的方式都行
                    loadMore();
                }
            }
        });
        mSwipeLayout.setOnRefreshListener(this);
        selectedAdapter = new LiveListAdapter(getActivity(), list, new LiveListAdapter.LiveListClick() {
            @Override
            public void click(LiveBean.DataBean dataBean) {
                if ("living".equals(dataBean.type)) {
                    T.getInstance().showToast("直播");
                } else {
                    T.getInstance().showToast("预告");
                }
            }
        });
        headview = LayoutInflater.from(getActivity()).inflate(R.layout.headview_live, mRecyclerView, false);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(selectedAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic,
                R.color.app_basic, R.color.app_basic);
        mBannerView = (BannerView) headview.findViewById(R.id.mBannerView);
        tvTitle = (TextView) headview.findViewById(R.id.tvTitle);
        loadLayout.showLoadingView();
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                getBanners();
                refresh();
            }
        });
        getBanners();
        refresh();
    }

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private BannerView mBannerView;

    @Override
    public void onResume() {
//        setVideoResume();
        super.onResume();
        if (mBannerView != null)
            mBannerView.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerView != null)
            mBannerView.stopTurning();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (mBannerView != null)
            //被hidden时未true 回到这个Fragment时返回false
            if (hidden) {
                mBannerView.stopTurning();
            } else {
//            setVideoResume();
                mBannerView.startTurning(5000);
            }

        super.onHiddenChanged(hidden);
    }

    private void getBanners() {
        RetrofitUtils.getInstance().getHomeBanners("LIVE")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HomeBanners.DataBean.BannersBean>>() {
                    @Override
                    public void call(List<HomeBanners.DataBean.BannersBean> banners) {
                        if (banners != null && !banners.isEmpty()) {
                            mBannerView.setData(banners);
                            mBannerView.setVisibility(View.VISIBLE);
                            int screenWidth = getResources().getDisplayMetrics().widthPixels;
                            mBannerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    (int) (screenWidth / 5f * 2)));
                            mBannerView.startTurning(5000);
                        } else {
                            mBannerView.setVisibility(View.GONE);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    private int mPage;

    private void refresh() {
        mPage = 1;
        RetrofitUtils.getInstance().getRecommandations(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LiveBean.DataBean>>() {
                    @Override
                    public void call(List<LiveBean.DataBean> albumsBeen) {
                        mSwipeLayout.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            list.clear();
                            list.addAll(albumsBeen);
                            tvTitle.setVisibility(View.VISIBLE);
                            loadLayout.showContentView();
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                        } else {
                            loadLayout.showContentView();
                            tvTitle.setVisibility(View.GONE);
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

    private void loadMore() {
        RetrofitUtils.getInstance().getRecommandations(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LiveBean.DataBean>>() {
                    @Override
                    public void call(List<LiveBean.DataBean> albumsBeen) {
                        mSwipeLayout.setRefreshing(false);
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mPage++;
                            list.addAll(albumsBeen);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                        } else {
                            T.getInstance().showToast("没有更多数据");
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

    private List<LiveBean.DataBean> list = new ArrayList<>();


    @Override
    public void onRefresh() {
        refresh();
    }
}
