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

/**
 * Lists the goals, how much you've currently saved (how much has been applied to a goal), and how much of the NEXT
 * deposit will go to this goal (given your current settings), and how long it will take at that rate to reach the
 * savings goal.
 * 
 * @author jjames
 *
 */
public class ListTheScheduleMenuItemHandler extends AttributeEditingMenuItemHandlerBean {

	private JSONObject data;
	
	public String getMenuText() {
		return "List the Schedule";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		this.data = data;
		
		JSONObject weights = Calculator.getWeights(data);
		
		JSONArray grpArr = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		JSONObject grpRootElement = (JSONObject)grpArr.get(0);
		
		JSONArray weightsArr = (JSONArray)weights.get(MenuItemUtils.getRootGroupName());
		JSONObject weightsRootElement = (JSONObject)weightsArr.get(0);
		
		Map<String, Long> dollarAmountsToBeAppliedPerGroup = Calculator.getDollarAmountsToBeAppliedPerGroup(data);
		
		System.out.println();
		System.out.format("%-35s%14s%8s%16s%29s%27s", Constants.DESCRIPTION, Constants.DATE_NEEDED, Constants.PRICE, "Prev Saved Amt", "$ From Curr Dep", "Time Remaining");
		System.out.println();
		
		listGroupsAndGoalsByWeight(grpRootElement, weightsRootElement, 1, dollarAmountsToBeAppliedPerGroup);
		
		System.out.println();
		System.out.println("Amount Saved per Period = " + data.get(Constants.AMT_SAVED_PER_PERIOD_JSON));
		System.out.println("Total in the Pot = " + data.get(Constants.TOTAL_IN_THE_POT_JSON));
		
		return rtn;
	}
	
	public void listGroupsAndGoalsByWeight(JSONObject data, JSONObject weights, int depth, Map<String, Long> dollarAmountsToBeAppliedPerGroup) {
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroupByWeight(data, weights);
		
		for (int x=0; x < list.size(); x++) {
			String key = list.get(x);
			JSONArray dataArr = (JSONArray)data.get(key);
			JSONArray weightArr = (JSONArray)weights.get(key);
			
			for (int y=0; y < depth; y++)
				System.out.print("-");
			
			System.out.println(key);
			
			List<String> subgroups = MenuItemUtils.getSubgroupNamesOfAGroupByWeight(dataArr, weightArr);
			
			if (subgroups.size() > 0) {
				for (int y=0; y<subgroups.size(); y++) {
					String sg = subgroups.get(y);
					
					for (int z=0; z<dataArr.size(); z++) {
						JSONObject dataObj = (JSONObject) dataArr.get(z);
						JSONObject weightObj = (JSONObject) weightArr.get(z);
						
						if (dataObj.containsKey(sg))
							listGroupsAndGoalsByWeight(dataObj, weightObj, depth+1, dollarAmountsToBeAppliedPerGroup);
					}
				}
			} else {
				List<String> goals = MenuItemUtils.getGoalsByWeight(dataArr, weightArr);
				
				for (String goalKey : goals) {
					for (int z=0; z<dataArr.size(); z++) {
						JSONObject dataObj = (JSONObject) dataArr.get(z);
						JSONObject weightObj = (JSONObject) weightArr.get(z);
						
						if (dataObj.get(Constants.DESCRIPTION_JSON).equals(goalKey))
							printGoal(dataObj, weightObj, depth, dollarAmountsToBeAppliedPerGroup);
					}
				}
			}
		}
	}
	
	private void printGoal(JSONObject goal, JSONObject weight, int depth, Map<String, Long> dollarAmountsToBeAppliedPerGroup) {
		Long dollarsClaimedInCurrentDeposit = dollarAmountsToBeAppliedPerGroup.get(goal.get(Constants.DESCRIPTION_JSON));
		Long daysToGoAtCurrentRateOfDeposit = getDaysToGoAtCurrentRateOfDeposit(goal, dollarsClaimedInCurrentDeposit);

		for (int y=0; y < depth+1; y++)
			System.out.print("-");
		
		System.out.format("%-35s%-11s%8s%16s%29s%27s", goal.get(Constants.DESCRIPTION_JSON), goal.get(Constants.DATE_NEEDED_JSON), goal.get(Constants.PRICE_JSON), goal.get(Constants.PREVIOUS_SAVED_AMT_JSON), dollarsClaimedInCurrentDeposit, getLongDaysToGoAsString(daysToGoAtCurrentRateOfDeposit));
		System.out.println();
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
	
	private long getDaysToGoAtCurrentRateOfDeposit(JSONObject item, Long depositPerPeriod) {
		Integer daysPerPeriod = Integer.parseInt(this.data.get(Constants.PERIOD_LENGTH_JSON).toString());
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
