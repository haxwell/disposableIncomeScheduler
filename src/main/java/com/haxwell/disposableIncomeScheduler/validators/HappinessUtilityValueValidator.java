package com.haxwell.disposableIncomeScheduler.validators;

public class HappinessUtilityValueValidator extends Validator {
	public String getValidValue(String str) {
		String rtn = str; 
		
		try {
			int i = Integer.parseInt(str);
			
			if (i < 1) rtn = "1";
			else if (i > 25) rtn = "25";
		}
		catch (NumberFormatException nfe) {
			rtn = "1";
		}
		
		return rtn;
	}
	
	public boolean isValidValue(String str) {
		boolean rtn = true;
		
		try {
			int i = Integer.parseInt(str);
			
			if (i < 1) rtn = false;
			else if (i > 25) rtn = false;
		}
		catch (NumberFormatException nfe) {
			rtn = false;
		}
		
		return rtn;
	}
}
