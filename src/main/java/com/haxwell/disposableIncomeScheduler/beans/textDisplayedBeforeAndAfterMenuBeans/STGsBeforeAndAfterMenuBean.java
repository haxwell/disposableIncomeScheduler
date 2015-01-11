package com.haxwell.disposableIncomeScheduler.beans.textDisplayedBeforeAndAfterMenuBeans;

import com.haxwell.disposableIncomeScheduler.Constants;

public class STGsBeforeAndAfterMenuBean extends AbstractBeforeAndAfterMenuBean {

	public String getAssociatedMenuFocusState() {
		return Constants.SHORT_TERM_GOALS_JSON;
	}
	
	@Override
	public String getBeforeText() {
		return "================\nShort Term Goals\n================\n";
	}
}
