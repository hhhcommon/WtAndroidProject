package com.wotingfm.ui.play.live.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.live.DemoCache;
import com.netease.nim.live.adapter.MemberAdapter;
import com.netease.nim.live.base.BaseFragment;
import com.netease.nim.live.widget.CircleImageView;
import com.netease.nim.uikit.R;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.woting.commonplat.widget.GlideCircleTransform;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.bean.LiveBean;
import com.wotingfm.ui.play.live.LiveRoomActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.wotingfm.R.id.ivPhoto;

/**
 * Created by zhukkun on 1/9/17.
 */
public class LiveRoomInfoFragment extends BaseFragment {

    public static final String EXTRA_IS_AUDIENCE = "is_audence";

    public static LiveRoomInfoFragment getInstance(boolean isAudience, LiveBean.DataBean dataBean) {
        LiveRoomInfoFragment fragment = new LiveRoomInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_AUDIENCE, isAudience);
        bundle.putSerializable("dataBean", dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    boolean isAudience;
    TextView tvOnlineCount;
    TextView tvRoomName;
    TextView tvTimeLeft;
    TextView tvMasterName;
    private CircleImageView master_head;

    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    int onlineCount;

    Handler handler = new Handler();
    private int recLen = 0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            String hms = formatter.format(recLen * 1000);
            tvTimeLeft.setText(hms);
            handler.postDelayed(this, 1000);
        }
    };
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private LiveBean.DataBean dataBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isAudience = getArguments().getBoolean(EXTRA_IS_AUDIENCE, true);
        dataBean = (LiveBean.DataBean) getArguments().getSerializable("dataBean");
        return inflater.inflate(isAudience ? R.layout.layout_live_audience_room_info : R.layout.layout_live_captrue_room_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        tvOnlineCount = findView(R.id.online_count_text);
        tvTimeLeft = findView(R.id.tvTimeLeft);
        tvRoomName = findView(R.id.room_name);
        master_head = findView(R.id.master_head);
        recyclerView = findView(R.id.rv_room_member);
        initRecycleView();
        if (isAudience) {
            tvMasterName = findView(R.id.master_name);
        }
        if (dataBean != null && dataBean.owner != null) {
            tvMasterName.setText(dataBean.owner.name);
            Glide.with(BSApplication.getInstance()).load(dataBean.owner.avatar)// Glide
                    .error(com.wotingfm.R.mipmap.oval_defut_photo)
                    .placeholder(com.wotingfm.R.mipmap.oval_defut_photo)
                    .into(master_head);
        }
        handler.postDelayed(runnable, 1000);
    }

    private void initRecycleView() {
        memberAdapter = new MemberAdapter(getContext());
        recyclerView.setAdapter(memberAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        memberAdapter.setOnItemClickListener(new MemberAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ChatRoomMember member) {
                ((LiveRoomActivity) getActivity()).onMemberOperate(member);
            }
        });
    }

    public void updateMember(List<ChatRoomMember> members) {
        memberAdapter.updateMember(members);
        onlineCount = members.size();
        tvOnlineCount.setText(onlineCount + "人");
    }

    public void refreshRoomInfo(ChatRoomInfo roomInfo) {
        onlineCount = roomInfo.getOnlineUserCount();
        tvOnlineCount.setText(onlineCount + "人");
        tvRoomName.setText(roomInfo.getRoomId());
       /* if (isAudience) {
            tvMasterName.setText(roomInfo.getCreator());
        }*/
    }

    public void onReceivedNotification(ChatRoomNotificationAttachment attachment) {

        ChatRoomMember chatRoomMember = new ChatRoomMember();
        chatRoomMember.setAccount(attachment.getTargets().get(0));
        chatRoomMember.setNick(attachment.getTargetNicks().get(0));

        if (!isAudience && chatRoomMember.getAccount().equals(DemoCache.getAccount())) {
            //主播的通知(主播进入房间,主播离开房间)不做处理,
            return;
        }

        switch (attachment.getType()) {
            case ChatRoomMemberIn:
                if (memberAdapter.addMember(chatRoomMember)) {
                    tvOnlineCount.setText(++onlineCount + "人");
                }
                break;
            case ChatRoomMemberExit:
            case ChatRoomMemberKicked:
                memberAdapter.removeMember(chatRoomMember);
                tvOnlineCount.setText(--onlineCount + "人");
                break;
            case ChatRoomMemberMuteAdd:
                chatRoomMember.setMuted(true);
                memberAdapter.updateSingleMember(chatRoomMember);
                break;
            case ChatRoomMemberMuteRemove:
                chatRoomMember.setMuted(false);
                memberAdapter.updateSingleMember(chatRoomMember);
                break;
        }
    }

    public void updateMemberState(ChatRoomMember current_operate_member) {
        memberAdapter.updateSingleMember(current_operate_member);
    }

    public ChatRoomMember getMember(String fromAccount) {
        return memberAdapter.getMember(fromAccount);
    }
}
