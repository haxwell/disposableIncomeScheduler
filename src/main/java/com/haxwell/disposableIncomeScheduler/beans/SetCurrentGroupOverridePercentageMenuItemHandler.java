package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Iterator;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class SetCurrentGroupOverridePercentageMenuItemHandler extends MenuItemHandlerBean {
	public String getMenuText() {
		return "Set Current Group's Override Percentage";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;
		
		JSONObject op = MenuItemUtils.getOverridingPercentages(data);
		
		// check to see if their is an overriding percentage set on a sibling group
		List<JSONObject> siblingsOfSelectedGroup = MenuItemUtils.getSiblingsOfSelectedGroup(data, state);
		
		boolean found = false;
		String foundStr = "";
		Iterator<JSONObject> iterator = siblingsOfSelectedGroup.iterator();
		
		// is the name of a sibling group found in the current list of OPs?
		while (iterator.hasNext() && !found) {
			JSONObject jo = iterator.next();
			
			for (String str : jo.keySet()) {
				if (op.keySet().contains(str)) {
					found = true;
					foundStr = str;
				}
			}
		}
		
		if (found)
			System.out.println("NOTE! The sibling group [" + foundStr + "] already has an overriding percentage set. Setting this one will remove that one!");
		
		JSONObject weights = Calculator.getWeights(data);
		
		JSONArray selectedGroupWeights = MenuItemUtils.getSelectedGroup(weights, state);
		
		String currentWeight = null;
		Iterator<Object> iterator1 = selectedGroupWeights.iterator();
		while (iterator1.hasNext() && currentWeight == null) {
			JSONObject jo = (JSONObject)iterator1.next();
			
			if (jo.containsKey(Constants.GROUP_WEIGHT_JSON)) {
				currentWeight = jo.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"";
			}
		}
		
		String selectedGroupName = MenuItemUtils.getSelectedGroupName(state);
		
		if (currentWeight != null)
			System.out.println("\nThe current percentage weight for [" + selectedGroupName + "] is " + currentWeight);
		
		System.out.print("Enter a new weight for " + selectedGroupName);
		String newWeight = getInputGetter().readInput();
		
		try {
			Double d = Double.parseDouble(newWeight);
			d = Calculator.getTwoDecimalPlaceDouble(d);
			
			if (foundStr != null)
				op.remove(foundStr);
			
			op.put(selectedGroupName, d);
			
			rtn = true;
		} catch (NumberFormatException nfe) {
			
		}
		
		return rtn;
	}
}
