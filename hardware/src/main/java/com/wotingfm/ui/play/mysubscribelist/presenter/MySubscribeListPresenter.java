package com.wotingfm.ui.play.mysubscribelist.presenter;

import com.wotingfm.common.utils.CommonUtils;
import com.wotingfm.common.utils.T;
import com.wotingfm.ui.bean.AlbumsBean;
import com.wotingfm.ui.play.mysubscribelist.model.MySubscribeListModel;
import com.wotingfm.ui.play.mysubscribelist.view.MeSubscribeListFragment;
import java.util.List;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class MySubscribeListPresenter {

    private MySubscribeListModel model;
    private MeSubscribeListFragment activity;
    private List<AlbumsBean> albumsBeen;

    public MySubscribeListPresenter(MeSubscribeListFragment activity) {
        this.activity = activity;
        this.model = new MySubscribeListModel();
        activity.showLoadingView();
        getPlayerList(CommonUtils.getUserId());
    }

    /**
     * 获取数据
     *
     * @param uid
     */
    private void getPlayerList(String uid) {
        model.getSubscriptionsList(uid, new MySubscribeListModel.OnLoadInterface() {
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
     * 获取我的订阅返回数据后的数据处理
     *
     * @param o
     */
    private void delGet(Object o) {
        albumsBeen = (List<AlbumsBean>) o;
        if (albumsBeen != null && !albumsBeen.isEmpty()) {
            activity.setData(albumsBeen);
            activity.showContentView();
        } else {
            activity.showEmptyView();
        }
    }

    /**
     * 取消订阅
     *
     * @param albumsBean
     */
    public void unFollowPlayer(final AlbumsBean albumsBean) {
        if (albumsBean.id != null) {
            activity.dialogShow();
            model.unFollowPlayer(albumsBean.id, new MySubscribeListModel.OnLoadInterface() {
                @Override
                public void onSuccess(Object o) {
                    activity.dialogCancel();
                    albumsBeen.remove(albumsBean);
                    T.getInstance().showToast("取消成功");
                    activity.setData(albumsBeen);
                }

                @Override
                public void onFailure(String msg) {
                    activity.dialogCancel();
                    T.getInstance().showToast("取消订阅失败");
                }
            });
        }
    }

    /**
     * 数据销毁
     */
    public void destroy() {
        model = null;
    }

}
