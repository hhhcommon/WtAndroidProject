//package com.wotingfm.ui.play.localaudio.other;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.wotingfm.R;
//import com.wotingfm.ui.base.basefragment.BaseFragment;
//import com.wotingfm.ui.play.localaudio.download.DownloadingFragment;
//import com.wotingfm.ui.play.localaudio.local.view.AlbumsFragment;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import butterknife.BindView;
//
///**
// * Created by amine on 2017/6/13.
// */
//
//public class DownloadProgramFragment extends BaseFragment implements View.OnClickListener {
//
//
//    public static DownloadProgramFragment newInstance() {
//        DownloadProgramFragment fragment = new DownloadProgramFragment();
//        return fragment;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void initView() {
//        ivBack.setOnClickListener(this);
//        tvAlbums.setOnClickListener(this);
//        tvDownloading.setOnClickListener(this);
//        tvProgram.setOnClickListener(this);
//    }
//
//    private AlbumsFragment albumsFragment;
//    private ProgramFragment programFragment;
//    private DownloadingFragment downloadingFragment;
//
//    private void initFragment(Bundle savedInstanceState) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        int currentTabPosition = getActivity().getIntent().getIntExtra("TO_DOWNLOAD", 0);
//        if (savedInstanceState != null) {
//            albumsFragment = (AlbumsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("albumsFragment");
//            programFragment = (ProgramFragment) getActivity().getSupportFragmentManager().findFragmentByTag("programFragment");
//            downloadingFragment = (DownloadingFragment) getActivity().getSupportFragmentManager().findFragmentByTag("downloadingFragment");
//        } else {
//            albumsFragment = AlbumsFragment.newInstance();
//            programFragment = ProgramFragment.newInstance();
//            downloadingFragment = DownloadingFragment.newInstance();
//
//            transaction.add(R.id.fl_body, albumsFragment, "albumsFragment");
//            transaction.add(R.id.fl_body, programFragment, "programFragment");
//            transaction.add(R.id.fl_body, downloadingFragment, "downloadingFragment");
//        }
//        transaction.commit();
//        SwitchTo(currentTabPosition);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//        //初始化frament
//        initFragment(savedInstanceState);
//    }
//
//    private void SwitchTo(int position) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        switch (position) {
//            //专辑
//            case 0:
//                transaction.hide(programFragment);
//                transaction.hide(downloadingFragment);
//                transaction.show(albumsFragment);
//                transaction.commitAllowingStateLoss();
//                break;
//
//            //节目
//            case 1:
//                transaction.hide(albumsFragment);
//                transaction.hide(downloadingFragment);
//                transaction.show(programFragment);
//                transaction.commitAllowingStateLoss();
//                break;
//            //下载中
//            case 2:
//                transaction.hide(albumsFragment);
//                transaction.hide(programFragment);
//                transaction.show(downloadingFragment);
//                transaction.commitAllowingStateLoss();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @BindView(R.id.ivBack)
//    ImageView ivBack;
//    @BindView(R.id.tvAlbums)
//    TextView tvAlbums;
//    @BindView(R.id.tvProgram)
//    TextView tvProgram;
//    @BindView(R.id.tvDownloading)
//    TextView tvDownloading;
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ivBack:
//                closeFragment();
//                break;
//            case R.id.tvAlbums:
//                setTextColor(tvAlbums, 0);
//                break;
//            case R.id.tvProgram:
//                setTextColor(tvProgram, 1);
//                break;
//            case R.id.tvDownloading:
//                setTextColor(tvDownloading, 2);
//                break;
//
//        }
//    }
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_download_program;
//    }
//
//    /**
//     * @param textViewBase 需要变颜色文本
//     * @param code         切换的下标
//     */
//    private void setTextColor(TextView textViewBase, int code) {
//        tvAlbums.setTextColor(Color.parseColor("#16181a"));
//        tvProgram.setTextColor(Color.parseColor("#16181a"));
//        tvDownloading.setTextColor(Color.parseColor("#16181a"));
//        textViewBase.setTextColor(Color.parseColor("#fd8548"));
//        SwitchTo(code);
//    }
//
//    //接受到下载完成的通知
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onMessageEventMainThread(String event) {
//        if (!TextUtils.isEmpty(event)) {
//            if (albumsFragment != null)
//                albumsFragment.refresh();
//            if (programFragment != null)
//                programFragment.refresh();
//            if (downloadingFragment != null)
//                downloadingFragment.refresh();
//        }
//    }
//}
