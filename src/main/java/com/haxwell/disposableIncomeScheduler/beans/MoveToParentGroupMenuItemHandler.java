package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class MoveToParentGroupMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Move to Parent Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		
		// if the current group is not the root
		// get the name of the parent of the selected group
		String parentGroupName = MenuItemUtils.getSelectedGroupParentName(state);
		
		// get the current path, and remove the name of the current group from it
		String path = MenuItemUtils.getSelectedGroupParentPath(state);
		
		// call menuitemutils, set the parent group name as the current group name
		// 		set the new path as the current path
		MenuItemUtils.setSelectedGroupName(state, parentGroupName);
		MenuItemUtils.setSelectedGroupPath(state, path);
		
		return rtn;
	}
}