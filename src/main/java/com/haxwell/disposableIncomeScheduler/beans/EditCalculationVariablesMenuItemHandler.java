package com.haxwell.disposableIncomeScheduler.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class EditCalculationVariablesMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Edit Calculation Variables";
	}
	
	private LinkedList<String> getListOfAttributes() {
		LinkedList<String> rtn = new LinkedList<>();
		
		rtn.add(Constants.PERIOD_LENGTH);
		rtn.add(Constants.AMT_PAID_PER_PERIOD);
//		rtn.add(Constants.AMT_SAVED_PER_MONTH);
		rtn.add(Constants.AMT_SAVED_FOR_RAINY_DAY);
//		rtn.add(Constants.TOTAL_IN_THE_POT);
		
		return rtn;
	}
	
	private Map<String, String> getDisplayToJSONMap() {
		Map<String, String> map = new HashMap<>();
		
		map.put(Constants.PERIOD_LENGTH, Constants.PERIOD_LENGTH_JSON);
		map.put(Constants.AMT_PAID_PER_PERIOD, Constants.AMT_PAID_PER_PERIOD_JSON);
//		map.put(Constants.AMT_SAVED_PER_MONTH, Constants.AMT_SAVED_PER_MONTH_JSON);
		map.put(Constants.AMT_SAVED_FOR_RAINY_DAY, Constants.AMT_SAVED_FOR_RAINY_DAY_JSON);
//		map.put(Constants.TOTAL_IN_THE_POT, Constants.TOTAL_IN_THE_POT_JSON);
		
		return map;
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		LinkedList<String> attrList = getListOfAttributes();
		Map<String, String> displayToJSONMap = getDisplayToJSONMap();
		Map<Integer, String> map = new HashMap<>();
		
		int count = 0;
		for (String str : attrList) {
			map.put(++count, str);
		}
		
		String choice = "";
		
		do {
			getPrintlner().println("");
			
			count = 0;
			for (String str : attrList)
				getPrintlner().println(++count + ". " + str + " = " + data.get(displayToJSONMap.get(str)));
			
			choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				boolean NaN = false;
				Integer choiceAsInt = null;
				try {
					choiceAsInt = Integer.parseInt(choice);
				}
				catch (NumberFormatException nfe) {
					NaN = true;
				}
				
				if (!NaN) {
					getPrintlner().println("");
					getPrintlner().print("New value: ");
					
					String val = getInputGetter().readInput();
					
					try {
						Integer.parseInt(val);
						data.put(displayToJSONMap.get(map.get(choiceAsInt)), val);
						rtn = true;
					}
					catch (NumberFormatException nfe) {
						getPrintlner().println("\nNot A Number!\n");
					}
				}
			}
		} while (choice != null && !choice.equals(""));
		
		return rtn;
	}
}
