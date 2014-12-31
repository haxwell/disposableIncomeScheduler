package com.haxwell.disposableIncomeScheduler.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.report.CommandList;
import com.haxwell.disposableIncomeScheduler.report.ReportGenerator;
import com.haxwell.disposableIncomeScheduler.report.ReportScreenPrinter;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;

public class DisplayTheReportMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Display the Report";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		
		// if the current date is the day of, or after the date of the next check following the most 
		// recent paycheck date, user needs to apply the current paycheck before displaying the report. 
		// The report should always reflect the current state of the account.
		Calendar cal = CalendarUtils.getCurrentCalendar();
		
		Calendar mrpd = CalendarUtils.getCurrentCalendar();
		mrpd.setTime(new PaycheckUtils(data).getMostRecentPaydate(data));
		
		Calendar next_pd = Calendar.getInstance();
		next_pd.setTime(mrpd.getTime());
		CalendarUtils.advanceCalendarByPeriodLength(next_pd);
		
		if ((cal.equals(next_pd) || cal.after(next_pd))) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			getPrintlner().println("The current date (" + sdf.format(cal.getTime()) + ") falls after what should be the next paycheck date (" + sdf.format(next_pd.getTime()) + "), and it seems the current paycheck has not been applied to your expenses and goals. Apply the current paycheck first, and then run this report.\n");
			return false;
		}
		
		ReportGenerator rg = new ReportGenerator();
		
		CommandList cl = rg.getReportData(data, state);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		getPrintlner().println("");
		
		rsp.print(getPrintlner());
		
		getPrintlner().println("Press Enter To Continue...");
		getInputGetter().readInput();
		
		return false;
	}
}
