package com.haxwell.disposableIncomeScheduler.beans;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class AddAGoalMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testHappyPath() {
		AddAGoalMenuItemHandler sut = new AddAGoalMenuItemHandler();
		
		String desc = "newTestGoal";
		String price = "1000";
		String prevSavedAmt = "0";
		String HU = "10";
		String dateNeededBy = "10/1/2016";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn(desc, price, prevSavedAmt, HU, HU, HU, HU, dateNeededBy);
		
		sut.setInputGetter(mockedInputGetter);

		initializeState_Kitchen();
		
		sut.doIt(data, state);
		
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		assertTrue(arr.size() == 1);
		
		JSONObject obj = (JSONObject)arr.get(0);
		
		assertTrue(obj.get(Constants.DESCRIPTION_JSON).equals(desc));
		assertTrue(obj.get(Constants.PRICE_JSON).equals(price));
		assertTrue(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON).equals(prevSavedAmt));
		assertTrue(obj.get(Constants.HAPPINESS_IMMEDIACY_JSON).equals(HU));
		assertTrue(obj.get(Constants.UTILITY_IMMEDIACY_JSON).equals(HU));
		assertTrue(obj.get(Constants.HAPPINESS_LENGTH_JSON).equals(HU));
		assertTrue(obj.get(Constants.UTILITY_LENGTH_JSON).equals(HU));
		assertTrue(obj.get(Constants.DATE_NEEDED_JSON).equals(dateNeededBy));
	}
}
