package com.haxwell.disposableIncomeScheduler.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class EditCalculationVariablesMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	LinkedList<String> keys = getListOfKeys();
	Map<String, Validator> validatorMap = getValidatorMap();
	Map<String, String> displayToJSONMap = getMapOfDisplayNamesToJSONFieldNames();
	
	public String getMenuText() {
		return "Edit Calculation Variables";
	}
	
	private LinkedList<String> getListOfAttributes() {
		LinkedList<String> rtn = new LinkedList<>();
		
		rtn.add(Constants.PERIOD_LENGTH);
		rtn.add(Constants.AMT_SAVED_PER_PERIOD);
		rtn.add(Constants.TOTAL_IN_THE_POT);
		
		return rtn;
	}
	
	private Map<String, String> getDisplayToJSONMap() {
		Map<String, String> map = new HashMap<>();
		
		map.put(Constants.PERIOD_LENGTH, Constants.PERIOD_LENGTH_JSON);
		map.put(Constants.AMT_SAVED_PER_PERIOD, Constants.AMT_SAVED_PER_PERIOD_JSON);
		map.put(Constants.TOTAL_IN_THE_POT, Constants.TOTAL_IN_THE_POT_JSON);
		
		return map;
	}
	
	public boolean doIt(JSONObject data) {
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
			System.out.println();
			
			count = 0;
			for (String str : attrList)
				System.out.println(++count + ". " + str + " = " + data.get(displayToJSONMap.get(str)));
			
			choice = System.console().readLine();
			
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
					System.out.println();
					System.out.print("New value: ");
					
					String val = System.console().readLine();
					
					try {
						Integer.parseInt(val);
						data.put(displayToJSONMap.get(map.get(choiceAsInt)), val);
						rtn = true;
					}
					catch (NumberFormatException nfe) {
						System.out.println("\nNot A Number!\n");
					}
				}
			}
		} while (choice != null && !choice.equals(""));
		
		return rtn;
	}
}
