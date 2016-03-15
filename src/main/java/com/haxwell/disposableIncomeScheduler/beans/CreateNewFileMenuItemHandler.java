package com.haxwell.disposableIncomeScheduler.beans;

import java.util.Calendar;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.StringUtil;
import com.haxwell.disposableIncomeScheduler.utils.CalendarUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class CreateNewFileMenuItemHandler extends MenuItemHandlerBean {

	private String paycheckNumber;
	private String initialBalance;
	private String amountPaidPerPeriod;
	private String rainyDayAmount;
	private String mostRecentPaycheckDateAsMMDDYYY;
	private String payPeriodLength;

	public boolean doIt(JSONObject data, JSONObject state) {

		System.out.println("You didn't start the application by telling me about a previously saved data file,");
		System.out.println("so I will assume you need a new one created. Actually, you can't use the program");
		System.out.println("anyway, until we create one. so.. let's get started.");
		System.out.println("");

		// ask them data questions
		String str;
		do {
			str = collectParameters();

			System.out.println("");
		} while (!str.equals("") && !str.toUpperCase().equals("Y"));

		JSONObject obj = new JSONObject();

		obj.put(Constants.MOST_RECENT_PAYDATE, mostRecentPaycheckDateAsMMDDYYY);
		obj.put(Constants.PERIOD_LENGTH_JSON, payPeriodLength);
		obj.put(Constants.AMT_PAID_PER_PERIOD_JSON, amountPaidPerPeriod);
		obj.put(Constants.MOST_RECENT_PAYDATE_PERIOD_NUMBER, paycheckNumber);
		obj.put(Constants.AMT_SAVED_FOR_RAINY_DAY_JSON, rainyDayAmount);
		obj.put(Constants.LONG_TERM_GOALS_JSON, new String[0]);
		obj.put(Constants.SHORT_TERM_GOALS_JSON, new String[0]);
		obj.put(Constants.EXPENSES_JSON, new String[0]);

		obj.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON,
				(Integer.parseInt(initialBalance) - Integer.parseInt(amountPaidPerPeriod)) + "");
		obj.put(Constants.PREV_TOTAL_IN_THE_POT_AFTER_APPLYING_FUNDS_JSON, "0");

		obj.put(Constants.EXPENSES_JSON, new JSONArray());
		obj.put(Constants.LONG_TERM_GOALS_JSON, new JSONArray());
		obj.put(Constants.SHORT_TERM_GOALS_JSON, new JSONArray());

		obj.put(Constants.EXPENSES_HAVE_NEVER_BEEN_APPLIED_JSON, true);

		obj.put(Constants.NUMBER_OF_PAYCHECKS_PROCESSED, "0");

		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		dass.setData(obj);

		return true;
	}

	private String collectParameters() {
		System.out.print("As MM/DD/YYYY, enter date of most recent paycheck: ");

		mostRecentPaycheckDateAsMMDDYYY = getInputGetter().readInput();

		System.out
				.print("Counting the number of paychecks in this month, which number was this most recent paycheck? ");

		paycheckNumber = getInputGetter().readInput();

		System.out.print("How many calendar days long is your pay period? (numbers, no cents) [14]? ");

		payPeriodLength = getInputGetter().readInput();
		if (StringUtil.isNullOrEmpty(payPeriodLength))
			payPeriodLength = "14";

		Calendar cal = CalendarUtils.getCalendar(mostRecentPaycheckDateAsMMDDYYY);
		cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(payPeriodLength));

		System.out.println("\nYour next paycheck would then be on " + CalendarUtils.getCalendarAsMMDDYYYY(cal));

		System.out.print("\n\nWhat's the balance on your account (numbers, no cents)? ");

		initialBalance = getInputGetter().readInput();

		System.out.print("How much is your check per pay period? (numbers, no cents)? ");

		amountPaidPerPeriod = getInputGetter().readInput();

		System.out.print("How much of that is saved for a rainy day, just in case (numbers, no cents)? ");

		rainyDayAmount = getInputGetter().readInput();

		System.out.println("All of that good? [Y/n]");

		return getInputGetter().readInput();
	}
}
