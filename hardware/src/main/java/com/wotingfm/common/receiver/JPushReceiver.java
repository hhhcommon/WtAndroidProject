package com.wotingfm.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.woting.commonplat.utils.JsonEncloseUtils;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;
import com.wotingfm.common.service.NotificationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 作者：xinlong on 2017/7/13 23:57
 * 邮箱：645700751@qq.com
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.e(TAG, "接收到推送数据，开始处理");
            printBundle(intent.getExtras());
//            Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(intent.getExtras()));
//			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//				loadNews(regId);
//				Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//				Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				NotificationService.saveM(bundle.getString(JPushInterface.EXTRA_MESSAGE));
//			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//				Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
//				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//				Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//				Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
//			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//				Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//				Log.e(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
//			} else {
//				Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//			}
        } catch (Exception e) {
        }

    }

    // 打印所有的 intent extra 数据
    private static void printBundle(Bundle bundle) {
        Map<String, Object> map = new HashMap<>();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                Log.e(TAG, "key:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                Log.e(TAG, "key:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.e(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        Log.e(TAG, "///////////////////////////////");
                        Log.e(TAG, "////                           ");
                        Log.e(TAG, "////  key:" + key);
                        Log.e(TAG, "////  myKey:" + myKey);
                        Log.e(TAG, "////  value:" + json.optString(myKey));
                        Log.e(TAG, "////                           ");
                        Log.e(TAG, "///////////////////////////////");
                        map.put(myKey, json.optString(myKey));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                if (key.equals("cn.jpush.android.MESSAGE")) {
                    Log.e(TAG, "///////////////////////////////");
                    Log.e(TAG, "////                           ");
                    Log.e(TAG, "////  key:" + key);
                    Log.e(TAG, "////  myKey:" + "message");
                    Log.e(TAG, "////  value:" + bundle.getString(key));
                    Log.e(TAG, "////                           ");
                    Log.e(TAG, "///////////////////////////////");
                    map.put("message", bundle.getString(key));
                }
                Log.e(TAG, "key:" + key + ", value:" + bundle.getString(key));
            }
        }
        // 组装展示消息
        String message = JsonEncloseUtils.jsonEnclose(map).toString();
        // 判断消息设置是否接收推送消息
        String type = BSApplication.SharedPreferences.getString(StringConstant.PUSH_MSG_SET, "true");
        if (type != null && !type.trim().equals("") && type.equals("true")) {
            NotificationService.saveM(message);
        }
    }

//    // 极光id绑定
//    private void loadNews(String s) {
//        try {
//            RetrofitUtils.getInstance().bingJG(s)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Object>() {
//                        @Override
//                        public void call(Object o) {
//                            try {
//                                Log.e("极光ID绑定返回数据", new GsonBuilder().serializeNulls().create().toJson(o));
//                                //填充UI
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            throwable.printStackTrace();
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
