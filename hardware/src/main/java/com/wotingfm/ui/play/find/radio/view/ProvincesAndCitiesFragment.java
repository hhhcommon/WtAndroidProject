package com.wotingfm.ui.play.find.radio.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.adapter.radioAdapter.ProvincesAdapter;
import com.wotingfm.ui.bean.Provinces;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 省市列表
 */
public class ProvincesAndCitiesFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    private ProvincesAdapter mAdapter;
    private List<Provinces.DataBean.ProvincesBean> albumsBeanList = new ArrayList<>();
    private View rootView;
    private ResultListener Listener;
    int type;

    /**
     * @param type
     * @return
     */
    public static ProvincesAndCitiesFragment newInstance(int type) {
        ProvincesAndCitiesFragment fragment = new ProvincesAndCitiesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_provinces_radio, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    public void inItView() {
        Bundle bundle = getArguments();
        if (bundle != null) type = bundle.getInt("type");
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProvincesAdapter(getActivity(), albumsBeanList, new ProvincesAdapter.ProvincesClick() {
            @Override
            public void clickAlbums(Provinces.DataBean.ProvincesBean singlesBean) {
                if (type == 1) {
                    GlobalAddressConfig.AdCode = singlesBean.id;
                    GlobalAddressConfig.CityName = singlesBean.title;
                    setResult();
                    closeFragment();
                } else if (type == 2) {
                    openFragment(ProvincesAndCitiesListRadioFragment.newInstance(singlesBean.title, singlesBean.id));
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                refresh();
            }
        });
        loadLayout.showLoadingView();
        refresh();
    }

    private void refresh() {
        RetrofitUtils.getInstance().getProvinces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Provinces.DataBean.ProvincesBean>>() {
                    @Override
                    public void call(List<Provinces.DataBean.ProvincesBean> albumsBeen) {
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            albumsBeanList.clear();
                            albumsBeanList.addAll(albumsBeen);
                            loadLayout.showContentView();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            loadLayout.showContentView();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                closeFragment();
                break;
        }
    }

    // 关闭页面
    private void closeFragment() {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.close();
        } else if (getActivity() instanceof MineActivity) {
            MineActivity.close();
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.close();
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.close();
        }
    }

    /**
     * 关闭界面
     *
     * @param fragment
     */
    public void openFragment(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity.open(fragment);
        } else if (getActivity() instanceof MineActivity) {
            MineActivity.open(fragment);
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity.open(fragment);
        }
    }

    /**
     * 设置返回值监听
     */
    public void setResult() {
        Listener.resultListener(true);
    }

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
