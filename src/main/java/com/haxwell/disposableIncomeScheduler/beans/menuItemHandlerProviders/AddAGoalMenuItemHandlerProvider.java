package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.AddAGoalMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class AddAGoalMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state) {
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);

		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(arr);
		
		if (list.size() == 0)
			return new AddAGoalMenuItemHandler();
		
		return null;
	}
}
