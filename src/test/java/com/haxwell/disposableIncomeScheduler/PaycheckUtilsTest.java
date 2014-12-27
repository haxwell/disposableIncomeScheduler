package com.haxwell.disposableIncomeScheduler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;

public class PaycheckUtilsTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}
	
	@Test
	public void testGetMostRecentPaydate() {
		String dateAsString = "12/05/2014";
		
		data.put(Constants.MOST_RECENT_PAYDATE, dateAsString);
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, "1");
		
		PaycheckUtils sut = new PaycheckUtils(data);
		
		Date mostRecentPaydate = sut.getMostRecentPaydate(data);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		assertTrue(sdf.format(mostRecentPaydate).equals(dateAsString));
	}

	@Test
	public void testGetNumberOfPaychecks_expect_2() {
		String dateAsString = "12/05/2014";
		
		data.put(Constants.MOST_RECENT_PAYDATE, dateAsString);
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, "1");
		
		PaycheckUtils sut = new PaycheckUtils(data);

		Date date = sut.getMostRecentPaydate(data);
		
		int val = sut.getNumberOfPaychecks(data, date);
		
		assertTrue(val == 2);
	}
	
	@Test
	public void testGetNumberOfPaychecks_expect_3() {
		String dateAsString = "01/02/2015";
		
		data.put(Constants.MOST_RECENT_PAYDATE, dateAsString);
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, "1");
		
		PaycheckUtils sut = new PaycheckUtils(data);

		Date date = sut.getMostRecentPaydate(data);
		
		int val = sut.getNumberOfPaychecks(data, date);
		
		assertTrue(val == 3);
	}

	@Test
	public void testGetNumberOfPaychecks_expect_3_givenADateAfterMRPDate() {
		String dateAsString = "12/05/2014";
		
		data.put(Constants.MOST_RECENT_PAYDATE, dateAsString);
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, "1");
		
		PaycheckUtils sut = new PaycheckUtils(data);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		
		try {
			date = sdf.parse("1/2/2015");
		} catch (ParseException pe) {
			fail("date parse exception");
		}
		
		int val = sut.getNumberOfPaychecks(data, date);
		assertTrue(val == 3);
	}

	@Test
	public void testGetPaycheckNumber() {
		String dateAsString = "12/05/2014";
		
		String mrp = dateAsString; 
		String mrppn = "1"; 
		
		data.put(Constants.MOST_RECENT_PAYDATE, dateAsString);
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, mrppn);
		
		PaycheckUtils sut = new PaycheckUtils(data);
		Date dateDecemberFifth2014 = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			dateDecemberFifth2014 = sdf.parse(mrp);
		}
		catch (ParseException pe) {
			fail();
		}
		
		// if we pass in the same date as the MRP, the MRPPN should be the same
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateDecemberFifth2014); 
				
		assertTrue(sut.getPaycheckNumber(data, dateDecemberFifth2014) == 1);
		
		// if we pass in a date 5 days shorter than the period length, the MRPPN should be the same
		final int PERIOD_LENGTH = 14;
		cal.setTime(dateDecemberFifth2014);		
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH - 5);
		
		assertTrue(sut.getPaycheckNumber(data, cal.getTime()) == 1);
		
		// if we pass in a date equal to the MRP + the period length, the MRPPN should be the next value
		cal.setTime(dateDecemberFifth2014);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		
		assertTrue(sut.getPaycheckNumber(data, cal.getTime()) == 2);
		
		// if we pass in a date equal to the MRP + two period length minus one day, the MRPPN is the next value 
		cal.setTime(dateDecemberFifth2014);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		assertTrue(sut.getPaycheckNumber(data, cal.getTime()) == 2);
		
		// test that if 12/05/14 is our start date, and period lengths are 14 days, and the given date is Jan 30
		//  that that counts as the third paycheck number of the month
		cal.setTime(dateDecemberFifth2014);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		assertTrue(sut.getPaycheckNumber(data, cal.getTime()) == 3);
		
		// ...and the one after that, feb 13, is the first paycheck of the month
		cal.add(Calendar.DAY_OF_MONTH, PERIOD_LENGTH);
		assertTrue(sut.getPaycheckNumber(data, cal.getTime()) == 1);
	}
}
