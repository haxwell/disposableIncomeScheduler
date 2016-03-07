package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProviderTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testHappyPath() {
		// TEST: The current date is not overriden, so it should be after the paydate following the default most recent paydate,
		//  so this menu option should be available.
		
		ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider sut = new ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider();
		
		assertFalse(sut.getMenuItemHandler() == null); 
	}

	@Test
	public void testHappyPath2() {
		
		// TEST: when the current date is after the most recent paydate, but not greater than the paydate after the most recent paydate, 
		//	this menu option should NOT be available
		
		ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider sut = new ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider();
		
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();
		
		Calendar cal = CalendarUtils.getCalendar(PaycheckUtils.getMostRecentPaydate(data));
		cal.add(Calendar.DAY_OF_MONTH, 3);
		
		data.put(Constants.TESTING_OVERRIDE_DATE_JSON, CalendarUtils.getCalendarAsMMDDYYYY(cal));
		
		assertTrue(sut.getMenuItemHandler() == null); 
	}

	@Test
	public void testHappyPath3() {

		// TEST: when the current date is equal to the paydate which is to occur after the most recent paydate, 
		//	this menu option should be available
		
		ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider sut = new ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider();
		
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();
		
		Calendar cal = CalendarUtils.getCalendar(PaycheckUtils.getFuturePaydate(data, 1));
		
		data.put(Constants.TESTING_OVERRIDE_DATE_JSON, CalendarUtils.getCalendarAsMMDDYYYY(cal));
		
		assertFalse(sut.getMenuItemHandler() == null); 
	}

	@Test
	public void testHappyPath4() {
		
		// TEST: when the current date is after the paydate which is to occur after the most recent paydate, 
		//	this menu option should be available
		
		ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider sut = new ApplyPaycheckToExpensesAndGoalsMenuItemHandlerProvider();
		
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();
		
		Calendar cal = CalendarUtils.getCalendar(PaycheckUtils.getFuturePaydate(data, 1));
		cal.add(Calendar.DAY_OF_MONTH, 3);
		
		data.put(Constants.TESTING_OVERRIDE_DATE_JSON, CalendarUtils.getCalendarAsMMDDYYYY(cal));
		
		assertFalse(sut.getMenuItemHandler() == null); 
	}
}
