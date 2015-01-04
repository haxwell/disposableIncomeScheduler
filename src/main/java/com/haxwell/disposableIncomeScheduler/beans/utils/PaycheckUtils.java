package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class PaycheckUtils {
	
	int[] paycheckNumberArray = null;
	Date paycheckNumberArrayBeginDate = null;
	
	public PaycheckUtils(JSONObject data) {
		paycheckNumberArray = getPaycheckNumberArray(data);
	}
	
	/**
	 * Given a date, returns the number of paychecks in that month.
	 */
	public int getNumberOfPaychecks(Date date) {
		int rtn = -1;
		
		if (date.equals(paycheckNumberArrayBeginDate) || date.after(paycheckNumberArrayBeginDate)) {
			long diff = date.getTime() - paycheckNumberArrayBeginDate.getTime();
			
			diff /= 1000 * 60 * 60 * 24;
			
			// get the paycheck number of the given date
			int i = paycheckNumberArray[(int)diff];
			
			// now iterate through the paycheckNumberArray until the
			//  number changes, and then we hit '1' again. The number
			//  before we hit '1' is the number of paychecks in that
			//  month.
			boolean weAreIteratingInTheCurrentMonth = true;
			boolean iteratedPastOneAlready = (i > 1);
			int idx = 1;
			int max = i;
			while (weAreIteratingInTheCurrentMonth) {
				int x = paycheckNumberArray[((int)diff) + idx++];
				
				if (x == 1 && iteratedPastOneAlready)
					weAreIteratingInTheCurrentMonth = false;
				
				if (x > 1)
					iteratedPastOneAlready = true;
				
				if (x > max)
					max = x;
			}
			
			rtn = max;
		}
		
		return rtn;
	}
	
	/**
	 * Given a date, returns the number of the paycheck in the month that the date is being covered by
	 */
	public int getPaycheckNumber(Date date) {
		int rtn = -1;
		
		if (date.equals(paycheckNumberArrayBeginDate) || date.after(paycheckNumberArrayBeginDate)) {
			long diff = date.getTime() - paycheckNumberArrayBeginDate.getTime();
			
			diff /= 1000 * 60 * 60 * 24;
			
			rtn = paycheckNumberArray[(int)diff];
		}
		
		return rtn;
	}
	
	public int getNextPaycheckNumber(Date date) {
		Calendar cal = CalendarUtils.getCalendar(date);
		CalendarUtils.advanceCalendarByPeriodLength(cal);
		
		return getPaycheckNumber(cal.getTime());
	}
	
	public String getPaycheckNumberAsString(Date date) {
		int pn = getPaycheckNumber(date);
		String rtn = "";
		
		if (pn == 1) rtn = "1st";
		if (pn == 2) rtn = "2nd";
		if (pn == 3) rtn = "3rd";
		
		return rtn;
	}
	
	public static Date getFuturePaydate(JSONObject data, int periodsAhead) {
		Date d = getMostRecentPaydate(data);
		Calendar cal = CalendarUtils.getCurrentCalendar();
		cal.setTime(d);
		
		for (int i = 0; i < periodsAhead; i++)
			CalendarUtils.advanceCalendarByPeriodLength(cal);
		
		return cal.getTime();
	}
	
	public static Date getMostRecentPaydate(JSONObject data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date rtn = null;
		
		try {
			rtn = sdf.parse(data.get(Constants.MOST_RECENT_PAYDATE)+"");
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		return rtn; 
	}
	
	public void advanceMostRecentPaydateByOnePeriod(JSONObject data) {
		Calendar cal = CalendarUtils.getCurrentCalendar();
		cal.setTime(getMostRecentPaydate(data));

		CalendarUtils.advanceCalendarByPeriodLength(cal);
//		int periodLength = Integer.parseInt(data.get(Constants.PERIOD_LENGTH_JSON)+"");
//		cal.add(Calendar.DAY_OF_MONTH, periodLength);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		data.put(Constants.MOST_RECENT_PAYDATE, sdf.format(cal.getTime()));
		data.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, getPaycheckNumber(cal.getTime()));
	}
	
	private int getPeriodLength() {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		
		JSONObject data = dass.getData();
		
		return Integer.parseInt(data.get(Constants.PERIOD_LENGTH_JSON)+"");
	}
	
	private int getMostRecentPaydatePeriodNumber() {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		
		JSONObject data = dass.getData();
		
		return Integer.parseInt(data.get(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER)+"");
	}

	private int[] getPaycheckNumberArray(JSONObject data) {
		final int ARRAY_SIZE = 365;
		int[] rtn = null;
		Calendar cal = CalendarUtils.getCurrentCalendar();
		paycheckNumberArrayBeginDate = getMostRecentPaydate(data);
		
		cal.setTime(paycheckNumberArrayBeginDate);
 
		rtn = new int[ARRAY_SIZE];
		int periodLength = getPeriodLength();
		int periodCounter = 0;
		int paycheckNumber = getMostRecentPaydatePeriodNumber();
		int month = cal.get(Calendar.MONTH);
		
		for (int i = 0; i < ARRAY_SIZE; i++) {
			rtn[i] = paycheckNumber;
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
			
			if (++periodCounter >= periodLength) {
				periodCounter = 0;
				
				int _month = cal.get(Calendar.MONTH);
				
				if (_month != month) {
					month = _month;
					paycheckNumber = 0;
				}

				paycheckNumber++;
			}
		}
		
		return rtn;
	}
}