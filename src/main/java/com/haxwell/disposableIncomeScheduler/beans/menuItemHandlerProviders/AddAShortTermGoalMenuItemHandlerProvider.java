package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.AddAShortTermGoalMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public class AddAShortTermGoalMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler(JSONObject data, JSONObject state) {
		return new AddAShortTermGoalMenuItemHandler();
	}
}