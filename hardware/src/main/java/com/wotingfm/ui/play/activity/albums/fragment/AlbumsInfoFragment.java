package com.wotingfm.ui.play.activity.albums.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AlbumInfo;
import com.wotingfm.common.bean.BaseResult;
import com.wotingfm.common.net.RetrofitService;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.FlowLayout;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/14.
 * 专辑详情。，详情fragment
 */

public class AlbumsInfoFragment extends BaseFragment {

    @BindView(R.id.labelContent)
    LinearLayout labelContent;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvFollow)
    TextView tvFollow;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;
    @BindView(R.id.labelTagContent)
    LinearLayout labelTagContent;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvFens)
    TextView tvFens;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_albums_info;
    }

    public static AlbumsInfoFragment newInstance(AlbumInfo albumInfo) {
        AlbumsInfoFragment fragment = new AlbumsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumInfo", albumInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private AlbumInfo albumInfo;


    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null)
            albumInfo = (AlbumInfo) bundle.getSerializable("albumInfo");
        if (albumInfo != null) {
            setResultData(albumInfo);
        }
    }

    private void setResultData(final AlbumInfo albumInfo) {
        Glide.with(BSApplication.getInstance()).load(albumInfo.data.album.owner.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .error(R.mipmap.oval_defut_photo)
                .placeholder(R.mipmap.oval_defut_photo)
                .into(ivPhoto);
        tvName.setText(albumInfo.data.album.owner.name);
        tvFens.setText("粉丝 " + albumInfo.data.album.owner.fans_count);
        if (TextUtils.isEmpty(albumInfo.data.album.introduction)) {
            labelContent.setVisibility(View.GONE);
        } else {
            labelContent.setVisibility(View.VISIBLE);
            tvContent.setText(albumInfo.data.album.introduction);
        }
        final boolean had_followed = albumInfo.data.album.owner.had_followed;
        if (had_followed == true) {
            tvFollow.setText("已关注");
        } else {
            tvFollow.setText("关注");
        }

        if (albumInfo.data.album.channels != null && !albumInfo.data.album.channels.isEmpty()) {
            labelTagContent.setVisibility(View.VISIBLE);
            flowLayout.removeAllViews();
            for (int i = 0, size = albumInfo.data.album.channels.size(); i < size; i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_channels, null);
                TextView textView = (TextView) view.findViewById(R.id.tvTag);
                textView.setText(albumInfo.data.album.channels.get(i).title);
                flowLayout.addView(view);
            }
        } else {
            labelTagContent.setVisibility(View.GONE);
        }
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (had_followed == true) {
                    submitNoFans(albumInfo.data.album.owner.id);
                } else {
                    submitFans(albumInfo.data.album.owner.id);
                }
            }
        });
    }

    private void submitFans(String idol_id) {
        showLodingDialog();
        RetrofitUtils.getInstance().submitFans(idol_id, RetrofitUtils.TEST_USERID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult != null && baseResult.ret == 0) {
                            if (albumInfo != null && albumInfo.data != null && albumInfo.data.album != null && albumInfo.data.album.owner != null) {
                                albumInfo.data.album.owner.had_followed = true;
                                albumInfo.data.album.owner.fans_count = albumInfo.data.album.owner.fans_count + 1;
                            }
                            T.getInstance().showToast("关注成功");
                        } else {
                            if (baseResult != null)
                                T.getInstance().showToast(baseResult.msg);
                            else
                                T.getInstance().showToast("关注失败");
                        }
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("关注失败");
                        dissmisDialog();
                    }
                });
    }

    private void submitNoFans(String idol_id) {
        showLodingDialog();
        RetrofitUtils.getInstance().submitNoFans(idol_id, RetrofitUtils.TEST_USERID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult != null && baseResult.ret == 0) {
                            if (albumInfo != null && albumInfo.data != null && albumInfo.data.album != null && albumInfo.data.album.owner != null) {
                                albumInfo.data.album.owner.had_followed = true;
                                albumInfo.data.album.owner.fans_count = albumInfo.data.album.owner.fans_count + 1;
                            }
                            T.getInstance().showToast("取消关注成功");
                        } else {
                            if (baseResult != null)
                                T.getInstance().showToast(baseResult.msg);
                            else
                                T.getInstance().showToast("取消关注失败");
                        }
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消关注失败");
                        dissmisDialog();
                    }
                });
    }

}
