package com.haxwell.disposableIncomeScheduler.beans.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
	public void testGetExpenses_Populated() {
		JSONArray expenses = MenuItemUtils.getExpenses(data);
		
		assertTrue(expenses.size() == 2);
	}
	
	@Test
	public void testSetSelectedGroupName_withoutPrefix() {
		MenuItemUtils.initializeState(state);
		
		MenuItemUtils.setSelectedGroupName(state, "foo");
		
		String str = MenuItemUtils.getSelectedGroupName(state);
		
		assertTrue(str.startsWith(Constants.GOALS_ATTR_KEY+"_"));
		assertTrue(str.equals(Constants.GOALS_ATTR_KEY+"_foo"));
	}
	
	@Test
	public void testSetSelectedGroupName_withPrefix() {
		MenuItemUtils.initializeState(state);
		
		MenuItemUtils.setSelectedGroupName(state, Constants.GOALS_ATTR_KEY+"_"+"foo");
		
		String str = MenuItemUtils.getSelectedGroupName(state);
		
		assertTrue(str.startsWith(Constants.GOALS_ATTR_KEY+"_"));
		assertFalse(str.startsWith(Constants.GOALS_ATTR_KEY+"_"+Constants.GOALS_ATTR_KEY));
		assertTrue(str.equals(Constants.GOALS_ATTR_KEY+"_foo"));
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
	public void testGetSelectedParentGroupName_withOneLevelPath() {
		MenuItemUtils.initializeState(state);
		
		String str = MenuItemUtils.getSelectedGroupParentName(state);
		
		assertTrue(str.equals(MenuItemUtils.getRootGroupName()));
	}
	
	@Test
	public void testGetSelectedParentGroupName_withTwoLevelPath() {
		initializeState_12880();
		
		String str = MenuItemUtils.getSelectedGroupParentName(state);
		
		assertTrue(str.equals(Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals));
	}
	
	@Test
	public void testGetSelectedParentGroupName_withThreeLevelPath() {
		initializeState_Bathroom();
		
		String str = MenuItemUtils.getSelectedGroupParentName(state);
		
		assertTrue(str.equals(Constants.GOALS_ATTR_KEY+"_"+str12880));
	}
	
	@Test
	public void testGetSelectedParentGroupPath_withOneLevelPath() {
		MenuItemUtils.initializeState(state);
		
		String str = MenuItemUtils.getSelectedGroupParentPath(state);
		
		assertTrue(str.equals(MenuItemUtils.getRootGroupName()));
	}
	
	@Test
	public void testGetSelectedParentGroupPath_withTwoLevelPath() {
		initializeState_12880();
		
		String str = MenuItemUtils.getSelectedGroupParentPath(state);
		
		assertTrue(str.equals(MenuItemUtils.getRootGroupName()+Constants.STATE_ATTR_PATH_DELIMITER+
				Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals));
	}
	
	@Test
	public void testGetSelectedParentGroupPath_withThreeLevelPath() {
		initializeState_Bathroom();
		
		String str = MenuItemUtils.getSelectedGroupParentPath(state);
		
		assertTrue(str.equals(MenuItemUtils.getRootGroupName()+Constants.STATE_ATTR_PATH_DELIMITER+
				Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals+Constants.STATE_ATTR_PATH_DELIMITER+
				Constants.GOALS_ATTR_KEY+"_"+str12880));
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
