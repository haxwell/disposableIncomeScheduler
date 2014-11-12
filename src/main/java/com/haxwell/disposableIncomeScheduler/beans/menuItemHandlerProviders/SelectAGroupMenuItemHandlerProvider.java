package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.SelectAGroupMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class SelectAGroupMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state) {
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		if (MenuItemUtils.getSubgroupNamesOfAGroup(arr).size() > 0)
			return new SelectAGroupMenuItemHandler();
		
		return null;
	}
}
