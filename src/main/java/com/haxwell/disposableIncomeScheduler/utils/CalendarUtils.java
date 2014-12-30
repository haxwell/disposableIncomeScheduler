package com.haxwell.disposableIncomeScheduler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.haxwell.disposableIncomeScheduler.Constants;

import net.minidev.json.JSONObject;

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
}
