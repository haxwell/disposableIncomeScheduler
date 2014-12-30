package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.RemoveAGoalMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class RemoveAGoalMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		
		String selGroupName = MenuItemUtils.getSelectedGroupName(state);
		
		if (selGroupName != null && !selGroupName.equals(MenuItemUtils.getRootGroupName())) {
			JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
			
			if (MenuItemUtils.getGoalsOfAGroup(arr).size() > 0)
				return new RemoveAGoalMenuItemHandler();
		}

		return null;
	}
}
