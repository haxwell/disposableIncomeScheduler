package com.haxwell.disposableIncomeScheduler.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateNeededValueValidator extends Validator {
	public String getValidValue(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		try {
			Date parse = sdf.parse(str);
			
			Calendar cal = Calendar.getInstance();
			if (parse.before(cal.getTime()))
				str = "";
			
		} catch (ParseException e) {
			str = "";
		}
		
		return str;
	}
	
	public boolean isValidValue(String str) {
		boolean rtn = true;
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		try {
			Date parse = sdf.parse(str);
			
			Calendar cal = Calendar.getInstance();
			if (parse.before(cal.getTime()))
				rtn = false;
			
		} catch (ParseException e) {
			rtn = false;
		}
		
		return rtn;
	}
}
