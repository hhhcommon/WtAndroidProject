package com.wotingfm.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.adapter.PlayerAdapter;
import com.wotingfm.common.adapter.PlayerListAdapter;
import com.wotingfm.common.bean.Player;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.media.CamcorderProfile.get;
import static com.wotingfm.R.id.mRecyclerView;


public class PlayerDialog extends Dialog implements View.OnClickListener {

    private RecyclerView mRecyclerViewList;
    private TextView tvClose;

    public PlayerDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        setContentView(R.layout.player_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);

        mRecyclerViewList = (RecyclerView) findViewById(R.id.mRecyclerViewList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewList.setLayoutManager(layoutManager);
        playerListAdapter = new PlayerListAdapter(getContext(), singLesBeans);
        mRecyclerViewList.setAdapter(playerListAdapter);
        tvClose = (TextView) findViewById(R.id.tvClose);
        tvClose.setOnClickListener(this);
    }

    private PlayerListAdapter playerListAdapter;
    private List<Player.DataBean.SinglesBean> singLesBeans = new ArrayList<>();


    public void showPlayDialo(final List<Player.DataBean.SinglesBean> singLesBeansBase, String pid, final PopPlayCallBack popPlay) {
        singLesBeans.clear();
        if (singLesBeansBase != null) {
            singLesBeans.addAll(singLesBeansBase);
            playerListAdapter.setPlayerClick(new PlayerListAdapter.PlayerClick() {
                @Override
                public void player(Player.DataBean.SinglesBean singlesBean, int postion) {
                    if (popPlay != null) {
                        for (int i = 0, size = singLesBeans.size(); i < size; i++) {
                            Player.DataBean.SinglesBean p = singLesBeans.get(i);
                            p.isPlay =false;
                            singLesBeans.set(i, p);
                        }
                        playerListAdapter.setPlayId(singlesBean.id);
                        popPlay.play(singlesBean, postion);
                        singlesBean.isPlay =true;
                        singLesBeans.set(postion, singlesBean);
                        playerListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void close(Player.DataBean.SinglesBean singlesBean) {
                    singLesBeans.remove(singlesBean);
                    if(popPlay!=null)
                        popPlay.close(singlesBean);
                    playerListAdapter.notifyDataSetChanged();
                }
            }, pid);
            playerListAdapter.notifyDataSetChanged();
        }
    }

    public interface PopPlayCallBack {
        void play(Player.DataBean.SinglesBean singlesBean, int postion);

        void close(Player.DataBean.SinglesBean singlesBean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                dismiss();
                break;
        }
    }


}
