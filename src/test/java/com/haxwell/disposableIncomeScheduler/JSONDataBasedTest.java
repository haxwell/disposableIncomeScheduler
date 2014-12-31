package com.haxwell.disposableIncomeScheduler;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

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
		
		obj.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, "0");
		obj.put(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON, "2000");		
		obj.put(Constants.TOTAL_IN_THE_POT_JSON, "2000");
		obj.put(Constants.PERIOD_LENGTH_JSON, "14");
		obj.put(Constants.AMT_PAID_PER_PERIOD_JSON, "2400");
		obj.put(Constants.AMT_SAVED_FOR_RAINY_DAY_JSON, "500");		
		obj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "500");
		obj.put(Constants.MOST_RECENT_PAYDATE, "12/19/2014");
		obj.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, "2");
		obj.put(Constants.NUMBER_OF_PAYCHECKS_PROCESSED, "0");
		
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
		stgobj.put(Constants.RESET_EACH_PERIOD_JSON, "n");
		
		arr.add(stgobj);
		
		stgobj = new JSONObject();
		stgobj.put(Constants.DESCRIPTION_JSON, "groceries");
		stgobj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
		stgobj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "150");
		stgobj.put(Constants.RESET_EACH_PERIOD_JSON, "y");		
		
		arr.add(stgobj);
		
		stgobj = new JSONObject();
		stgobj.put(Constants.DESCRIPTION_JSON, "gas");
		stgobj.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "0");
		stgobj.put(Constants.AMT_SAVED_PER_PERIOD_JSON, "120");
		stgobj.put(Constants.RESET_EACH_PERIOD_JSON, "y");
		
		arr.add(stgobj);

		obj.put(Constants.SHORT_TERM_GOALS_JSON, arr);
		
		JSONObject iobj = new JSONObject();
		iobj.put(strBathroom, "0.15");
//		iobj.put(strOutside), "0.20");
		obj.put(Constants.OVERRIDING_PERCENTAGE_AMT_JSON, iobj);
		
		JSONObject bathroom = new JSONObject();
		arr = new JSONArray();
		
		arr.add(getGoal(strBathroom_sink, strBathroom_sink_price, 0, 10, 10, 10, 10, "06/21/2015"));
		arr.add(getGoal(strBathroom_shower, strBathroom_shower_price, 0, 10, 10, 10, 10, "06/21/2015"));
		bathroom.put(strBathroom, arr);

		JSONObject outside = new JSONObject();
		arr = new JSONArray();
		arr.add(getGoal(strOutside_garage, strOutside_garage_price));
		outside.put(strOutside, arr);
		
		JSONObject kitchen = new JSONObject();
		arr = new JSONArray();
		kitchen.put(strKitchen, arr);
		
		JSONObject _12880 = new JSONObject();
		arr = new JSONArray();
		arr.add(bathroom);
		arr.add(outside);
		arr.add(kitchen);
		_12880.put(str12880, arr);
		
		// -- trip to france
		JSONObject tripToFrance = new JSONObject();
		arr = new JSONArray();
		arr.add(getGoal(strTripToFrance_airfare, strTripToFrance_airfare_price, 0, 10, 10, 10, 10, "09/01/2016"));
		arr.add(getGoal(strTripToFrance_lodging, strTripToFrance_lodging_price, 0, 10, 10, 10, 10, "10/01/2016"));
		tripToFrance.put(strTripToFrance, arr);
		
		arr = new JSONArray();
		arr.add(_12880);
		arr.add(tripToFrance);
		
		JSONObject johnathansGoals = new JSONObject();
		johnathansGoals.put(strJohnathansGoals, arr);
		
		arr = new JSONArray();
		arr.add(johnathansGoals);

		obj.put(Constants.LONG_TERM_GOALS_JSON, arr);
		
		data = obj;
		state = new JSONObject();
		
		MenuItemUtils.initializeState(state);
		
		DataAndStateSingleton.getInstance().setData(data);
		DataAndStateSingleton.getInstance().setState(state);
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
	
	protected void simulateSelectingAGroup(JSONObject state, String groupName) {
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, groupName);
		
		String pathToGroup = (String)state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		pathToGroup += Constants.STATE_ATTR_PATH_DELIMITER + groupName;
		
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, pathToGroup);
	}
	
	protected JSONObject getJSONObjectByDescriptionFromJSONArray(JSONArray arr, String description) {
		JSONObject rtn = null;
		boolean found = false;
		
		for (int i=0; i < arr.size() && !found; i++) {
			JSONObject obj = (JSONObject)arr.get(i);
			
			if (obj.get(Constants.DESCRIPTION_JSON).equals(description)) {
				rtn = obj;
				found = true;
			}
		}
		
		return rtn;
	}
	
	protected void initializeState_12880() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
	}

	protected void initializeState_Bathroom() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strBathroom);
	}

	protected void initializeState_Kitchen() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strKitchen);
	}

	protected void initializeState_Outside() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strOutside);
	}
}
