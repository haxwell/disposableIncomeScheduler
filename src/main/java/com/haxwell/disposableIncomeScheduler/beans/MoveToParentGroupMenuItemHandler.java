package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class MoveToParentGroupMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Move to Parent Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		
		JSONArray arr = MenuItemUtils.getParentOfSelectedGroup(data, state);
		
		return rtn;
	}
}