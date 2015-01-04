package com.haxwell.disposableIncomeScheduler.validators;

public class PositiveIntegerValidator extends Validator {
	
	public String getValidValue(String str) {
		try {
			int i = Integer.parseInt(str);

			if (i < 0)
				str ="0";
		}
		catch (NumberFormatException nfe)
		{
			str = "0";
		}
		
		return str;
	}
	
	public boolean isValidValue(String str) {
		boolean rtn = true;
		
		try {
			Integer.parseInt(str);
		}
		catch (NumberFormatException nfe) {
			rtn = false;
		}
		
		return rtn;
	}
}
