package com.haxwell.disposableIncomeScheduler.beans;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class RemoveSelectedGroupMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	// TODO: Test One Level (selected group is root) and Two Level (selected group is child of root) 
	
	@Test
	public void testRemoveSelectedGroup_andPathAndSelectedGroupAreCorrectlyUpdated_ThreeLevels() {
		RemoveSelectedGroupMenuItemHandler sut = new RemoveSelectedGroupMenuItemHandler();
		
		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		
		String path12880 = MenuItemUtils.getSelectedGroupPath(state);
		
		simulateSelectingAGroup(state, strBathroom);
		
		sut.doIt(data, state);
		
		assertTrue(MenuItemUtils.getSelectedGroupName(state).equals(str12880));
		assertTrue(MenuItemUtils.getSelectedGroupPath(state).equals(path12880));
	}
}
