package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class RemoveAnExpenseMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Remove An Expense";
	}
	
	// TODO: This and Remove A Goal are using damn near the exact same code.. refactor.
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getExpenses(data);
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			String description = ((JSONObject)arr.get(count)).get(Constants.DESCRIPTION_JSON)+"";
			System.out.println(count+1 + ". " + description);
		}
		
		if (count > 0) {
			String choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				JSONObject removedObj = (JSONObject)arr.remove(Integer.parseInt(choice) - 1);
				
				System.out.println(removedObj.get(Constants.DESCRIPTION_JSON) + " removed.");
				rtn = true;
			}
		}
		else
			System.out.println("\nNo Items to Remove!\n");
		
		return rtn;
	}
}