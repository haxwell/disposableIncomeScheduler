package com.haxwell.disposableIncomeScheduler.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.GroupedGoalsIterator;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.report.commands.AddCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.CalculateLongTermGoalsFunctionCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.StringCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.SubtotalCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.SubtractCommand;

public class ReportGenerator {

	final String REPORT_BEGIN = "reportBeginDate";
	final String REPORT_END = "reportEndDate";
	final String CHECK_OF_THE_MONTH = "checkOfTheMonth";
	final String BEGINNING_BALANCE = "beginningBalance";
	final String RAINY_DAY_FUND_AMT = "rainyDayFundAmt";
	
	PaycheckUtils pu;
	JSONObject reportData = new JSONObject();
	
	public ReportGenerator() {
	}
	
	public CommandList getReportData(JSONObject data) {
		pu = new PaycheckUtils(data);
		
		CommandList cl = new CommandList();
		
		cl.add(new StringCommand("Report for " + getReportBeginDate(data) + " thru " + getReportEndDate(data)));
		cl.add(new StringCommand(""));
		cl.add(new StringCommand(getCheckOfTheMonth(data) + " check of the month"));
		cl.add(new StringCommand(""));
		
		cl.add(new AddCommand(getBeginningBalance(data), "Previous balance"));
		cl.add(new AddCommand(getAmountPaidPerPeriod(data), "Paycheck"));
		cl.add(new SubtotalCommand("Beginning balance - " + getReportBeginDate(data)));
		
		cl.add(new SubtractCommand(getRainyDayFundAmount(data), "Rainy Day Fund"));
		cl.add(new SubtotalCommand("After Generically Reserved Funds"));
		
		double offset = getOffset(data);
		JSONArray expenses = MenuItemUtils.getExpenses(data);
		for (int i = 0; i < expenses.size(); i++) {
			JSONObject obj = (JSONObject) expenses.get(i);
			
			cl.add(new SubtractCommand(Integer.parseInt(obj.get(Constants.PRICE_JSON)+"")*offset, obj.get(Constants.DESCRIPTION_JSON)+"", "expensesGroup"));
		}
		
		cl.add(new SubtotalCommand("Total Monthly Expenses", "expensesGroup"));
		cl.add(new SubtotalCommand("Total in account, After Monthly Expenses"));
		
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		for (int i = 0; i < stgs.size(); i++) {
			JSONObject obj = (JSONObject) stgs.get(i);
			
			cl.add(new SubtractCommand(Integer.parseInt(obj.get(Constants.AMT_SAVED_PER_PERIOD_JSON)+"")*offset, obj.get(Constants.DESCRIPTION_JSON)+"", "stgsGroup"));
		}
		
		cl.add(new SubtotalCommand("Total Short Term Goals", "stgsGroup"));
		cl.add(new SubtotalCommand("Savings, to be spread over Long Term Goals", data, Constants.AMT_SAVED_PER_PERIOD_JSON));
		
		cl.add(new CalculateLongTermGoalsFunctionCommand(data));
		
		cl.add(new SubtotalCommand("Total - Amount Remaining"));

		return cl;
	}
	
	public double getOffset(JSONObject data) {
		int num = pu.getPaycheckNumber(data, Calendar.getInstance().getTime());
		double rtn = -1;
		
		if (num == 1) rtn = 0.5;
		if (num == 2) rtn = 1.0;
		if (num == 3) rtn = 0.0;
		
		return rtn;
	}
	
	public void print(JSONObject data) {
		setReportHeaderVariables(data);
	}
	
	public void setReportHeaderVariables(JSONObject data) {
		reportData.put(REPORT_BEGIN, getReportBeginDate(data));
		reportData.put(REPORT_END, getReportEndDate(data));
		reportData.put(CHECK_OF_THE_MONTH, getCheckOfTheMonth(data));
	}
	
	public void setBeginningBalance(JSONObject data) {

		int beginningBalance = getBeginningBalance(data);
		int rainyDayFundAmount = getRainyDayFundAmount(data);
		
		reportData.put(BEGINNING_BALANCE, beginningBalance+"");
		reportData.put(RAINY_DAY_FUND_AMT, rainyDayFundAmount+"");
		
//		reportData.put()
	}
	
	protected int getAmountPaidPerPeriod(JSONObject data) {
		return Integer.parseInt(data.get(Constants.AMT_PAID_PER_PERIOD_JSON)+"");
	}
	
	protected int getRainyDayFundAmount(JSONObject data) {
		return Integer.parseInt(data.get(Constants.AMT_SAVED_FOR_RAINY_DAY_JSON)+"");
	}
	
	protected int getBeginningBalance(JSONObject data) {
		int totalInThePot = Integer.parseInt(data.get(Constants.TOTAL_IN_THE_POT_JSON)+"");
//		int amtPaidThisPeriod = Integer.parseInt(data.get(Constants.AMT_PAID_PER_PERIOD_JSON)+"");
		
		return totalInThePot; // + amtPaidThisPeriod;
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
			
			cal = Calendar.getInstance();
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
	
	protected String getCheckOfTheMonth(JSONObject data) {
		return pu.getPaycheckNumberAsString(data, Calendar.getInstance().getTime()); 
	}
}
