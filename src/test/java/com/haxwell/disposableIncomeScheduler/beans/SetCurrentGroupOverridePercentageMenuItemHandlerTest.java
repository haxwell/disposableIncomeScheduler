package com.haxwell.disposableIncomeScheduler.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		
		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strOutside);
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("0.255");
		
		sut.setInputGetter(mockedInputGetter);
		
		JSONObject op = MenuItemUtils.getOverridingPercentages(data);
		
		assertTrue(op.containsKey(strBathroom));
		
		sut.doIt(data, state);
		
		op = MenuItemUtils.getOverridingPercentages(data);
		
		assertFalse(op.containsKey(strBathroom));
		assertTrue(op.containsKey(strOutside));
	}

	@Test
	public void testFoo_NoPreviouslyExistingOverride() {
		SetCurrentGroupOverridePercentageMenuItemHandler sut = new SetCurrentGroupOverridePercentageMenuItemHandler();
		
		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strBathroom);
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("0.75");
		
		sut.setInputGetter(mockedInputGetter);
		
		JSONObject op = MenuItemUtils.getOverridingPercentages(data);
		
		assertTrue(op.containsKey(strBathroom));
		
		sut.doIt(data, state);
		
		op = MenuItemUtils.getOverridingPercentages(data);
		
		assertTrue(op.containsKey(strBathroom));
		assertTrue(op.get(strBathroom).equals("0.75"));
	}
}
