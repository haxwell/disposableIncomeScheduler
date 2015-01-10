package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class ManageShortTermGoalsMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Manage Short Term Goals";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		state.put(Constants.MENU_FOCUS, Constants.SHORT_TERM_GOALS_JSON);
		
		return false;
	}
}
