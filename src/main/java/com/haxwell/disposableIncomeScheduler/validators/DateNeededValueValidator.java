package com.haxwell.disposableIncomeScheduler.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;

public class DateNeededValueValidator extends Validator {
	public String getValidValue(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		try {
			Date parse = sdf.parse(str);

			Calendar cal = CalendarUtils.getCurrentCalendar();
			if (parse.before(cal.getTime()))
				str = "";

		} catch (ParseException e) {
			str = "";
		}

		return str;
	}

	/**
	 * Return true if string is either (1. Not Null, Not empty, and parseable) OR (2. Not Null, empty)
	 */
	public boolean isValidValue(String str) {
		boolean rtn = (str != null);

		if (str != null && !str.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

			try {
				Date parse = sdf.parse(str);

				Calendar cal = CalendarUtils.getCurrentCalendar();
				if (parse.before(cal.getTime())) {
					rtn = false;
					System.out.println("Not Valid Value!");
				}

			} catch (ParseException e) {
				rtn = false;
			}
		}

		return rtn;
	}
}
