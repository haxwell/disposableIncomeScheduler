package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.validators.Validator;

public class EditAShortTermGoalMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	Map<String, Validator> validatorMap = getValidatorMap();
	
	public String getMenuText() {
		return "Edit A Short Term Goal";
	}
	
	// TODO: This and EditAnExpense are very similar.. refactor
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			String description = ((JSONObject)arr.get(count)).get(Constants.DESCRIPTION_JSON)+"";
			getPrintlner().println(count+1 + ". " + description);
		}
		
		if (count > 0) {
			String choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				JSONObject obj = (JSONObject)arr.get(Integer.parseInt(choice) - 1);
				
				getPrintlner().print("Name [" + obj.get(Constants.DESCRIPTION_JSON) + "]: ");
				String name = getInputGetter().readInput();
				
				if (name != null && !name.equals("")) {
					obj.put(Constants.DESCRIPTION_JSON, name);
					rtn = true;
				}
				
				getPrintlner().print("Amount Saved Per Period [" + obj.get(Constants.AMT_SAVED_PER_MONTH_JSON) + "]: ");
				String price = getInputGetter().readInput();
				
				if (price != null && !price.equals("") && getPriceValidator().isValidValue(price)) {
					obj.put(Constants.AMT_SAVED_PER_MONTH_JSON, price);
					rtn = true;
				}

				getPrintlner().print("Reset With Each New Period? [" + obj.get(Constants.RESET_EACH_PERIOD_JSON) + "]: (y/n)");
				String reset = getInputGetter().readInput();
				
				if (reset != null && !reset.equals("")) {
					String upperReset = reset.toUpperCase();
					
					if (upperReset.equals("Y") || upperReset.equals("N")) {
						obj.put(Constants.RESET_EACH_PERIOD_JSON, reset);
						rtn = true;
					}
					else {
						getPrintlner().println("No change made. Expected 'y' or 'n'.\n");
					}
				}

				reset = obj.get(Constants.RESET_EACH_PERIOD_JSON)+"";
				if (reset.toUpperCase().equals("N")) {
					getPrintlner().print("Total Amount Saved [" + obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON) + "]: ");
					String tas = getInputGetter().readInput();
					
					if (tas != null && !tas.equals("") && getPriceValidator().isValidValue(tas)) {
						obj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, tas);
						rtn = true;
					}
				}
				else {
					obj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
				}
			}
		}
		else
			System.out.println("\nNo Items to Edit!\n");
		
		return rtn;
	}
	
	private Validator getPriceValidator() {
		return validatorMap.get(Constants.PRICE);
	}
}