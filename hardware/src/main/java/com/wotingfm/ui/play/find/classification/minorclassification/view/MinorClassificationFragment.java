package com.wotingfm.ui.play.find.classification.minorclassification.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.classification.minorclassification.SubcategoryFragment;
import com.wotingfm.ui.play.find.classification.minorclassification.adapter.MinorClassificationAdapter;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 发现分类中的小分类
 */

public class MinorClassificationFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.head_left_btn)
    ImageView head_left_btn;

    private List<Fragment> mFragment = new ArrayList<>();
    private MinorClassificationAdapter mAdapter;
    private String albumId, title;
    private View rootView;

    public static MinorClassificationFragment newInstance(String albumId, String title) {
        MinorClassificationFragment fragment = new MinorClassificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumId", albumId);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_classification, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    public void inItView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumId = bundle.getString("albumId");
            title = bundle.getString("title");
            tv_center.setText(title);
            head_left_btn.setOnClickListener(this);
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
                                SubcategoryFragment fragment = SubcategoryFragment.newInstance(albumsBeen.get(i), albumId, title);
                                mFragment.add(fragment);
                            }
                            mAdapter = new MinorClassificationAdapter(getChildFragmentManager(), albumsBeen, mFragment);
                            viewPager.setAdapter(mAdapter);
                            if (albumsBeen.size() > 6) {
                                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                            } else {
                                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                            }
                           /* if (albumsBeen.size() > 1)
                                viewPager.setOffscreenPageLimit(albumsBeen.size() - 1);*/
                            tabLayout.setupWithViewPager(viewPager);
                            tabLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //拿到tabLayout的mTabStrip属性
                                        Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                                        mTabStripField.setAccessible(true);

                                        LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                                        int dp10 = dp2px(getContext(), 15);

                                        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                                            View tabView = mTabStrip.getChildAt(i);

                                            //拿到tabView的mTextView属性
                                            Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                                            mTextViewField.setAccessible(true);

                                            TextView mTextView = (TextView) mTextViewField.get(tabView);

                                            tabView.setPadding(0, 0, 0, 0);

                                            //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                                            int width = 0;
                                            width = mTextView.getWidth();
                                            if (width == 0) {
                                                mTextView.measure(0, 0);
                                                width = mTextView.getMeasuredWidth();
                                            }

                                            //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                                            params.width = width;
                                            params.leftMargin = dp10;
                                            params.rightMargin = dp10;
                                            tabView.setLayoutParams(params);

                                            tabView.invalidate();
                                        }

                                    } catch (NoSuchFieldException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
//                setIndicator(tabs,20,20);
                                }
                            });
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
     * dpתpx
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
}
