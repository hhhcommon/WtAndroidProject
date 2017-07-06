package com.woting.commonplat.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *JsonEnclose
 * @author 辛龙
 *2016年7月21日
 */
public class JsonEncloseUtils {
	
	/**
	 * 将对象分装为json字符串
	 * @param obj
	 * @return
	 */
	public static  Object jsonEnclose(Object obj) {
		try {
			if (obj instanceof Map) {   //如果是Map则转换为JsonObject
				Map<String, Object> map = (Map<String, Object>)obj;
				Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
				JSONStringer jsonStringer = new JSONStringer().object();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					jsonStringer.key(entry.getKey()).value(jsonEnclose(entry.getValue()));
				}
				JSONObject jsonObject = new JSONObject(new JSONTokener(jsonStringer.endObject().toString()));
				return jsonObject;
			} else if (obj instanceof List) {  //如果是List则转换为JsonArray
				List<Object> list = (List<Object>)obj;
				JSONStringer jsonStringer = new JSONStringer().array();
				for (int i = 0; i < list.size(); i++) {
					jsonStringer.value(jsonEnclose(list.get(i)));
				}
				JSONArray jsonArray = new JSONArray(new JSONTokener(jsonStringer.endArray().toString()));
				return jsonArray;
			} else {
				return obj;
			}
		} catch (Exception e) {
			Log.e("jsonUtil--Enclose", e.getMessage());
			return e.getMessage();
		}
	}

	/**
	 * Object转成String
	 * @param msg
	 * @return
	 */
	public static  String btToString(Object msg){
		return	new Gson().toJson(msg);
	}

	/**
	 * 读取assets文件数据
	 * @param context
	 * @param fileName assets文件名
     * @return
     */
	public static String getJsonDataForFile(Context context,String fileName){
		String src = "";
		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(context.getAssets().open(fileName), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			inputStreamReader.close();
			bufferedReader.close();
			src = stringBuilder.toString();
			Log.e("读取assets文件数据==", stringBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src;
	}


}
