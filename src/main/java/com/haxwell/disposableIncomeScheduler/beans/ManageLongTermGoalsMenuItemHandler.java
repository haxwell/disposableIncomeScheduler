package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class ManageLongTermGoalsMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Manage Long Term Goals";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		state.put(Constants.MENU_FOCUS, Constants.LONG_TERM_GOALS_JSON);
		
		return false;
	}
}
