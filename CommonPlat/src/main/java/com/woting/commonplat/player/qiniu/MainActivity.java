//package com.woting.commonplat.player.qiniu;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//import com.pili.pldroid.player.AVOptions;
//import com.pili.pldroid.player.PLNetworkManager;
//import com.pili.pldroid.playerdemo.utils.GetPathFromUri;
//import com.woting.commonplat.R;
//
//import java.net.UnknownHostException;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//    private static final String DEFAULT_TEST_URL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//
//    private static final String[] DEFAULT_PLAYBACK_DOMAIN_ARRAY = {
//            "live.hkstv.hk.lxdns.com",
//    };
//
//    private Spinner mActivitySpinner;
//    private EditText mEditText;
//    private RadioGroup mStreamingTypeRadioGroup;
//    private RadioGroup mDecodeTypeRadioGroup;
//    private CheckBox mVideoCacheCheckBox;
//    private CheckBox mLoopCheckBox;
//    private CheckBox mVideoDataCallback;
//    private CheckBox mAudioDataCallback;
//
//    public static final String[] TEST_ACTIVITY_ARRAY = {
//            "PLMediaPlayerActivity",
//            "PLAudioPlayerActivity",
//            "PLVideoViewActivity",
//            "PLVideoTextureActivity",
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        try {
//            PLNetworkManager.getInstance().startDnsCacheService(this, DEFAULT_PLAYBACK_DOMAIN_ARRAY);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//
//        TextView mVersionInfoTextView = (TextView) findViewById(R.id.version_info);
//        mVersionInfoTextView.setText("版本号: " + BuildConfig.VERSION_NAME);
//
//        mEditText = (EditText) findViewById(R.id.VideoPathEdit);
//        mEditText.setText(DEFAULT_TEST_URL);
//
//        mStreamingTypeRadioGroup = (RadioGroup) findViewById(R.id.StreamingTypeRadioGroup);
//        mDecodeTypeRadioGroup = (RadioGroup) findViewById(R.id.DecodeTypeRadioGroup);
//
//        mActivitySpinner = (Spinner) findViewById(R.id.TestSpinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, TEST_ACTIVITY_ARRAY);
//        mActivitySpinner.setAdapter(adapter);
//        mActivitySpinner.setSelection(2);
//
//        mVideoCacheCheckBox = (CheckBox) findViewById(R.id.CacheCheckBox);
//        mLoopCheckBox = (CheckBox) findViewById(R.id.LoopCheckBox);
//        mVideoDataCallback = (CheckBox) findViewById(R.id.VideoCallback);
//        mAudioDataCallback = (CheckBox) findViewById(R.id.AudioCallback);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        PLNetworkManager.getInstance().stopDnsCacheService(this);
//    }
//
//    public void onClickLocalFile(View v) {
//        Intent intent = new Intent();
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setType("video/*");
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("video/*");
//        }
//        startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), 0);
//    }
//
//    public void onClickScanQrcode(View v){
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//        integrator.setOrientationLocked(true);
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(true);
//        integrator.initiateScan();
//    }
//
//    public void onClickPlay(View v) {
//        String videopath = mEditText.getText().toString();
//        if (!"".equals(videopath)) {
//            jumpToPlayerActivity(videopath);
//        }
//    }
//
//    public void jumpToPlayerActivity(String videopath) {
//        Class<?> cls;
//        switch (mActivitySpinner.getSelectedItemPosition()) {
//            case 0:
//                cls = PLMediaPlayerActivity.class;
//                break;
//            case 1:
//                cls = PLAudioPlayerActivity.class;
//                break;
//            case 2:
//                cls = PLVideoViewActivity.class;
//                break;
//            case 3:
//                cls = PLVideoTextureActivity.class;
//                break;
//            default:
//                return;
//        }
//        Intent intent = new Intent(this, cls);
//        intent.putExtra("videoPath", videopath);
//        if (mDecodeTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioHWDecode) {
//            intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_HW_DECODE);
//        } else if (mDecodeTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioSWDecode) {
//            intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
//        } else {
//            intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
//        }
//        if (mStreamingTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioLiveStreaming) {
//            intent.putExtra("liveStreaming", 1);
//        } else {
//            intent.putExtra("liveStreaming", 0);
//        }
//        intent.putExtra("cache", mVideoCacheCheckBox.isChecked());
//        intent.putExtra("loop", mLoopCheckBox.isChecked());
//        intent.putExtra("video-data-callback", mVideoDataCallback.isChecked());
//        intent.putExtra("audio-data-callback", mAudioDataCallback.isChecked());
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//        if (requestCode == 0) {
//            String selectedFilepath = GetPathFromUri.getPath(this, data.getData());
//            Log.i(TAG, "Select file: " + selectedFilepath);
//            if (selectedFilepath != null && !"".equals(selectedFilepath)) {
//                mEditText.setText(selectedFilepath, TextView.BufferType.EDITABLE);
//            }
//        } else if (requestCode ==  IntentIntegrator.REQUEST_CODE) {
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            if(result != null) {
//                if(result.getContents() == null) {
//                    Toast.makeText(this, "扫码取消！", Toast.LENGTH_SHORT).show();
//                } else {
//                    mEditText.setText(result.getContents());
//                }
//            }
//        }
//    }
//}
