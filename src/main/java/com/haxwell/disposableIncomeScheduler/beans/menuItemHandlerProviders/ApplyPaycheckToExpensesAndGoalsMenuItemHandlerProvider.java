package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.ApplyPaycheckToExpensesAndGoalsMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		MenuItemHandlerBean rtn = null;
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();
		JSONObject state = dass.getState();
		
		if ((isCurrentDateOnOrAfterTheNextPaycheckDateToOccurAfterTheMostRecentPaycheck() ||
				(data.containsKey(Constants.EXPENSES_HAVE_NEVER_BEEN_APPLIED_JSON)))
				&& (!state.containsKey(Constants.CURRENT_PAYCHECK_HAS_BEEN_APPLIED_TO_LTGS)
						&& (MenuItemUtils.isMenuFocusedOnTheMainLevel(state)))) {
			rtn = new ApplyPaycheckToExpensesAndGoalsMenuItemHandler();
		} 
		
		return rtn;
	}
	
	private boolean isCurrentDateOnOrAfterTheNextPaycheckDateToOccurAfterTheMostRecentPaycheck() {
		boolean rtn = false;
		
		// if the curr date is on or after the date of the next paycheck to occur after the most recent paycheck to be applied
		Calendar curCal = CalendarUtils.getCurrentCalendar();
		Date curDate = curCal.getTime();
		
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		Date nextPaycheckAfterMRP = PaycheckUtils.getFuturePaydate(dass.getData(), 1);
		
		// return true
		rtn = (curDate.equals(nextPaycheckAfterMRP) || curDate.after(nextPaycheckAfterMRP));
		
		return rtn;
	}
}
