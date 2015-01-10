package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import static org.junit.Assert.assertFalse;	
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class SetCurrentGroupOverridePercentageMenuItemHandlerProviderTest extends JSONDataBasedTest {

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
		SetCurrentGroupOverridePercentageMenuItemHandlerProvider sut = new SetCurrentGroupOverridePercentageMenuItemHandlerProvider();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}

	@Test
	public void testHappyPath2() {
		// TEST: By default the longTermGoals group, the wrapper for all the other long term goals, is selected.
		//  ensure by default this menu option does not appear.
		SetCurrentGroupOverridePercentageMenuItemHandlerProvider sut = new SetCurrentGroupOverridePercentageMenuItemHandlerProvider();
		
		initializeMenuFocus_LongTermGoals();
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertTrue(bean == null);
	}

	@Test
	public void testHappyPath3() {
		// TEST: Once a group is selected, the user should be able to set the group override percentage.
		//  ensure the menu option appears once a group with siblings is selected.
		SetCurrentGroupOverridePercentageMenuItemHandlerProvider sut = new SetCurrentGroupOverridePercentageMenuItemHandlerProvider();
		
		initializeState_Bathroom();
		
		assertTrue(MenuItemUtils.getSiblingsOfSelectedGroup(data, state).size() > 0);
		
		MenuItemHandlerBean bean = sut.getMenuItemHandler();
		
		assertFalse(bean == null);
	}
}
