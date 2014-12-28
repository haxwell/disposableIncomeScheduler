package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class AddThisPeriodsSavedAmountToEachEntryMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	private boolean amtHasAlreadyBeenAdded = false;
	
	public String getMenuText() {
		return "Add This Period's Saved Amount To Each Entry";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray rootGroup = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		
		if (rootGroup == null || rootGroup.size() == 0) {
			System.out.println("\nThere are no entries!\n");
			return rtn;
		}
		
		if (state.containsKey(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED)) {
			System.out.println("\nThe periodically saved amount has already been added!\n");
			return rtn;
		}

		getPrintlner().println("Type 'yes' to update each entry..");
		
		String input = getInputGetter().readInput();
		
		if (input.toLowerCase().equals("yes")) {
			setBeginningBalanceToIncludeThisPeriodsPay(data);
			
			updateShortTermGoalTotalSavedAmounts(data);
			
			// get the total that would be remaining after accounting for expenses and short term goals
			long totalDollarAmount = Calculator.getDollarAmountToBeSpreadOverLongTermGoals(data, state);

			Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);
			
			rtn = true;
			amtHasAlreadyBeenAdded = true;
			
			state.put(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED, "true");
		}
		
		return rtn;
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
		
		String totalInThePot = (String)data.get(Constants.TOTAL_IN_THE_POT_JSON);
		Integer total = Integer.parseInt(totalInThePot == null ? "0" : totalInThePot);
		
		data.put(Constants.TOTAL_IN_THE_POT_JSON, (total + amt)+"");
	}
}
