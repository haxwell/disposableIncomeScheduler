package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import static org.junit.Assert.assertFalse;	
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public class MoveToParentGroupMenuItemHandlerProviderTest extends JSONDataBasedTest {

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
		MoveToParentGroupMenuItemHandlerProvider sut = new MoveToParentGroupMenuItemHandlerProvider();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}

	@Test
	public void testHappyPath2() {
		// TEST: Once a group is selected, the user should be able to move to the parent group.
		//  ensure the menu option appears once a group is selected.
		MoveToParentGroupMenuItemHandlerProvider sut = new MoveToParentGroupMenuItemHandlerProvider();
		
		initializeState_12880();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertFalse(bean == null);
	}
}
