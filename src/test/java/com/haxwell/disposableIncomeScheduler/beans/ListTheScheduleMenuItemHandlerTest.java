package com.haxwell.disposableIncomeScheduler.beans;

import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.Println;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

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
		
		data.put(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON, "2000");
		
		// set the price for all the goals to 20.. so they can quickly be fully saved.
		initializeState_Bathroom();
		List<JSONObject> goals = MenuItemUtils.getGoalsOfAGroup(MenuItemUtils.getSelectedGroup(data, state));
		
		for (JSONObject goal : goals) {
			goal.put(Constants.PRICE_JSON, "20");
		}
		
		initializeState_Outside();
		goals = MenuItemUtils.getGoalsOfAGroup(MenuItemUtils.getSelectedGroup(data, state));
		
		for (JSONObject goal : goals) {
			goal.put(Constants.PRICE_JSON, "20");
		}

		// skip initializing state to Kitchen, because it should be empty.
		
		initializeState_TripToFrance();
		goals = MenuItemUtils.getGoalsOfAGroup(MenuItemUtils.getSelectedGroup(data, state));
		
		for (JSONObject goal : goals) {
			goal.put(Constants.PRICE_JSON, "20");
		}
		
		ApplyPaycheckToExpensesAndGoalsMenuItemHandler mih = new ApplyPaycheckToExpensesAndGoalsMenuItemHandler();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("yes");
		
		mih.setInputGetter(mockedInputGetter);
		
//		Println printlner = mock(Println.class);
		Println printlner = new Println();
		mih.setPrintlner(printlner);
		
		mih.doIt(data, state);
		
		try {
			sut.doIt(data, state);
		} catch (Exception e) {
			fail();
		}
	}
}
