package com.wotingfm.ui.photocut;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

import com.woting.commonplat.widget.photocut.ClipImageLayout;
import com.wotingfm.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 照片裁剪页
 * 作者：xinlong on 2016/11/6 21:18
 * 邮箱：645700751@qq.com
 */
public class PhotoCutActivity extends Activity implements OnClickListener {
    private Bitmap bitmap;
    private ClipImageLayout mClipImageLayout;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photocut);
        initView();
    }

    // 初始化视图
    private void initView() {
        findViewById(R.id.lin_save).setOnClickListener(this);// 保存
        findViewById(R.id.head_left_btn).setOnClickListener(this);// 保存

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);

        if (getIntent() != null) {
            Intent intent = getIntent();
            String imageUrl = intent.getStringExtra("URI");
            type = intent.getIntExtra("type", -1);
            if (imageUrl != null && !imageUrl.equals("")) {
                mClipImageLayout.setImage(PhotoCutActivity.this, Uri.parse(imageUrl));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_save:
                bitmap = mClipImageLayout.clip();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)) {
                    try {
                        if (type == 1) {
                            long a = System.currentTimeMillis();
                            String s = String.valueOf(a);
                            // 找不到文件夹会有问题  是不是要需要判断没有的话就创建文件夹？
                            FileOutputStream out = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/woting/image/" + s + ".png"));
                            out.write(outputStream.toByteArray());
                            out.flush();
                            out.close();
                            Intent intent = new Intent();
                            intent.putExtra("return", Environment.getExternalStorageDirectory() + "/woting/image/" + s + ".png");
                            setResult(1, intent);
                        } else {
                            FileOutputStream out = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/woting/image/portaitUser.png"));
                            out.write(outputStream.toByteArray());
                            out.flush();
                            out.close();
                            setResult(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();
                break;
            case R.id.head_left_btn:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        if (mClipImageLayout != null) {
            mClipImageLayout.CloseResource();
            mClipImageLayout = null;
        }
    }
}
