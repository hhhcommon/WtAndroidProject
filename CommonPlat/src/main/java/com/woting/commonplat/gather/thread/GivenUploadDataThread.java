package com.woting.commonplat.gather.thread;

import android.util.Log;

import com.woting.commonplat.gather.GatherData;
import com.woting.commonplat.gather.model.DataModel;
import com.woting.commonplat.utils.JsonEncloseUtils;

import java.util.ArrayList;

/**
 * 定时、定量上传数据线程
 * Created by Administrator on 2017/4/11.
 */
public class GivenUploadDataThread extends Thread {
    private volatile Object Lock = new Object();            // 锁

    @Override
    public void run() {
        while (GatherData.isRun) {
            Log.v("TAG", "Gather Data Thread start");
            try {
                if (GatherData.givenList.size() >= GatherData.uploadCount) {
                    synchronized (Lock) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < GatherData.givenList.size(); i++) {
                            DataModel n = GatherData.givenList.take();
                            String jsonStr = JsonEncloseUtils.btToString(n);
                            list.add(jsonStr);
                        }

                        String jsonString = JsonEncloseUtils.jsonEnclose(list).toString();
                        if (jsonString != null) {
                            Log.v("TAG", "GIVEN jsonStr -- > > " + jsonString);
                            // 上传数据
                            GatherData.updateData(jsonString);
                            list.clear();
                        }
                    }
                }
                Thread.sleep(2 * 1000 * 60);// 一定时间检查一次  如果有数据则上传
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
