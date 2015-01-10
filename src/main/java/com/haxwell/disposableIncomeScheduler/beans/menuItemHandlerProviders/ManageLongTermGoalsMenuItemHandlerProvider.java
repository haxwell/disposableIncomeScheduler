package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.ManageLongTermGoalsMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ManageLongTermGoalsMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		Object obj = state.get(Constants.MENU_FOCUS);
		
		MenuItemHandlerBean rtn = null;
		
		if (obj == null)
			rtn = new ManageLongTermGoalsMenuItemHandler();
		
		return rtn;
	}
}
