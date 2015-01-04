package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class ListShortTermGoalsMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "List Short Term Goals";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		if (arr.size() > 0)
			getPrintlner().println("Short Term Goals\n-------------");
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			JSONObject jobj = (JSONObject)arr.get(count);
			String description = jobj.get(Constants.DESCRIPTION_JSON)+"";
			String tas = jobj.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"";
			String aspp = jobj.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"";
			getPrintlner().println(count+1 + ". " + description + "(Total Already Saved: " + tas + " / Amt Saved Per Month: " + aspp + ")");
		}
		
		getPrintlner().println("-------\n");
		
		return rtn;
	}
}
