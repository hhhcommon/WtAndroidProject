package com.wotingfm.ui.play.look.activity.classification.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/21.
 * 发现分类中的小分类
 */

public class MinorClassificationFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    public static MinorClassificationFragment newInstance(String albumId, String title) {
        MinorClassificationFragment fragment = new MinorClassificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumId", albumId);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }


    public class MyAdapter extends FragmentPagerAdapter {

        private List<ChannelsBean> title;
        private List<Fragment> views;

        public MyAdapter(FragmentManager fm, List<ChannelsBean> title, List<Fragment> views) {
            super(fm);
            this.title = title;
            this.views = views;
        }

        @Override
        public Fragment getItem(int position) {
            return views.get(position);
        }

        @Override
        public int getCount() {
            return views.size();
        }


        //配置标题的方法
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position).title;
        }
    }

    private List<Fragment> mFragment = new ArrayList<>();
    private MyAdapter mAdapter;
    private String albumId,title;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_classification;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumId = bundle.getString("albumId");
            title = bundle.getString("title");
            setTitle(title);
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
    }

    private void refresh() {
        RetrofitUtils.getInstance().getChannels(albumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChannelsBean>>() {
                    @Override
                    public void call(List<ChannelsBean> albumsBeen) {
                        if (albumsBeen != null && !albumsBeen.isEmpty()) {
                            mFragment.clear();
                            for (int i = 0; i < albumsBeen.size(); i++) {
                                SubcategoryFragment fragment = SubcategoryFragment.newInstance(albumsBeen.get(i),albumId,title);
                                mFragment.add(fragment);
                            }
                            mAdapter = new MyAdapter(getChildFragmentManager(), albumsBeen, mFragment);
                            viewPager.setAdapter(mAdapter);
                            if (albumsBeen.size() > 6) {
                                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                            } else {
                                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                            }
                           /* if (albumsBeen.size() > 1)
                                viewPager.setOffscreenPageLimit(albumsBeen.size() - 1);*/
                            tabLayout.setupWithViewPager(viewPager);
                            loadLayout.showContentView();
                        } else {
                            loadLayout.showEmptyView();
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
}
