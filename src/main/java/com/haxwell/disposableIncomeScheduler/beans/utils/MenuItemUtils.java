package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.utils.StringUtils;

public class MenuItemUtils {

	public static void initializeState(JSONObject state) {
		state.clear();

		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, getRootGroupName());
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, getRootGroupName());
	}

	public static boolean isMenuFocusedOn(JSONObject state, String focus) {
		Object obj = state.get(Constants.MENU_FOCUS);

		return obj != null && obj.equals(focus);
	}

	public static boolean isMenuFocusedOnTheMainLevel(JSONObject state) {
		Object obj = state.get(Constants.MENU_FOCUS);

		return obj == null;
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
			rtn = (String) state.get(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME);

		return rtn;
	}

	public static String getSelectedGroupPath(JSONObject state) {
		String rtn = null;

		if (state.containsKey(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP))
			rtn = (String) state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);

		return rtn;
	}

	public static JSONArray getSelectedGroup(JSONObject data, JSONObject state) {
		return getSelectedGroup(data, state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP) + "");
	}

	/**
	 * Searches the DATA for the node at the given path. Returns it and its children.
	 * 
	 * @param data
	 * @param path
	 * @return
	 */
	public static JSONArray getSelectedGroup(JSONObject data, String path) {
		LinkedList<String> tokenList = getTokenList(new StringTokenizer(path, Constants.STATE_ATTR_PATH_DELIMITER));

		JSONArray arr = null;
		JSONObject obj = data;

		int tokenListIndex = 0;

		do {
			String token = tokenList.get(tokenListIndex);

			if (obj.containsKey(token)) {
				Object o = obj.get(token);

				if (o instanceof JSONArray) {
					arr = (JSONArray) obj.get(token);
				} else {
					arr = new JSONArray();
				}
			}

			if (tokenListIndex + 1 < tokenList.size()) {
				boolean found = false;
				int index = 0;
				String nextOnPath = tokenList.get(tokenListIndex + 1);
				while (!found && index < arr.size()) {

					obj = (JSONObject) arr.get(index);

					if (obj.containsKey(nextOnPath)) {
						found = true;

						// if this is the leaf..
						if (tokenListIndex + 2 == tokenList.size()) {
							arr = (JSONArray) obj.get(nextOnPath);
							tokenListIndex++;
						}
					} else {
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
		for (; count < list.size() - 1; count++) {
			sb.append(list.get(count));

			if (count < (list.size() - 1)) {
				sb.append(Constants.STATE_ATTR_PATH_DELIMITER);
			}
		}

		return getSelectedGroup(data, sb.toString());
	}

	public static List<JSONArray> getSubgroups(JSONObject element) {
		return getSubgroups(element, null);
	}

	public static List<JSONArray> getSubgroups(JSONObject element, List<String> excludedList) {
		ArrayList<JSONArray> rtn = new ArrayList<>();

		List<String> subgroupNames = getSubgroupNamesOfAGroup(element);

		for (String str : subgroupNames) {
			if (excludedList != null) {
				if (!excludedList.contains(str)) {
					rtn.add((JSONArray) element.get(str));
				}
			} else {
				rtn.add((JSONArray) element.get(str));
			}
		}

		return rtn;
	}

	public static List<JSONObject> getSubgroups(JSONArray arr) {
		return getSubgroups(arr, null);
	}

	public static List<JSONObject> getSubgroups(JSONArray arr, List<String> excludedList) {
		ArrayList<JSONObject> rtn = new ArrayList<>();

		for (int y = 0; y < arr.size(); y++) {
			JSONObject jo = (JSONObject) arr.get(y);
			String joKey = jo.keySet().iterator().next();

			if (!jo.containsKey(Constants.GROUP_WEIGHT_JSON)) {
				if (excludedList != null) {
					if (!excludedList.contains(joKey)) {
						rtn.add(jo);
					}
				} else {
					rtn.add(jo);
				}
			}
		}

		return rtn;
	}

	public static LinkedList<String> getTokenList(StringTokenizer tokenizer) {
		LinkedList<String> list = new LinkedList<String>();

		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}

		return list;
	}

	public static String getRootGroupName() {
		return Constants.LONG_TERM_GOALS_JSON;
	}

	public static void setSelectedGroupNameAndPath(JSONObject state, String groupName) {
		setSelectedGroupName(state, groupName);

		String pathToGroup = (String) state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		pathToGroup += Constants.STATE_ATTR_PATH_DELIMITER + groupName;

		MenuItemUtils.setSelectedGroupPath(state, pathToGroup);
	}

	public static void setSelectedGroupPath(JSONObject state, String path) {
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, path);
	}

	public static String getSelectedGroupParentPath(JSONObject state) {
		String rtn = getRootGroupName();
		String str = (String) state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		String delim = Constants.STATE_ATTR_PATH_DELIMITER;

		int indexOfLastSlash = str.lastIndexOf(delim);

		if (indexOfLastSlash > 0) {
			rtn = str.substring(0, indexOfLastSlash);
		}

		return rtn;
	}

	public static String getSelectedGroupParentName(JSONObject state) {
		String rtn = getRootGroupName();
		String str = (String) state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		String delim = Constants.STATE_ATTR_PATH_DELIMITER;

		int indexOfLastSlash = str.lastIndexOf(delim);

		if (indexOfLastSlash > 0) {
			rtn = str.substring(0, indexOfLastSlash);
			indexOfLastSlash = rtn.lastIndexOf(delim);

			if (indexOfLastSlash > 0) {
				rtn = rtn.substring(indexOfLastSlash + 1, rtn.length());
			}
		}

		return rtn;
	}

	public static String setSelectedGroupName(JSONObject state, String groupName) {
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, groupName);

		return groupName;
	}

	public static boolean doesGroupHaveSubgroups(JSONArray group) {
		boolean rtn = false;

		if (group.size() > 0) {
			JSONObject object = (JSONObject) group.get(0);
			Set<String> keySet = object.keySet();

			// if it has subgroups, keySet should only contain one key: the subgroup name. 
			//  Otherwise, keySet is all the keys of a goal.
			rtn = (keySet.size() == 1);
		}

		return rtn;
	}

	public static boolean doesGroupHaveSubgroups(JSONObject obj) {
		return obj.keySet().size() == 1;
	}

	public static LinkedList<String> getGoalsByWeight(JSONArray goals, JSONArray weights) {
		Map<String, Double> map = new HashMap<>();

		for (int x = 0; x < weights.size(); x++) {
			JSONObject jo = (JSONObject) weights.get(x);

			if (!jo.containsKey(Constants.GROUP_WEIGHT_JSON)) {
				Double d = Double.parseDouble(jo.get(Constants.WEIGHT_AS_PERCENTAGE_JSON) + "");
				map.put(jo.get(Constants.DESCRIPTION_JSON) + "", d);
			}
		}

		return getOrderedListOfStrings(map);
	}

	public static LinkedList<String> getOrderedListOfStrings(Map<String, Double> map) {
		LinkedList<String> rtn = new LinkedList<String>();

		Map<String, Double> mapCopy = new HashMap<String, Double>();

		for (String str : map.keySet()) {
			mapCopy.put(str, map.get(str));
		}

		int origMapSize = mapCopy.size();

		for (int x = 0; x < origMapSize; x++) {

			Iterator<String> iterator = mapCopy.keySet().iterator();
			Double hi = null;
			String hiKey = null;

			while (iterator.hasNext()) {
				String key = iterator.next();
				Double d = mapCopy.get(key);

				if (hi == null || d > hi) {
					hi = d;
					hiKey = key;
				}
			}

			rtn.add(hiKey);
			mapCopy.remove(hiKey);
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

				for (int x = 0; x < grp.size(); x++) {
					String key = subgroupList.get(x);
					JSONObject w = (JSONObject) weights.get(x);
					JSONArray arr = (JSONArray) w.get(key);

					for (int y = 0; y < arr.size(); y++) {
						JSONObject jo = (JSONObject) arr.get(y);

						if (jo.containsKey(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)) {
							Double d = Double.parseDouble(jo.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON) + "");

							map.put(key, d);
						}
					}
				}

				int origMapSize = map.size();
				for (int x = 0; x < origMapSize; x++) {
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

			for (int x = 0; x < grp.size(); x++) {
				String key = subgroupList.get(x);
				JSONObject w = (JSONObject) weight.get(x);
				JSONArray arr = (JSONArray) w.get(key);

				for (int y = 0; y < arr.size(); y++) {
					JSONObject jo = (JSONObject) arr.get(y);

					if (jo.containsKey(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)) {
						Double d = Double.parseDouble(jo.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON) + "");

						map.put(key, d);
					}
				}
			}

			int origMapSize = map.size();
			for (int x = 0; x < origMapSize; x++) {
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

	public static List<String> getSubgroupNamesOfAGroup(JSONArray arr) {
		List<String> rtn = new ArrayList<String>();

		for (int count = 0; count < arr.size(); count++) {
			JSONObject obj = (JSONObject) arr.get(count);

			Iterator<String> iterator = obj.keySet().iterator();
			String key = iterator.next();

			if (obj.get(key) instanceof JSONArray)
				rtn.add(key);
		}

		return rtn;
	}

	public static List<String> getGoalNamesOfAGroup(JSONArray arr) {
		List<String> rtn = new ArrayList<>();

		for (int count = 0; count < arr.size(); count++) {
			JSONObject obj = (JSONObject) arr.get(count);

			String str = (String) obj.get(Constants.DESCRIPTION_JSON);

			if (!StringUtils.isNullOrEmpty(str))
				rtn.add(str);
		}

		return rtn;
	}

	public static List<JSONObject> getGoalsOfAGroup(JSONArray arr) {
		List<JSONObject> rtn = new ArrayList<>();

		for (int count = 0; count < arr.size(); count++) {
			JSONObject obj = (JSONObject) arr.get(count);

			Iterator<String> iterator = obj.keySet().iterator();
			String key = iterator.next();

			if (obj.get(key) instanceof String)
				rtn.add(obj);
		}

		return rtn;
	}

	public static List<JSONObject> getSiblingsOfSelectedGroup(JSONObject data, JSONObject state) {
		JSONArray arr = getParentOfSelectedGroup(data, state);

		String str = getSelectedGroupName(state);
		List<String> list = new ArrayList<>();

		list.add(str);

		return getSubgroups(arr, list);
	}

	public static void setOverridingPercentages(JSONObject data, JSONObject overrides) {
		data.put(Constants.OVERRIDING_PERCENTAGE_AMT_JSON, overrides);
	}

	public static JSONObject getOverridingPercentages(JSONObject data) {
		JSONObject rtn = (JSONObject) data.get(Constants.OVERRIDING_PERCENTAGE_AMT_JSON);

		if (rtn == null)
			rtn = new JSONObject();

		return rtn;
	}

	public static JSONArray getExpenses(JSONObject data) {
		return (JSONArray) data.get(Constants.EXPENSES_JSON);
	}

	public static JSONObject getExpense(JSONObject data, String name) {
		JSONArray arr = getExpenses(data);
		JSONObject rtn = null;

		for (int i = 0; rtn == null && i < arr.size(); i++) {
			JSONObject obj = (JSONObject) arr.get(i);

			if (obj.get(Constants.DESCRIPTION_JSON).equals(name)) {
				rtn = obj;
			}
		}

		return rtn;
	}

	public static JSONArray getShortTermGoals(JSONObject data) {
		return (JSONArray) data.get(Constants.SHORT_TERM_GOALS_JSON);
	}

	public static JSONObject getShortTermGoal(JSONObject data, String name) {
		JSONArray arr = getShortTermGoals(data);
		JSONObject rtn = null;

		for (int i = 0; rtn == null && i < arr.size(); i++) {
			JSONObject obj = (JSONObject) arr.get(i);

			if (obj.get(Constants.DESCRIPTION_JSON).equals(name)) {
				rtn = obj;
			}
		}

		return rtn;
	}

	public static JSONArray getLongTermGoals(JSONObject data) {
		return (JSONArray) data.get(Constants.LONG_TERM_GOALS_JSON);
	}

	public static JSONObject removeLongTermGoal(JSONObject data, String path) {
		JSONArray sg = MenuItemUtils.getSelectedGroup(data, path);
		int sgSize = sg.size();

		// iterate over returned array till you find the goal, keep track of the index
		int idx = 0;
		boolean found = false;
		for (; idx < sgSize; idx++) {
			JSONObject obj = (JSONObject) sg.get(idx);

			if (obj.containsKey(Constants.DESCRIPTION_JSON) && obj.get(Constants.DESCRIPTION_JSON).equals("sink")) {
				found = true;
				break;
			}
		}

		// remove the goal from the array
		JSONObject rtn = null;
		if (found)
			rtn = (JSONObject) sg.remove(idx);

		return rtn;

	}
}
