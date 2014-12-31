package com.haxwell.disposableIncomeScheduler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;

public class CalendarUtils {

	public static Calendar getCurrentCalendar() {
		Calendar rtn = Calendar.getInstance();
		
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		
		if (data != null && data.containsKey(Constants.TESTING_OVERRIDE_DATE_JSON)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				rtn.setTime(sdf.parse(data.get(Constants.TESTING_OVERRIDE_DATE_JSON).toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return rtn;
	}
	
	public static Calendar getCalendar(Date date) {
		Calendar rtn = Calendar.getInstance();
		rtn.setTime(date);
		
		return rtn;
	}
	
	public static void advanceCalendarByPeriodLength(Calendar cal) {
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		JSONObject data = dass.getData();
		int periodLength = Integer.parseInt(data.get(Constants.PERIOD_LENGTH_JSON)+"");

		cal.add(Calendar.DAY_OF_MONTH, periodLength);
	}
}
