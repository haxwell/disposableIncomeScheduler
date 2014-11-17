package com.haxwell.disposableIncomeScheduler.beans;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class RemoveAGoalMenuItemHandler extends MenuItemHandlerBean {

	public String getMenuText() {
		return "Remove A Goal";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			String description = ((JSONObject)arr.get(count)).get("description")+"";
			System.out.println(count+1 + ". " + description);
		}
		
		if (count > 0) {
			String choice = System.console().readLine();
			
			if (choice != null && !choice.equals("")) {
				JSONObject removedObj = (JSONObject)arr.remove(Integer.parseInt(choice) - 1);
				
				System.out.println(removedObj.get("description") + " removed.");
				rtn = true;
			}
		}
		else
			System.out.println("\nNo Items to Remove!\n");
		
		return rtn;
	}
}
