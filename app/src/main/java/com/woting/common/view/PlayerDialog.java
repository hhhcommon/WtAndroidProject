package com.woting.common.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woting.R;
import com.woting.common.adapter.PlayerListAdapter;
import com.woting.common.bean.SinglesBase;

import java.util.ArrayList;
import java.util.List;

//播放节目选择dialog
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
    private List<SinglesBase> singLesBeans = new ArrayList<>();


    public void showPlayDialo(final List<SinglesBase> singLesBeansBase, String pid, final PopPlayCallBack popPlay) {
        singLesBeans.clear();
        if (singLesBeansBase != null) {
            singLesBeans.addAll(singLesBeansBase);
            playerListAdapter.setPlayerClick(new PlayerListAdapter.PlayerClick() {
                @Override
                public void player(SinglesBase singlesBean, int postion) {
                    if (popPlay != null) {
                        for (int i = 0, size = singLesBeans.size(); i < size; i++) {
                            SinglesBase p = singLesBeans.get(i);
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
                public void close(SinglesBase singlesBean) {
                    if(singlesBean.isPlay==false) {
                        singLesBeans.remove(singlesBean);
                        if (popPlay != null)
                            popPlay.close(singlesBean);
                        playerListAdapter.notifyDataSetChanged();
                    }
                }
            }, pid);
            playerListAdapter.notifyDataSetChanged();
        }
    }

    public interface PopPlayCallBack {
        void play(SinglesBase singlesBean, int postion);

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


}