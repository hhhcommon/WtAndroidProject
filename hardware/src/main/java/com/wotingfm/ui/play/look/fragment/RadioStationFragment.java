package com.wotingfm.ui.play.look.fragment;

import com.wotingfm.R;
import com.wotingfm.ui.base.basefragment.BaseFragment;

/**
 * Created by amine on 2017/6/14.
 * 发现电台
 */

public class RadioStationFragment extends BaseFragment {


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_selected;
    }

    public static RadioStationFragment newInstance() {
        RadioStationFragment fragment = new RadioStationFragment();
        return fragment;
    }


    @Override
    protected void initView() {
    }

}
