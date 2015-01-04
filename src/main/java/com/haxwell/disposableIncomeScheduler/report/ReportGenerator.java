package com.haxwell.disposableIncomeScheduler.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.report.commands.AddCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.CalculateLongTermGoalsFunctionCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.StringCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.SubtotalCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.SubtractCommand;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;

public class ReportGenerator {

	PaycheckUtils pu;
	JSONObject reportData = new JSONObject();
	
	public ReportGenerator() {
	}
	
	// TODO: A current problem is that we have the calculation of the report happening in at least two places.. First when
	//  the money is applied, and second HERE in displaying the report. In both places we are getting values and adding this
	//  subtracting that. There should be one means of describing the calculation that both (all) places can use.
	//
	//  see: AddThisPeriodsSavedAmountToEachEntryMenuItemHandler.java::doIt()
	
	public CommandList getReportData(JSONObject data, JSONObject state) {
		pu = new PaycheckUtils(data);
		
		CommandList cl = new CommandList();
		
		cl.add(new StringCommand("Report for " + getReportBeginDate(data) + " thru " + getReportEndDate(data)));
		cl.add(new StringCommand(""));
		cl.add(new StringCommand(getCheckOfTheMonth() + " of " + getNumberOfChecksForTheMonth() + " checks in the month"));
		cl.add(new StringCommand(""));
		
		Map<String, Long> map = Calculator.getDollarAmountsToBeAppliedToGenericallyReservedFunds(data);
		
		cl.add(new AddCommand(map.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON), "Previous balance"));
		cl.add(new AddCommand(map.get(Constants.PAYCHECK_AMT), "Paycheck"));
		cl.add(new SubtotalCommand("Beginning balance - " + getReportBeginDate(data)));
		
		cl.add(new SubtractCommand(map.get(Constants.RAINY_DAY_FUND_AMT), "Rainy Day Fund"));
		cl.add(new SubtotalCommand("After Generically Reserved Funds"));
		
		Map<String, Double> expenseMap = Calculator.getDollarAmountsToBeAppliedToExpenses(data);
		
		for (String key : expenseMap.keySet()) {
			cl.add(new SubtractCommand(expenseMap.get(key), key, "expensesGroup"));
		}
		
		cl.add(new SubtotalCommand("Total Monthly Expenses", "expensesGroup"));
		cl.add(new SubtotalCommand("Total in account, After Monthly Expenses"));
		
		Map<String, Double> stgMap = Calculator.getDollarAmountsToBeAppliedToShortTermGoals(data, state);
		
		for (String key : stgMap.keySet()) {
			cl.add(new SubtractCommand(stgMap.get(key), key, "stgsGroup"));
		}
		
		cl.add(new SubtotalCommand("Total Short Term Goals", "stgsGroup"));
		cl.add(new SubtotalCommand("Savings, to be spread over Long Term Goals", data, Constants.AMT_SAVED_PER_MONTH_JSON));
		
		cl.add(new CalculateLongTermGoalsFunctionCommand(data, map, expenseMap, stgMap));
		
		cl.add(new SubtotalCommand("Total - Amount Remaining"));

		return cl;
	}
	
	protected String getReportBeginDate(JSONObject data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		String rtn = null;
		
		try {
			date = sdf.parse(data.get(Constants.MOST_RECENT_PAYDATE)+"");
 
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		rtn = sdf.format(date);
		
		return rtn;
	}
	
	protected String getReportEndDate(JSONObject data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		String rtn = null;
		
		try {
			Calendar cal = null;
			date = sdf.parse(data.get(Constants.MOST_RECENT_PAYDATE)+"");
			
			cal = CalendarUtils.getCurrentCalendar();
			cal.setTime(date);
			
			int periodLength = Integer.parseInt(data.get(Constants.PERIOD_LENGTH_JSON)+"");
			
			cal.add(Calendar.DAY_OF_MONTH, periodLength);
			date = cal.getTime();
			
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		rtn = sdf.format(date);
		
		return rtn;
	}
	
	protected String getCheckOfTheMonth() {
		Date d = CalendarUtils.getCurrentCalendar().getTime();
		return pu.getPaycheckNumberAsString(d); 
	}
	
	protected String getNumberOfChecksForTheMonth() {
		Date d = CalendarUtils.getCurrentCalendar().getTime();
		return pu.getNumberOfPaychecks(d)+"";
	}
}
