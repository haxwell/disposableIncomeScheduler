package com.haxwell.disposableIncomeScheduler.beans;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class ToMainMenuMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testHappyPath() {
		ToMainMenuMenuItemHandler sut = new ToMainMenuMenuItemHandler();
		
		initializeState_Bathroom();
		
		sut.doIt(data, state);
		
		assertTrue(MenuItemUtils.isMenuFocusedOnTheMainLevel(state));
	}
}
