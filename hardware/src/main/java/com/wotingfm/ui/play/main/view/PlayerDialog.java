package com.wotingfm.ui.play.main.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woting.commonplat.amine.ARecyclerView;
import com.woting.commonplat.amine.OnRefreshListener;
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
public class PlayerDialog extends Dialog implements View.OnClickListener, OnRefreshListener {

    private ARecyclerView mRecyclerViewList;
    private TextView tvClose;
    private PlayerListAdapter playerListAdapter;

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
        mRecyclerViewList = (ARecyclerView) findViewById(R.id.mRecyclerViewList);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PhoneMsgManager.ScreenHeight/2, PhoneMsgManager.ScreenWidth);
        mRecyclerViewList.setLayoutParams(params);//将设置好的布局参数应用到控件中

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewList.setOnRefreshListener(this);
        mRecyclerViewList.setLayoutManager(layoutManager);
        tvClose = (TextView) findViewById(R.id.tvClose);
    }

    private void inItListener() {
        tvClose.setOnClickListener(this);
    }

    private void setData() {
        playerListAdapter = new PlayerListAdapter(getContext(), PlayerFragment.singLesBeans);
        mRecyclerViewList.setIAdapter(playerListAdapter);
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
                    setIsPlay(PlayerFragment.singLesBeans.get(position-2));
                    playerListAdapter.notifyDataSetChanged();
                    if (popPlay != null) popPlay.play(position-2);
                }

                @Override
                public void close(int position) {
                    SinglesBase singlesBean = PlayerFragment.singLesBeans.get(position-2);
                    PlayerFragment.singLesBeans.remove(singlesBean);
                    playerListAdapter.notifyDataSetChanged();
                    if (popPlay != null) popPlay.close(singlesBean);
                }

                @Override
                public void getList(int position) {
                    if (popPlay != null) popPlay.getList(position-2);
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
               mRecyclerViewList.setRefreshing(false);
           }else{
               EventBus.getDefault().post(new MessageEvent(2005));
           }
        }else{
            mRecyclerViewList.setRefreshing(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEventBase(MessageEvent messageEvent) {
        int type = messageEvent.getType();
        switch (type) {
            case 1100:
                mRecyclerViewList.setRefreshing(false);
                playerListAdapter.notifyDataSetChanged();
                break;
        }
    }


    public void destroy(){
        EventBus.getDefault().unregister(this);
    }
}
