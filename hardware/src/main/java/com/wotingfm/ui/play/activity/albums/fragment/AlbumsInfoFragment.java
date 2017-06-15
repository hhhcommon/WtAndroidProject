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
import com.wotingfm.common.view.FlowLayout;
import com.wotingfm.ui.base.basefragment.BaseFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
            boolean had_followed = albumInfo.data.album.owner.had_followed;
            if (had_followed == true) {
                tvFollow.setText("已关注");
            } else {
                tvFollow.setText("关注");
            }
        }
    }

}
