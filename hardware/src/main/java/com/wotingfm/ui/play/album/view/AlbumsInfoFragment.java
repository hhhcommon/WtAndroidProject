package com.wotingfm.ui.play.album.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.FlowLayout;
import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.user.logo.LogoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by amine on 2017/6/14.
 * 专辑详情。，详情fragment
 */

public class AlbumsInfoFragment extends Fragment implements View.OnClickListener{

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

    private AlbumInfo albumInfo;
    private View rootView;
    private Dialog dialog;

    public static AlbumsInfoFragment newInstance(AlbumInfo albumInfo) {
        AlbumsInfoFragment fragment = new AlbumsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumInfo", albumInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_albums_info, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
        }
        return rootView;
    }

    private void inItView() {
        tvFollow.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null)
            albumInfo = (AlbumInfo) bundle.getSerializable("albumInfo");
        if (albumInfo != null) {
            setResultData(albumInfo);
        }
    }

    private void setResultData(final AlbumInfo albumInfo) {
        String url=null;
        try {
            url=albumInfo.data.album.owner.avatar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url != null && !url.equals("") && url.startsWith("http")) {
            GlideUtils.loadImageViewRound(url, ivPhoto, 150, 150);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, ivPhoto, 60, 60);
        }
        try {
            tvName.setText(albumInfo.data.album.owner.nick_name);
        } catch (Exception e) {
            e.printStackTrace();
            tvName.setText("主播");
        }
        try {
            tvFens.setText("粉丝 " + albumInfo.data.album.owner.fans_count);
        } catch (Exception e) {
            e.printStackTrace();
            tvFens.setText("粉丝 0" );
        }
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

        // 设置标签
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
    }

    private void submitFans(String idol_id) {
        dialogShow();
        RetrofitUtils.getInstance().submitFans(idol_id, CommonUtils.getUserId())
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
                        dialogCancel();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("关注失败");
                        dialogCancel();
                    }
                });
    }

    private void submitNoFans(String idol_id) {
        dialogShow();
        RetrofitUtils.getInstance().submitNoFans(idol_id, CommonUtils.getUserId())
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
                        dialogCancel();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().showToast("取消关注失败");
                        dialogCancel();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvFollow:
                boolean isLogin = CommonUtils.isLogin();
                if (isLogin == false) {
                    LogoActivity.start(getActivity());
                    return;
                }
                 boolean had_followed=false;
                try {
                    had_followed = albumInfo.data.album.owner.had_followed;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (had_followed) {
                    submitNoFans(albumInfo.data.album.owner.id);
                } else {
                    submitFans(albumInfo.data.album.owner.id);
                }
                break;
        }
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
}
