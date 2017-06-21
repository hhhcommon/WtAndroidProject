package com.wotingfm.ui.play.look.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.wotingfm.R;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.play.activity.ReportsPlayerActivity;
import com.wotingfm.ui.play.look.fragment.ClassificationFragment;
import com.wotingfm.ui.play.look.fragment.LiveFragment;
import com.wotingfm.ui.play.look.fragment.RadioStationFragment;
import com.wotingfm.ui.play.look.fragment.SelectedFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amine on 2017/6/21.
 * 发现列表
 */

public class LookListActivity extends NoTitleBarBaseActivity implements View.OnClickListener {
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.ivVoice)
    ImageView ivVoice;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static void start(Context activity) {
        Intent intent = new Intent(activity, LookListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_look_list;
    }

    public class MyAdapter extends FragmentPagerAdapter {

        private List<String> title;
        private List<Fragment> views;

        public MyAdapter(FragmentManager fm, List<String> title, List<Fragment> views) {
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
            return title.get(position);
        }
    }

    private List<Fragment> mFragment = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    public void initView() {
        ivClose.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        List<String> type = new ArrayList<>();
        type.add("精选");
        type.add("分类");
        type.add("电台");
        type.add("直播");
        mFragment.add(SelectedFragment.newInstance());
        mFragment.add(ClassificationFragment.newInstance());
        mFragment.add(RadioStationFragment.newInstance());
        mFragment.add(LiveFragment.newInstance());
        mAdapter = new MyAdapter(getSupportFragmentManager(), type, mFragment);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                finish();
                break;
            case R.id.ivVoice:
                break;
        }
    }
}
