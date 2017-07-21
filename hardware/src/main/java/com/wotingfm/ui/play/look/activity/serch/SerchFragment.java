package com.wotingfm.ui.play.look.activity.serch;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.woting.commonplat.manager.VoiceRecognizer;
import com.wotingfm.R;
import com.wotingfm.common.adapter.MyAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.BroadcastConstants;
import com.wotingfm.ui.base.baseactivity.AppManager;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.look.activity.LookListFragment;
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

public class SerchFragment extends BaseFragment {
    @BindView(R.id.et_searchlike)
    EditText etSearchlike;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    public static SerchFragment newInstance(String q, int code) {
        SerchFragment fragment = new SerchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("q", q);
        bundle.putInt("code", code);
        fragment.setArguments(bundle);
        return fragment;
    }


    private List<Fragment> mFragment = new ArrayList<>();
    private MyAdapter mAdapter;

    private AlbumsListFragment albumsListFragment;
    private ProgramListFragment programListFragment;
    private AnchorListFragment anchorListFragment;
    private RadioStationListFragment radioStationListFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_serch;
    }

    private String q;

    @Override
    public void initView() {
        List<String> type = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            q = bundle.getString("q");
            int code = bundle.getInt("code",0);
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
            mFragment.clear();
            mFragment.add(albumsListFragment);
            mFragment.add(programListFragment);
            mFragment.add(anchorListFragment);
            mFragment.add(radioStationListFragment);
            mAdapter = new MyAdapter(getChildFragmentManager(), type, mFragment);
            viewPager.setAdapter(mAdapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeFragment();
                }
            });
            etSearchlike.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    serch(s + "");
                }
            });
            etSearchlike.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                       @Override
                                                       public boolean onEditorAction(TextView v, int arg1, KeyEvent event) {
                                                           if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                                                               String content = etSearchlike.getText().toString().trim();
                                                               serch(content);
                                                           }
                                                           return false;
                                                       }
                                                   }

            );
            viewPager.setCurrentItem(code);
        }
    }

    private void serch(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (albumsListFragment != null)
                albumsListFragment.refresh(content);
            if (programListFragment != null)
                programListFragment.refresh(content);
            if (anchorListFragment != null)
                anchorListFragment.refresh(content);
            if (radioStationListFragment != null)
                radioStationListFragment.refresh(content);
        }
    }
}
