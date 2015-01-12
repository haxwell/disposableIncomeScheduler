package com.haxwell.disposableIncomeScheduler.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.springframework.util.StringUtils;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;
import com.haxwell.disposableIncomeScheduler.validators.PositiveIntegerValidator;

public class ApplyPaycheckToExpensesAndGoalsMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	PaycheckUtils pu;
	
	public String getMenuText() {
		String str = getDateOfPaycheckToBeAppliedAsString();
		return "Apply the most recent paycheck (" + str + ")";
	}

	private String getDateOfPaycheckToBeAppliedAsString() {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();
		
		// get the mrpd
		Date mrpd = PaycheckUtils.getMostRecentPaydate(data);
		
		// get the next pd
		Date pd = PaycheckUtils.getFuturePaydate(data, 1);
		
		Date today = Calendar.getInstance().getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String str = null;
		// if today is the day of or after next pd, display next pd
		if (today.equals(pd) || today.after(pd)) {
			str = sdf.format(pd);
		} else {
			// 	otherwise use mrpd
			str = sdf.format(mrpd);
		}
		return str;
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		pu = new PaycheckUtils(data);
		
		getPrintlner().println("Type 'yes' to apply the paycheck for " + getDateOfPaycheckToBeAppliedAsString() + "..");
		
		String input = getInputGetter().readInput();
		
		if (input.toLowerCase().equals("yes")) {
			
			accountForAmountsSpentOnEachSTG(data);
			
			initializePreviousBalanceState(data);
			
			// if this is the first check of the month, subtract the monthly expenses from the prev total.
			if (pu.getNextPaycheckNumber(pu.getMostRecentPaydate(data)) == 1) {
				subtractMonthlyExpensesFromThePrevTotal(data);
			}
			
			// subtract any non-accumulating stgs from the prev total.. accumulating stgs must be reset manually as they are spent
			subtractSTGsFromThePrevTotal(data);
			
			setBeginningBalanceToIncludeThisPeriodsPay(data);
			
			updateShortTermGoalTotalSavedAmounts(data);
			state.put(Constants.CURRENT_PAYCHECK_HAS_BEEN_APPLIED_TO_STGS, "true");			
			
			// get the total that would be remaining after accounting for expenses and short term goals
			long totalDollarAmount = Calculator.getDollarAmountToBeSpreadOverLongTermGoals(data, state);

			Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);
			
			rtn = true;
			
			state.put(Constants.CURRENT_PAYCHECK_HAS_BEEN_APPLIED_TO_LTGS, "true");
			long numPaychecksProcessed = Integer.parseInt(data.get(Constants.NUMBER_OF_PAYCHECKS_PROCESSED)+"");
			data.put(Constants.NUMBER_OF_PAYCHECKS_PROCESSED, numPaychecksProcessed+1);
			
			pu.advanceMostRecentPaydateByOnePeriod(data);
		}
		
		return rtn;
	}

	private void accountForAmountsSpentOnEachSTG(JSONObject data) {
		PositiveIntegerValidator posIntValidator = new PositiveIntegerValidator();
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		
		getPrintlner().println("How much was spent in the last period for each of the following short term goals? (ENTER = 0)");
		
		for (int idx=0; idx < stgs.size(); idx++) {
			JSONObject obj = (JSONObject)stgs.get(idx);
			
			getPrintlner().print(obj.get(Constants.DESCRIPTION_JSON) + " [" + obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON) + "]: ");
			String input = getInputGetter().readInput();
			
			if (StringUtils.isEmpty(input)) {
				input = "0";
			}
			
			Long amtSpent = Long.parseLong(posIntValidator.getValidValue(input));
			
			Long totalInThePot = Long.parseLong(data.get(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON)+"") - amtSpent;
			data.put(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON, totalInThePot+"");
			
			Long totalAmtSaved = Long.parseLong(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"") - amtSpent;
			obj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, totalAmtSaved+"");
		}
	}

	private void initializePreviousBalanceState(JSONObject data) {
		Object object = data.get(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON);
		data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, object);
	}

	private void subtractMonthlyExpensesFromThePrevTotal(JSONObject data) {
		long previousTotalInThePot = Integer.parseInt(data.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON)+"");
		long numPaychecksProcessed = Integer.parseInt(data.get(Constants.NUMBER_OF_PAYCHECKS_PROCESSED)+"");
		
		// check if the numberOfPaychecksProcessed is greater than zero. If it is not, we assume this is
		//  the first time this process is being run or some other anomalous case is in the offing.
		if (numPaychecksProcessed > 0) {
			JSONArray expenses = MenuItemUtils.getExpenses(data);
			
			for (int i = 0; i < expenses.size(); i++) {
				JSONObject obj = (JSONObject)expenses.get(i);
				Long price = Long.parseLong(obj.get(Constants.PRICE_JSON)+"");
				
				previousTotalInThePot -= price;
			}
			
			data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, previousTotalInThePot+"");
		}
	}

	private void subtractSTGsFromThePrevTotal(JSONObject data) {
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		long previousTotalInThePot = Integer.parseInt(data.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON)+"");
		long numPaychecksProcessed = Integer.parseInt(data.get(Constants.NUMBER_OF_PAYCHECKS_PROCESSED)+"");
		
		// check if the numberOfPaychecksProcessed is greater than zero. If it is not, we assume this is
		//  the first time this process is being run or some other anomalous case is in the offing.
		if (numPaychecksProcessed > 0) {
			for (int i = 0; i < stgs.size(); i++) {
				JSONObject obj = (JSONObject)stgs.get(i);
				Long amt = Long.parseLong(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"");
				
				previousTotalInThePot -= amt;
			}

			data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, previousTotalInThePot+"");
		}
	}

	private void updateShortTermGoalTotalSavedAmounts(JSONObject data) {
		// update all the short term goals 
		JSONArray shortTermGoals = MenuItemUtils.getShortTermGoals(data);
		
		for (int i = 0; i < shortTermGoals.size(); i++) {
			JSONObject obj = (JSONObject)shortTermGoals.get(i);
			
			long numPaychecksInThisPeriod = pu.getNumberOfPaychecks(CalendarUtils.getCurrentCalendar().getTime()); 
					
			int amount = Math.round(Long.parseLong(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"") / numPaychecksInThisPeriod);
			Long total = Long.parseLong(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"");
			
			obj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, (amount + total)+"");
		}
	}

	private void setBeginningBalanceToIncludeThisPeriodsPay(JSONObject data) {
		String amtStr = (String)data.get(Constants.AMT_PAID_PER_PERIOD_JSON);
		Integer amt = Integer.parseInt(amtStr);
		
		String prevInThePot = (String)data.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON);
		Integer prevTotal = Integer.parseInt(prevInThePot == null ? "0" : prevInThePot);
		
		String value = (prevTotal + amt)+"";
		data.put(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON, value);
	}
}
