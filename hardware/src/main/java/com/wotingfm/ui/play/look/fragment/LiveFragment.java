package com.wotingfm.ui.play.look.fragment;

import com.wotingfm.R;
import com.wotingfm.ui.base.basefragment.BaseFragment;

/**
 * Created by amine on 2017/6/14.
 * 发现直播
 */

public class LiveFragment extends BaseFragment {


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_selected;
    }

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }


    @Override
    protected void initView() {
    }

}
