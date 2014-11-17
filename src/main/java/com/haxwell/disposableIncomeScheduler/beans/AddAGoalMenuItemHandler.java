package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class AddAGoalMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add A Goal";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONObject newObj = new JSONObject();
		LinkedList<String> keys = getListOfKeys();
		Map<String, String> mapOfDisplayNamesToJSONFieldNames = getMapOfDisplayNamesToJSONFieldNames();
		Map<String, Validator> validatorMap = getValidatorMap();
		
		printHeader();
		
		Iterator<String> iterator = keys.iterator();
		boolean lastEntryWasBlank = false;
		while (iterator.hasNext() && !lastEntryWasBlank) {
			String key = iterator.next();
			
			System.out.println("Enter value for '" + key + "' :");
			String val = System.console().readLine();

			lastEntryWasBlank = (val == null || val.equals(""));
			
			Validator v = validatorMap.get(key);
			val = v.getValidValue(val);
			
			newObj.put(mapOfDisplayNamesToJSONFieldNames.get(key), val);
		}

		if (!lastEntryWasBlank) {
			JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
			arr.add(newObj);
			rtn = true;
			
			System.out.println("\nAdded entry '" + newObj.get(Constants.DESCRIPTION_JSON) + "'.");
			System.out.println(newObj.toJSONString());
		}
		
		return rtn;
	}
	
	private void printHeader() {
		System.out.println("Immediacy and Length values are on a scale of 1-25");
		System.out.println("Happiness Immediacy: 1 = Phht. Whatever, 5 = One less worry, 13 = Satisfied. A goal accomplished, 20 = exuberant, 25 = bliss");
		System.out.println("Utility Immediacy: 1 = Could've done without, 5 = Did what I had to do, 13 = Good! Baby steps!, 20 = moving in new circles, 25 = new life skill/ability");
		System.out.println("Happiness and Utility Length: 1 = A fleeting moment, 5 = scale of months, 13 = a good year or so, 20 = more than 3 years, 25 = the forseeable future");
		System.out.println();
	}

}
