package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class PaycheckUtils {
	
	// Given date, get the number of the most recent paycheck in the month.
	//
	// So if the first paycheck date is Jan 2 2015, and the given date is
	//  Jan 20 2015, this would return 2, because the second check was on
	//  Jan 16 2015.
	public static int getPaycheckNumber(JSONObject data) {
		int[] arr = getPaycheckNumberArray(data);
		
		return -1;
	}
	
	public static int[] getPaycheckNumberArray(JSONObject data) {
		final int ARRAY_SIZE = 365;
		int[] rtn = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = null;
		
		try {
			Date date = sdf.parse(data.get(Constants.MOST_RECENT_PAYDATE)+"");
			
			cal = Calendar.getInstance();
			cal.setTime(date);
 
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
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