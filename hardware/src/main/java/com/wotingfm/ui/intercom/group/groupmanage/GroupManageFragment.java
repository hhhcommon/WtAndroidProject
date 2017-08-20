package com.wotingfm.ui.intercom.group.groupmanage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.config.GlobalStateConfig;
import com.wotingfm.common.utils.BeanCloneUtil;
import com.wotingfm.common.utils.ToastUtils;
import com.wotingfm.ui.intercom.group.applygrouptype.view.ApplyGroupTypeFragment;
import com.wotingfm.ui.intercom.group.editgroupmessage.view.EditGroupMessageFragment;
import com.wotingfm.ui.intercom.group.setmanager.view.SetManagerFragment;
import com.wotingfm.ui.intercom.group.standbychannel.view.StandbyChannelFragment;
import com.wotingfm.ui.intercom.group.transfergroup.view.TransferGroupFragment;
import com.wotingfm.ui.intercom.main.contacts.model.Contact;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import java.io.Serializable;
import java.util.List;

/**
 * 组管理界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class GroupManageFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private Contact.group group;
    private List<Contact.user> list;
    private ResultListener Listener;
    private boolean ow = false;
    private RelativeLayout re_groupManager, re_changeGroup;
    private TextView tv_groupManager;
    private boolean change = false;// 数据是否更改。根据这个参数返回时上层进行判断

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupmanager, container, false);
            rootView.setOnClickListener(this);
            getData();
            inItView();
        }
        return rootView;
    }

    private void getData() {
        try {
            group = (Contact.group) this.getArguments().getSerializable("group");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ow = this.getArguments().getBoolean("ow");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            list = (List<Contact.user>) this.getArguments().getSerializable("list");// 成员列表
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.re_groupNews).setOnClickListener(this);// 编辑资料
        tv_groupManager = (TextView) rootView.findViewById(R.id.tv_groupManager);
        re_groupManager = (RelativeLayout) rootView.findViewById(R.id.re_groupManager);
        re_groupManager.setOnClickListener(this);// 设置管理员
        rootView.findViewById(R.id.re_channel).setOnClickListener(this);// 设置备用频道
        rootView.findViewById(R.id.re_applyGroupType).setOnClickListener(this);// 更改加群方式
        re_changeGroup = (RelativeLayout) rootView.findViewById(R.id.re_changeGroup);
        re_changeGroup.setOnClickListener(this);// 转让群
        setView(ow);
    }

    // 是否是群主or管理员
    private void setView(boolean ow) {
        if (ow) {
            tv_groupManager.setVisibility(View.VISIBLE);
            re_groupManager.setVisibility(View.VISIBLE);
            re_changeGroup.setVisibility(View.VISIBLE);
        } else {
            tv_groupManager.setVisibility(View.GONE);
            re_groupManager.setVisibility(View.GONE);
            re_changeGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                setResult(change);
                InterPhoneActivity.close();
                break;
            case R.id.re_groupNews:// 编辑资料
                if (group != null) {
                    EditGroupMessageFragment fragment = new EditGroupMessageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", group);
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                    fragment.setResultListener(new EditGroupMessageFragment.ResultListener() {
                        @Override
                        public void resultListener(int type, String s) {
                            if (type == 1) {// 更改头像
                                group.setLogo_url(s);
                                // 更改上层界面数据
                                change = true;
                            } else if (type == 2) {// 更改昵称
                                group.setTitle(s);
                                // 更改上层界面数据
                                change = true;
                            } else if (type == 3) {// 更改地点
                                group.setLocation(s);
                                // 更改上层界面数据
                                change = true;
                            } else if (type == 4) {// 更改介绍
                                group.setIntroduction(s);
                                // 更改上层界面数据
                                change = true;
                            }
                        }
                    });
                } else {
                    ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                }
                break;
            case R.id.re_groupManager:// 设置管理员
                if (GlobalStateConfig.test) {
                    // 测试数据
                    InterPhoneActivity.open(new SetManagerFragment());
                } else {
                    // 实际数据
                    if (group != null && list != null && list.size() > 0  && group.getId() != null) {
                        List<Contact.user> _list= BeanCloneUtil.cloneTo(list);
                        SetManagerFragment fragment = new SetManagerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", (Serializable) _list);
                        bundle.putString("gid", group.getId());
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                        fragment.setResultListener(new SetManagerFragment.ResultListener() {
                            @Override
                            public void resultListener(boolean type, String id) {
                                if (type) {
                                    changeList(id);
                                    // 更改上层界面数据
                                    change = true;
                                }
                            }
                        });

                    } else {
                        ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                    }
                }
                break;
            case R.id.re_channel:// 设置备用频道
                if (GlobalStateConfig.test) {
                    StandbyChannelFragment fragment = new StandbyChannelFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("fromType", "message");
                    bundle.putString("groupId", "000");
                    bundle.putString("channel1", "");
                    bundle.putString("channel2", "");
                    fragment.setArguments(bundle);
                    InterPhoneActivity.open(fragment);
                } else {
                    if (group != null && group.getId() != null) {
                        String channel = "";
                        String channel1 = "";
                        String channel2 = "";
                        try {
                            channel = group.getChannel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (channel != null && !channel.equals("")) {
                            if (channel.contains(",")) {
                                String[] strArray = channel.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                                channel1 = strArray[0];
                                channel2 = strArray[1];
                            } else {
                                channel1 = channel;
                                channel2 = "";
                            }
                        }

                        StandbyChannelFragment fragment = new StandbyChannelFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("fromType", "message");
                        bundle.putString("groupId", group.getId());
                        bundle.putString("channel1", channel1);
                        bundle.putString("channel2", channel2);
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                        fragment.setResultListener(new StandbyChannelFragment.ResultListener() {
                            @Override
                            public void resultListener(boolean type, String channel) {
                                if (type) {
                                    group.setChannel(channel);
                                    // 更改上层界面数据
                                    change = true;
                                }
                            }
                        });

                    } else {
                        ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                    }
                }
                break;
            case R.id.re_applyGroupType:// 加群方式
                if (GlobalStateConfig.test) {
                    ApplyGroupTypeFragment fragment = new ApplyGroupTypeFragment();
                    InterPhoneActivity.open(fragment);
                } else {
                    if (group != null) {
                        ApplyGroupTypeFragment fragment = new ApplyGroupTypeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("group", group);
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                        fragment.setResultListener(new ApplyGroupTypeFragment.ResultListener() {
                            @Override
                            public void resultListener(int type, String password) {
                                group.setPassword(password);
                                group.setMember_access_mode(String.valueOf(type));
                                // 更改上层界面数据
                                change = true;
                            }
                        });
                    } else {
                        ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                    }
                }
                break;
            case R.id.re_changeGroup:// 转让群
                if (GlobalStateConfig.test) {
                    InterPhoneActivity.open(new TransferGroupFragment());
                } else {
                    if (group != null && list != null && list.size() > 0  && group.getId() != null) {
                        List<Contact.user> _list= BeanCloneUtil.cloneTo(list);
                        TransferGroupFragment fragment = new TransferGroupFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list", (Serializable) _list);
                        bundle.putString("gid", group.getId());
                        fragment.setArguments(bundle);
                        InterPhoneActivity.open(fragment);
                        fragment.setResultListener(new TransferGroupFragment.ResultListener() {
                            @Override
                            public void resultListener(boolean type) {
                                if (type) {
                                    // 更改上层界面数据
                                    setResult(true);
                                    // 已经不是群主,更改当前界面展示
                                    InterPhoneActivity.close();
                                }
                            }
                        });
                    } else {
                        ToastUtils.show_always(this.getActivity(), "数据出错了，请您稍后再试！");
                    }
                }
                break;
        }
    }

    /**
     * 更改成员数据
     *
     * @param id
     */
    private void changeList(String id) {
        if (id != null && !id.equals("")) {
            if (id.contains(",")) {
                String[] strArray = id.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                for (int i = 0; i < strArray.length; i++) {
                    String _id = strArray[i];
                    for (int j = 0; j < list.size(); j++) {
                        String pid = list.get(j).getId();
                        if (pid != null && !pid.equals("")) {
                            if (pid.equals(_id)) {
                                list.get(j).setIs_admin(true);
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    String pid = list.get(i).getId();
                    if (pid != null && !pid.equals("")) {
                        if (pid.equals(id)) {
                            list.get(i).setIs_admin(true);
                            break;
                        }
                    }
                }
            }
        }else{
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setIs_admin(false);
            }
        }
    }

    /**
     * 返回值设置
     *
     * @param type
     */
    public void setResult(boolean type) {
        Listener.resultListener(type);
    }

    /**
     * 回调结果值
     *
     * @param l
     */
    public void setResultListener(ResultListener l) {
        Listener = l;
    }

    public interface ResultListener {
        void resultListener(boolean type);
    }
}
