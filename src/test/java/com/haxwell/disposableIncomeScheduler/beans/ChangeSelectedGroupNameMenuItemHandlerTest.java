package com.haxwell.disposableIncomeScheduler.beans;

import static org.mockito.Mockito.*;	
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;

import net.minidev.json.JSONArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

//TODO: add these junit category types
//@ Category(com.haxwell.disposableIncomeScheduler.testTypes.UnitTests.class) 
public class ChangeSelectedGroupNameMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testSelectedGroupNameIsNOTChanged() {
		ChangeSelectedGroupNameMenuItemHandler sut = new ChangeSelectedGroupNameMenuItemHandler();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("");
		
		sut.setInputGetter(mockedInputGetter);
		
		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strBathroom);
		
		boolean b = sut.doIt(data, state);
		
		assertFalse(b);
		assertTrue(MenuItemUtils.getSelectedGroupName(state).equals(strBathroom));
	}
	
	@Test
	public void testSelectedGroupNameIsChanged() {
		ChangeSelectedGroupNameMenuItemHandler sut = new ChangeSelectedGroupNameMenuItemHandler();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("UpstairsBathroom");
		
		sut.setInputGetter(mockedInputGetter);
		
		simulateSelectingAGroup(state, strJohnathansGoals);
		simulateSelectingAGroup(state, str12880);
		simulateSelectingAGroup(state, strBathroom);
		
		boolean b = sut.doIt(data, state);
		
		assertTrue(b);
		assertTrue(MenuItemUtils.getSelectedGroupName(state).equals("UpstairsBathroom"));
		
		List<String> subgroupNamesOfAGroup = MenuItemUtils.getSubgroupNamesOfAGroup(MenuItemUtils.getParentOfSelectedGroup(data, state));
		
		boolean found = false;
		for (int i = 0; !found && i< subgroupNamesOfAGroup.size(); i++) {
			found = subgroupNamesOfAGroup.get(i).equals("UpstairsBathroom");
		}
		
		assertTrue(found);
	}
}
