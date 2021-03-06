package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.ListExpensesMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ListExpensesMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		
		MenuItemHandlerBean rtn = null;
		
		if (MenuItemUtils.isMenuFocusedOn(dass.getState(), Constants.EXPENSES_JSON))
			rtn = new ListExpensesMenuItemHandler();
		
		return rtn;
	}
}
