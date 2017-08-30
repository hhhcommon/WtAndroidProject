package com.wotingfm.ui.play.find.classification.minorclassification.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wotingfm.ui.bean.ChannelsBean;

import java.util.List;

/**
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */

    public class MinorClassificationAdapter extends FragmentPagerAdapter {

        private List<ChannelsBean> title;
        private List<Fragment> views;

        public MinorClassificationAdapter(FragmentManager fm, List<ChannelsBean> title, List<Fragment> views) {
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

