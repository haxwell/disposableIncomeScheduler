package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.SelectAGroupMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class SelectAGroupMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		JSONObject state = DataAndStateSingleton.getInstance().getState();

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		if (MenuItemUtils.getSubgroupNamesOfAGroup(arr).size() > 0 && MenuItemUtils.isMenuFocusedOn(state, Constants.LONG_TERM_GOALS_JSON))
			return new SelectAGroupMenuItemHandler();
		
		return null;
	}
}
