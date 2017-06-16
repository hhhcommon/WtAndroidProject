package com.wotingfm.ui.play.activity.albums;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.view.ObservableScrollView;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.play.activity.AnchorPersonalCenterActivity;
import com.wotingfm.ui.play.activity.albums.fragment.AlbumsInfoFragment;
import com.wotingfm.ui.play.activity.albums.fragment.ProgramInfoFragment;
import com.wotingfm.ui.play.activity.albums.fragment.SimilarInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wotingfm.R.id.loadLayout;

/**
 * Created by amine on 2017/6/15.
 * 专辑详情
 */

public class AlbumsInfoActivity extends NoTitleBarBaseActivity implements View.OnClickListener, ObservableScrollView.ScrollViewListener {
    @BindView(R.id.ivPhotoBg)
    ImageView ivPhotoBg;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvFollow)
    TextView tvFollow;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.mScrollView)
    ObservableScrollView mScrollView;

    private int height = 640;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private int overallXScroll = 0;

    public static void start(Activity activity, String albumsId) {
        Intent intent = new Intent(activity, AlbumsInfoActivity.class);
        intent.putExtra("albumsId", albumsId);
        activity.startActivityForResult(intent, 8080);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_albums_info;
    }

    private void setResultData(AlbumInfo s) {
        tvTitle.setText(s.data.album.title);
        Glide.with(context)
                .load(s.data.album.owner.avatar)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(context, 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivPhotoBg);
        Glide.with(BSApplication.getInstance()).load(s.data.album.owner.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .error(R.mipmap.oval_defut_photo)
                .placeholder(R.mipmap.oval_defut_photo)
                .into(ivPhoto);
        tvFollow.setText("订阅(" + s.data.album.subscriptions_count + ")");
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int dy, int oldx, int oldy) {
        overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
        if (overallXScroll <= 0) {   //设置标题的背景颜色
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 0, 22, 24, 26));
        } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) overallXScroll / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) alpha, 22, 24, 26));
        } else {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 255, 22, 24, 26));
        }
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
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    public void initView() {
        height = DementionUtil.dip2px(this, 220);
        String albumsId = getIntent().getStringExtra("albumsId");
        getAlbumInfo(albumsId);
        mScrollView.setScrollViewListener(this);
    }

    private void getAlbumInfo(String albumsId) {
        loadLayout.showLoadingView();
        RetrofitUtils.getInstance().getAlbumInfo(albumsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AlbumInfo>() {
                    @Override
                    public void call(AlbumInfo s) {
                        loadLayout.showContentView();
                        List<String> type = new ArrayList<>();
                        type.add("详情");
                        type.add("节目");
                        type.add("相似");
                        mFragment.add(AlbumsInfoFragment.newInstance(s));
                        String albumsID = s.data.album.id;
                        mFragment.add(ProgramInfoFragment.newInstance(albumsID));
                        mFragment.add(SimilarInfoFragment.newInstance(albumsID));
                        mAdapter = new MyAdapter(getSupportFragmentManager(), type, mFragment);
                        viewPager.setAdapter(mAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        setResultData(s);
                    }


                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                    }
                });
    }
}
