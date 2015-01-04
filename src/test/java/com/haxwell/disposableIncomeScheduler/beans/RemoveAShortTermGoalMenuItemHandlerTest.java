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

public class RemoveAShortTermGoalMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testHappyPath() {
		RemoveAShortTermGoalMenuItemHandler sut = new RemoveAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1");
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(arr.size() == Math.max(0, arrSize - 1));
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, "clothing");
		assertTrue(obj == null);
	}
		
	@Test
	public void testEnterBlankLine() {
		RemoveAShortTermGoalMenuItemHandler sut = new RemoveAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("");
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(arr.size() == arrSize);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, "clothing");
		assertFalse(obj == null);

		assertTrue(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals("75"));
		assertTrue(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals("0"));
	}

	@Test
	public void testEnterInvalidSelection_NegativeNumber() {
		RemoveAShortTermGoalMenuItemHandler sut = new RemoveAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("-5");
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(arr.size() == arrSize);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, "clothing");
		assertFalse(obj == null);

		assertTrue(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals("75"));
		assertTrue(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals("0"));
	}

	@Test
	public void testEnterInvalidSelection_PositiveNumber() {
		RemoveAShortTermGoalMenuItemHandler sut = new RemoveAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("50");
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(arr.size() == arrSize);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, "clothing");
		assertFalse(obj == null);

		assertTrue(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals("75"));
		assertTrue(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals("0"));
	}

	@Test
	public void testEnterInvalidSelection_Letters() {
		RemoveAShortTermGoalMenuItemHandler sut = new RemoveAShortTermGoalMenuItemHandler();
		
		JSONArray arr = MenuItemUtils.getShortTermGoals(data);
		
		int arrSize = arr.size();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("AAB");
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(arr.size() == arrSize);
		
		JSONObject obj = MenuItemUtils.getShortTermGoal(data, "clothing");
		assertFalse(obj == null);

		assertTrue(obj.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals("75"));
		assertTrue(obj.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals("0"));
	}
}
