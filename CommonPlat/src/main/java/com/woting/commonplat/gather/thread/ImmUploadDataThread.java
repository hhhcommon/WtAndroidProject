package com.woting.commonplat.gather.thread;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woting.commonplat.gather.GatherData;
import com.woting.commonplat.gather.model.DataModel;
import com.woting.commonplat.net.volley.VolleyCallback;
import com.woting.commonplat.net.volley.VolleyRequest;
import com.woting.commonplat.utils.JsonEncloseUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 立即上传数据线程
 * Created by Administrator on 2017/4/11.
 */
public class ImmUploadDataThread extends Thread {


    @Override
    public void run() {
        while (GatherData.isRun) {
            try {
                DataModel data = GatherData.immQueue.take();
                if (data != null) {
                    ArrayList<String> list = new ArrayList<>();
                    String jsonStr = JsonEncloseUtils.btToString(data);
                    list.add(jsonStr);

                    // 上传数据
                    String jsonString = JsonEncloseUtils.jsonEnclose(list).toString();
                    Log.v("TAG", "IMM jsonStr -- > > " + jsonString);
                    GatherData.updateData(jsonString);
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
