package com.haxwell.disposableIncomeScheduler.beans;

import static org.mockito.Mockito.mock;	
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class EditAShortTermGoalMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testChangeAllFields() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = "newGoalName";
		final String RESET = "n";
		final String AMT_SAVED_PP = "999";
		final String AMT_SAVED_SO_FAR = "25";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", NEW_GOAL_NAME, AMT_SAVED_PP, RESET, AMT_SAVED_SO_FAR);
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}

	@Test
	public void testChangeOnlyGoalName() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = "newGoalName";
		final String RESET = stg.get(Constants.RESET_EACH_PERIOD_JSON)+"";
		final String AMT_SAVED_PP = stg.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"";
		final String AMT_SAVED_SO_FAR = stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", NEW_GOAL_NAME, "", "", "");
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}

	@Test
	public void testChangeOnlyAmountSavedPerPeriod() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = stg.get(Constants.DESCRIPTION_JSON)+"";
		final String RESET = stg.get(Constants.RESET_EACH_PERIOD_JSON)+"";
		final String AMT_SAVED_PP = "1024";
		final String AMT_SAVED_SO_FAR = stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", "", AMT_SAVED_PP, "", "");
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}

	@Test
	public void testChangeOnlyResetEachPeriod() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = stg.get(Constants.DESCRIPTION_JSON)+"";
		final String RESET = "y";
		final String AMT_SAVED_PP = stg.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"";
		final String AMT_SAVED_SO_FAR = stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", "", "", RESET, "");
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}

	@Test
	public void testChangeOnlyResetEachPeriod_badInput() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = stg.get(Constants.DESCRIPTION_JSON)+"";
		final String AMT_SAVED_PP = stg.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"";
		final String RESET = stg.get(Constants.RESET_EACH_PERIOD_JSON)+"";
		final String AMT_SAVED_SO_FAR = stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", "", "", "blah", "");
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}

	@Test
	public void testChangeOnlyAmountSavedSoFar() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = stg.get(Constants.DESCRIPTION_JSON)+"";
		final String AMT_SAVED_PP = stg.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"";
		final String RESET = stg.get(Constants.RESET_EACH_PERIOD_JSON)+"";
		final String AMT_SAVED_SO_FAR = "1024";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", "", "", "", AMT_SAVED_SO_FAR);
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertTrue(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));		
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}

	@Test
	public void testNoChangesMade() {
		EditAShortTermGoalMenuItemHandler sut = new EditAShortTermGoalMenuItemHandler();
		
		JSONObject stg = MenuItemUtils.getShortTermGoal(data, "clothing");
		
		final String NEW_GOAL_NAME = stg.get(Constants.DESCRIPTION_JSON)+"";
		final String AMT_SAVED_PP = stg.get(Constants.AMT_SAVED_PER_MONTH_JSON)+"";
		final String RESET = stg.get(Constants.RESET_EACH_PERIOD_JSON)+"";		
		final String AMT_SAVED_SO_FAR = stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON)+"";
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("1", "", "", "", "");
		
		sut.setInputGetter(mockedInputGetter);

		boolean rtn = sut.doIt(data, state);
		
		assertFalse(rtn);
		
		assertTrue(stg.get(Constants.DESCRIPTION_JSON).equals(NEW_GOAL_NAME));
		assertTrue(stg.get(Constants.AMT_SAVED_PER_MONTH_JSON).equals(AMT_SAVED_PP));
		assertTrue(stg.get(Constants.RESET_EACH_PERIOD_JSON).equals(RESET));		
		assertTrue(stg.get(Constants.TOTAL_AMOUNT_SAVED_JSON).equals(AMT_SAVED_SO_FAR));
	}
}
