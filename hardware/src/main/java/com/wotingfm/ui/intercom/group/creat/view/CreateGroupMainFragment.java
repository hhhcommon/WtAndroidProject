package com.wotingfm.ui.intercom.group.creat.view;

import android.app.Dialog;
import android.graphics.Bitmap;
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
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.DialogUtils;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.group.creat.presenter.CreateGroupMainPresenter;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;

/**
 * 创建群主页
 * 作者：xinLong on 2017/6/5 01:30
 * 邮箱：645700751@qq.com
 */
public class CreateGroupMainFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private ImageView img_url, img_password, img_shen;
    private EditText et_phoneNumber, et_password;
    private TextView tv_send,tv_show;
    private CreateGroupMainPresenter presenter;
    private Dialog dialog,imgDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_creatgroup, container, false);
            rootView.setOnClickListener(this);
            inItView();
            imgDialog();
            presenter = new CreateGroupMainPresenter(this);
        }
        return rootView;
    }

    private void inItView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);
        img_url = (ImageView) rootView.findViewById(R.id.img_url);          // 群头像
        tv_show = (TextView) rootView.findViewById(R.id.tv_show);              // 提示
        img_url.setOnClickListener(this);
        img_password = (ImageView) rootView.findViewById(R.id.img_password);// 密码群
        img_password.setOnClickListener(this);
        img_shen = (ImageView) rootView.findViewById(R.id.img_shen);        // 审核群
        img_shen.setOnClickListener(this);

        et_phoneNumber = (EditText) rootView.findViewById(R.id.et_phoneNumber);// 群名
        et_password = (EditText) rootView.findViewById(R.id.et_password);      // 群密码
        tv_send = (TextView) rootView.findViewById(R.id.tv_send);              // 提交
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:
                InterPhoneActivity.close();
                break;
            case R.id.img_url:
                // 头像上传
                if (imgDialog != null) {
                    imgDialog.show();
                }
                break;
            case R.id.img_password:
                presenter.setPassword();
                break;
            case R.id.img_shen:
                presenter.setSen();
                break;
            case R.id.tv_send:
                String name = et_phoneNumber.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                presenter.send(name, password);
                break;
            case R.id.tv_paizhao:
                presenter.camera();
                if (imgDialog != null) {
                    imgDialog.dismiss();
                }
                break;
            case R.id.tv_xiangce:
                presenter.photoAlbum();
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
            GlideUtils.loadImageViewSize(this.getActivity(),url, 72, 72, img_url, false);
            tv_show.setVisibility(View.INVISIBLE);
        } else {
            Bitmap bmp = BitmapUtils.readBitMap(this.getActivity(), R.mipmap.icon_avatar_d);
            img_url.setImageBitmap(bmp);
            tv_show.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置密码群样式
     */
    public void setViewM(boolean b) {
        if(b){
            img_password.setImageResource(R.mipmap.create_group_icon_selected_s);
        }else{
            img_password.setImageResource(R.mipmap.create_group_icon_selected_n);
        }
    }

    /**
     * 设置审核群样式
     */
    public void setViewS(boolean b) {
        if(b){
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_s);
        }else{
            img_shen.setImageResource(R.mipmap.create_group_icon_selected_n);
        }
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
        presenter.destroy();
    }
}
