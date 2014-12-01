package com.haxwell.disposableIncomeScheduler;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class Main {
	
	public static void main(String[] args) {

		/**
		 * WILO: Need to add functionality for Expenses, and Short Term Goals.. Expenses are items paid monthly. There should be a 
		 * title and price for each one. STGs, have a title, amount saved per month, and total amt saved. Need menu options to add 
		 * an expense, remove an expense, add an STG, edit an STG, remove an STG, and reset an STG (when its money is spent, but the STG should
		 * stay for next month).
		 * 
		 * Need to add functionality to list a report, which should take into account the date, and therefore which check of the month
		 * has been deposited already. It should show how much of the money in the account is claimed by each expense and STG, and 
		 * how the remaining money is spread over the long term goals. 
		 * 
		 * Need to add a starting date to the JSON file, a date of first paycheck, so a utility can then determine when the next check
		 * is, given the amtPerPeriod variable, and therefore which check of the month has already been deposited, given the current date.
		 * 
		 */
		
		JSONObject state = new JSONObject();
		JSONObject obj = DataFileManager.read(args[0]); // arg[0] = path to json data file
		
		MenuItemUtils.initializeState(state);
		
		boolean changes = Processor.process(obj, state);
		
		if (changes) {
			System.out.println();
			System.out.println("You made changes. Type 'yes' to save them.");
			String val = System.console().readLine();
			
			if (val.toLowerCase().equals("yes")) {
				DataFileManager.write(args[0], obj);
			}
		}

		boolean rtn = ("true".endsWith("dsfad"));
	}
}
