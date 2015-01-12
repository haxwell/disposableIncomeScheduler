package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.validators.Validator;

public class AddAShortTermGoalMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add A Short Term Goal";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
				
		getPrintlner().print("Enter the name of the short term goal: ");
		String name = getInputGetter().readInput();
		
		if (name != null && name.length() > 0) {
			getPrintlner().print("Amount to save per month: ");
			String amtPerMonth = getInputGetter().readInput();
			
			Validator v = getValidatorMap().get(Constants.PRICE);
			if (v.isValidValue(amtPerMonth)) {
				
				JSONObject obj = new JSONObject();
				
				obj.put(Constants.AMT_SAVED_PER_MONTH_JSON, amtPerMonth);
				obj.put(Constants.DESCRIPTION_JSON, name);
				obj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
				
				arr.add(obj);
				
				rtn = true;
			}
		}
		
		return rtn;
	}
}