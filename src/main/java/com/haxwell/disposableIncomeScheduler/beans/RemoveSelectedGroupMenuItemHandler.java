package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class RemoveSelectedGroupMenuItemHandler extends MenuItemHandlerBean {

	public String getMenuText() {
		return "Remove Selected Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		
		String selectedGroupParentName = MenuItemUtils.getSelectedGroupParentName(state);
		String selectedGroupParentPath = MenuItemUtils.getSelectedGroupParentPath(state);
		
		String selectedGroupName = MenuItemUtils.getSelectedGroupName(state);

		JSONArray arrParent = MenuItemUtils.getParentOfSelectedGroup(data, state);
		
		boolean found = false;
		for (int i = 0; i < arrParent.size() && !found; i++) {
			JSONObject obj = (JSONObject)arrParent.get(i);
			
			if (obj.containsKey(selectedGroupName)) {
				arrParent.remove(i);
				
				MenuItemUtils.setSelectedGroupName(state, selectedGroupParentName);
				MenuItemUtils.setSelectedGroupPath(state, selectedGroupParentPath);

				found = true;
				rtn = true;
			}
		}

		System.out.println(selectedGroupName + " removed.");
		
		return rtn;
	}
}
