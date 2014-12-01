package com.haxwell.disposableIncomeScheduler.beans;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class SetCurrentGroupOverridePercentageMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testFoo() {
		SetCurrentGroupOverridePercentageMenuItemHandler sut = new SetCurrentGroupOverridePercentageMenuItemHandler();
		
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strOutside);
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("0.255");
		
		sut.setInputGetter(mockedInputGetter);
		
		JSONObject op = MenuItemUtils.getOverridingPercentages(data);
		
		assertTrue(op.containsKey(Constants.GOALS_ATTR_KEY+"_"+strBathroom));
		
		sut.doIt(data, state);
		
		op = MenuItemUtils.getOverridingPercentages(data);
		
		assertFalse(op.containsKey(Constants.GOALS_ATTR_KEY+"_"+strBathroom));
		assertTrue(op.containsKey(Constants.GOALS_ATTR_KEY+"_"+strOutside));
	}

	@Test
	public void testFoo_NoPreviouslyExistingOverride() {
		SetCurrentGroupOverridePercentageMenuItemHandler sut = new SetCurrentGroupOverridePercentageMenuItemHandler();
		
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strJohnathansGoals);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+str12880);
		simulateSelectingAGroup(state, Constants.GOALS_ATTR_KEY+"_"+strBathroom);
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("0.75");
		
		sut.setInputGetter(mockedInputGetter);
		
		JSONObject op = MenuItemUtils.getOverridingPercentages(data);
		
		assertTrue(op.containsKey(Constants.GOALS_ATTR_KEY+"_"+strBathroom));
		
		sut.doIt(data, state);
		
		op = MenuItemUtils.getOverridingPercentages(data);
		
		assertTrue(op.containsKey(Constants.GOALS_ATTR_KEY+"_"+strBathroom));
		assertTrue(op.get(Constants.GOALS_ATTR_KEY+"_"+strBathroom).equals("0.75"));
	}
}
