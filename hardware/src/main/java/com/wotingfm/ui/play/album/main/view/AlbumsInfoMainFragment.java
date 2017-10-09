package com.wotingfm.ui.play.album.main.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.view.myscrollview.ObservableNestedScrollView;
import com.wotingfm.common.view.viewpager.CustomViewPager;
import com.wotingfm.ui.base.baseadapter.MyFragmentPagerAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.base.baseinterface.NestedScrollViewListener;
import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.play.album.main.presenter.AlbumsInfoMainPresenter;
import com.wotingfm.ui.play.album.view.AlbumsInfoFragment;
import com.wotingfm.ui.play.album.view.ProgramInfoFragment;
import com.wotingfm.ui.play.album.view.SimilarInfoFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 专辑详情
 */

public class AlbumsInfoMainFragment extends BaseFragment implements View.OnClickListener, NestedScrollViewListener {


    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
/*    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;*/

    @BindView(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.mObservableScrollView)
    ObservableNestedScrollView mObservableScrollView;
    @BindView(R.id.ivPhotoBg)
    ImageView ivPhotoBg;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.ivMore)
    ImageView ivMore;

    @BindView(R.id.tvAlbumsInfo)
    TextView tvAlbumsInfo;
    @BindView(R.id.tvProgramInfo)
    TextView tvProgramInfo;
    @BindView(R.id.tvSimilarInfo)
    TextView tvSimilarInfo;

    @BindView(R.id.tvFollow)
    TextView tvFollow;
    @BindView(R.id.img_sub)
    ImageView img_sub;
    @BindView(R.id.lin_follow)
    LinearLayout lin_follow;

    @BindView(R.id.viewpager)
    CustomViewPager mPager;

    private int height = 640;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private View rootView;
    private Dialog dialog;
    private AlbumsInfoMainPresenter presenter;
    private AlbumMenuDialog menuDialog;


    public static AlbumsInfoMainFragment newInstance(String albumsID) {
        AlbumsInfoMainFragment fragment = new AlbumsInfoMainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_albums_info, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new AlbumsInfoMainPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        height = DementionUtil.dip2px(getActivity(), 210);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ivBack.setOnClickListener(this);
        tvAlbumsInfo.setOnClickListener(this);
        tvProgramInfo.setOnClickListener(this);
        tvSimilarInfo.setOnClickListener(this);
        lin_follow.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        mObservableScrollView.setScrollViewListener(this);
    }

    public void initFragment(AlbumInfo s) {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        AlbumsInfoFragment  albumsFragment = AlbumsInfoFragment.newInstance(s);
        ProgramInfoFragment  programFragment = ProgramInfoFragment.newInstance(s.data.album.id);
        SimilarInfoFragment similarInfoFragment = SimilarInfoFragment.newInstance(s.data.album.id);

        fragmentList.add(albumsFragment);
        fragmentList.add(programFragment);
        fragmentList.add(similarInfoFragment);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        setTextColor(1);
    }

    /**
     * 根据返回数据设置页面信息
     *
     * @param s
     */
    public void setResultData(final AlbumInfo s) {
        tvTitle.setText(s.data.album.title);
        Glide.with(getActivity())
                .load(s.data.album.logo_url)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(getActivity(), 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivPhotoBg);
        String url=null;
        try {
            url=s.data.album.logo_url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url != null && !url.equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewRoundCorners(url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRoundCorners(R.mipmap.icon_avatar_d, ivPhoto, 60, 60);
        }
        if (s.data.album.had_subscibed == true) {
            tvFollow.setText("已订阅(" + s.data.album.subscriptions_count + ")");
            tvFollow.setTextColor(Color.parseColor("#50ffffff"));
            img_sub.setImageResource(R.mipmap.icon_subscription_s);
            lin_follow.setBackgroundResource(R.drawable.anchor_personal);
        } else {
            tvFollow.setText("订阅(" + s.data.album.subscriptions_count + ")");
            tvFollow.setTextColor(Color.parseColor("#ffffff"));
            img_sub.setImageResource(R.mipmap.icon_subscription_n);
            lin_follow.setBackgroundResource(R.drawable.anchor_personal_n);
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
     * @param code         切换的下标
     */
    private void setTextColor( int code) {
        tvAlbumsInfo.setTextColor(Color.parseColor("#16181a"));
        tvProgramInfo.setTextColor(Color.parseColor("#16181a"));
        tvSimilarInfo.setTextColor(Color.parseColor("#16181a"));
        switch (code){
            case 0:
                tvAlbumsInfo.setTextColor(Color.parseColor("#fd8548"));
                break;
            case 1:
                tvProgramInfo.setTextColor(Color.parseColor("#fd8548"));
                break;
            case 2:
                tvSimilarInfo.setTextColor(Color.parseColor("#fd8548"));
                break;
        }
        mPager.setCurrentItem(code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                closeFragment();
                break;
            case R.id.tvAlbumsInfo:
                setTextColor(0);
                break;
            case R.id.tvProgramInfo:
                setTextColor(1);
                break;
            case R.id.tvSimilarInfo:
                setTextColor( 2);
                break;
            case R.id.lin_follow:
                presenter.follow();
                break;
            case R.id.ivMore:
                if (menuDialog == null) {
                    menuDialog = new AlbumMenuDialog(getActivity());
                }
                if (presenter.setMenuData(menuDialog)) menuDialog.show();
                break;
        }
    }

    @Override
    public void onScrollChanged(ObservableNestedScrollView scrollView, int x, int dy, int oldx, int oldy) {
        if (dy == (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight())) {
            EventBus.getDefault().post(new MessageEvent(true));
        }

        if (dy <= 0) {   //设置标题的背景颜色
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 0, 22, 24, 26));
            ivBack.setImageResource(R.drawable.icon_mine_img_close);
            ivMore.setImageResource(R.drawable.icon_chat_other);
        } else if (dy > 0 && dy <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) dy / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) alpha, 22, 24, 26));
            ivBack.setImageResource(R.drawable.icon_mine_img_close);
            ivMore.setImageResource(R.drawable.icon_chat_other);
        } else {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 255, 22, 24, 26));
            ivBack.setImageResource(R.drawable.icon_back_black);
            ivMore.setImageResource(R.drawable.icon_chat_other_black);
        }
    }

    public void showContentView() {
        loadLayout.showContentView();
    }

    public void showEmptyView() {
        loadLayout.showEmptyView();
    }

    public void showLoadingView() {
        loadLayout.showLoadingView();
    }

    public void showErrorView() {
        loadLayout.showErrorView();
    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
