package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	
	public static boolean doesGroupHaveSubgroups(JSONObject group) {
		boolean rtn = group.size() > 0;
		
		Iterator<String> iterator = group.keySet().iterator();
		String key = iterator.next();
		
		rtn &= key.startsWith(Constants.GOALS_ATTR_KEY);
		
		return rtn;
	}

	public static List<String> getSubgroupNamesOfAGroup(JSONObject obj) {
		List<String> rtn = new ArrayList<String>();

		Iterator<String> iterator = obj.keySet().iterator();
		
		while (iterator.hasNext()) {
			String key = iterator.next();
			
			if (obj.get(key) instanceof JSONArray)
				rtn.add(key);
		}
		
		return rtn;
	}
	
	public static LinkedList<String> getGoalsByWeight(JSONArray goals, JSONArray weights) {
		LinkedList<String> rtn = new LinkedList<String>();
		
		Map<String, Double> map = new HashMap<>();
		
		for (int x=0; x < weights.size(); x++) {
			JSONObject jo = (JSONObject)weights.get(x);
			
			if (!jo.containsKey(Constants.GROUP_WEIGHT_JSON)) {
				Double d = Double.parseDouble(jo.get(Constants.WEIGHT_AS_PERCENTAGE_JSON)+"");
				map.put(jo.get(Constants.DESCRIPTION_JSON)+"", d);
			}
		}
		
		int origMapSize = map.size();
		
		for (int x=0; x < origMapSize; x++) {

			Iterator<String> iterator = map.keySet().iterator();
			Double hi = null;
			String hiKey = null;
			
			while (iterator.hasNext()) {
				String key = iterator.next();
				Double d = map.get(key);
				
				if (hi == null || d > hi) {
					hi = d;
					hiKey = key;
				}
			}
			
			rtn.add(hiKey);
			map.remove(hiKey);
		}
		
		return rtn;
	}
	
	// TODO: the two versions of this method are practically the same.. figure out how to refactor them..
	public static LinkedList<String> getSubgroupNamesOfAGroupByWeight(JSONArray grp, JSONArray weights) {
		LinkedList<String> rtn = new LinkedList<String>();
		
		List<String> subgroupList = getSubgroupNamesOfAGroup(grp);

		if (subgroupList.size() > 0) {
			if (subgroupList.size() == 1) {
				rtn.add(subgroupList.get(0));
			} else {
				Map<String, Double> map = new HashMap<>();
				
				for (int x=0; x < grp.size(); x++) {
					String key = subgroupList.get(x);
					JSONObject w = (JSONObject)weights.get(x);
					JSONArray arr = (JSONArray)w.get(key);
					
					for (int y=0; y < arr.size(); y++) {
						JSONObject jo = (JSONObject)arr.get(y);
						
						if (jo.containsKey(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)) {
							Double d = Double.parseDouble(jo.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"");
							
							map.put(key, d);
						}
					}
				}
				
				int origMapSize = map.size();
				for (int x=0; x < origMapSize; x++) {
					Double hi = null;
					String hiKey = "";
					
					Iterator<String> iterator = map.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						Double d = map.get(key);
						
						if (hi == null || d > hi) {
							hi = d;
							hiKey = key;
						}
					}
					
					rtn.add(hiKey);
					map.remove(hiKey);
				}
			}
		}
		
		return rtn;
	}
	
	// TODO: the two versions of this method are practically the same.. figure out how to refactor them..
	public static LinkedList<String> getSubgroupNamesOfAGroupByWeight(JSONObject grp, JSONObject weight) {
		LinkedList<String> rtn = new LinkedList<String>();
		
		List<String> subgroupList = getSubgroupNamesOfAGroup(grp);

		if (subgroupList.size() == 1) {
			rtn.add(subgroupList.get(0));
			return rtn;
		} else {
			Map<String, Double> map = new HashMap<>();
			
			for (int x=0; x < grp.size(); x++) {
				String key = subgroupList.get(x);
				JSONObject w = (JSONObject)weight.get(x);
				JSONArray arr = (JSONArray)w.get(key);
				
				for (int y=0; y < arr.size(); y++) {
					JSONObject jo = (JSONObject)arr.get(y);
					
					if (jo.containsKey(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)) {
						Double d = Double.parseDouble(jo.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"");
						
						map.put(key, d);
					}
				}
			}
			
			int origMapSize = map.size();
			for (int x=0; x < origMapSize; x++) {
				Double hi = 0.0;
				String hiKey = "";
				
				Iterator<String> iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					Double d = map.get(key);
					
					if (d > hi) {
						hi = d;
						hiKey = key;
					}
				}
				
				rtn.add(hiKey);
				map.remove(hiKey);
			}
		}

		return rtn;
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
