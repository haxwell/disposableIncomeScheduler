package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.EditAGoalMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class EditAGoalMenuItemHandlerProvider extends
		AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state) {
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		List<JSONObject> arrGoals = MenuItemUtils.getGoalsOfAGroup(arr);
		
		if (arrGoals.size() > 0)
			return new EditAGoalMenuItemHandler();
		
		return null;
	}
}
