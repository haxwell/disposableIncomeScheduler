package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.AddAGroupMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class AddAGroupMenuItemHandlerProvider extends
		AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state) {
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);

		List<JSONObject> list = MenuItemUtils.getGoalsOfAGroup(arr);
		
		if (list.size() == 0)
			return new AddAGroupMenuItemHandler();
		
		return null;
	}
}
