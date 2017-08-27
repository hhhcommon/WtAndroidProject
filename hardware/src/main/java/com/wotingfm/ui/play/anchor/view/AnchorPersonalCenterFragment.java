package com.wotingfm.ui.play.anchor.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.ReportsDialog;
import com.wotingfm.ui.bean.ChannelsBean;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.album.view.AlbumsInfoFragmentMain;
import com.wotingfm.ui.play.anchor.adapter.AnchorPersonalCenterInfoAdapter;
import com.wotingfm.ui.base.basefragment.BaseFragment;
import com.wotingfm.ui.bean.AnchorInfo;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.anchor.presenter.AnchorPersonalCenterPresenter;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.main.PlayerActivity;
import com.wotingfm.ui.play.report.presenter.ReportPresenter;
import com.wotingfm.ui.user.logo.LogoActivity;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by amine on 2017/6/12.
 * 主播个人中心
 */

public class AnchorPersonalCenterFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.loadLayout)
    LoadFrameLayout loadLayout;
    @BindView(R.id.mRelativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private View rootView;
    private AnchorPersonalCenterPresenter presenter;
    private int height = 640;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private int overallXScroll = 0;
    private AnchorPersonalCenterInfoAdapter anchorPersonalCenterInfoAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private ReportsDialog dialog;
    //headview
    private ImageView ivPhotoBg, ivPhoto;
    private TextView tvName, tvFensNub, tvFollowNub, tvFollow, tvContent;
    private Dialog dialogs;
    private List<AnchorInfo.DataBeanXX.UserBean.DataBeanX> dataBeanXes = new ArrayList<>();
    private String uid;

    public static AnchorPersonalCenterFragment newInstance(String uid) {
        AnchorPersonalCenterFragment fragment = new AnchorPersonalCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_anchor_personal_center, container, false);
            rootView.setOnClickListener(this);
            ButterKnife.bind(this, rootView);
            inItView();
            presenter = new AnchorPersonalCenterPresenter(this);
        }
        return rootView;
    }

    public void inItView() {
        height = DementionUtil.dip2px(getActivity(), 200);
        ivMore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getAnchorInfo();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        anchorPersonalCenterInfoAdapter = new AnchorPersonalCenterInfoAdapter(getActivity(), dataBeanXes);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(anchorPersonalCenterInfoAdapter);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.header_anchor_personal, mRecyclerView, false);
        ivPhotoBg = (ImageView) headView.findViewById(R.id.ivPhotoBg);
        ivPhoto = (ImageView) headView.findViewById(R.id.ivPhoto);
        tvFensNub = (TextView) headView.findViewById(R.id.tvFensNub);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvFollowNub = (TextView) headView.findViewById(R.id.tvFollowNub);
        tvFollow = (TextView) headView.findViewById(R.id.tvFollow);
        tvFollow.setOnClickListener(this);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        mHeaderAndFooterWrapper.addHeaderView(headView);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
                if (overallXScroll <= 0) {   //设置标题的背景颜色
                    mRelativeLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                    tvTitle.setTextColor(Color.argb((int) 0, 22, 24, 26));

                    ivBack.setImageResource(R.drawable.icon_mine_img_close);
                    ivMore.setImageResource(R.drawable.icon_chat_other);
                } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) overallXScroll / height;
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
        });
    }

    public void setUid(String id) {
        uid = id;
    }

    /**
     * 进行数据适配
     *
     * @param dataBeanXess
     */
    public void setData(List<AnchorInfo.DataBeanXX.UserBean.DataBeanX> dataBeanXess) {
        dataBeanXes.addAll(dataBeanXess);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        anchorPersonalCenterInfoAdapter.setAlbumsMoreClick(new AnchorPersonalCenterInfoAdapter.AlbumsMoreClick() {
            @Override
            public void ItemClick(String albumsId) {
                if (getActivity() instanceof PlayerActivity) {
                    PlayerActivity.open(AlbumsInfoFragmentMain.newInstance(albumsId));
                } else if (getActivity() instanceof MineActivity) {
                    MineActivity.open(AlbumsInfoFragmentMain.newInstance(albumsId));
                } else if (getActivity() instanceof LookListActivity) {
                    LookListActivity.open(AlbumsInfoFragmentMain.newInstance(albumsId));
                }
            }

            @Override
            public void playClick(String albumsId) {
                startMain(albumsId);
            }

            @Override
            public void ItemSendClick(AnchorInfo.DataBeanXX.UserBean.DataBeanX.DataBean dataBean) {
                GlobalStateConfig.IS_RESULT = true;
                GlobalStateConfig.isIS_BACK = true;
                SinglesBase singlesBase = new SinglesBase();
                singlesBase.album_logo_url = dataBean.single_logo_url;
                singlesBase.album_title = dataBean.single_title;
                singlesBase.album_lastest_news = dataBean.album_title;
                singlesBase.album_id = dataBean.album_id;
                singlesBase.creator_id = uid;
                singlesBase.single_file_url = dataBean.single_file_url;
                startMain(singlesBase);
            }

            @Override
            public void MoreClick(AnchorInfo.DataBeanXX.UserBean.DataBeanX s) {
                AlbumsListMeFragment fragment = AlbumsListMeFragment.newInstance(uid, "全部专辑(" + s.total_count + ")");
                presenter.openFragment(fragment);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                presenter.close();
                break;
            case R.id.ivMore:
                if (TextUtils.isEmpty(uid)) return;
                if (dialog == null) {
                    dialog = new ReportsDialog(getActivity());
                }
                dialog.setUserId(uid);
                dialog.show();
                break;
            case R.id.tvFollow:
                boolean isLogin = CommonUtils.isLogin();
                if (isLogin == false) {
                    LogoActivity.start(getActivity());
                    return;
                }
                presenter.follow();
                break;
        }
    }

    /**
     * 设置界面数据
     *
     * @param s
     */
    public void setHeadViewData(final AnchorInfo s) {
        Glide.with(getActivity())
                .load(s.data.user.avatar)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(getActivity(), 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivPhotoBg);
        Glide.with(BSApplication.getInstance()).load(s.data.user.avatar)// Glide
                .transform(new GlideCircleTransform(BSApplication.getInstance()))
                .error(R.mipmap.oval_defut_photo)
                .placeholder(R.mipmap.oval_defut_photo)
                .into(ivPhoto);

        tvName.setText(s.data.user.name);
        tvTitle.setText(s.data.user.name);
        tvFensNub.setText("粉丝 " + s.data.user.fans_count);
        tvFollowNub.setText("关注 " + s.data.user.idols_count);
        if (TextUtils.isEmpty(s.data.user.signature)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(s.data.user.signature);
        }
        if (s.data.user.had_followd == true) {
            tvFollow.setText("已关注");
            tvFollow.setBackgroundResource(R.drawable.anchor_personal_y);
        } else {
            tvFollow.setText("关注");
            tvFollow.setBackgroundResource(R.drawable.anchor_personal);
        }
    }

    public void startMain(String albumsId) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent("stop&" + albumsId));
    }

    public void startMain(SinglesBase singlesBase) {
        GlobalStateConfig.activityA = "A";
        EventBus.getDefault().post(new MessageEvent("one"));
        EventBus.getDefault().post(new MessageEvent(singlesBase, 2));
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
        dialogs = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialogs != null) dialogs.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 7070 && resultCode == RESULT_OK && data != null) {
//            String albumsId = data.getStringExtra("albumsId");
//            startMain(albumsId);
//        }
//    }
}
