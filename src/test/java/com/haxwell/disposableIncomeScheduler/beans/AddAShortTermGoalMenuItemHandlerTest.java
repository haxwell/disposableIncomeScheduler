package com.haxwell.disposableIncomeScheduler.beans;

import static org.mockito.Mockito.mock;	
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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

public class AddAShortTermGoalMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testHappyPath() {
		AddAShortTermGoalMenuItemHandler sut = new AddAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		final String NAME = "name";
		final String AMT_PP = "150";
		final String RESET = "y";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn(NAME, AMT_PP, RESET);
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(arr.size() == arrSize + 1);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, NAME);
		assertFalse(obj == null);
		
		assertTrue(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_PP));
		assertTrue(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals("0"));
	}

	@Test
	public void testHappyPath_WithPressingEnterToSelectDefaultReset() {
		AddAShortTermGoalMenuItemHandler sut = new AddAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		final String NAME = "name";
		final String AMT_PP = "150";
		final String RESET = "";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn(NAME, AMT_PP, RESET);
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(arr.size() == arrSize + 1);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, NAME);
		assertFalse(obj == null);
		
		assertTrue(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_PP));
		assertTrue(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals("0"));
	}
		
	@Test
	public void testEnterNameButNoAmountPerPeriod() {
		AddAShortTermGoalMenuItemHandler sut = new AddAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		final String NAME = "name";
		final String AMT_PP = "";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn(NAME, AMT_PP);
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(arr.size() == arrSize);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, NAME);
		assertTrue(obj == null);
	}
		
	@Test
	public void testEnterBlankNameAndNoAmountPerPeriod() {
		AddAShortTermGoalMenuItemHandler sut = new AddAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		final String NAME = "";
		final String AMT_PP = "";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn(NAME, AMT_PP);
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(arr.size() == arrSize);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, NAME);
		assertTrue(obj == null);
	}
}
