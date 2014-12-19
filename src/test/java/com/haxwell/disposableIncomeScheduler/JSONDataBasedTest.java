package com.haxwell.disposableIncomeScheduler;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class JSONDataBasedTest {

	protected JSONObject data;
	protected JSONObject state;
	
	protected final String strBathroom = "bathroom";
	protected final String strOutside = "outside";
	protected final String strKitchen = "kitchen";
	protected final String str12880 = "12880";
	protected final String strTripToFrance = "trip to France";
	protected final String strJohnathansGoals = "johnathans goals";
	
	protected final String strBathroom_sink = "sink";
	protected final Integer strBathroom_sink_price = 500;
	
	protected final String strBathroom_shower = "shower";
	protected final Integer strBathroom_shower_price = 1500;
	
	protected final String strOutside_garage = "garage door";
	protected final Integer strOutside_garage_price = 1000;
	
	protected final String strTripToFrance_airfare = "airfare";
	protected final Integer strTripToFrance_airfare_price = 3000;

	protected final String strTripToFrance_lodging = "lodging";
	protected final Integer strTripToFrance_lodging_price = 2000;

	protected void createDataAndStateObjects() {
		JSONObject obj = new JSONObject();
		
		obj.put(Constants.PERIOD_LENGTH_JSON, "14");
		obj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "500");
		
		JSONArray arr = new JSONArray();

		JSONObject eobj = new JSONObject();
		eobj.put(Constants.DESCRIPTION_JSON, "mortgage");
		eobj.put(Constants.PRICE_JSON, "1025");
		
		arr.add(eobj);
		
		eobj = new JSONObject();
		eobj.put(Constants.DESCRIPTION_JSON, "cellphone");
		eobj.put(Constants.PRICE_JSON, "100");
		
		arr.add(eobj);
		
		obj.put(Constants.EXPENSES_JSON, arr);
		
		arr = new JSONArray();
		
		JSONObject stgobj = new JSONObject();
		stgobj.put(Constants.DESCRIPTION_JSON, "clothing");
		stgobj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
		stgobj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "75");
		
		arr.add(stgobj);
		
		stgobj = new JSONObject();
		stgobj.put(Constants.DESCRIPTION_JSON, "groceries");
		stgobj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
		stgobj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "150");
		
		arr.add(stgobj);
		
		stgobj = new JSONObject();
		stgobj.put(Constants.DESCRIPTION_JSON, "gas");
		stgobj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
		stgobj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "120");
		
		arr.add(stgobj);

		obj.put(Constants.SHORT_TERM_GOALS_JSON, arr);
		
		JSONObject iobj = new JSONObject();
		iobj.put(getGoalGroupName(strBathroom), "0.15");
//		iobj.put(getGoalGroupName(strOutside), "0.20");
		obj.put(Constants.OVERRIDING_PERCENTAGE_AMT_JSON, iobj);
		
		JSONObject bathroom = new JSONObject();
		arr = new JSONArray();
		
		arr.add(getGoal(strBathroom_sink, strBathroom_sink_price, 0, 10, 10, 10, 10, "06/21/2015"));
		arr.add(getGoal(strBathroom_shower, strBathroom_shower_price, 0, 10, 10, 10, 10, "06/21/2015"));
		bathroom.put(getGoalGroupName(strBathroom), arr);

		JSONObject outside = new JSONObject();
		arr = new JSONArray();
		arr.add(getGoal(strOutside_garage, strOutside_garage_price));
		outside.put(getGoalGroupName(strOutside), arr);
		
		JSONObject kitchen = new JSONObject();
		arr = new JSONArray();
		kitchen.put(getGoalGroupName(strKitchen), arr);
		
		JSONObject _12880 = new JSONObject();
		arr = new JSONArray();
		arr.add(bathroom);
		arr.add(outside);
		arr.add(kitchen);
		_12880.put(getGoalGroupName(str12880), arr);
		
		// -- trip to france
		JSONObject tripToFrance = new JSONObject();
		arr = new JSONArray();
		arr.add(getGoal(strTripToFrance_airfare, strTripToFrance_airfare_price, 0, 10, 10, 10, 10, "09/01/2016"));
		arr.add(getGoal(strTripToFrance_lodging, strTripToFrance_lodging_price, 0, 10, 10, 10, 10, "10/01/2016"));
		tripToFrance.put(getGoalGroupName(strTripToFrance), arr);
		
		arr = new JSONArray();
		arr.add(_12880);
		arr.add(tripToFrance);
		
		JSONObject johnathansGoals = new JSONObject();
		johnathansGoals.put(getGoalGroupName(strJohnathansGoals), arr);
		
		arr = new JSONArray();
		arr.add(johnathansGoals);

		obj.put(getGoalGroupName(Constants.ROOT_GOAL_GROUP_NAME), arr);
		
		data = obj;
		state = new JSONObject();
		
		MenuItemUtils.initializeState(state);
	}
	
	private JSONObject getGoal(String name, Integer price) {
		return getGoal(name, price, 0, 10, 10, 10, 10, "");
	}
	
	private JSONObject getGoal(String name, Integer price, Integer previouslySavedAmt, Integer utilityImmediacy, Integer happinessImmediacy,
			Integer utilityLength, Integer happinessLength, String dateNeededBy) {
		JSONObject obj = new JSONObject();
		
		obj.put(Constants.PRICE_JSON, price+"");
		obj.put(Constants.UTILITY_IMMEDIACY_JSON, utilityImmediacy+"");
		obj.put(Constants.HAPPINESS_IMMEDIACY_JSON, happinessImmediacy+"");
		obj.put(Constants.UTILITY_LENGTH_JSON, utilityLength+"");
		obj.put(Constants.HAPPINESS_LENGTH_JSON, happinessLength+"");
		obj.put(Constants.DESCRIPTION_JSON, name);
		obj.put(Constants.DATE_NEEDED_JSON, dateNeededBy);
		obj.put(Constants.PREVIOUS_SAVED_AMT_JSON, previouslySavedAmt);
		
		return obj;
	}
	
	private String getGoalGroupName(String str) {
		return Constants.GOALS_ATTR_KEY+"_"+str;
	}
	
	protected void simulateSelectingAGroup(JSONObject state, String groupName) {
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, groupName);
		
		String pathToGroup = (String)state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		pathToGroup += Constants.STATE_ATTR_PATH_DELIMITER + groupName;
		
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, pathToGroup);
	}
}
