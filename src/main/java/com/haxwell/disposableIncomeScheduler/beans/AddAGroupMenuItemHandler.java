package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class AddAGroupMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add A Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		String selectedGroupName = MenuItemUtils.getSelectedGroupName(state);
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);

		System.out.println("\nEnter the name of the new group to add to " + selectedGroupName + ": ");
		
		String input = System.console().readLine();

		if (input != null && input.length() > 0) {
			JSONObject obj = new JSONObject();
			obj.put(Constants.GOALS_ATTR_KEY+"_"+input, new JSONArray());
			arr.add(obj);
			
			rtn = true;
		}
		
		return rtn;
	}

}