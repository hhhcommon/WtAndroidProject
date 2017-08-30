package com.wotingfm.ui.play.search.main.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.base.baseadapter.MyFragmentPagerAdapter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.search.fragment.AlbumsListFragment;
import com.wotingfm.ui.play.search.fragment.AnchorListFragment;
import com.wotingfm.ui.play.search.fragment.ProgramListFragment;
import com.wotingfm.ui.play.search.fragment.RadioStationListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索主页
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.et_searchlike)
    EditText etSearchlike;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tv_search_album)
    TextView tv_search_album;
    @BindView(R.id.tv_search_program)
    TextView tv_search_program;
    @BindView(R.id.tv_search_audio)
    TextView tv_search_audio;
    @BindView(R.id.tv_search_anchor)
    TextView tv_search_anchor;

    private View rootView;
    private String q;

    private AlbumsListFragment albumsListFragment;
    private ProgramListFragment programListFragment;
    private AnchorListFragment anchorListFragment;
    private RadioStationListFragment radioStationListFragment;

    public static SearchFragment newInstance(String q, int code) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("q", q);
        bundle.putInt("code", code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_serch, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    public void inItView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            q = bundle.getString("q");
            int code = bundle.getInt("code", 0);
            etSearchlike.setText(q);
            if (!TextUtils.isEmpty(q)) etSearchlike.setSelection(q.length());
            inItViewPager(code);
            inItListener();
        }
    }

    private void inItViewPager(int code) {
        albumsListFragment = AlbumsListFragment.newInstance(q);
        programListFragment = ProgramListFragment.newInstance(q);
        radioStationListFragment = RadioStationListFragment.newInstance(q);
        anchorListFragment = AnchorListFragment.newInstance(q);
        ArrayList<Fragment> mFragment = new ArrayList<>();
        mFragment.add(albumsListFragment);
        mFragment.add(programListFragment);
        mFragment.add(radioStationListFragment);
        mFragment.add(anchorListFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), mFragment));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        viewPager.setOffscreenPageLimit(3);
        setTextColor(code);
    }

    private void inItListener() {
        tv_search_album.setOnClickListener(this);
        tv_search_program.setOnClickListener(this);
        tv_search_audio.setOnClickListener(this);
        tv_search_anchor.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        etSearchlike.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s + "");
            }
        });

        etSearchlike.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                   @Override
                                                   public boolean onEditorAction(TextView v, int arg1, KeyEvent event) {
                                                       if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                                                           String content = etSearchlike.getText().toString().trim();
                                                           search(content);
                                                       }
                                                       return false;
                                                   }
                                               }

        );
    }

    private void search(String content) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                //                    hideSoftKeyboard();
                closeFragment();
                //backResult();
                break;
            case R.id.tv_search_album:
                setTextColor(0);
                break;
            case R.id.tv_search_program:
                setTextColor(1);
                break;
            case R.id.tv_search_audio:
                setTextColor(2);
                break;
            case R.id.tv_search_anchor:
                setTextColor(3);
                break;
        }
    }

    /**
     * 关闭界面
     */
    public void closeFragment() {
        if (this.getActivity() instanceof PlayerActivity) {
            PlayerActivity.close();
        } else if (this.getActivity() instanceof MineActivity) {
            MineActivity.close();
        } else if (this.getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity.close();
        } else if (this.getActivity() instanceof LookListActivity) {
            LookListActivity.close();
        }
    }

    // ViewPager 监听事件
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setTextColor(arg0);
        }
    }

    /**
     * @param code 切换的下标
     */
    private void setTextColor(int code) {
        tv_search_album.setTextColor(Color.parseColor("#16181a"));
        tv_search_album.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_search_program.setTextColor(Color.parseColor("#16181a"));
        tv_search_program.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_search_audio.setTextColor(Color.parseColor("#16181a"));
        tv_search_audio.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_search_anchor.setTextColor(Color.parseColor("#16181a"));
        tv_search_anchor.setBackgroundColor(Color.parseColor("#ffffff"));
        switch (code) {
            case 0:
                tv_search_album.setTextColor(Color.parseColor("#ffffff"));
                tv_search_album.setBackground(getResources().getDrawable(R.drawable.background_circle_orange));
                break;
            case 1:
                tv_search_program.setTextColor(Color.parseColor("#ffffff"));
                tv_search_program.setBackground(getResources().getDrawable(R.drawable.background_circle_orange));
                break;
            case 2:
                tv_search_audio.setTextColor(Color.parseColor("#ffffff"));
                tv_search_audio.setBackground(getResources().getDrawable(R.drawable.background_circle_orange));
                break;
            case 3:
                tv_search_anchor.setTextColor(Color.parseColor("#ffffff"));
                tv_search_anchor.setBackground(getResources().getDrawable(R.drawable.background_circle_orange));
                break;
        }
        viewPager.setCurrentItem(code);
    }
}
