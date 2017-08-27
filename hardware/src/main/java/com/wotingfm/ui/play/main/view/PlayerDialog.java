package com.wotingfm.ui.play.main.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.ui.play.main.adapter.PlayerListAdapter;
import com.wotingfm.ui.bean.SinglesBase;

import java.util.ArrayList;
import java.util.List;

//播放节目选择dialog
public class PlayerDialog extends Dialog implements View.OnClickListener {

    private RecyclerView mRecyclerViewList;
    private TextView tvClose;

    private PlayerListAdapter playerListAdapter;
    private List<SinglesBase> singLesBeans = new ArrayList<>();

    public PlayerDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        setContentView(R.layout.player_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        inItView();
        inItListener();
        setData();
    }

    private void inItView() {
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
        playerListAdapter = new PlayerListAdapter(getContext(), singLesBeans);
        mRecyclerViewList.setAdapter(playerListAdapter);
    }

    /**
     * 展示dialog
     *
     * @param singLesBeansBase
     * @param pid
     * @param popPlay
     */
    public void showPlayDialog(final List<SinglesBase> singLesBeansBase, String pid, final PopPlayCallBack popPlay) {
        singLesBeans.clear();
        if (singLesBeansBase != null) {
            singLesBeans.addAll(singLesBeansBase);
            playerListAdapter.setPlayerClick(new PlayerListAdapter.PlayerClick() {
                @Override
                public void player(SinglesBase singlesBean, int position) {
                    if (popPlay != null) {
                        for (int i = 0, size = singLesBeans.size(); i < size; i++) {
                            SinglesBase p = singLesBeans.get(i);
                            p.isPlay = false;
                            singLesBeans.set(i, p);
                        }
                        playerListAdapter.setPlayId(singlesBean.id);
                        popPlay.play(singlesBean, position);
                        singlesBean.isPlay = true;
                        singLesBeans.set(position, singlesBean);
                        playerListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void close(SinglesBase singlesBean) {
                    if (singlesBean.isPlay == false) {
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
        void play(SinglesBase singlesBean, int position);

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
