package com.wotingfm.ui.mine.bluetooth.presenter;

import android.bluetooth.BluetoothAdapter;

import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.bluetooth.view.EditBlueToothNameFragment;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 作者：xinLong on 2017/6/5 13:55
 * 邮箱：645700751@qq.com
 */
public class EditBlueToothPresenter {

    private EditBlueToothNameFragment activity;

    public EditBlueToothPresenter(EditBlueToothNameFragment activity) {
        this.activity = activity;
        getName();
    }

    private void getName() {
        String name = BluetoothAdapter.getDefaultAdapter().getName();
        if (name != null && !name.toString().equals("")) {
            activity.setName(name);
        } else {
            activity.setName("BlueTooth");
        }
    }

    /**
     * 保存蓝牙名称
     *
     * @param s
     */
    public void save(final String s) {
        if (s != null && !s.trim().equals("") && s.trim().length() < 20) {
            boolean b = BluetoothAdapter.getDefaultAdapter().setName(s);//设置蓝牙名称
            if (b) {
                activity.setResult(true, s);
                activity.closeFragment();
            } else {
                ToastUtils.show_always(activity.getActivity(), "修改失败，请稍后再试！");
            }
        } else {
            ToastUtils.show_always(activity.getActivity(), "蓝牙名称的长度必须为1-20");
        }
    }

}
