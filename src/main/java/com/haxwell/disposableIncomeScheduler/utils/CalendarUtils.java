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
		
		if (data != null && data.containsKey(Constants.TESTING_OVERRIDE_DATE_JSON)) 
			rtn = getCalendar(data.get(Constants.TESTING_OVERRIDE_DATE_JSON).toString());
		
		return rtn;
	}
	
	public static Calendar getCalendar(String mmddyyyy) {
		Calendar rtn = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date parse = sdf.parse(mmddyyyy);
			rtn = Calendar.getInstance();
			rtn.setTime(parse);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return rtn;
	}
	
	public static String getCalendarAsMMDDYYYY(Calendar cal) {
		String str = cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
		return str;
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
