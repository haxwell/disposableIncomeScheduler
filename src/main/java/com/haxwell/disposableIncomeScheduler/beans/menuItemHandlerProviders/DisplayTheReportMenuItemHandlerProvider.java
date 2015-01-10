package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.Calendar;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.DisplayTheReportMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class DisplayTheReportMenuItemHandlerProvider extends AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();

		// TODO: This same code is in ListTheScheduleMenuItemHandlerProvider. Refactor.
		
		// if the current date is the day of, or after the date of the next check following the most 
		// recent paycheck date, user needs to apply the current paycheck before displaying the report. 
		// The report should always reflect the current state of the account.
		Calendar cal = CalendarUtils.getCurrentCalendar();
		
		Calendar mrpd = CalendarUtils.getCurrentCalendar();
		mrpd.setTime(PaycheckUtils.getMostRecentPaydate(data));
		
		Calendar next_pd = Calendar.getInstance();
		next_pd.setTime(mrpd.getTime());
		CalendarUtils.advanceCalendarByPeriodLength(next_pd);
		
		MenuItemHandlerBean rtn = null;
		if ((cal.equals(next_pd) || cal.after(next_pd))) {
			rtn = null;
		} else {
			if (MenuItemUtils.isMenuFocusedOnTheMainLevel(dass.getState()))
				rtn = new DisplayTheReportMenuItemHandler();
		}
		
		return rtn;
	}
}
