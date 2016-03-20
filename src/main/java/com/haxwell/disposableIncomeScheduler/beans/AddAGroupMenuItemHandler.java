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
		List<String> subgroupNamesList = MenuItemUtils.getSubgroupNamesOfAGroup(arr);

		String selectedGroupName = MenuItemUtils.getSelectedGroupName(state);
		boolean selectedGroupIsRootGroup = selectedGroupName.equals(MenuItemUtils.getRootGroupName());

		if (!selectedGroupIsRootGroup && subgroupNamesList.size() > 0) {
			System.out.println(selectedGroupName + "'s child groups...");
			System.out.println("-----===-----------------");

			for (String str : subgroupNamesList) {
				System.out.println(str);
			}

			System.out.println("");
		}

		String str = "\nEnter the name of the new group to add";

		if (!selectedGroupIsRootGroup)
			str += " to " + selectedGroupName;

		str += ": ";

		System.out.println(str);

		String input = System.console().readLine();

		if (input != null && input.length() > 0) {
			JSONObject obj = new JSONObject();
			obj.put(input, new JSONArray());
			arr.add(obj);

			rtn = true;

			if (selectedGroupName.equals(MenuItemUtils.getRootGroupName()))
				MenuItemUtils.setSelectedGroupNameAndPath(state, input);

			System.out.println("\n--> Added the group [" + input + "]");
		}

		return rtn;
	}

}