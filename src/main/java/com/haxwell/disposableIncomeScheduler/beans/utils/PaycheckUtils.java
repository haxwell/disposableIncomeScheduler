package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;

public class PaycheckUtils {
	
	int[] paycheckNumberArray = null;
	
	public PaycheckUtils(JSONObject data) {
		paycheckNumberArray = getPaycheckNumberArray(data);
	}
	
	/**
	 * Given a date, returns the number of paychecks in that month.
	 */
	public int getNumberOfPaychecks(JSONObject data, Date date) {
		int rtn = -1;
		Date mrpDate = getMostRecentPaydate(data);
		
		if (date.equals(mrpDate) || date.after(mrpDate)) {
			long diff = date.getTime() - mrpDate.getTime();
			
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
	public int getPaycheckNumber(JSONObject data, Date date) {
		int rtn = -1;
		Date mrpDate = getMostRecentPaydate(data);
		
		if (date.equals(mrpDate) || date.after(mrpDate)) {
			long diff = date.getTime() - mrpDate.getTime();
			
			diff /= 1000 * 60 * 60 * 24;
			
			return paycheckNumberArray[(int)diff];
		}
		
		return rtn;
	}
	
	public String getPaycheckNumberAsString(JSONObject data, Date date) {
		int pn = getPaycheckNumber(data, date);
		String rtn = "";
		
		if (pn == 1) rtn = "1st";
		if (pn == 2) rtn = "2nd";
		if (pn == 3) rtn = "3rd";
		
		return rtn;
	}
	
	public Date getMostRecentPaydate(JSONObject data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date rtn = null;
		
		try {
			rtn = sdf.parse(data.get(Constants.MOST_RECENT_PAYDATE)+"");
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		return rtn; 
	}
	
	private int[] getPaycheckNumberArray(JSONObject data) {
		final int ARRAY_SIZE = 365;
		int[] rtn = null;
		Calendar cal = CalendarUtils.getCurrentCalendar();
		Date date = getMostRecentPaydate(data);
		
		cal.setTime(date);
 
		if (cal != null) {
			rtn = new int[ARRAY_SIZE];
			int periodLength = Integer.parseInt(data.get(Constants.PERIOD_LENGTH_JSON)+"");
			int periodCounter = 0;
			int paycheckNumber = Integer.parseInt(data.get(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER)+"");
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
		}
		
		return rtn;
	}
}