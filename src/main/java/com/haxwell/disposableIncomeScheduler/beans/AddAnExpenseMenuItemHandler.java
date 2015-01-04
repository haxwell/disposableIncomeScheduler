package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.validators.Validator;

public class AddAnExpenseMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add An Expense";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getExpenses(data);
				
		System.out.print("Enter the name of the expense: ");
		String name = getInputGetter().readInput();
		
		if (name != null && name.length() > 0) {
			System.out.print("Cost per month: ");
			String cost = getInputGetter().readInput();
			
			Validator v = getValidatorMap().get(Constants.PRICE);
			if (v.isValidValue(cost)) {
				JSONObject obj = new JSONObject();
				
				obj.put(Constants.PRICE_JSON, cost);
				obj.put(Constants.DESCRIPTION_JSON, name);
				
				arr.add(obj);
				
				rtn = true;
			}
		}
		
		return rtn;
	}
}