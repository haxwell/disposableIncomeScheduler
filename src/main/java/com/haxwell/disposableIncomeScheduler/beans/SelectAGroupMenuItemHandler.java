package com.haxwell.disposableIncomeScheduler.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class SelectAGroupMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Select A Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);

		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(arr);
		
		// put the group keys in a map, display them in a menu
		Iterator<String> iterator = list.iterator();
		int count = 0;
		Map<Integer, String> map = new HashMap<>();
		
		while (iterator.hasNext()) {
			map.put(++count, iterator.next());
		}
		
		System.out.println();
		
		count = 0;
		while (count < list.size()) {
			System.out.println(++count + ". " + map.get(count));
		}
		
		// get input
		String input = System.console().readLine();
		
		Integer i = null;
		boolean NaN = false;
		
		try {
			i = Integer.parseInt(input);
		}
		catch (NumberFormatException nfe) {
			NaN = true;
		}

		// if valid, set the associated selected group in the state
		if (!NaN && (i != 0 && i <= list.size())) {
			
			String groupName = map.get(i); 
			MenuItemUtils.setSelectedGroupName(state, groupName);
			
			String pathToGroup = (String)state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
			pathToGroup += Constants.STATE_ATTR_PATH_DELIMITER + groupName;
			
			MenuItemUtils.setSelectedGroupPath(state, pathToGroup);
		}

		return rtn;
	}
}