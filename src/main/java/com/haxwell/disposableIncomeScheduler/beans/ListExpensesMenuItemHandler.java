package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class ListExpensesMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "List Expenses";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getExpenses(data);
		
		if (arr.size() > 0)
			System.out.println("Expenses\n-------------");
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			JSONObject jobj = (JSONObject)arr.get(count);
			String description = jobj.get(Constants.DESCRIPTION_JSON)+"";
			String cost = jobj.get(Constants.PRICE_JSON)+"";
			System.out.println(count+1 + ". " + description + "( " + cost + ")");
		}
		
		System.out.println("-------\n");
		
		return rtn;
	}
}
