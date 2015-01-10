package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import static org.junit.Assert.assertFalse;	
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public class RemoveAGoalMenuItemHandlerProviderTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testHappyPath() {
		// TEST: By default the longTermGoals group, the wrapper for all the other long term goals, is selected.
		//  ensure by default this menu option does not appear.
		RemoveAGoalMenuItemHandlerProvider sut = new RemoveAGoalMenuItemHandlerProvider();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}

	@Test
	public void testHappyPath2() {
		// TEST: By default the longTermGoals group, the wrapper for all the other long term goals, is selected.
		//  ensure by default this menu option does not appear.
		RemoveAGoalMenuItemHandlerProvider sut = new RemoveAGoalMenuItemHandlerProvider();
		
		initializeMenuFocus_LongTermGoals();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}

	@Test
	public void testHappyPath3() {
		// TEST: Once a group is selected, the user should be able to remove a goal from the group.
		//  ensure the menu option appears once a group with goals in it is selected.
		RemoveAGoalMenuItemHandlerProvider sut = new RemoveAGoalMenuItemHandlerProvider();
		
		initializeState_Bathroom();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertFalse(bean == null);
	}

	@Test
	public void testHappyPath4() {
		// TEST: ensure the menu option does not appear if an empty group is selected.
		RemoveAGoalMenuItemHandlerProvider sut = new RemoveAGoalMenuItemHandlerProvider();
		
		initializeState_Kitchen();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}

	@Test
	public void testHappyPath5() {
		// TEST: ensure the menu option does not appear if a group with subgroups in it is selected.
		RemoveAGoalMenuItemHandlerProvider sut = new RemoveAGoalMenuItemHandlerProvider();
		
		initializeState_12880();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}
}
