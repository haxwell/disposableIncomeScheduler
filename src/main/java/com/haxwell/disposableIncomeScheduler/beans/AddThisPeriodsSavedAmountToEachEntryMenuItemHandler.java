package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Map;

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
			
			// for each item, calculate its weight, and add its share of the amount saved this month to its total
			Map<String, Double> weights = Calculator.getWeights(data);

			int count = 0;
			for (; count < items.size(); count++) {
				JSONObject obj = (JSONObject)items.get(count);
				
				Double objWeight = weights.get(obj.get(Constants.DESCRIPTION_JSON));
				
				Double objShare = objWeight * amt;
				
				Object psam = obj.get(Constants.PREVIOUS_SAVED_AMT_JSON);
				Long objPrevSaved = 0l;
				
				if (psam != null)
					objPrevSaved = Long.parseLong(psam.toString());
				
				objPrevSaved += Math.round(objShare);
				
				obj.put(Constants.PREVIOUS_SAVED_AMT_JSON, objPrevSaved+"");
			}
			
			rtn = true;
			amtHasAlreadyBeenAdded = true;
		}
		
		return rtn;
	}
}
