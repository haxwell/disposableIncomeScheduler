package com.haxwell.disposableIncomeScheduler.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class EditAnEntryMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	LinkedList<String> keys = getListOfKeys();
	Map<String, Validator> validatorMap = getValidatorMap();
	Map<String, String> displayToJSONMap = getMapOfDisplayNamesToJSONFieldNames();
	
	public String getMenuText() {
		return "Edit An Entry";
	}
	
	public boolean doIt(JSONObject data) {
		boolean rtn = false;

		JSONArray items = (JSONArray)data.get("items");
		String choice = "";
		
		do {
			// list each item
			int count = 0;
			for (; count < items.size(); count++) {
				String description = ((JSONObject)items.get(count)).get("description")+"";
				
				System.out.println(count+1 + ". " + description);
			}
			
			if (count > 0) {
				choice = System.console().readLine();
				
				if (choice != null && !choice.equals("")) {
					JSONObject obj = (JSONObject)items.get(Integer.parseInt(choice) - 1);
					
					rtn = doTheAttributeEditing(obj);
				}
			}
			else
				System.out.println("\nNo Items to Edit!\n");
		} while (choice != null && !choice.equals(""));
		
		return rtn;
	}

	private boolean doTheAttributeEditing(JSONObject obj) {
		Map<Integer, String> map = new HashMap<>();
		boolean rtn = false;
		
		System.out.println();
		
		// list each attr and its value
		int count = 0;
		for (String key : keys) {
			System.out.println(++count + ". " + key + " -> " + obj.get(displayToJSONMap.get(key)));
			map.put(count, key);
		}

		// they select an attribute
		String choice = System.console().readLine();
		
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
				
				String key = map.get(choiceAsInt);
				Validator v = validatorMap.get(key);
				val = v.getValidValue(val);
				
				obj.put(displayToJSONMap.get(key), val);
				rtn = true;
			}
		}
		
		return rtn;
	}
}
