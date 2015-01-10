package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class ManageExpensesMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Manage Expenses";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		state.put(Constants.MENU_FOCUS, Constants.EXPENSES_JSON);
		
		return false;
	}
}
