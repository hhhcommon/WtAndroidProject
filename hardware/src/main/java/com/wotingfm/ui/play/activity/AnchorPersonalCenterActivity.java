package com.wotingfm.ui.play.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.woting.commonplat.utils.DementionUtil;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.woting.commonplat.widget.LoadFrameLayout;
import com.wotingfm.R;
import com.wotingfm.common.adapter.userAdapter.AnchorPersonalCenterInfoAdapter;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.AnchorInfo;
import com.wotingfm.common.net.RetrofitUtils;
import com.wotingfm.common.utils.L;
import com.wotingfm.common.utils.T;
import com.wotingfm.common.view.ReportsDialog;
import com.wotingfm.ui.base.baseactivity.NoTitleBarBaseActivity;
import com.wotingfm.ui.play.activity.albums.AlbumsListActivity;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amine on 2017/6/12.
 * 主播个人中心
 */

public class AnchorPersonalCenterActivity extends NoTitleBarBaseActivity implements View.OnClickListener {
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

    public static void start(Activity activity, String uid) {
        Intent intent = new Intent(activity, AnchorPersonalCenterActivity.class);
        intent.putExtra("uid", uid);
        activity.startActivityForResult(intent, 8080);
    }


    private int height = 640;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private int overallXScroll = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_anchor_personal_center;
    }

    private AnchorPersonalCenterInfoAdapter anchorPersonalCenterInfoAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private String uid;

    @Override
    public void initView() {
        ivMore.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        height = DementionUtil.dip2px(this, 220);
        uid = getIntent().getStringExtra("uid");
        loadLayout.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout.showLoadingView();
                reportsPlayer(uid);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        anchorPersonalCenterInfoAdapter = new AnchorPersonalCenterInfoAdapter(this, dataBeanXes);
        anchorPersonalCenterInfoAdapter.setAlbumsMoreClick(new AnchorPersonalCenterInfoAdapter.AlbumsMoreClick() {
            @Override
            public void ItmeClick(String albumsId) {
                Intent intent = getIntent();
                intent.putExtra("albumsId", albumsId);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void MoreClick(AnchorInfo.DataBeanXX.UserBean.DataBeanX s) {
                AlbumsListActivity.start(AnchorPersonalCenterActivity.this, uid, "全部专辑(" + s.total_count + ")");
            }

        });
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(anchorPersonalCenterInfoAdapter);
        View headview = LayoutInflater.from(this).inflate(R.layout.header_anchor_personal, mRecyclerView, false);
        ivPhotoBg = (ImageView) headview.findViewById(R.id.ivPhotoBg);
        ivPhoto = (ImageView) headview.findViewById(R.id.ivPhoto);
        tvFensNub = (TextView) headview.findViewById(R.id.tvFensNub);
        tvName = (TextView) headview.findViewById(R.id.tvName);
        tvFollowNub = (TextView) headview.findViewById(R.id.tvFollowNub);
        tvFollow = (TextView) headview.findViewById(R.id.tvFollow);
        tvContent = (TextView) headview.findViewById(R.id.tvContent);
        mHeaderAndFooterWrapper.addHeaderView(headview);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        L.i("mingku","uid="+uid);
        reportsPlayer(uid);
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
        });
    }

    private List<AnchorInfo.DataBeanXX.UserBean.DataBeanX> dataBeanXes = new ArrayList<>();

    private ReportsDialog dialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivMore:
                if (TextUtils.isEmpty(uid))
                    return;
                if (dialog == null) {
                    dialog = new ReportsDialog(this);
                }
                dialog.setUserId(uid);
                dialog.show();
                break;
        }
    }

    private void reportsPlayer(String uid) {
        loadLayout.showLoadingView();
        RetrofitUtils.getInstance().getAnchorInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AnchorInfo>() {
                    @Override
                    public void call(AnchorInfo s) {
                        if (s == null) {
                            loadLayout.showErrorView();
                        } else {
                            if (s.data != null && s.data.user != null) {
                                loadLayout.showContentView();
                                setHeadViewData(s);
                                dataBeanXes.addAll(s.data.user.data);
                                mHeaderAndFooterWrapper.notifyDataSetChanged();
                            } else {
                                loadLayout.showErrorView();
                            }
                        }
                    }


                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadLayout.showErrorView();
                    }
                });
    }

    //headview
    private ImageView ivPhotoBg, ivPhoto;
    private TextView tvName, tvFensNub, tvFollowNub, tvFollow, tvContent;

    private void setHeadViewData(final AnchorInfo s) {
        Glide.with(context)
                .load(s.data.user.avatar)
                .placeholder(R.mipmap.oval_defut_other)
                .error(R.mipmap.oval_defut_other)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(context, 23, 4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
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
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLodingDialog();
                if (s.data.user.had_followd == true) {
                    unFollowAnchor(RetrofitUtils.TEST_USERID, s);
                } else {
                    followAnchor(RetrofitUtils.TEST_USERID, s);
                }
            }
        });
    }

    private void followAnchor(String uid, final AnchorInfo sw) {
        L.i("mingku", "uid=" + uid + ":" + sw.data.user.id);
        RetrofitUtils.getInstance().followAnchor(uid, sw.data.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object Object) {
                        T.getInstance().equals("关注成功");
                        sw.data.user.had_followd = true;
                        sw.data.user.idols_count = sw.data.user.idols_count + 1;
                        setHeadViewData(sw);
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().equals("关注成功");
                        dissmisDialog();
                    }
                });
    }

    private void unFollowAnchor(String uid, final AnchorInfo sw) {
        L.i("mingku", "uid=" + uid + ":" + sw.data.user.id);
        RetrofitUtils.getInstance().unFollowAnchor(uid, sw.data.user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        T.getInstance().equals("取消关注成功");
                        sw.data.user.had_followd = false;
                        sw.data.user.idols_count = sw.data.user.idols_count - 1;
                        setHeadViewData(sw);
                        dissmisDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        T.getInstance().equals("关注关注失败");
                        dissmisDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7070 && resultCode == RESULT_OK && data != null) {
            String albumsId = data.getStringExtra("albumsId");
            Intent intent = getIntent();
            intent.putExtra("albumsId", albumsId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}