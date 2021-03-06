package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class ChangeSelectedGroupNameMenuItemHandler extends MenuItemHandlerBean {
	public String getMenuText() {
		return "Change Group Name";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		String selectedGroupName = MenuItemUtils.getSelectedGroupName(state);
		
		System.out.println("Current Group Name is [" + selectedGroupName + "]");
		System.out.print("Change it to: ");
		
		String newName = getInputGetter().readInput();
		
		JSONArray parentOfSelectedGroup = MenuItemUtils.getParentOfSelectedGroup(data, state);
		
		boolean found = false;
		
		if (newName != null && !newName.equals("")) {
			for (int i = 0; i < parentOfSelectedGroup.size() && !found; i++) {
				JSONObject jo = (JSONObject)parentOfSelectedGroup.get(i);
				
				if (jo.containsKey(selectedGroupName)) {
					Object remove = jo.remove(selectedGroupName);
					jo.put(newName, remove);
					
					String parentPath = MenuItemUtils.getSelectedGroupParentPath(state);
					
					newName = MenuItemUtils.setSelectedGroupName(state, newName);
					MenuItemUtils.setSelectedGroupPath(state, parentPath+Constants.STATE_ATTR_PATH_DELIMITER+newName);

					found = true;
				}
			}
		}

		return found;
	}
}
