package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class RemoveAShortTermGoalMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Remove A Short Term Goal";
	}
	
	// TODO: This and Remove A Goal are using damn near the exact same code.. refactor.
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			String description = ((JSONObject)arr.get(count)).get(Constants.DESCRIPTION_JSON)+"";
			getPrintlner().println(count+1 + ". " + description);
		}
		
		if (count > 0) {
			String choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				int index = -1;
				
				try {
					index = Integer.parseInt(choice) - 1;
				} catch (NumberFormatException nfe) {
					index = -1;
				}
				
				if (index >= 0 && index <= arr.size()) {
					JSONObject removedObj = (JSONObject)arr.remove(index);
					
					getPrintlner().println(removedObj.get(Constants.DESCRIPTION_JSON) + " removed.");
					rtn = true;
				}
			}
		}
		else
			getPrintlner().println("\nNo Items to Remove!\n");
		
		return rtn;
	}
}