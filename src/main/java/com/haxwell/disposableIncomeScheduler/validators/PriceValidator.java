package com.haxwell.disposableIncomeScheduler.validators;

public class PriceValidator extends Validator {
	
	public String getValidValue(String str) {
		try {
			int i = Integer.parseInt(str);

			if (i < 1)
				str ="1";
		}
		catch (NumberFormatException nfe)
		{
			str = "1";
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
