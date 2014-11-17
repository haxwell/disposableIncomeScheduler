package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class MenuItemUtils {

	public static void initializeState(JSONObject state) {
		state.clear();
		
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, getRootGroupName());
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, getRootGroupName());
	}
	
	/**
	 * returns the selected group name, or if none is selected, the name of the root group.
	 * 
	 * @param state
	 * @return
	 */
	public static String getSelectedGroupName(JSONObject state) {
		String rtn = null;
		
		if (state.containsKey(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME))
			rtn = (String)state.get(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME);
		else
			rtn = getRootGroupName();
		
		return rtn;
	}
	
	public static JSONArray getSelectedGroup(JSONObject data, JSONObject state) {
		String path = (String)state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		
		LinkedList<String> tokenList = getTokenList(new StringTokenizer(path, Constants.STATE_ATTR_PATH_DELIMITER));
		
		JSONArray arr = null;
		JSONObject obj = data;
		
		int tokenListIndex = 0;
		
		do {
			String token = tokenList.get(tokenListIndex);

			if (obj.containsKey(token))
				arr = (JSONArray)obj.get(token);

			if (tokenListIndex+1 < tokenList.size()) {
				boolean found = false;
				int index = 0;
				String nextOnPath = tokenList.get(tokenListIndex+1);
				while (!found && index < arr.size()) {
					
					obj = (JSONObject)arr.get(index);
					
					if (obj.containsKey(nextOnPath)) {
						found = true;

						// if this is the leaf..
						if (tokenListIndex+2 == tokenList.size()) { 
							arr = (JSONArray)obj.get(nextOnPath);
							tokenListIndex++;
						}
					}
					else {
						index++;
					}
				}
			}

			tokenListIndex++;
			
		} while (tokenListIndex < tokenList.size());
		
		return arr;
	}
	
	public static JSONArray getParentOfSelectedGroup(JSONObject data, JSONObject state) {
		StringTokenizer tokenizer = new StringTokenizer(state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP).toString(), Constants.STATE_ATTR_PATH_DELIMITER);
		
		LinkedList<String> list = getTokenList(tokenizer);
		StringBuffer sb = new StringBuffer();
		
		int count = 0;
		for (; count < list.size()-1; count++) {
			sb.append(list.get(count));
			
			if (count < (list.size()-1)) {
				sb.append(Constants.STATE_ATTR_PATH_DELIMITER);
			}
		}
		
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, sb.toString());
		
		count = list.size() - 2;
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, list.get(count));
		
		return getSelectedGroup(data, state);
	}

	public static LinkedList<String> getTokenList(StringTokenizer tokenizer) {
		LinkedList<String> list = new LinkedList<String>();
		
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		
		return list;
	}

	public static String getRootGroupName() {;
		return Constants.GOALS_ATTR_KEY+"_"+Constants.ROOT_GOAL_GROUP_NAME;
	}

	public static void setSelectedGroupName(JSONObject state, String groupName) {
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, groupName);
	}

	public static List<String> getSubgroupNamesOfAGroup(JSONArray arr) {
		List<String> rtn = new ArrayList<String>();
		
		for (int count=0; count < arr.size(); count++) {
			JSONObject obj = (JSONObject)arr.get(count);
			
			Iterator<String> iterator = obj.keySet().iterator();
			String key = iterator.next();
			
			if (obj.get(key) instanceof JSONArray)
				rtn.add(key);
		}
		
		return rtn;
	}

	public static List<JSONObject> getGoalsOfAGroup(JSONArray arr) {
		List<JSONObject> rtn = new ArrayList<>();
		
		for (int count=0; count < arr.size(); count++) {
			JSONObject obj = (JSONObject)arr.get(count);
			
			Iterator<String> iterator = obj.keySet().iterator();
			String key = iterator.next();
			
			if (obj.get(key) instanceof String)
				rtn.add(obj);
		}
		
		return rtn;
	}

}
