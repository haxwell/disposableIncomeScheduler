package com.haxwell.disposableIncomeScheduler.beans;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class AddAGroupMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add A Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(arr);

		String selectedGroupName = MenuItemUtils.getSelectedGroupName(state);
		
		System.out.println(selectedGroupName + "'s children...");
		System.out.println("-----===-----------------");
		
		for (String str : list) {
			System.out.println(str);
		}
		
		System.out.println("\nEnter the name of the new group to add to " + selectedGroupName + ": ");
		
		String input = System.console().readLine();

		if (input != null && input.length() > 0) {
			JSONObject obj = new JSONObject();
			obj.put(input, new JSONArray());
			arr.add(obj);
			
			rtn = true;

			System.out.println("\n--> Added the group [" + input + "]");
		}
		
		return rtn;
	}

}