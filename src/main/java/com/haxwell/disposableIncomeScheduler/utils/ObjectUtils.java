package com.haxwell.disposableIncomeScheduler.utils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ObjectUtils {

	public static JSONObject getFirstObjectInArray(JSONArray arr) {
		JSONObject rtn = null;
		
		if (arr == null || arr.size() == 0) 
			rtn = (JSONObject)new JSONObject();
		else 
			rtn = (JSONObject)arr.get(0);
		
		return rtn;
	}
}
