package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import com.haxwell.disposableIncomeScheduler.beans.AddThisPeriodsSavedAmountToEachEntryMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public class ApplyThisPeriodsSavedAmountToGoalsMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		return new AddThisPeriodsSavedAmountToEachEntryMenuItemHandler();
	}
}
