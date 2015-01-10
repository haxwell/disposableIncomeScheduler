package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.ToMainMenuMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ToMainMenuMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		Object obj = state.get(Constants.MENU_FOCUS);
		
		MenuItemHandlerBean rtn = null;
		
		if (obj != null && (obj.equals(Constants.LONG_TERM_GOALS_JSON) || 
				obj.equals(Constants.SHORT_TERM_GOALS_JSON) || 
				obj.equals(Constants.EXPENSES_JSON))) 
		{
			rtn = new ToMainMenuMenuItemHandler();
		}
		
		return rtn;
	}
}
