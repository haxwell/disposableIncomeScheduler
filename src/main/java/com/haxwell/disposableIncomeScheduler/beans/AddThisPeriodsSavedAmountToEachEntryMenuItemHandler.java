package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;

public class AddThisPeriodsSavedAmountToEachEntryMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add This Period's Saved Amount To Each Entry";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		if (state.containsKey(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_LTGS)) {
			System.out.println("\nThe periodically saved amount has already been added!\n");
			return rtn;
		}

		getPrintlner().println("Type 'yes' to update each entry..");
		
		String input = getInputGetter().readInput();
		
		if (input.toLowerCase().equals("yes")) {
			PaycheckUtils pu = new PaycheckUtils(data);
			
			initializePreviousBalanceState(data);
			
			// if this is the first check of the month, subtract the monthly expenses from the prev total.
			if (pu.getNextPaycheckNumber(pu.getMostRecentPaydate(data)) == 1) {
				subtractMonthlyExpensesFromThePrevTotal(data);
			}
			
			// subtract any non-accumulating stgs from the prev total.. accumulating stgs must be reset manually as they are spent
			subtractNonAccumulatingSTGsFromThePrevTotal(data);
			
			setBeginningBalanceToIncludeThisPeriodsPay(data);
			
			updateShortTermGoalTotalSavedAmounts(data);
			state.put(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_STGS, "true");			
			
			// get the total that would be remaining after accounting for expenses and short term goals
			long totalDollarAmount = Calculator.getDollarAmountToBeSpreadOverLongTermGoals(data, state);

			Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);
			
			rtn = true;
			
			state.put(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_LTGS, "true");
			long numPaychecksProcessed = Integer.parseInt(data.get(Constants.NUMBER_OF_PAYCHECKS_PROCESSED)+"");
			data.put(Constants.NUMBER_OF_PAYCHECKS_PROCESSED, numPaychecksProcessed+1);
			
			pu.advanceMostRecentPaydateByOnePeriod(data);
		}
		
		return rtn;
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

	private void subtractNonAccumulatingSTGsFromThePrevTotal(JSONObject data) {
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		long previousTotalInThePot = Integer.parseInt(data.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON)+"");
		long numPaychecksProcessed = Integer.parseInt(data.get(Constants.NUMBER_OF_PAYCHECKS_PROCESSED)+"");
		
		// check if the numberOfPaychecksProcessed is greater than zero. If it is not, we assume this is
		//  the first time this process is being run or some other anomalous case is in the offing.
		if (numPaychecksProcessed > 0) {
			for (int i = 0; i < stgs.size(); i++) {
				JSONObject obj = (JSONObject)stgs.get(i);
				
				String resetEachPeriod = obj.get(Constants.RESET_EACH_PERIOD_JSON)+"";
				if (resetEachPeriod.toUpperCase().equals("Y")) {
					Long amt = Long.parseLong(obj.get(Constants.AMT_SAVED_PER_PERIOD_JSON)+"");
					
					previousTotalInThePot -= amt;
				}
			}

			data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, previousTotalInThePot+"");
		}
	}

	private void updateShortTermGoalTotalSavedAmounts(JSONObject data) {
		// update all the short term goals that need updating...
		JSONArray shortTermGoals = MenuItemUtils.getShortTermGoals(data);
		
		for (int i = 0; i < shortTermGoals.size(); i++) {
			JSONObject obj = (JSONObject)shortTermGoals.get(i);
			
			String resetEachPeriod = obj.get(Constants.RESET_EACH_PERIOD_JSON)+"";
			if (resetEachPeriod.toUpperCase().equals("N")) {
				Long amount = Long.parseLong(obj.get(Constants.AMT_SAVED_PER_PERIOD_JSON)+"");
				Long total = Long.parseLong(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"");
				
				obj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, (amount + total)+"");
			}
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
