package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public abstract class AbstractMenuItemHandlerProvider {

	public abstract MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state);
	
}
