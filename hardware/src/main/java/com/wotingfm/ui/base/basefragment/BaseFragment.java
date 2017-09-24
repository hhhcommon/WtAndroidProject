package com.wotingfm.ui.base.basefragment;

import android.support.v4.app.Fragment;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;
import com.wotingfm.ui.play.find.main.view.LookListActivity;
import com.wotingfm.ui.play.live.LiveRoomActivity;
import com.wotingfm.ui.play.main.PlayerActivity;



public abstract class BaseFragment extends Fragment  {

    public void closeFragment() {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.close();
        } else if (getActivity() instanceof MineActivity) {
            MineActivity mineActivity = (MineActivity) getActivity();
            mineActivity.close();
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity interPhoneActivity = (InterPhoneActivity) getActivity();
            interPhoneActivity.close();
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity lookListActivity = (LookListActivity) getActivity();
            lookListActivity.close();
        }else if (getActivity() instanceof LiveRoomActivity) {
            LiveRoomActivity liveRoomActivity = (LiveRoomActivity) getActivity();
            liveRoomActivity.close();
        }
    }


    public void openFragment(Fragment fragment) {
        if (getActivity() instanceof PlayerActivity) {
            PlayerActivity playerActivity = (PlayerActivity) getActivity();
            playerActivity.open(fragment);
        } else if (getActivity() instanceof MineActivity) {
            MineActivity mineActivity = (MineActivity) getActivity();
            mineActivity.open(fragment);
        } else if (getActivity() instanceof InterPhoneActivity) {
            InterPhoneActivity interPhoneActivity = (InterPhoneActivity) getActivity();
            interPhoneActivity.open(fragment);
        } else if (getActivity() instanceof LookListActivity) {
            LookListActivity lookListActivity = (LookListActivity) getActivity();
            lookListActivity.open(fragment);
        }else if (getActivity() instanceof LiveRoomActivity) {
            LiveRoomActivity liveRoomActivity = (LiveRoomActivity) getActivity();
            liveRoomActivity.open(fragment);
        }
    }
}
