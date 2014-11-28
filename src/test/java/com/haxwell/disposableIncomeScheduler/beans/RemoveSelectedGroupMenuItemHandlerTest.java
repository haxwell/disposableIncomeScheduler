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

	@Test
	public void testFoo() {
		RemoveSelectedGroupMenuItemHandler sut = new RemoveSelectedGroupMenuItemHandler();
		
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
		
		String path12880 = MenuItemUtils.getSelectedGroupPath(state);
		
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strBathroom);
		
		sut.doIt(data, state);
		
		assertTrue(MenuItemUtils.getSelectedGroupName(state).equals(Constants.GOALS_ATTR_KEY+"_"+str12880));
		assertTrue(MenuItemUtils.getSelectedGroupPath(state).equals(path12880));
	}
}
