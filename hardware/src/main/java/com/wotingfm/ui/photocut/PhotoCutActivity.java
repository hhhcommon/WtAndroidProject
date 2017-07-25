package com.wotingfm.ui.photocut;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.woting.commonplat.widget.photocut.ClipImageLayout;
import com.wotingfm.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        switch (v.getId()) {
            case R.id.lin_save:
                bitmap = mClipImageLayout.clip();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)) {
                    String f1 = getSDPath() + "/woting";
                    File file1 = new File(f1);

                    if (!file1.exists()) {
                        if (!file1.mkdirs()) {
                            Log.e("photoCut1", "Directory not created");
                        }
                    }
                    String f2 = getSDPath() + "/woting/image/";
                    File file2 = new File(f2);
                    if (!file2.exists()) {
                        if (!file2.mkdirs()) {
                            Log.e("photoCut2", "Directory not created");
                        }
                    }
                    String p = getSDPath() + "/woting/image/" + String.valueOf(System.currentTimeMillis()) + ".png";
                    save(outputStream, p);
                }
                finish();
                break;
            case R.id.head_left_btn:
                finish();
                break;
        }
    }

    private void save(ByteArrayOutputStream outputStream, String path) {
        try {
            // 找不到文件夹会有问题  是不是要需要判断没有的话就创建文件夹？
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                Log.e("photoCut3", "Directory created");

            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(outputStream.toByteArray());
            out.flush();
            out.close();
            Intent intent = new Intent();
            intent.putExtra("return", path);
            setResult(1, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
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
