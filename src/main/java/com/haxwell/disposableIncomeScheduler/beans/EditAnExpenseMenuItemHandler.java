package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.validators.Validator;

public class EditAnExpenseMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	Map<String, Validator> validatorMap = getValidatorMap();
	
	public String getMenuText() {
		return "Edit An Expense";
	}
	
	// TODO: This and EditAShortTermGoal are very similar.. refactor
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		JSONArray arr = MenuItemUtils.getExpenses(data);
		
		// list each item
		int count = 0;
		for (; count < arr.size(); count++) {
			String description = ((JSONObject)arr.get(count)).get(Constants.DESCRIPTION_JSON)+"";
			System.out.println(count+1 + ". " + description);
		}
		
		if (count > 0) {
			String choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				JSONObject obj = (JSONObject)arr.get(Integer.parseInt(choice) - 1);
				
				System.out.print("Name [" + obj.get(Constants.DESCRIPTION_JSON) + "]: ");
				String name = getInputGetter().readInput();
				
				if (name != null && !name.equals("")) {
					obj.put(Constants.DESCRIPTION_JSON, name);
					rtn = true;
				}
				
				System.out.print("Cost [" + obj.get(Constants.PRICE_JSON) + "]: ");
				String price = getInputGetter().readInput();
				
				if (price != null && !price.equals("") && getPriceValidator().isValidValue(price)) {
					obj.put(Constants.PRICE_JSON, price);
					rtn = true;
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
