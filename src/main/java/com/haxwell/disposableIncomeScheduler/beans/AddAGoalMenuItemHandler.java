package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.StringUtils;
import com.haxwell.disposableIncomeScheduler.validators.Validator;

public class AddAGoalMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Add A Goal";
	}

	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONObject newObj = new JSONObject();
		LinkedList<String> keys = getListOfKeys();
		Map<String, Boolean> isRequiredKeys = getIsRequiredKeys();
		Map<String, String> mapOfDisplayNamesToJSONFieldNames = getMapOfDisplayNamesToJSONFieldNames();
		Map<String, Validator> validatorMap = getValidatorMap();

		printHeader();

		Iterator<String> iterator = keys.iterator();
		String key = "";
		boolean badDataEntry = false;

		while (iterator.hasNext() && !badDataEntry) {
			key = iterator.next();

			getPrintlner().println("Enter value for '" + key + "' :");
			String val = getInputGetter().readInput();

			if (isRequiredKeys.containsKey(key) && StringUtils.isNullOrEmpty(val)) {
				badDataEntry = true;
			} else {

				Validator v = validatorMap.get(key);
				val = v.getValidValue(val);

				newObj.put(mapOfDisplayNamesToJSONFieldNames.get(key), val);
			}
		}

		if (!badDataEntry) {
			JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
			arr.add(newObj);
			rtn = true;

			getPrintlner().println("\nAdded goal '" + newObj.get(Constants.DESCRIPTION_JSON) + "'.");
			getPrintlner().println(newObj.toJSONString());
		}

		return rtn;
	}

	private void printHeader() {
		getPrintlner().println("Immediacy and Length values are on a scale of 1-25");
		getPrintlner().println(
				"Happiness Immediacy: 1 = Phht. Whatever, 5 = One less worry, 13 = Satisfied. A goal accomplished, 20 = exuberant, 25 = bliss");
		getPrintlner().println(
				"Utility Immediacy: 1 = Could've done without, 5 = Did what I had to do, 13 = Good! Baby steps!, 20 = moving in new circles, 25 = new life skill/ability");
		getPrintlner().println(
				"Happiness and Utility Length: 1 = A fleeting moment, 5 = scale of months, 13 = a good year or so, 20 = more than 3 years, 25 = the forseeable future");
		getPrintlner().println();
	}
}
