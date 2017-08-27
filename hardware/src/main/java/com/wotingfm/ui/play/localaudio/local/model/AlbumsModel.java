package com.wotingfm.ui.play.localaudio.local.model;

import com.wotingfm.ui.bean.SinglesDownload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据中心
 * 作者：xinLong on 2017/5/16 14:28
 * 邮箱：645700751@qq.com
 */
public class AlbumsModel {

    private HashMap<String, SinglesDownload> hashMap = new HashMap<>();
    private HashMap<String, List<SinglesDownload>> hashMapList = new HashMap<>();

    public SinglesDownload getMapContent(String key) {
        if (hashMap == null && hashMap.isEmpty())
            return null;
        if (hashMap.containsKey(key)) {
            return hashMap.get(key);
        } else {
            return null;
        }
    }

    public List<SinglesDownload> getMapContentList(String key) {
        if (hashMapList == null && hashMapList.isEmpty())
            return new ArrayList<>();
        if (hashMapList.containsKey(key)) {
            return hashMapList.get(key);
        } else {
            return new ArrayList<>();
        }
    }

    public void mapPut(String album_id, SinglesDownload so) {
        hashMap.put(album_id, so);
    }

    public void mapListPut(String album_id, List<SinglesDownload> sos) {
        hashMapList.put(album_id, sos);
    }

    public HashMap<String, SinglesDownload> getMap() {
        return hashMap;
    }

    public HashMap<String, List<SinglesDownload>> getMapList() {
        return hashMapList;
    }
}
