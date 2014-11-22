package com.haxwell.disposableIncomeScheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class Calculator {
	
	protected static final String DESCRIPTION_JSON = "description";
	protected static final String HAPPINESS_IMMEDIACY_JSON = "happinessImmediacy";
	protected static final String UTILITY_IMMEDIACY_JSON = "utilityImmediacy";
	protected static final String HAPPINESS_LENGTH_JSON = "happinessLength";
	protected static final String UTILITY_LENGTH_JSON = "utilityLength";
	protected static final String DATE_NEEDED_JSON = "dateNeededBy";

	public static void applyMoney(JSONObject data) {
		JSONObject weights = Calculator.getWeights(data);
		int dollarAmount = Integer.parseInt(data.get(Constants.AMT_SAVED_PER_PERIOD_JSON)+"");
		
		JSONArray grpArr = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		JSONObject grpRootElement = (JSONObject)grpArr.get(0);
		
		JSONArray weightsArr = (JSONArray)weights.get(MenuItemUtils.getRootGroupName());
		JSONObject weightsRootElement = (JSONObject)weightsArr.get(0);
		
		applyMoneyHelper(grpRootElement, weightsRootElement, dollarAmount);
	}
	
	private static void applyMoneyHelper(JSONObject ge, JSONObject we, long dollarAmount) {
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(ge);
		
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				JSONArray groupElements = (JSONArray)ge.get(key);
				JSONArray weightElements = (JSONArray)we.get(key);
				
				assert groupElements.size() == weightElements.size() : "GroupElements and WeightElements must contain the same number of elements";
				
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
						
					/*JSONObject obj =*/ applyMoneyHelper(groupElement, weightElement, weightedDollarAmount);
					}
					else {
						// this is a goal.. we need to apply money here
						long previousSavedAmount = Long.parseLong(groupElement.get(Constants.PREVIOUS_SAVED_AMT_JSON)+"");
						Double goalWeightAsPercentage = Double.parseDouble(weightElement.get(Constants.WEIGHT_AS_PERCENTAGE_JSON)+"");

						previousSavedAmount += Math.round(dollarAmount * goalWeightAsPercentage);
						
						groupElement.put(Constants.PREVIOUS_SAVED_AMT_JSON, previousSavedAmount+"");
					}
				}				
			}
		} else {
			// this is a leaf, a goal
			String str = "foo";
		}
	}
	
	public static JSONObject getWeights(JSONObject data) {
		JSONArray arr = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		JSONObject rootElement = (JSONObject)arr.get(0);
		int dateArr[] = getArrayToCalculateRelativeWeightOfDates(rootElement);
		
		JSONObject rtn = buildJSONWeightObject(rootElement, dateArr);
		
		rtn = addGroupWeightsToJSONWeightObject(rtn, (JSONObject)data.get(Constants.OVERRIDING_PERCENTAGE_AMT_JSON));
		
		JSONArray rtnArr = new JSONArray();
		rtnArr.add(rtn);
		
		JSONObject obj = new JSONObject();
		obj.put(MenuItemUtils.getRootGroupName(), rtnArr);
				
		return obj;
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

	private static JSONObject buildJSONWeightObject(JSONObject element, int[] dateArr) {
		JSONObject weights = new JSONObject();
		
		List<String> list = MenuItemUtils.getSubgroupNamesOfAGroup(element);
		
		if (list.size() > 0) {
			
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				JSONArray groupElements = (JSONArray)element.get(key);
				
				if (groupElements.size() == 0) {
					// this is an empty group.. it has no goals or subgroups
					JSONArray arr = new JSONArray();
					JSONObject obj = new JSONObject();
					
					obj.put(Constants.DESCRIPTION_JSON, element.get(Constants.DESCRIPTION_JSON));
					obj.put(Constants.WEIGHT_JSON, "0");
					obj.put(Constants.WEIGHT_AS_PERCENTAGE_JSON, "0.0");
					
					arr.add(obj);
					weights.put(key, arr);
				}
				else {
					int total = 0;
					
					for (int x=0; x < groupElements.size(); x++) {
						JSONObject groupElement = (JSONObject)groupElements.get(x);
						JSONObject weight = buildJSONWeightObject(groupElement, dateArr);
						
						JSONArray arr = null;
						if (weights.containsKey(key))
							arr = (JSONArray)weights.get(key);
						else {
							arr = new JSONArray();
							weights.put(key, arr);
						}
						
						arr.add(weight);
						
						if (weight.containsKey(Constants.WEIGHT_JSON)) // if the weights we just got are the weights for a leaf...
							total += Integer.parseInt(weight.get(Constants.WEIGHT_JSON)+"");
					}
					
					JSONArray arr = (JSONArray)weights.get(key);
					
					for (int x=0; arr != null && x < arr.size(); x++) {
						JSONObject obj = (JSONObject)arr.get(x);
	
						if (obj.containsKey(Constants.WEIGHT_JSON)) {
							Object valueObj = obj.get(Constants.WEIGHT_JSON);
							
							int value = Integer.parseInt(valueObj+"");
							obj.put(Constants.WEIGHT_AS_PERCENTAGE_JSON, ((value*1.0)/total)+"");
						}
					}
				}
			}
		} else {
			// this is a goal, a leaf
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
		if (!goal.get(DATE_NEEDED_JSON).toString().equals("") && dateArr.length > 0) {
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
								double weightAsPercentage = (weight == 0) ? 0 : (weight*1.0)/total;
								
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
						innerGroupWeight += Integer.parseInt(groupElement.get(Constants.WEIGHT_JSON)+"");
					}
					
					JSONObject groupWeightObj = new JSONObject();
					groupWeightObj.put(Constants.GROUP_WEIGHT_JSON, innerGroupWeight+"");
					
					if (overridingPercentages.containsKey(key))
						groupWeightObj.put(Constants.OVERRIDING_PERCENTAGE_AMT_JSON, overridingPercentages.get(key));
					
					groupElements.add(groupWeightObj);
					outerGroupWeight += innerGroupWeight;
				}
			}
		} 
		
		return element;
	}
	
	public static int getNumberOfDaysFromToday(JSONObject obj) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		int rtn = -1;
		
		try {
			cal.setTime(sdf.parse(obj.get(DATE_NEEDED_JSON).toString()));
			rtn = (int)(((((cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000 ) / 60) / 60) / 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
	}
	
	public static int[] getArrayToCalculateRelativeWeightOfDates(JSONObject data) {
		Date furthestDate = getFurthestNeededByDate(data);
		
		int rangeInDays = (int)(((((furthestDate.getTime() - Calendar.getInstance().getTimeInMillis()) / 1000 ) / 60) / 60) / 24);
		
		int scale = 25;
		int maxSegmentSize = rangeInDays / scale;
		int currSegmentSize = 0;
		int segmentIndex = rangeInDays / maxSegmentSize;
		
		int[] arr = new int[rangeInDays];
		
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
		
		return arr;
	}
}
