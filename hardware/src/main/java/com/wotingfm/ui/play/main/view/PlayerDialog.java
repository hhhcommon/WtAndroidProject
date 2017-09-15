package com.wotingfm.ui.play.main.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.R;
import com.wotingfm.ui.bean.MessageEvent;
import com.wotingfm.ui.bean.SinglesBase;
import com.wotingfm.ui.play.main.PlayerFragment;
import com.wotingfm.ui.play.main.adapter.PlayerListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//播放节目选择dialog
public class PlayerDialog extends Dialog implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerViewList;
    private TextView tvClose;
    private PlayerListAdapter playerListAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    public PlayerDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        setContentView(R.layout.player_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        EventBus.getDefault().register(this);
        inItView();
        inItListener();
        setData();
    }

    private void inItView() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setColorSchemeResources(R.color.app_basic, R.color.app_basic, R.color.app_basic, R.color.app_basic);
        mSwipeLayout.setOnRefreshListener(this);

        LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) mSwipeLayout.getLayoutParams();
        params.height=PhoneMsgManager.ScreenHeight/2;
        mSwipeLayout.setLayoutParams(params);//将设置好的布局参数应用到控件中
        mRecyclerViewList = (RecyclerView) findViewById(R.id.mRecyclerViewList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerViewList.setLayoutManager(layoutManager);
        tvClose = (TextView) findViewById(R.id.tvClose);
    }

    private void inItListener() {
        tvClose.setOnClickListener(this);
    }

    private void setData() {
        playerListAdapter = new PlayerListAdapter(getContext(), PlayerFragment.singLesBeans);
        mRecyclerViewList.setAdapter(playerListAdapter);
    }

    /**
     * 展示dialog
     *
     * @param popPlay
     */
    public void showPlayDialog(final PopPlayCallBack popPlay) {
        if (PlayerFragment.singLesBeans != null) {
            playerListAdapter.setPlayerClick(new PlayerListAdapter.PlayerClick() {
                @Override
                public void player(int position) {
                    setIsPlay(PlayerFragment.singLesBeans.get(position));
                    playerListAdapter.notifyDataSetChanged();
                    if (popPlay != null) popPlay.play(position);
                }

                @Override
                public void close(int position) {
                    SinglesBase singlesBean = PlayerFragment.singLesBeans.get(position);
                    PlayerFragment.singLesBeans.remove(singlesBean);
                    playerListAdapter.notifyDataSetChanged();
                    if (popPlay != null) popPlay.close(singlesBean);
                }

                @Override
                public void getList(int position) {
                    if (popPlay != null) popPlay.getList(position);
                    dismiss();
                }
            });
            playerListAdapter.notifyDataSetChanged();
        }
    }

    private void setIsPlay(SinglesBase singlesBase) {
        for (int i = 0; i < PlayerFragment.singLesBeans.size(); i++) {
            PlayerFragment.singLesBeans.get(i).isPlay = false;
        }
        singlesBase.isPlay = true;
    }

    public interface PopPlayCallBack {
        void play(int position);
        void getList(int position);
        void close(SinglesBase singlesBean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                dismiss();
                break;
        }
    }

    @Override
    public void onRefresh() {
        Log.e("执行操作","刷新1");
        if( PlayerFragment.singLesBeans!=null&&PlayerFragment.singLesBeans.get(0)!=null){
            Log.e("执行操作","刷新");
           if(PlayerFragment.singLesBeans.get(0).isAlbumList) {
               refreshCancel();
           }else{
               EventBus.getDefault().post(new MessageEvent(2005));
           }
        }else{
            refreshCancel();
        }
    }

    private void refreshCancel() {
        mSwipeLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEventBase(MessageEvent messageEvent) {
        int type = messageEvent.getType();
        switch (type) {
            case 1100:
                refreshCancel();
                playerListAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void destroy(){
        EventBus.getDefault().unregister(this);
    }
}
