package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class RemoveSelectedGroupMenuItemHandler extends MenuItemHandlerBean {

	public String getMenuText() {
		return "Remove Selected Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray items = (JSONArray)data.get("items");
		
		// list each item
		int count = 0;
		for (; count < items.size(); count++) {
			String description = ((JSONObject)items.get(count)).get("description")+"";
			System.out.println(count+1 + ". " + description);
		}
		
		if (count > 0) {
			String choice = System.console().readLine();
			
			if (choice != null && !choice.equals("")) {
				JSONObject removedObj = (JSONObject)items.remove(Integer.parseInt(choice) - 1);
				
				System.out.println(removedObj.get("description") + " removed.");
				rtn = true;
			}
		}
		else
			System.out.println("\nNo Items to Remove!\n");
		
		return rtn;
	}
}
