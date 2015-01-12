package com.haxwell.disposableIncomeScheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.disposableIncomeScheduler.beans.utils.GroupedGoalsIterator;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;

public class Calculator {
	
	private static class InnerCalculatePreviousSavedAmountHandler
			implements CalculatedPreviousSavedAmountHandler {
		@Override
		public void handleIt(JSONObject groupElement, long dollarAmountToBeApplied) {
			Object object = groupElement.get(Constants.PREVIOUS_SAVED_AMT_JSON);
			long previousSavedAmount = Long.parseLong((object == null) ? "0" : object.toString());
			previousSavedAmount += dollarAmountToBeApplied;
			groupElement.put(Constants.PREVIOUS_SAVED_AMT_JSON, previousSavedAmount+"");
			
			object = groupElement.get(Constants.AMT_CLAIMED_DURING_LAST_APPLICATION);
			long amtClaimed = Long.parseLong((object == null) ? "0" : object.toString());
			groupElement.put(Constants.AMT_CLAIMED_DURING_LAST_APPLICATION, amtClaimed + dollarAmountToBeApplied);
		}
	}

	public interface CalculatedPreviousSavedAmountHandler {
		public void handleIt(JSONObject groupElement, long dollarAmountToBeApplied);
	}

	public static Map<String, Long> getDollarAmountsToBeAppliedToGenericallyReservedFunds(JSONObject data) {
		Map<String, Long> map = new HashMap<>();
		
		map.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, getPreviousTotalInThePot(data));
		map.put(Constants.PAYCHECK_AMT, getAmountPaidPerPeriod(data));
		map.put(Constants.RAINY_DAY_FUND_AMT,  getRainyDayFundAmount(data));
		
		return map;
	}
	
	public static Map<String, Double> getDollarAmountsToBeAppliedToExpenses(JSONObject data) {
		Map<String, Double> map = new HashMap<>();
		
		double offset = getOffset(data);
		JSONArray expenses = MenuItemUtils.getExpenses(data);
		for (int i = 0; i < expenses.size(); i++) {
			JSONObject obj = (JSONObject) expenses.get(i);
			
			map.put(obj.get(Constants.DESCRIPTION_JSON)+"", Long.parseLong(obj.get(Constants.PRICE_JSON)+"")*offset);
		}
		
		return map;
	}
	
	public static Map<String, Double> getDollarAmountsToBeAppliedToShortTermGoals(JSONObject data, JSONObject state) {
		Map<String, Double> map = new HashMap<>();
		
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		for (int i = 0; i < stgs.size(); i++) {
			JSONObject stg = (JSONObject) stgs.get(i);
			
			Long amount = Long.parseLong(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"");
			
			map.put(stg.get(Constants.DESCRIPTION_JSON)+"", amount*1.0);
		}
		
		return map;
	}
	
	public static Map<String, Double> getAmountsAlreadySavedForLongTermGoals(JSONObject data) {
		Map<String, Double> map = new HashMap<>();
		GroupedGoalsIterator ggi = new GroupedGoalsIterator(MenuItemUtils.getLongTermGoals(data));
		
		while (ggi.hasNext()) {
			JSONObject obj = ggi.next();
			
			if (obj.containsKey(Constants.DESCRIPTION_JSON)) {
				map.put(obj.get(Constants.DESCRIPTION_JSON)+"", Long.parseLong(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON)+"")*1.0 );	
			}
		}
		
		return map;
	}
	
	public static long getDollarAmountToBeSpreadOverLongTermGoals(JSONObject data, JSONObject state) {
		return getDollarAmountToBeSpreadOverLongTermGoals(
				Calculator.getDollarAmountsToBeAppliedToGenericallyReservedFunds(data),
				Calculator.getDollarAmountsToBeAppliedToExpenses(data),
				Calculator.getDollarAmountsToBeAppliedToShortTermGoals(data, state),
				Calculator.getAmountsAlreadySavedForLongTermGoals(data));
	}
	
	public static long getDollarAmountToBeSpreadOverLongTermGoals(Map<String, Long> genericFundsMap, 
			Map<String, Double> expenseMap, Map<String, Double> stgMap, Map<String, Double> ltgMap) {

		long total = getTotalDollarAmountAfterAccountingForGenericallyReservedFunds(genericFundsMap);
		
		for (String key : expenseMap.keySet()) {
			total -= expenseMap.get(key);
		}
		
		for (String key : stgMap.keySet()) {
			total -= stgMap.get(key);
		}
		
		for (String key : ltgMap.keySet()) {
			total -= ltgMap.get(key);
		}
		
		return total;
	}
	
	public static long getTotalDollarAmountAfterAccountingForGenericallyReservedFunds(Map<String, Long> genericFundsMap) {
		long total = 0;
		total += genericFundsMap.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON);
		total += genericFundsMap.get(Constants.PAYCHECK_AMT);
		total -= genericFundsMap.get(Constants.RAINY_DAY_FUND_AMT);
		
		return total;
	}
	
	private static JSONObject getFirstElementInRootGroupArray(JSONObject data) {
		JSONArray arr = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		JSONObject rootElement = null;
		
		if (arr != null && arr.size() > 0) 
			rootElement = (JSONObject)arr.get(0);
		
		return rootElement;
	}
	
//	public static Map<String, Long> getDollarAmountsToBeAppliedPerLongTermGoalGroup(JSONObject data, long totalDollarAmount) {
//		String dataString = data.toJSONString();
//		JSONObject dataCopy = (JSONObject)JSONValue.parse(dataString);
//		
//		JSONObject grpRootElement = getFirstElementInRootGroupArray(dataCopy); 
//		
//		final Map<String, Long> map = new HashMap<>();
//		
//		if (grpRootElement != null) {
//			boolean finished = false;
//			
//			do {
//				JSONObject weightsRootElement = getFirstElementInRootGroupArray(Calculator.getWeights(dataCopy));
//				
//				ApplyMoneyHelperReturnValue rtn = new ApplyMoneyHelperReturnValue();
//				
//				rtn = applyMoneyHelper(grpRootElement, weightsRootElement, totalDollarAmount, new InnerCalculatePreviousSavedAmountHandler() {
//					@Override
//					public void handleIt(JSONObject groupElement, long dollarAmountToBeApplied) {
//						super.handleIt(groupElement, dollarAmountToBeApplied);
//
//						String key = groupElement.get(Constants.DESCRIPTION_JSON)+"";
//						long l = 0;
//						
//						if (map.containsKey(key)) {
//							l = map.get(key);
//						}
//						
//						map.put(groupElement.get(Constants.DESCRIPTION_JSON)+"", l + dollarAmountToBeApplied);
//					}
//				});
//						
//				if (rtn.applied == 0 && rtn.overage == 0)
//					finished = true;
//				else if (rtn.applied == totalDollarAmount && rtn.overage == 0)
//					// then all money was applied, no goal had more money applied to it than it could take.. we're done! .. I think.
//					finished = true;
//				else
//					// if rtn.applied == totalDollarAmount && rtn.overage > 0 then all money was applied, but at least one goal had more money applied to it than it could take.. run it again.
//					totalDollarAmount = rtn.overage;
//				
//			} while (!finished);
//		}
//		
//		return map;
//	}
	
	public static void applyMoneyToLongTermGoals(JSONObject data, long totalDollarAmount) {
		resetDollarAmountsClaimed(data);
		JSONObject grpRootElement = getFirstElementInRootGroupArray(data);
		
		if (grpRootElement != null) {
			boolean finished = false;

			do {
				JSONObject weightsRootElement = getFirstElementInRootGroupArray(Calculator.getWeights(data));
				
				ApplyMoneyHelperReturnValue rtn = new ApplyMoneyHelperReturnValue();
				
				rtn = applyMoneyHelper(grpRootElement, weightsRootElement, totalDollarAmount, new InnerCalculatePreviousSavedAmountHandler());
				
				if (rtn.applied == 0 && rtn.overage == 0)
					finished = true;
				else if (rtn.applied == totalDollarAmount && rtn.overage == 0)
					// then all money was applied, no goal had more money applied to it than it could take.. we're done! .. I think.
					finished = true;
				else
					// if rtn.applied == totalDollarAmount && rtn.overage > 0 then all money was applied, but at least one goal had more money applied to it than it could take.. run it again.
					totalDollarAmount = rtn.overage;
				
			} while (!finished);
		}
	}
	
	private static void resetDollarAmountsClaimed(JSONObject grpRootElement) {
		GroupedGoalsIterator ggi = new GroupedGoalsIterator(MenuItemUtils.getLongTermGoals(grpRootElement));
		
		while (ggi.hasNext()) {
			JSONObject obj = ggi.next();
			
			if (obj.containsKey(Constants.DESCRIPTION_JSON)) {
				obj.put(Constants.AMT_CLAIMED_DURING_LAST_APPLICATION, "0");
			}
		}
	}

	private static class ApplyMoneyHelperReturnValue {
		// OVERAGE is the amount that was over what was needed to fully save for the goals on a given level and its children.
		long overage = 0;
		
		// APPLIED is the amount attempted to be applied to goals. It it equals 0, then all subgoals had zero weight, meaning they are all fully saved.
		long applied = 0;
	}
	
	private static ApplyMoneyHelperReturnValue applyMoneyHelper(JSONObject ge, JSONObject we, long dollarAmount, CalculatedPreviousSavedAmountHandler func) {
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(ge);

		ApplyMoneyHelperReturnValue rtn = new ApplyMoneyHelperReturnValue();
		
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				JSONArray groupElements = (JSONArray)ge.get(key);
				JSONArray weightElements = (JSONArray)we.get(key);
				
				for (int x=0; x < groupElements.size(); x++) {
					JSONObject groupElement = (JSONObject)groupElements.get(x);
					JSONObject weightElement = (JSONObject)weightElements.get(x);
					
					List<String> subgroupNames = MenuItemUtils.getSubgroupNamesOfAGroup(groupElement);

					if (subgroupNames.size() > 0) {
						JSONArray arr = (JSONArray)weightElement.get(subgroupNames.get(0));
						Double groupWeightAsPercentage = 0.0;
						boolean found = false;
						
						for (int y=0; !found && y < arr.size(); y++) {
							JSONObject obj = (JSONObject)arr.get(y);
							
							if (obj.containsKey(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)) {
								found = true;
								groupWeightAsPercentage = Double.parseDouble(obj.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"");
							}
						}
						
						long weightedDollarAmount = Math.round(dollarAmount * groupWeightAsPercentage);
						
						rtn.applied += weightedDollarAmount;
						
						ApplyMoneyHelperReturnValue rtn_ = applyMoneyHelper(groupElement, weightElement, weightedDollarAmount, func);
						
						rtn.overage += rtn_.overage;
					}
					else {
						// this is a goal.. we need to apply money here
						Double goalWeightAsPercentage = Double.parseDouble(weightElement.get(Constants.WEIGHT_AS_PERCENTAGE_JSON)+"");
						
						long price = Long.parseLong(groupElement.get(Constants.PRICE_JSON)+"");
						long alreadySaved = Long.parseLong(groupElement.get(Constants.PREVIOUS_SAVED_AMT_JSON)+"");
						long remainingToBeSaved = price - alreadySaved;
						
						long amtToBeApplied = Math.round(dollarAmount * goalWeightAsPercentage);
						
						if (amtToBeApplied > remainingToBeSaved) {
							rtn.overage += (amtToBeApplied - remainingToBeSaved);
							amtToBeApplied = remainingToBeSaved;
						}
						
						func.handleIt(groupElement, amtToBeApplied);
					}
				}
			}
		}
		
		return rtn;
	}
	
	public static JSONObject getWeights(JSONObject data) {
		JSONObject rtn = new JSONObject();
		JSONObject rootElement = getFirstElementInRootGroupArray(data); 
		
		if (rootElement != null) {
			
			int dateArr[] = getArrayToCalculateRelativeWeightOfDates(rootElement);
			
			JSONObject obj = buildJSONWeightObject(rootElement, dateArr);
			
			obj = addGroupWeightsToJSONWeightObject(obj, MenuItemUtils.getOverridingPercentages(data));
			
			obj = applyOverridingPercentagesToJSONWeightObject(obj);
			
			JSONArray objArr = new JSONArray();
			objArr.add(obj);
			
			rtn.put(MenuItemUtils.getRootGroupName(), objArr);
		}
				
		return rtn;
	}
	
	private static JSONObject applyOverridingPercentagesToJSONWeightObject(JSONObject weightElement) {
		return applyOverridingPercentagesToJSONWeightObject(weightElement, new JSONObject());
	}
	
	private static JSONObject applyOverridingPercentagesToJSONWeightObject(JSONObject weightElement, JSONObject state) {
		final String GOAL_PARENT_LEVEL = "foo1";
		final String GOAL_GRANDPARENT_LEVEL = "foo2";
		
		JSONObject rtn = weightElement;
		
		// get the names of the groups on the level of weightElement's highest group
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(weightElement);
		
		// if the list is empty, then weightElement represents a goal, and not a group..
		//  otherwise, lets process the group
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				
				// get the groups contained within this group
				JSONArray arr = (JSONArray)weightElement.get(key);
				
				Map<String, Double> groupToOPMap = new HashMap<>();
				
				for (int x = 0; x < arr.size(); x++) {
					JSONObject obj = (JSONObject)arr.get(x);
					
					if (!obj.containsKey(Constants.GROUP_WEIGHT_JSON)) {
						applyOverridingPercentagesToJSONWeightObject(obj, state);
						
						if (state.containsKey(GOAL_PARENT_LEVEL)) {
							// then the children of this level are the individual goals, with no subgroups
							state.remove(GOAL_PARENT_LEVEL);
							state.put(GOAL_GRANDPARENT_LEVEL, GOAL_GRANDPARENT_LEVEL);
							return rtn;
						}
						
						if (state.containsKey(GOAL_GRANDPARENT_LEVEL)) {
							groupToOPMap.clear();
							
							// first go over the grandchildren groups, and get their overriding percentages mapped to their group names
							for (int y = 0; y < list.size(); y++) {
								String currentLevelGroupName = list.get(y);
								JSONArray childrenGroupsOfTheCurrentLevel = (JSONArray)weightElement.get(currentLevelGroupName);
								
								List<String> listOfGrandchildGroupNames = MenuItemUtils.getSubgroupNamesOfAGroup(childrenGroupsOfTheCurrentLevel);
								for (int z = 0; z < listOfGrandchildGroupNames.size(); z++) {
									String grandchildGroupName = listOfGrandchildGroupNames.get(z);
									
									Iterator<Object> iterator = childrenGroupsOfTheCurrentLevel.iterator();
									boolean found = false;
									
									while (iterator.hasNext() && !found) {
										JSONObject grandchildGroup = (JSONObject)iterator.next();
										
										if (grandchildGroup.containsKey(grandchildGroupName)) {
											JSONArray grandchildGroupGoalWeights = (JSONArray)grandchildGroup.get(grandchildGroupName);
											
											for (int xx=0; xx < grandchildGroupGoalWeights.size(); xx++) {
												JSONObject grandchildGroupGoalWeight = (JSONObject)grandchildGroupGoalWeights.get(xx);
												if (grandchildGroupGoalWeight.containsKey(Constants.OVERRIDING_PERCENTAGE_AMT_JSON)) {
													String groupWeightAsPercentage = grandchildGroupGoalWeight.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"";
													
													// if group weight as percentage == 1.0, then it is the only group among its siblings (if any) that has any weight
													//  doesn't make sense to override the percentage, so we check for it here.
													if (!groupWeightAsPercentage.equals("1.0")) {
														if (groupToOPMap.size() == 0) {
															Double d = Double.parseDouble(grandchildGroupGoalWeight.get(Constants.OVERRIDING_PERCENTAGE_AMT_JSON)+"");
															groupToOPMap.put(grandchildGroupName, d);
														}
														else {
															/**
															 * We set a rule saying there can only be one group with an overriding percentage 
															 * set, because there are so many corner cases that arise when multiple groups have 
															 * an overriding percentage set, that my mind is currently boggled. The problem 
															 * lies in the apparent need to account for the difference in the original percentage 
															 * weight of the group and the overridden percentage. the sum of the sibling group's 
															 * percentages must equal 1.0. so if you override one, the others must adjust as well.
															 * 
															 * Its easy enough with one group changed, but if two groups change, how do you 
															 * handle that? the way it is done now is to spread the difference over the sibling 
															 * groups .01% at a time. .01 of the difference is added/subtracted to the weight of 
															 * the first sibling group, and .01 is added/subtracted to the next. In that way the 
															 * difference is spread over each of the siblings, and you still come up with 1.0 
															 * total for all. But if a second is overridden, what do you do? Do you exclude the 
															 * first group from being adjusted? Then what if there are only two groups? Only the 
															 * second group remains, and to adjust it would change the overridden percentage set 
															 * on it. What if there are three? well, it depends on whether the overriding values 
															 * in the first two groups are positive or negative adjustments. I think it may be 
															 * possible if they are both negative. Haven't worked it out, it just feels like it 
															 * would work. But there are, as I said, a lot more corner cases, possibilities, 
															 * and I don't see the benefit now. And if you don't exclude the first group, 
															 * then its overriding value changes when the second is applied. So thats a dead end 
															 * I believe. Anyway, this is a difficult problem, and I leave it to my future self 
															 * to think about, if it turns out setting only one overriden percentage per sibling 
															 * group doesn't work out.
															 */
															
															throw new IllegalStateException("Only one group in a set of sibling groups can have an overridden percentage!");
														}
													}
												}
											}
											
											found = true;
										}
									}
								}
							}
							
							// then, in order of OP, calc difference between GWP and OP => Diff
							List<String> listOfGrandchildGroupNamesSortedByOP = MenuItemUtils.getOrderedListOfStrings(groupToOPMap);
							Map<String, Double> grandchildGroupsToPercentageDiffMap = new HashMap<>();
							
							for (int y = 0; y < list.size(); y++) {
								String currentLevelGroupName = list.get(y);
								JSONArray childrenGroupsOfTheCurrentLevel = (JSONArray)weightElement.get(currentLevelGroupName);
								
								// for each grandchild group that we found that has an OP,
								//  iterate over the children groups on this level to find the grandchild with the same name as the OP group
								//  then on that grandchild, set the OP as the GWP, and save the difference between the two values in a map keyed by the child's name
								for (int z=0; z < listOfGrandchildGroupNamesSortedByOP.size(); z++) {
									String grandchildGroupName = listOfGrandchildGroupNamesSortedByOP.get(z);
									Iterator<Object> iterator = childrenGroupsOfTheCurrentLevel.iterator();
									boolean found = false;
									while (iterator.hasNext() && !found) {
										JSONObject grandchildGroup = (JSONObject)iterator.next();
										
										if (grandchildGroup.containsKey(grandchildGroupName)) {
											JSONArray grandchildGroupGoals = (JSONArray)grandchildGroup.get(grandchildGroupName);
											
											for (int xx=0; xx < grandchildGroupGoals.size(); xx++) {
												JSONObject grandchildGroupGoal = (JSONObject)grandchildGroupGoals.get(xx);
												if (grandchildGroupGoal.containsKey(Constants.OVERRIDING_PERCENTAGE_AMT_JSON)) {
													Double op = Double.parseDouble(grandchildGroupGoal.get(Constants.OVERRIDING_PERCENTAGE_AMT_JSON)+"");
													Double gwp = Double.parseDouble(grandchildGroupGoal.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"");
													
													grandchildGroupsToPercentageDiffMap.put(grandchildGroupName, op - gwp);
	
													//		set the group weight percentage to the OP
													grandchildGroupGoal.remove(Constants.OVERRIDING_PERCENTAGE_AMT_JSON);
													grandchildGroupGoal.put(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON, op);
												}
											}
											
											found = true;
										}
									}
								}
							}
	
							// now that we know the difference between the original GWP for each group, and the OP,
							//  spread the difference over each of the sibling groups by adjusting their GWP up or
							//  down by .01 until the difference is fully spread. This is to keep the GWP for all
							//  the siblings equaling 1.00
							for (String str : grandchildGroupsToPercentageDiffMap.keySet()) {
								List<String> excludeList = new ArrayList<>();
								
								excludeList.add(str);
								
								// get a list of all the siblings that are NOT the group who's difference we are spreading
								List<JSONObject> listOfFilteredJSONObjects = MenuItemUtils.getSubgroups(arr, excludeList);
	
								// get the amount of difference we are spreading
								Double diff = grandchildGroupsToPercentageDiffMap.get(str);
								
								// if the difference is negative (the overriding percent is less than the original GWP) then
								//  we'll need to effectively add to the GWPs of the other groups. If its positive, we subtract.
								//  we multiply by -1 to make that happen either way, and we set that up here.
								int multiplier = 1;
								if (diff > 0) {
									multiplier = -1;
									diff = diff * -1;
								}
								
								Double offset = ((multiplier * 0.01) * multiplier);
								
								Double prevDiff = null;
								// while the difference has not been completely spread...
								while (diff != 0.0 && prevDiff != diff) { // checking prevDiff prevents an infinite loop
									prevDiff = diff;
									
									// then for each group whos GWP we are adjusting
									for (JSONObject grandchildGroup : listOfFilteredJSONObjects) {
										String grandchildGroupName = grandchildGroup.keySet().iterator().next();
										JSONArray grandchildGroupGoals = (JSONArray)grandchildGroup.get(grandchildGroupName);
										
										// iterate over the weights set for each goal in it...
										for (int y = 0; y < grandchildGroupGoals.size(); y++) {
											JSONObject grandchildGroupGoal = (JSONObject)grandchildGroupGoals.get(y);
											
											// ...until we find the one with the GWP info in it
											if (grandchildGroupGoal.containsKey(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)) {
												// then do the adjustment to it.
												Double d = Double.parseDouble(grandchildGroupGoal.get(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON)+"");
												
												if (d > 0.0) {
													// apply the inc/dec to Double d
													d = getTwoDecimalPlaceDouble(d + offset);
													grandchildGroupGoal.put(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON, d);
													
													// adjust diff
													diff = getTwoDecimalPlaceDouble(diff + offset);
												}
											}
										}
									}
								}
							}
							
							state.remove(GOAL_GRANDPARENT_LEVEL);
						}
					}
				}
			}
		} else {
			state.put(GOAL_PARENT_LEVEL, GOAL_PARENT_LEVEL);
		}
		
		
		return rtn;
	}
	
	/**
	 * Iterates over a subset of the long term goals, reading their happiness and utility values, adding them up
	 * and calculating a percentage, relative to the goal's siblings, which indicates which portion of the money
	 * this goal should get compared to its siblings.
	 * 
	 * The resulting JSONObject has the same names, structure and relationships as the original long term goal 
	 * subset, but the details of each leaf (goal) are specific to calculating the weights. 
	 * 
	 * @param element
	 * @param dateArr
	 * @return
	 */
	private static JSONObject buildJSONWeightObject(JSONObject element, int[] dateArr) {
		JSONObject weights = new JSONObject();
		
		// get the names of the children of this group
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(element);
		
		if (list.size() > 0) {
			
			// for each of the children
			for (int i = 0; i < list.size(); i++) {
				String subgroupName = list.get(i); // get the name
				JSONArray groupElements = (JSONArray)element.get(subgroupName); // get the group elements
				
				if (groupElements.size() == 0) {
					// this is an empty group.. it has no goals or subgroups
					JSONArray arr = new JSONArray();
					JSONObject obj = new JSONObject();
					
					obj.put(Constants.DESCRIPTION_JSON, element.get(Constants.DESCRIPTION_JSON));
					obj.put(Constants.WEIGHT_JSON, "0");
					obj.put(Constants.WEIGHT_AS_PERCENTAGE_JSON, "0.0");
					
					arr.add(obj);
					weights.put(subgroupName, arr);
				}
				else {
					int total = 0;
					
					// step into each of the children, recursively build the overall JSON weight object
					for (int x=0; x < groupElements.size(); x++) {
						JSONObject groupElement = (JSONObject)groupElements.get(x);
						JSONObject weight = buildJSONWeightObject(groupElement, dateArr);
						
						JSONArray arr = null;
						if (weights.containsKey(subgroupName)) {
							// it has more than one goal, and we've already been over it
							// and created a weights object. Use the one we've already created
							arr = (JSONArray)weights.get(subgroupName);
						}
						else {
							// a new weights object is needed
							arr = new JSONArray();
							weights.put(subgroupName, arr);
						}
						
						// add the child JSON weight object to this level's....
						arr.add(weight);
						
						// if the weights we just got from the child are the weights for a leaf...
						if (weight.containsKey(Constants.WEIGHT_JSON)) 
							total += Integer.parseInt(weight.get(Constants.WEIGHT_JSON)+"");
					}
					
					JSONArray arr = (JSONArray)weights.get(subgroupName);
					
					for (int x=0; arr != null && x < arr.size(); x++) {
						JSONObject obj = (JSONObject)arr.get(x);
	
						// the weights object has the same structure as the groups object which comes from the data file..
						//  except the weights object has a summary JSON object with the key WEIGHT_JSON in it. This is
						//  the total weight of the level, and is where we put the weight as a percentage (which is a portion
						//  relative to its siblings)
						if (obj.containsKey(Constants.WEIGHT_JSON)) {
							int objWeight = Integer.parseInt(obj.get(Constants.WEIGHT_JSON)+"");
							double objWeightAsPercentage;
							
							if (obj.containsKey(Constants.SAVING_IS_COMPLETE_JSON)) {
								objWeightAsPercentage = 0.0;
								total -= objWeight;
							}
							else
								objWeightAsPercentage = (objWeight*1.0)/total;
								
							obj.put(Constants.WEIGHT_AS_PERCENTAGE_JSON, (objWeightAsPercentage)+"");
						}
					}
				}
			}
		} else {
			// this is a goal, a leaf
			long prevSaveAmt = Long.parseLong(element.get(Constants.PREVIOUS_SAVED_AMT_JSON)+"");			
			long price = Long.parseLong(element.get(Constants.PRICE_JSON)+"");
			
			if (prevSaveAmt >= price)
				weights.put(Constants.SAVING_IS_COMPLETE_JSON, "true");

			int value = getValueForGoal(element, dateArr); 
			weights.put(Constants.DESCRIPTION_JSON, element.get(Constants.DESCRIPTION_JSON));
			weights.put(Constants.WEIGHT_JSON, value+"");
		}

		return weights;
	}
	
	private static int getValueForGoal(JSONObject goal, int[] dateArr) {
		Integer rtn = Integer.parseInt(goal.get(Constants.HAPPINESS_IMMEDIACY_JSON)+"");
		
		rtn += Integer.parseInt(goal.get(Constants.HAPPINESS_LENGTH_JSON)+"");
		rtn += Integer.parseInt(goal.get(Constants.UTILITY_IMMEDIACY_JSON)+"");
		rtn += Integer.parseInt(goal.get(Constants.UTILITY_LENGTH_JSON)+"");
		
		int hsn = 0;
		if (!goal.get(Constants.DATE_NEEDED_JSON).toString().equals("") && dateArr.length > 0) {
			int days = getNumberOfDaysFromToday(goal);
			hsn = dateArr[days - 1];
		}

		rtn += hsn;
		
		return rtn;
	}
	
	private static JSONObject addGroupWeightsToJSONWeightObject(JSONObject element, JSONObject overridingPercentages) {
		return addGroupWeightsToJSONWeightObject(element, overridingPercentages, true);
	}
	
	private static JSONObject addGroupWeightsToJSONWeightObject(JSONObject element, JSONObject overridingPercentages, boolean isRoot) {
		
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(element);
		
		if (list.size() > 0) {
			Integer outerGroupWeight = 0;
			
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				JSONArray groupElements = (JSONArray)element.get(key);
				
				List<String> subgroupNames = MenuItemUtils.getSubgroupNamesOfAGroup(groupElements);
				
				if (subgroupNames.size() > 0) {
					Integer total = 0;
					
					for (int x=0; x < groupElements.size(); x++) {
						JSONObject groupElement = (JSONObject)groupElements.get(x);
						JSONObject obj = addGroupWeightsToJSONWeightObject(groupElement, overridingPercentages, false);
						
						JSONArray arr = (JSONArray)obj.get(subgroupNames.get(x));
						
						// we should have a collection of goals with a group weight on it
						//  add that weight to the total, comprised of it and its siblings weight
						boolean found = false;
						int count = 0;
						
						while (!found && count < arr.size()) {
							JSONObject jo = (JSONObject)arr.get(count++);
							if (jo.containsKey(Constants.GROUP_WEIGHT_JSON)) {
								found = true;
								total += Integer.parseInt(jo.get(Constants.GROUP_WEIGHT_JSON)+"");
							}
						}
					}
					
					// now we need to go through each of the siblings and set a weight relative to
					//  its portion of the total
					for (int x=0; x < groupElements.size(); x++) {
						JSONObject jo1 = (JSONObject)groupElements.get(x);
						JSONArray arr = (JSONArray)jo1.get(subgroupNames.get(x));
						
						boolean found = false;
						int count = 0;
						
						while (!found && count < arr.size()) {
							JSONObject jo2 = (JSONObject)arr.get(count++);
							if (jo2.containsKey(Constants.GROUP_WEIGHT_JSON)) {
								found = true;
								int weight = Integer.parseInt(jo2.get(Constants.GROUP_WEIGHT_JSON)+"");
								double weightAsPercentage = getTwoDecimalPlaceDouble((weight == 0) ? 0 : (weight*1.0)/total);
								
								jo2.put(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON, weightAsPercentage+"");
							}
						}
					}
					
					// now we need to save the total on this group, the parent of the siblings we edited above
					JSONObject jo3 = new JSONObject();
					jo3.put(Constants.GROUP_WEIGHT_JSON, total+"");
					
					if (isRoot && list.size() == 1) {
						jo3.put(Constants.GROUP_WEIGHT_AS_PERCENTAGE_JSON, "1.0");
					}
					
					groupElements.add(jo3);
					
				} else {
					// is collection of goals
					Integer innerGroupWeight = 0;
					for (int x=0; x < groupElements.size(); x++) {
						JSONObject groupElement = (JSONObject)groupElements.get(x);

						if (!groupElement.containsKey(Constants.SAVING_IS_COMPLETE_JSON)) {
							innerGroupWeight += Integer.parseInt(groupElement.get(Constants.WEIGHT_JSON)+"");
						}
					}
					
					JSONObject groupWeightObj = new JSONObject();
					groupWeightObj.put(Constants.GROUP_WEIGHT_JSON, innerGroupWeight+"");
					
					if (overridingPercentages != null && overridingPercentages.containsKey(key))
						groupWeightObj.put(Constants.OVERRIDING_PERCENTAGE_AMT_JSON, overridingPercentages.get(key));
					
					groupElements.add(groupWeightObj);
					outerGroupWeight += innerGroupWeight;
				}
			}
		} 
		
		return element;
	}
	
	public static double getTwoDecimalPlaceDouble(double d) {
		return (Math.round(d*100.0)) / 100.0;
	}

	public static int getNumberOfDaysFromToday(JSONObject obj) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = CalendarUtils.getCurrentCalendar();
		int rtn = -1;
		
		try {
			cal.setTime(sdf.parse(obj.get(Constants.DATE_NEEDED_JSON).toString()));
			rtn = (int)(((((cal.getTimeInMillis() - CalendarUtils.getCurrentCalendar().getTimeInMillis()) / 1000 ) / 60) / 60) / 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
	}
	
	/**
	 * Calculates the number of days from now to the furthest dateNeededBy of any of the goals. Then
	 * creates an array with that many elements in it, and spreads over all of them an even distribution
	 * of the numbers 25 to 1. Elements at the lower end have higher numbers than the higher end of the
	 * array. Think of the 0 element as being today, and the farthest element being the date of the 
	 * furthest needed by date. The closer the needed by date is to today, the greater weight it has.
	 * 
	 * @param data
	 * @return
	 */
	public static int[] getArrayToCalculateRelativeWeightOfDates(JSONObject data) {
		int[] arr = new int[0];
		
		Date furthestDate = getFurthestNeededByDate(data);
		
		if (furthestDate != null) {
			int rangeInDays = (int)(((((furthestDate.getTime() - CalendarUtils.getCurrentCalendar().getTimeInMillis()) / 1000 ) / 60) / 60) / 24);
			arr = new int[rangeInDays];
			
			int scale = 25;
			int maxSegmentSize = rangeInDays / scale;
			int currSegmentSize = 0;
			int segmentIndex = rangeInDays / maxSegmentSize;
			
			for (int i = 0; i < rangeInDays; i++) {
				
				if (currSegmentSize < maxSegmentSize)
					arr[i] = segmentIndex;
				else {
					
					segmentIndex--;
					arr[i] = segmentIndex;
					
					currSegmentSize = -1;
				}
				
				currSegmentSize++;
			}
		}
		
		return arr;
	}
	
	private static Date getFurthestNeededByDate(JSONObject element) {
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(element);
		Date rtn = null;
		
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				JSONArray groupElements = (JSONArray)element.get(key);
				
				for (int x=0; x < groupElements.size(); x++) {
					JSONObject groupElement = (JSONObject)groupElements.get(x);
					Date date = getFurthestNeededByDate(groupElement);
					
					if (rtn == null || (date != null && date.after(rtn)))
						rtn = date;
				}				
			}
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String dateStr = element.get(Constants.DATE_NEEDED_JSON)+"";
			
			try {
				if (dateStr != null && dateStr.length() > 0)
					rtn = sdf.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return rtn;
	}
	
//	public static long getTotalInThePot(JSONObject data) {
//		long l = Integer.parseInt(data.get(Constants.TOTAL_IN_THE_POT_JSON)+"");
//		return l;
//	}
	
	public static long getPreviousTotalInThePot(JSONObject data) {
		long l = Integer.parseInt(data.get(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON)+"");
		return l;
	}
	
	public static long getAmountPaidPerPeriod(JSONObject data) {
		return Integer.parseInt(data.get(Constants.AMT_PAID_PER_PERIOD_JSON)+"");
	}
	
	public static long getRainyDayFundAmount(JSONObject data) {
		return Integer.parseInt(data.get(Constants.AMT_SAVED_FOR_RAINY_DAY_JSON)+"");
	}

	private static double getOffset(JSONObject data) {
		Date date = CalendarUtils.getCurrentCalendar().getTime();
		PaycheckUtils pu = new PaycheckUtils(data);
		int num = pu.getNumberOfPaychecks(date);
		double rtn = -1;
		
		// TODO: should be taking into account the period length from the JSON file
		//  currently offsets are created assuming a 14 day period.
		
		if (num == 2) {
			int chkNum = pu.getPaycheckNumber(date);
			
			if (chkNum == 1) rtn = 0.5;
			else rtn = 1.0;
		
		} else if (num == 3) {
			int chkNum = pu.getPaycheckNumber(date);
			
			if (chkNum == 1) rtn = 0.33;
			else if (chkNum == 2) rtn = 0.66;
			else rtn = 1.0;
		}
		
		return rtn;
	}
	
	
}
