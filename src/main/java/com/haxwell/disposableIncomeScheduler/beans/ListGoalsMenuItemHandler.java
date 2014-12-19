package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class ListGoalsMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "List All Goals";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
	
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			String description = ((JSONObject)arr.get(count)).get("description")+"";
			
			System.out.println("> " + description);
		}
		
		
		return rtn;
	}
}
