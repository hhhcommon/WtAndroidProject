package com.wotingfm.ui.play.album.main.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.AlbumInfo;
import com.wotingfm.ui.bean.BaseResult;
import com.wotingfm.ui.play.album.main.model.AlbumsInfoMainModel;
import com.wotingfm.ui.play.album.main.view.AlbumMenuDialog;
import com.wotingfm.ui.play.album.main.view.AlbumsInfoMainFragment;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class AlbumsInfoMainPresenter {

    private AlbumsInfoMainModel model;
    private AlbumsInfoMainFragment activity;
    private AlbumInfo s;
    private String albumsId;

    public AlbumsInfoMainPresenter(AlbumsInfoMainFragment activity) {
        this.activity = activity;
        this.model = new AlbumsInfoMainModel();
        getData();
    }

    // 获取传递数据
    private void getData() {
        Bundle bundle = activity.getArguments();
        if (bundle != null) {
            albumsId = bundle.getString("albumsID");
            if (!TextUtils.isEmpty(albumsId)) {
                activity.showLoadingView();
                getAlbumInfo(albumsId);
            } else {
                activity.showErrorView();
            }
        } else {
            activity.showErrorView();
        }
    }

    // 获取专辑信息
    private void getAlbumInfo(String albumsId) {
        model.getAlbumInfo(albumsId, new AlbumsInfoMainModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                delGet(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.showErrorView();
            }
        });
    }

    /**
     * 获取数据后的数据处理
     *
     * @param o
     */
    private void delGet(Object o) {
        s = (AlbumInfo) o;
        if (s != null) {
//            List<String> type = new ArrayList<>();
//            type.add("详情");
//            type.add("节目");
//            type.add("相似");
            activity.initFragment(s);
            // mFragment.add(newInstance(s));
    /*                    String albumsID = s.data.album.id;
                        mFragment.add(ProgramInfoFragment.newInstance(albumsID));
                        mFragment.add(SimilarInfoFragment.newInstance(albumsID));
                        mAdapter = new MyAdapter(getSupportFragmentManager(), type, mFragment);*/
            activity.setResultData(s);
            activity.showContentView();
        } else {
            activity.showErrorView();
        }
    }

    public boolean setMenuData(AlbumMenuDialog view){
        if(s!=null){
            view.setMenuData(s);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 订阅按钮的点击事件
     */
    public void follow() {
        try {
            if (s.data.album.had_subscibed == true) {
                deleteSubscriptionsAlbums();
            } else {
                subscriptionsAlbums();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送订阅请求
    private void subscriptionsAlbums() {
        model.follow(albumsId, new AlbumsInfoMainModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealFollow(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                T.getInstance().showToast("订阅失败");
            }
        });
    }

    // 处理订阅返回数据
    private void dealFollow(Object o) {
        BaseResult baseResult = (BaseResult) o;
        if (baseResult != null && baseResult.ret == 0) {
            if (s != null && s.data != null && s.data.album != null) {
                s.data.album.subscriptions_count = s.data.album.subscriptions_count + 1;
                s.data.album.had_subscibed = true;
                activity.setResultData(s);
            }
            T.getInstance().showToast("订阅成功");
        } else {
            if (baseResult != null)
                T.getInstance().showToast(baseResult.msg);
            else
                T.getInstance().showToast("订阅失败");
        }
    }

    // 发送取消订阅请求
    private void deleteSubscriptionsAlbums() {
        model.unFollow(albumsId, new AlbumsInfoMainModel.OnLoadInterface() {
            @Override
            public void onSuccess(Object o) {
                activity.dialogCancel();
                dealUnFollow(o);
            }

            @Override
            public void onFailure(String msg) {
                activity.dialogCancel();
                T.getInstance().showToast("取消订阅失败");
            }
        });
    }

    // 处理取消订阅返回数据
    private void dealUnFollow(Object o) {
        BaseResult baseResult = (BaseResult) o;
        if (baseResult != null && baseResult.ret == 0) {
            if (s != null && s.data != null && s.data.album != null) {
                s.data.album.subscriptions_count = s.data.album.subscriptions_count - 1;
                s.data.album.had_subscibed = false;
                activity.setResultData(s);
            }
            T.getInstance().showToast("取消订阅成功");
        } else {
            if (baseResult != null)
                T.getInstance().showToast(baseResult.msg);
            else
                T.getInstance().showToast("取消订阅失败");
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }
}
