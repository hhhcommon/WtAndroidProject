package com.wotingfm.ui.play.find.live.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.woting.commonplat.nim.DemoCache;
import com.woting.commonplat.nim.base.util.string.MD5;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.BannerView;
import com.wotingfm.ui.play.find.live.adapter.LiveListAdapter;
import com.wotingfm.ui.bean.HomeBanners;
import com.wotingfm.ui.bean.LiveBean;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 *直播
 */

public class LiveFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    private View rootView;

    private View headview;
    private TextView tvTitle;
    private LiveListAdapter selectedAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private BannerView mBannerView;
    private List<LiveBean.DataBean> list = new ArrayList<>();


    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live_list, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
//            register();
        }
        return rootView;
    }

    private void register(){
        final String account = "13200000008";
        final String token = tokenFromPassword("123456");
        // 登录
        AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                T.getInstance().showToast("login success");
                DemoCache.setAccount(account);
            }

            @Override
            public void onFailed(int code) {
                T.getInstance().showToast("登录失败");
            }

            @Override
            public void onException(Throwable exception) {
                T.getInstance().showToast("登录出异常");
            }
        });
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        return MD5.getStringMD5(password);
    }

    protected void inItView() {
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
                    //   LiveActivity.start(getActivity(), false, true);
//                    if (CommonUtils.isLogin() == false) {
//                        LogoActivity.start(getActivity());
//                        return;
//                    }
                    LiveRoomActivity.startAudience(getActivity(), dataBean);
                } else {
                    openFragment(TrailerInfoFragment.newInstance(dataBean.id));
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

    @Override
    public void onResume() {
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
                        Log.e("直播列表返回数据", new GsonBuilder().serializeNulls().create().toJson(albumsBeen));
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


    private void openFragment(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.open(fragment);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onClick(View v) {

    }
}
