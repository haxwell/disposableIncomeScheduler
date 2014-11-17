package com.haxwell.disposableIncomeScheduler.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class ListTheScheduleMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "List the Schedule";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		JSONArray items = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		
		if (items == null || items.size() == 0) {
			System.out.println("\nThere are no entries to list!\n");
			return rtn;
		}
		
		Map<String, Double> weights = Calculator.getWeights(data);
		
		List list = getSortedEntryList(weights);

		System.out.println();
		System.out.format("%-3s%-35s%-12s%-8s%-16s%-30s%-25s", "#","Description","Date", "Price","Prev. Saved Amt","$ of current deposit claimed","Days To Go @ Current Rate");
		System.out.println();
		System.out.format("%-3s%-35s%-12s%-8s%-16s%-30s%-25s", "-","-----------","-----------","-------","---------------","----------------------------","-------------------------");
		System.out.println();
		
		int count = 0;
		for (; count < items.size(); count++) {
			// rank, description, price, previously saved, $ of current deposit claimed, days to go at this rate of deposit, 
			// last line: amount being saved per period, total in the pot
			
			JSONObject item = (JSONObject)items.get(count);
			Double weight = ((Entry<String, Double>)list.get(count)).getValue();
			
			Long dollarsClaimedInCurrentDeposit = Math.round(getPercentageOfCurrentDepositClaimed(data, weight));
			Long daysToGoAtCurrentRateOfDeposit = getDaysToGoAtCurrentRateOfDeposit(data, item, dollarsClaimedInCurrentDeposit);
			
			System.out.format("%-3d%-35s%-11s%8s%16s%29s%27s", count+1, item.get(Constants.DESCRIPTION_JSON), item.get(Constants.DATE_NEEDED_JSON), item.get(Constants.PRICE_JSON), item.get(Constants.PREVIOUS_SAVED_AMT_JSON), dollarsClaimedInCurrentDeposit, getLongDaysToGoAsString(daysToGoAtCurrentRateOfDeposit));
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Amount Saved per Period = " + data.get(Constants.AMT_SAVED_PER_PERIOD_JSON));
		System.out.println("Total in the Pot = " + data.get(Constants.TOTAL_IN_THE_POT_JSON));
		
		return rtn;
	}
	
	private String getLongDaysToGoAsString(Long l) {
		Long  days = l;
		String rtn = "";
		
		if (days > 360) {
			long l2 = days / 360;
			
			rtn += l2 + " y";
			days -= (l2 * 360);
		}
		
		if (days > 30) {
			long l2 = days / 30;
			
			if (!rtn.equals("")) rtn += ", ";
			
			rtn += l2 + " m";
			days -= (l2 * 30);
		}
		
		if (!rtn.equals("")) rtn += ", ";
		
		rtn += days + " d";
		
		return rtn;
	}
	
	private long getDaysToGoAtCurrentRateOfDeposit(JSONObject data, JSONObject item, Long depositPerPeriod) {
		Integer daysPerPeriod = Integer.parseInt(data.get(Constants.PERIOD_LENGTH_JSON).toString());
		Integer price = Integer.parseInt(item.get(Constants.PRICE_JSON).toString());
		Integer alreadySaved;
		
		try {
			alreadySaved = Integer.parseInt(item.get(Constants.PREVIOUS_SAVED_AMT_JSON).toString());
		}
		catch (NullPointerException | NumberFormatException e) {
			alreadySaved = 0;
		}
		
		return Math.round((price - alreadySaved) / depositPerPeriod) * daysPerPeriod;
	}
	
	private Double getPercentageOfCurrentDepositClaimed(JSONObject data, Double weight) {
		return weight * (Integer.parseInt(data.get(Constants.AMT_SAVED_PER_PERIOD_JSON).toString()));
	}

	private List getSortedEntryList(Map<String, Double> weights) {
		List list = new ArrayList<>(Arrays.asList(weights.entrySet().toArray()));

		Comparator comp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				Entry<String, Double> e1 = (Entry<String, Double>)o1;
				Entry<String, Double> e2 = (Entry<String, Double>)o2;
				
				if (e1.getValue() > e2.getValue())
					return -1;
				if (e1.getValue() < e2.getValue())
					return 1;
				
				return 0;
			}
		};
		
		Collections.sort(list, comp);
		
		return list;
	}

}
