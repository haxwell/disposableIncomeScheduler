package com.haxwell.disposableIncomeScheduler.beans.textDisplayedBeforeAndAfterMenuBeans;

import com.haxwell.disposableIncomeScheduler.Constants;

public class ExpensesBeforeAndAfterMenuBean extends AbstractBeforeAndAfterMenuBean {

	public String getAssociatedMenuFocusState() {
		return Constants.EXPENSES_JSON;
	}
	
	@Override
	public String getBeforeText() {
		return "================\nExpenses\n================\n";
	}
}
