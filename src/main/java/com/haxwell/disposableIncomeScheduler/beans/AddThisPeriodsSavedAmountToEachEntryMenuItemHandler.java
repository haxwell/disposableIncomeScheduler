package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;

public class AddThisPeriodsSavedAmountToEachEntryMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	private boolean amtHasAlreadyBeenAdded = false;
	
	public String getMenuText() {
		return "Add This Period's Saved Amount To Each Entry";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray items = (JSONArray)data.get("items");
		
		if (items == null || items.size() == 0) {
			System.out.println("\nThere are no entries!\n");
			return rtn;
		}
		
		if (amtHasAlreadyBeenAdded) {
			System.out.println("\nThe periodically saved amount has already been added!\n");
			return rtn;
		}

		System.out.println("Type 'yes' to update each entry..");
		
		String input = System.console().readLine();
		
		if (input.toLowerCase().equals("yes")) {
			// add the amount saved per month to the total in the pot
			String amtStr = (String)data.get(Constants.AMT_SAVED_PER_PERIOD_JSON);
			Integer amt = Integer.parseInt(amtStr);
			
			String totalInThePot = (String)data.get(Constants.TOTAL_IN_THE_POT_JSON);
			Integer total = Integer.parseInt(totalInThePot);
			
			data.put(Constants.TOTAL_IN_THE_POT_JSON, (total + amt)+"");
			
			Calculator.applyMoney(data);
			
			rtn = true;
			amtHasAlreadyBeenAdded = true;
		}
		
		return rtn;
	}
}
