package com.woting.commonplat.gather;

import android.util.Log;

import com.woting.commonplat.constant.IntegerConstants;
import com.woting.commonplat.gather.model.DataModel;
import com.woting.commonplat.gather.thread.GivenUploadDataThread;
import com.woting.commonplat.gather.thread.ImmUploadDataThread;

import org.json.JSONObject;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 收集用户数据
 * Created by Administrator on 2017/4/11.
 */
public class GatherData {
    // 用户数据收集接口
    public static String url = "";
    public static boolean isRun = false;

    public static int uploadCount = IntegerConstants.DATA_UPLOAD_COUNT;// 指定上传的数量

    public static ArrayBlockingQueue<DataModel> immQueue = new ArrayBlockingQueue<>(1024);// 保存即时上传数据

    public static ArrayBlockingQueue<DataModel> givenList = new ArrayBlockingQueue<>(1024);// 保存定时或定量上传的数据

    private GatherData() {

    }

    /**
     * 初始化 开启上传数据的线程
     */
    public static void initThread() {
        // 防止 application 创建多次
        if (!isRun) {
            // 收集数据地址拼接
//            GlobalConfig.gatherData = "http://" +
//                    BSApplication.SharedPreferences.getString(CollocationConstant.socketUrl, "182.92.175.134") +
//                    ":708/sendGatherData.do";

//            Log.i("TAG", "数据收集地址 - >  " + GlobalConfig.gatherData);

            isRun = true;

            // 定量上传数据线程
            GivenUploadDataThread givenUploadDataThread = new GivenUploadDataThread();
            givenUploadDataThread.start();

            // 即时上传数据线程
            ImmUploadDataThread immUploadDataThread = new ImmUploadDataThread();
            immUploadDataThread.start();
        }
    }

    /**
     * 收集数据
     */
    public static void collectData(int uploadType, DataModel data) {
        switch (uploadType) {
            case IntegerConstants.DATA_UPLOAD_TYPE_IMM:// 即时上传
                try {
                    immQueue.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case IntegerConstants.DATA_UPLOAD_TYPE_GIVEN:// 定时检查上传
                try {
                    givenList.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 销毁线程
     */
    public static void destroyThread() {
        GatherData.isRun = false;

        Log.v("TAG", "GatherData Thread interrupt");
    }

    /**
     * 上传数据
     *
     * @param data 数据
     */
    public static void updateData(String data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
