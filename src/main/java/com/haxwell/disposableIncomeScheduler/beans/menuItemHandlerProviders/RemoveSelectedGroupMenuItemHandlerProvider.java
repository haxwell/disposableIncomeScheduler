package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.RemoveSelectedGroupMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class RemoveSelectedGroupMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state) {
		String selGroupName = MenuItemUtils.getSelectedGroupName(state);
		
		if (selGroupName != null && !selGroupName.equals(MenuItemUtils.getRootGroupName())) {
			return new RemoveSelectedGroupMenuItemHandler();
		}

		return null;
	}
}
