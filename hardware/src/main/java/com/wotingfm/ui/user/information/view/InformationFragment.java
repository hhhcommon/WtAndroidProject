package com.wotingfm.ui.user.information.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.user.information.presenter.InformationPresenter;
import com.wotingfm.ui.user.logo.LogoActivity;

/**
 * 信息完善界面
 * 作者：xinLong on 2017/6/4 19:45
 * 邮箱：645700751@qq.com
 */
public class InformationFragment extends Fragment implements View.OnClickListener {
    private TextView tv_news, tv_commit;
    private ImageView img_url;
    private EditText et_name;
    private InformationPresenter Presenter;
    private View rootView;
    private Dialog dialog, imgDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_information, container, false);
            rootView.setOnClickListener(this);
            inItView();
            imgDialog();
            Presenter = new InformationPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        img_url = (ImageView) rootView.findViewById(R.id.img_url);            // 头像
        tv_news = (TextView) rootView.findViewById(R.id.tv_news);             // 消息
        et_name = (EditText) rootView.findViewById(R.id.et_name);             // 输入框
        tv_commit = (TextView) rootView.findViewById(R.id.tv_commit);         // 完成
        img_url.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
    }

    // 监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_url:
                // 头像上传
                if (imgDialog != null) {
                    imgDialog.show();
                }
                break;
            case R.id.tv_commit:
                String name = et_name.getText().toString().trim();
                Presenter.send(name);
                break;
            case R.id.tv_paizhao:
                Presenter.camera();
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
            case R.id.tv_xiangce:
                Presenter.photoAlbum();
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
            case R.id.tv_quxiao:
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
        }
    }

    /**
     * 设置头像
     *
     * @param url
     */
    public void setImageUrl(String url) {
        if (url != null && !url.equals("")) {
            GlideUtils.loadImageViewRound(url, img_url, 150,150);
            tv_news.setVisibility(View.INVISIBLE);
        } else {
            GlideUtils.loadImageViewRound(R.mipmap.icon_avatar_d, img_url, 72, 72);
            tv_news.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 关闭当前界面
     */
    public void close() {
        LogoActivity.close();
    }

    // 上传头像选择框
    private void imgDialog() {
        final View dialog = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_updata_img, null);
        dialog.findViewById(R.id.tv_paizhao).setOnClickListener(this);  // 拍照
        dialog.findViewById(R.id.tv_xiangce).setOnClickListener(this);  // 相册
        dialog.findViewById(R.id.tv_quxiao).setOnClickListener(this);   // 取消

        imgDialog = new Dialog(this.getActivity(), R.style.MyDialogs);
        imgDialog.setContentView(dialog);
        imgDialog.setCanceledOnTouchOutside(true);

        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        ViewGroup.LayoutParams params = dialog.getLayoutParams();
        params.width = screenWidth;
        dialog.setLayoutParams(params);

        Window window = imgDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.inOutStyle);
        window.setBackgroundDrawableResource(R.color.transparent_40_black);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Presenter.destroy();
        Presenter=null;
    }
}
