package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class ToMainMenuMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Move to Main Menu";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		state.put(Constants.MENU_FOCUS, null);
		
		return false;
	}
}
