package com.wotingfm.ui.mine.qrcodes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.commonplat.manager.CreateQRImageHelper;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.woting.commonplat.utils.BitmapUtils;
import com.wotingfm.R;
import com.wotingfm.common.utils.GlideUtils;
import com.wotingfm.ui.intercom.main.view.InterPhoneActivity;
import com.wotingfm.ui.mine.main.MineActivity;

/**
 * 展示二维码
 * 作者：xinlong on 2017/7/18 17:18
 * 邮箱：645700751@qq.com
 */
public class EWMShowFragment extends Fragment implements OnClickListener {
    private ImageView imageEwm;
    private ImageView imageHead;
    private TextView textName,tv_news;
    private Bitmap bmp;
    private View rootView;
    private String from = "person";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_ewm, container, false);
            rootView.setOnClickListener(this);
            initView();
            getData();
        }
        return rootView;
    }


    // 初始化视图
    private void initView() {
        rootView.findViewById(R.id.head_left_btn).setOnClickListener(this);// 返回
        imageEwm = (ImageView) rootView.findViewById(R.id.imageView_ewm);
        imageHead = (ImageView) rootView.findViewById(R.id.image);
        textName = (TextView) rootView.findViewById(R.id.name);
        tv_news = (TextView) rootView.findViewById(R.id.tv_news);

    }

    // 初始化视图
    private void getData() {
        if (getArguments() != null) {
            from = getArguments().getString("from");// 路径来源
            String image = getArguments().getString("image");// 头像
            String name = getArguments().getString("name");// 姓名
            String uri = getArguments().getString("uri");// 内容路径
            setData(uri, image, name);

            if (from.equals("person")) {
                tv_news.setText("扫一扫二维码，加我为好友。");
            } else if (from.equals("interPhone")) {
                tv_news.setText("扫一扫二维码，加入群组。");
            }
        }
    }

    // 初始化数据
    private void setData(String uri, String imageUrl, String name) {
        if (name != null && !name.equals("")) {
            textName.setText(name);
        }

        if (imageUrl != null && !imageUrl.equals("") && imageUrl.startsWith("http:")) {
            GlideUtils.loadImageViewSize(this.getActivity(), imageUrl, 80, 80, imageHead, true);
        } else {
            imageHead.setImageBitmap(BitmapUtils.readBitMap(this.getActivity(), R.mipmap.icon_avatar_d));
        }

        if (uri != null && !uri.equals("")) {
            bmp = CreateQRImageHelper.getInstance().createQRImage(uri, PhoneMsgManager.ScreenWidth-80, PhoneMsgManager.ScreenWidth-80);
        }

        if (bmp == null) {
            bmp = BitmapUtils.readBitMap(this.getActivity(), R.mipmap.ewm);
        }

        imageEwm.setImageBitmap(bmp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_btn:// 返回
                if (from.equals("person")) {
                    MineActivity.close();
                } else if (from.equals("interPhone")) {
                    InterPhoneActivity.close();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }
    }
}
