package com.haxwell.disposableIncomeScheduler.beans.utils;

import static org.junit.Assert.assertTrue;		

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;

public class MenuItemUtilsTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}
	
	@Test
	public void testGetSelectedGroup_FindKitchenGroup() {
		
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals);
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
	
	@Test
	public void testMovingFromAChildGroupToItsParent2() {
		initializeState_Bathroom();

		String groupName = MenuItemUtils.getSelectedGroupName(state);
		assertTrue(groupName.equals(Constants.GOALS_ATTR_KEY + "_" + strBathroom));
		
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);

		List<JSONObject> goals = MenuItemUtils.getGoalsOfAGroup(arr);
		
		assertTrue(goals.size() == 2);
		
		arr = MenuItemUtils.getParentOfSelectedGroup(data, state);
		groupName = MenuItemUtils.getSelectedGroupName(state);
		assertTrue(groupName.equals(Constants.GOALS_ATTR_KEY + "_" + str12880));
		
		goals = MenuItemUtils.getGoalsOfAGroup(arr);
		assertTrue(goals.size() == 0);
		
		List<String> subgrps = MenuItemUtils.getSubgroupNamesOfAGroup(arr);
		assertTrue(subgrps.size() == 3);
	}
	
	private void initializeState_12880() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
	}

	private void initializeState_Bathroom() {
		MenuItemUtils.initializeState(state);

		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strBathroom);
	}
}
