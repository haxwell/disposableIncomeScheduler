package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.ChangeSelectedGroupNameMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ChangeSelectedGroupNameMenuItemHandlerProvider extends
		AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject state = DataAndStateSingleton.getInstance().getState();

		String selGroupName = MenuItemUtils.getSelectedGroupName(state);
		
		if (selGroupName != null && !selGroupName.equals(MenuItemUtils.getRootGroupName()) && MenuItemUtils.isMenuFocusedOn(state, Constants.LONG_TERM_GOALS_JSON))
			return new ChangeSelectedGroupNameMenuItemHandler();
		
		return null;
	}
}
