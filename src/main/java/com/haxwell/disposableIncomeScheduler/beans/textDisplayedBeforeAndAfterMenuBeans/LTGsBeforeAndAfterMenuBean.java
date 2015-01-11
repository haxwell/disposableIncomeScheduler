package com.haxwell.disposableIncomeScheduler.beans.textDisplayedBeforeAndAfterMenuBeans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class LTGsBeforeAndAfterMenuBean extends AbstractBeforeAndAfterMenuBean {

	public String getAssociatedMenuFocusState() {
		return Constants.LONG_TERM_GOALS_JSON;
	}
	
	@Override
	public String getBeforeText() {
		return "================\nLong Term Goals\n================\n";
	}
	
	@Override 
	public String getAfterText() {
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		
		return "\nCurrent Group: " + MenuItemUtils.getSelectedGroupName(state) + "\n";
	}
}
