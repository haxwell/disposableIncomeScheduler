package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.haxwell.disposableIncomeScheduler.Constants;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class AddAnEntryMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add An Entry";
	}
	
	public boolean doIt(JSONObject data) {
		boolean rtn = false;

		JSONObject newObj = new JSONObject();
		LinkedList<String> keys = getListOfKeys();
		Map<String, String> mapOfDisplayNamesToJSONFieldNames = getMapOfDisplayNamesToJSONFieldNames();
		Map<String, Validator> validatorMap = getValidatorMap();
		
		Iterator<String> iterator = keys.iterator();
		boolean lastEntryWasBlank = false;
		while (iterator.hasNext() && !lastEntryWasBlank) {
			String key = iterator.next();
			
			System.out.println("Enter value for '" + key + "' :");
			String val = System.console().readLine();

			lastEntryWasBlank = (val == null || val.equals(""));
			
			Validator v = validatorMap.get(key);
			val = v.getValidValue(val);
			
			System.out.println(newObj.toJSONString());
			newObj.put(mapOfDisplayNamesToJSONFieldNames.get(key), val);
			System.out.println(newObj.toJSONString());
		}

		if (!lastEntryWasBlank) {
			JSONArray items = (JSONArray)data.get("items");
			items.add(newObj);
			rtn = true;
			
			System.out.println("\nAdded entry '" + newObj.get(Constants.DESCRIPTION_JSON) + "'.");
			System.out.println(newObj.toJSONString());
		}
		
		return rtn;
	}

}
