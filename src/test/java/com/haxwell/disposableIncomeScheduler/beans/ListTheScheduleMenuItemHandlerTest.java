package com.haxwell.disposableIncomeScheduler.beans;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;

public class ListTheScheduleMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testFoo() {
		ListTheScheduleMenuItemHandler sut = new ListTheScheduleMenuItemHandler();
		
		sut.doIt(data, state);
	}
}
