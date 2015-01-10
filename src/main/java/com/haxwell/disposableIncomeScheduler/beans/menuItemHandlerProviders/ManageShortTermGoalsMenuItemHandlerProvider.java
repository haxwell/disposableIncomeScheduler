package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.ManageShortTermGoalsMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ManageShortTermGoalsMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		Object obj = state.get(Constants.MENU_FOCUS);
		
		MenuItemHandlerBean rtn = null;
		
		if (obj == null)
			rtn = new ManageShortTermGoalsMenuItemHandler();
		
		return rtn;
	}
}
