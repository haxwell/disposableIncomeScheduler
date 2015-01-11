package com.haxwell.disposableIncomeScheduler.beans.textDisplayedBeforeAndAfterMenuBeans;

import com.haxwell.disposableIncomeScheduler.Constants;

public class MainLevelBeforeAndAfterMenuBean extends AbstractBeforeAndAfterMenuBean {

	public String getAssociatedMenuFocusState() {
		return Constants.MAIN_LEVEL_MENU_FOCUS;
	}
	
	@Override
	public String getBeforeText() {
		return "================\nMain Menu\n================\n";
	}
}
