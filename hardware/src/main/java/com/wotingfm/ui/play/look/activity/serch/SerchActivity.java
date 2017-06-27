package com.wotingfm.ui.play.look.activity.serch;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.woting.commonplat.manager.VoiceRecognizer;
import com.wotingfm.R;
import com.wotingfm.common.adapter.MyAdapter;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.look.activity.LookListActivity;
import com.wotingfm.ui.play.look.activity.serch.fragment.AlbumsListFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.AnchorListFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.ProgramListFragment;
import com.wotingfm.ui.play.look.activity.serch.fragment.RadioStationListFragment;
import com.wotingfm.ui.play.look.fragment.ClassificationFragment;
import com.wotingfm.ui.play.look.fragment.LiveFragment;
import com.wotingfm.ui.play.look.fragment.RadioStationFragment;
import com.wotingfm.ui.play.look.fragment.SelectedFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wotingfm.R.id.ivClose;
import static com.wotingfm.R.id.ivVoice;

/**
 * Created by amine on 2017/6/26.
 */

public class SerchActivity extends NoTitleBarBaseActivity {
    @BindView(R.id.et_searchlike)
    EditText etSearchlike;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_serch;
    }

    public static void start(Context activity, String q) {
        Intent intent = new Intent(activity, SerchActivity.class);
        intent.putExtra("q", q);
        activity.startActivity(intent);
    }

    private List<Fragment> mFragment = new ArrayList<>();
    private MyAdapter mAdapter;

    private AlbumsListFragment albumsListFragment;
    private ProgramListFragment programListFragment;
    private AnchorListFragment anchorListFragment;
    private RadioStationListFragment radioStationListFragment;

    @Override
    public void initView() {
        List<String> type = new ArrayList<>();
        String q = getIntent().getStringExtra("q");
        etSearchlike.setText(q);
        if (!TextUtils.isEmpty(q))
            etSearchlike.setSelection(q.length());
        type.add("专辑");
        type.add("节目");
        type.add("主播");
        type.add("电台");
        albumsListFragment = AlbumsListFragment.newInstance(q);
        programListFragment = ProgramListFragment.newInstance(q);
        anchorListFragment = AnchorListFragment.newInstance(q);
        radioStationListFragment = RadioStationListFragment.newInstance(q);
        mFragment.add(albumsListFragment);
        mFragment.add(programListFragment);
        mFragment.add(anchorListFragment);
        mFragment.add(radioStationListFragment);
        mAdapter = new MyAdapter(getSupportFragmentManager(), type, mFragment);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etSearchlike.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                   @Override
                                                   public boolean onEditorAction(TextView v, int arg1, KeyEvent event) {
                                                       if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                                                           String content = etSearchlike.getText().toString().trim();
                                                           if (!TextUtils.isEmpty(content)) {
                                                               if (albumsListFragment != null)
                                                                   albumsListFragment.refresh();
                                                               if (programListFragment != null)
                                                                   programListFragment.refresh();
                                                               if (anchorListFragment != null)
                                                                   anchorListFragment.refresh();
                                                               if (radioStationListFragment != null)
                                                                   radioStationListFragment.refresh();
                                                           }
                                                       }
                                                       return false;
                                                   }
                                               }

        );
    }

}
