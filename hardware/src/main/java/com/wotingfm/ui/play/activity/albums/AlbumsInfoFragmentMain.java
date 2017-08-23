package com.wotingfm.ui.play.activity.albums;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.ui.base.baseinterface.ScrollViewListener;
import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.myscrollview.ObservableScrollView;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.play.activity.albums.fragment.AlbumsInfoFragment;
import com.wotingfm.ui.play.activity.albums.fragment.ProgramInfoFragment;
import com.wotingfm.ui.play.activity.albums.fragment.SimilarInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/15.
 * 专辑详情
 */

public class AlbumsInfoFragmentMain extends BaseFragment implements View.OnClickListener ,ScrollViewListener {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
/*    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;*/

    @BindView(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.mObservableScrollView)
    ObservableScrollView mObservableScrollView;
    @BindView(R.id.ivPhotoBg)
    ImageView ivPhotoBg;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvFollow)
    TextView tvFollow;
    @BindView(R.id.tvAlbumsInfo)
    TextView tvAlbumsInfo;
    @BindView(R.id.tvProgramInfo)
    TextView tvProgramInfo;
    @BindView(R.id.tvSimilarInfo)
    TextView tvSimilarInfo;
    private int height = 640;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定


    private String albumsId;

    public static AlbumsInfoFragmentMain newInstance(String albumsID) {
        AlbumsInfoFragmentMain fragment = new AlbumsInfoFragmentMain();
        Bundle bundle = new Bundle();
        bundle.putString("albumsID", albumsID);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setResultData(final AlbumInfo s) {
        tvTitle.setText(s.data.album.title);
        Glide.with(getActivity())
                .load(s.data.album.owner.avatar)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(getActivity(), 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivPhotoBg);
        Glide.with(BSApplication.getInstance()).load(s.data.album.owner.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .error(R.mipmap.oval_defut_photo)
                .placeholder(R.mipmap.oval_defut_photo)
                .into(ivPhoto);
        if (s.data.album.had_subscibed == true) {
            tvFollow.setText("已订阅(" + s.data.album.subscriptions_count + ")");
            tvFollow.setTextColor(Color.parseColor("#50ffffff"));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_subscription_s);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tvFollow.setCompoundDrawables(rightDrawable, null, null, null);
        } else {
            tvFollow.setText("订阅(" + s.data.album.subscriptions_count + ")");
            tvFollow.setTextColor(Color.parseColor("#ffffff"));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_subscription_n);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tvFollow.setCompoundDrawables(rightDrawable, null, null, null);
        }
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.data.album.had_subscibed == true) {
                    deleteSubscriptionsAlbums();
                } else {
                    subscriptionsAlbums();
                }
            }
        });

    }

    private AlbumInfo albumInfoBase;

    private void subscriptionsAlbums() {
        showLodingDialog();
        RetrofitUtils.getInstance().subscriptionsAlbums(albumsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult != null && baseResult.ret == 0) {
                            if (albumInfoBase != null && albumInfoBase.data != null && albumInfoBase.data.album != null) {
                                albumInfoBase.data.album.subscriptions_count = albumInfoBase.data.album.subscriptions_count + 1;
                                albumInfoBase.data.album.had_subscibed = true;
                                setResultData(albumInfoBase);
                            }
                            T.getInstance().showToast("订阅成功");
                        } else {
                            if (baseResult != null)
                                T.getInstance().showToast(baseResult.msg);
                            else
                                T.getInstance().showToast("订阅失败");
                        }
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("订阅失败");
                        dissmisDialog();
                    }
                });
    }

    private void deleteSubscriptionsAlbums() {
        showLodingDialog();
        RetrofitUtils.getInstance().deleteSubscriptionsAlbums(albumsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult != null && baseResult.ret == 0) {
                            if (albumInfoBase != null && albumInfoBase.data != null && albumInfoBase.data.album != null) {
                                albumInfoBase.data.album.subscriptions_count = albumInfoBase.data.album.subscriptions_count - 1;
                                albumInfoBase.data.album.had_subscibed = false;
                                setResultData(albumInfoBase);
                            }
                            T.getInstance().showToast("取消订阅成功");
                        } else {
                            if (baseResult != null)
                                T.getInstance().showToast(baseResult.msg);
                            else
                                T.getInstance().showToast("取消订阅失败");
                        }
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消订阅失败");
                        dissmisDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                closeFragment();
                break;
            case R.id.tvAlbumsInfo:
                setTextColor(tvAlbumsInfo, 0);
                break;
            case R.id.tvProgramInfo:
                setTextColor(tvProgramInfo, 1);
                break;
            case R.id.tvSimilarInfo:
                setTextColor(tvSimilarInfo, 2);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_albums_info;
    }


    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;

    @Override
    public void initView() {
        height = DementionUtil.dip2px(getActivity(), 210);
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumsId = bundle.getString("albumsID");
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ivBack.setOnClickListener(this);
            tvAlbumsInfo.setOnClickListener(this);
            tvProgramInfo.setOnClickListener(this);
            tvSimilarInfo.setOnClickListener(this);
            mObservableScrollView.setScrollViewListener(this);
            getAlbumInfo(albumsId);
        }
    }

    private AlbumsInfoFragment albumsFragment;
    private ProgramInfoFragment programFragment;
    private SimilarInfoFragment similarInfoFragment;

    private void initFragment(AlbumInfo s) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        albumsFragment = AlbumsInfoFragment.newInstance(s);
        programFragment = ProgramInfoFragment.newInstance(s.data.album.id);
        similarInfoFragment = SimilarInfoFragment.newInstance(s.data.album.id);
        transaction.add(R.id.fl_body, albumsFragment, "albumsFragment");
        transaction.add(R.id.fl_body, programFragment, "programFragment");
        transaction.add(R.id.fl_body, similarInfoFragment, "similarInfoFragment");
        transaction.commit();
        SwitchTo(0);
    }

    private void SwitchTo(int position) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                transaction.hide(programFragment);
                transaction.hide(similarInfoFragment);
                transaction.show(albumsFragment);
                transaction.commitAllowingStateLoss();
                break;

            case 1:
                transaction.hide(albumsFragment);
                transaction.hide(similarInfoFragment);
                transaction.show(programFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 2:
                transaction.hide(albumsFragment);
                transaction.hide(programFragment);
                transaction.show(similarInfoFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    private void getAlbumInfo(String albumsId) {
        loadLayout.showLoadingView();
        RetrofitUtils.getInstance().getAlbumInfo(albumsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AlbumInfo>() {
                    @Override
                    public void call(AlbumInfo s) {
                        List<String> type = new ArrayList<>();
                        type.add("详情");
                        type.add("节目");
                        type.add("相似");
                        initFragment(s);
                        // mFragment.add(newInstance(s));
    /*                    String albumsID = s.data.album.id;
                        mFragment.add(ProgramInfoFragment.newInstance(albumsID));
                        mFragment.add(SimilarInfoFragment.newInstance(albumsID));
                        mAdapter = new MyAdapter(getSupportFragmentManager(), type, mFragment);*/
                        albumInfoBase = s;
                        setResultData(s);
                        loadLayout.showContentView();
                    }


                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                    }
                });
    }

    /**
     * @param textViewBase 需要变颜色文本
     * @param code         切换的下标
     */
    private void setTextColor(TextView textViewBase, int code) {
        tvAlbumsInfo.setTextColor(Color.parseColor("#16181a"));
        tvProgramInfo.setTextColor(Color.parseColor("#16181a"));
        tvSimilarInfo.setTextColor(Color.parseColor("#16181a"));
        textViewBase.setTextColor(Color.parseColor("#fd8548"));
        SwitchTo(code);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int dy, int oldx, int oldy) {
        if (dy <= 0) {   //设置标题的背景颜色
            mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 0, 22, 24, 26));
        } else if (dy > 0 && dy <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) dy / height;
            float alpha = (255 * scale);
            mRelativeLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) alpha, 22, 24, 26));
        } else {
            mRelativeLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            tvTitle.setTextColor(Color.argb((int) 255, 22, 24, 26));
        }
    }
}
