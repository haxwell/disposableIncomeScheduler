package com.haxwell.disposableIncomeScheduler.beans.utils;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;

public class MenuItemUtilsTest {

	JSONObject data;
	JSONObject state;
	
	final String strBathroom = "bathroom";
	final String strOutside = "outside";
	final String strKitchen = "kitchen";
	final String str12880 = "12880";
	final String strRealEstate = "realEstate";
	
	final String strBathroom_sink = "sink";
	final Integer strBathroom_sink_price = 500;
	
	final String strBathroom_shower = "shower";
	final Integer strBathroom_shower_price = 1500;
	
	final String strOutside_garage = "garage door";
	final Integer strOutside_garage_price = 1000;
	
	
	@Before
	public void setup() {
		JSONObject obj = new JSONObject();
		
		obj.put("periodLength", "14");
		obj.put("amountSavedPerPeriod", "500");
		
		JSONObject bathroom = new JSONObject();
		
		JSONArray arr = new JSONArray();
		arr.add(getGoal(strBathroom_sink, strBathroom_sink_price));
		arr.add(getGoal(strBathroom_shower, strBathroom_shower_price));
		bathroom.put(Constants.GOALS_ATTR_KEY+"_"+ strBathroom, arr);

		JSONObject outside = new JSONObject();
		arr = new JSONArray();
		arr.add(getGoal(strOutside_garage, strOutside_garage_price));
		outside.put(Constants.GOALS_ATTR_KEY+"_"+ strOutside, arr);
		
		JSONObject kitchen = new JSONObject();
		arr = new JSONArray();
		kitchen.put(Constants.GOALS_ATTR_KEY+"_"+ strKitchen, arr);
		
		JSONObject _12880 = new JSONObject();
		arr = new JSONArray();
		arr.add(bathroom);
		arr.add(outside);
		arr.add(kitchen);
		_12880.put(Constants.GOALS_ATTR_KEY+"_"+ str12880, arr);
		
		arr = new JSONArray();
		arr.add(_12880);
		
		JSONObject realEstate = new JSONObject();
		realEstate.put(Constants.GOALS_ATTR_KEY+"_"+ strRealEstate, arr);
		
		arr = new JSONArray();
		arr.add(realEstate);

		obj.put(Constants.GOALS_ATTR_KEY+"_"+Constants.ROOT_GOAL_GROUP_NAME, arr);
		
		data = obj;
		state = new JSONObject();
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void testGetSelectedGroup_FindKitchenGroup() {
		
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strRealEstate);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strKitchen);

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		assertTrue(arr.size() == 0);
	}
	
	@Test
	public void testGetSelectedGroup_FindBathroomGroup() {
		
		initializeState_Bathroom();

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		assertTrue(arr.size() == 2);
	}

	@Test
	public void testGetSubgroupNamesOfAGroup_ExpectThreeChildren() {
		initializeState_12880();
		
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<String> children = MenuItemUtils.getSubgroupNamesOfAGroup(arr);
		
		assertTrue(children.size() == 3);
	}
	
	@Test
	public void testGetSubgroupNamesOfAGroup_ExpectEmptySet() {
		initializeState_Bathroom();
		
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<String> children = MenuItemUtils.getSubgroupNamesOfAGroup(arr);
		
		assertTrue(children.size() == 0);
	}
	
	@Test
	public void testGetGoalsOfAGroup_ExpectTwo() {
		initializeState_Bathroom();
		
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<JSONObject> goalsOfAGroup = MenuItemUtils.getGoalsOfAGroup(arr);

		assertTrue(goalsOfAGroup.size() == 2);
	}
	
	@Test
	public void testGetGoalsOfAGroup_ExpectZero() {
		initializeState_12880();
		
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<JSONObject> goalsOfAGroup = MenuItemUtils.getGoalsOfAGroup(arr);

		assertTrue(goalsOfAGroup.size() == 0);
	}
	
	private void initializeState_12880() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strRealEstate);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
	}

	private void initializeState_Bathroom() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strRealEstate);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strBathroom);
	}
	
	private void simulateSelectingAGroup(JSONObject state, String groupName) {
		state.put(Constants.STATE_ATTR_KEY_SELECTED_GROUP_NAME, groupName);
		
		String pathToGroup = (String)state.get(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP);
		pathToGroup += Constants.STATE_ATTR_PATH_DELIMITER + groupName;
		
		state.put(Constants.STATE_ATTR_PATH_TO_SELECTED_GROUP, pathToGroup);
	}
	
	private JSONObject getGoal(String name, Integer price) {
		JSONObject obj = new JSONObject();
		
		obj.put("goal", name);
		obj.put("price", price+"");
		
		return obj;
	}
}
