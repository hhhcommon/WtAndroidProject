package com.wotingfm.common.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟对讲频率
 * 作者：xinlong on 2016/9/5 11:37
 * 邮箱：645700751@qq.com
 */
public class FrequencyUtil {

    public static String DefaultFrequnce = "CH01-409.7500";
    public static String DEFAULT_FREQUENCE_NOSHOW="409.75000";

    /**
     * 获取默认频率
     * @return
     */
    public static List<String> getFrequencyList() {
            List<String> frequencyList = new ArrayList<>();
            frequencyList.add("CH01-409.7500");
            frequencyList.add("CH02-409.7625");
            frequencyList.add("CH03-409.7750");
            frequencyList.add("CH04-409.7875");
            frequencyList.add("CH05-409.8000");
            frequencyList.add("CH06-409.8125");
            frequencyList.add("CH07-409.8250");
            frequencyList.add("CH08-409.8375");
            frequencyList.add("CH09-409.8500");
            frequencyList.add("CH10-409.8625");
            frequencyList.add("CH11-409.8750");
            frequencyList.add("CH12-409.8875");
            frequencyList.add("CH13-409.9000");
            frequencyList.add("CH14-409.9125");
            frequencyList.add("CH15-409.9250");
            frequencyList.add("CH16-409.9375");
            frequencyList.add("CH17-409.9500");
            frequencyList.add("CH18-409.9625");
            frequencyList.add("CH19-409.9750");
            frequencyList.add("CH20-409.9875");
            return frequencyList;
    }

    public static List<String> getFrequence(String freq) {
        if (!TextUtils.isEmpty(freq)) {
            String a[] = freq.split(",");
            List<String> frequencyList = getFrequencyList();
            if (frequencyList != null && frequencyList.size() > 0) {
                List<String> freqList = new ArrayList<>();
                for (int i = 0; i < a.length; i++) {
                    for (int j = 0; j < frequencyList.size(); j++) {
                        if (frequencyList.get(j).contains(a[i])) {
                            freqList.add(frequencyList.get(j));
                        }
                    }
                }
                if (freqList.size() > 0) {
                    return freqList;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

}
