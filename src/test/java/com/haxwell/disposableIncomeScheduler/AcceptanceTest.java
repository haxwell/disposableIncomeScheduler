package com.haxwell.disposableIncomeScheduler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.beans.AddThisPeriodsSavedAmountToEachEntryMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.DisplayTheReportMenuItemHandler;

public class AcceptanceTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testFoo() {
		// This test allows me to quickly see, without actually running the program or waiting several weeks,
		//  what the values of the report will be over time given a likely set of data. It starts with the 
		//  first run simulating a report after the report is run for the first time ever, and that being the
		//  first check of the month. Then it advances the date two weeks ahead, and runs the report again.
		//  At this point, the monthly expenses should be reserving their full amounts, and the expenses for
		//  the non-accumulating STGs from the first check should have been accounted for. In the third run,
		//  we've moved ahead two weeks more, and are past the point money should have been withdrawn to pay
		//  monthly expenses, so those should be accounted for, along with non-accumulating STGs. In this test
		//  it is the first check of a three check month, so the reserved amounts for monthly expenses should
		//  be in thirds, rather than normal halves.
		
		// There no asserts.. I couldn't think what to assert.
		
		String dateAsString = "11/21/2014";
		String mrppn = "2"; 
		
		data.put(Constants.MOST_RECENT_PAYDATE, dateAsString);
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, mrppn);
		data.put(Constants.TESTING_OVERRIDE_DATE_JSON, "12/06/2014");
		
		AddThisPeriodsSavedAmountToEachEntryMenuItemHandler addT = new AddThisPeriodsSavedAmountToEachEntryMenuItemHandler();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("yes");
		
		addT.setInputGetter(mockedInputGetter);
		
//		Println printlner = mock(Println.class);
		Println printlner = new Println();
		addT.setPrintlner(printlner);
		
		addT.doIt(data, state);
		
		DisplayTheReportMenuItemHandler dispTR = new DisplayTheReportMenuItemHandler();
		
		InputGetter mockedInputGetter2 = mock(InputGetter.class);
		when(mockedInputGetter2.readInput()).thenReturn("");
		
		dispTR.setInputGetter(mockedInputGetter2);
		dispTR.setPrintlner(printlner);
		dispTR.doIt(data, state);
		
		data.put(Constants.TESTING_OVERRIDE_DATE_JSON, "12/20/2014");
		
		state.remove(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_STGS);
		state.remove(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_LTGS);
		
		addT.doIt(data, state);
		dispTR.doIt(data, state);
		
		data.put(Constants.TESTING_OVERRIDE_DATE_JSON, "01/03/2015");
		
		state.remove(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_STGS);
		state.remove(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED_TO_LTGS);
		
		addT.doIt(data, state);
		dispTR.doIt(data, state);
	}
}
