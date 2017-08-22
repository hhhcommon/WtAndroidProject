package com.woting.ui.intercom.group.setmanager.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.woting.commonplat.widget.TipView;
import com.woting.R;
import com.woting.common.utils.DialogUtils;
import com.woting.ui.intercom.group.setmanager.adapter.SetManagerAdapter;
import com.woting.ui.intercom.group.setmanager.presenter.SetManagerPresenter;
import com.woting.ui.intercom.main.contacts.model.Contact;
import com.woting.ui.intercom.main.view.InterPhoneActivity;

import java.util.List;

/**
 * 设置管理员界面
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class SetManagerFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private SetManagerPresenter presenter;
    private ListView listView;
    private SetManagerAdapter adapter;
    private Dialog dialog;
    private TipView tip_view;
    private int type;
    private ResultListener Listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_groupsetmanager, container, false);
            rootView.setOnClickListener(this);
            inItView();
            presenter = new SetManagerPresenter(this);
            presenter.getData();
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        tip_view = (TipView) rootView.findViewById(R.id.tip_view);// 提示界面
        listView = (ListView) rootView.findViewById(R.id.lv_manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.tv_send:
                presenter.send();
                break;
        }
    }

    /**
     * 设置界面
     *
     * @param list
     */
    public void setView(List<Contact.user> list) {
        if (adapter == null) {
            adapter = new SetManagerAdapter(this.getActivity(), list);
            listView.setAdapter(adapter);
        } else {
            adapter.ChangeDate(list);
        }
        setOnItemClickListener();
    }

    private void setOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.changeData(position);
            }
        });
    }

    /**
     * 是否登录，是否有数据
     *
     * @param type 登录后数据类型
     *             0 正常有数据
     *             NO_DATA,没有数据 1
     *             NO_NET,没有网络 2
     *             NO_LOGIN,没有登录 3
     *             IS_ERROR,加载错误 4
     */
    public void isLoginView(int type) {
        this.type = type;
        if (type == 0) {
            // 已经登录，并且有数据
            listView.setVisibility(View.VISIBLE);
            tip_view.setVisibility(View.GONE);
        } else if (type == 1) {
            // 已经登录，没有数据
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_DATA);
        } else if (type == 2) {
            // 没有网络
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_NET);
        } else if (type == 3) {
            // 没有登录
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.NO_LOGIN);
        } else if (type == 4) {
            // 已经登录，数据加载失败
            listView.setVisibility(View.GONE);
            tip_view.setVisibility(View.VISIBLE);
            tip_view.setTipView(TipView.TipStatus.IS_ERROR);
        }
    }

    /**
     * 展示弹出框
     */
    public void dialogShow() {
        dialog = DialogUtils.Dialog(this.getActivity());
    }

    /**
     * 取消弹出框
     */
    public void dialogCancel() {
        if (dialog != null) dialog.dismiss();
    }

    /**
     * 返回值设置
     *
     * @param type
     * @param news
     */
    public void setResult(boolean type, String news) {
        Listener.resultListener(type, news);
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
        void resultListener(boolean type, String news);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("界面","执行销毁");
        presenter.destroy();
        presenter=null;
    }
}
