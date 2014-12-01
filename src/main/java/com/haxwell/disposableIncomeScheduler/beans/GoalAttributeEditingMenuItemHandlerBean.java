package com.haxwell.disposableIncomeScheduler.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.haxwell.disposableIncomeScheduler.Constants;

public class GoalAttributeEditingMenuItemHandlerBean extends MenuItemHandlerBean {

	protected Map<String, String> getMapOfDisplayNamesToJSONFieldNames() {
		Map<String, String> map = new HashMap<>();
		
		map.put(Constants.DESCRIPTION, Constants.DESCRIPTION_JSON);
		map.put(Constants.PRICE, Constants.PRICE_JSON);
		map.put(Constants.PREVIOUS_SAVED_AMT, Constants.PREVIOUS_SAVED_AMT_JSON);
		map.put(Constants.HAPPINESS_IMMEDIACY, Constants.HAPPINESS_IMMEDIACY_JSON);
		map.put(Constants.UTILITY_IMMEDIACY, Constants.UTILITY_IMMEDIACY_JSON);
		map.put(Constants.HAPPINESS_LENGTH, Constants.HAPPINESS_LENGTH_JSON);
		map.put(Constants.UTILITY_LENGTH, Constants.UTILITY_LENGTH_JSON);
		map.put(Constants.DATE_NEEDED, Constants.DATE_NEEDED_JSON);
		
		return map;
	}
	
	protected Map<String, Validator> getValidatorMap() {
		Map<String, Validator> validatorMap = new HashMap<>();
		validatorMap.put(Constants.DESCRIPTION, new Validator());
		validatorMap.put(Constants.PRICE, new PriceValidator());
		validatorMap.put(Constants.PREVIOUS_SAVED_AMT, new PriceValidator());
		validatorMap.put(Constants.HAPPINESS_IMMEDIACY, new HappinessUtilityValueValidator());
		validatorMap.put(Constants.UTILITY_IMMEDIACY, new HappinessUtilityValueValidator());
		validatorMap.put(Constants.HAPPINESS_LENGTH, new HappinessUtilityValueValidator());
		validatorMap.put(Constants.UTILITY_LENGTH, new HappinessUtilityValueValidator());
		validatorMap.put(Constants.DATE_NEEDED, new DateNeededValueValidator());
		return validatorMap;
	}

	protected LinkedList<String> getListOfKeys() {
		LinkedList<String> keys = new LinkedList<>();
		keys.add(Constants.DESCRIPTION);
		keys.add(Constants.PRICE);
		keys.add(Constants.PREVIOUS_SAVED_AMT);
		keys.add(Constants.HAPPINESS_IMMEDIACY);
		keys.add(Constants.UTILITY_IMMEDIACY);
		keys.add(Constants.HAPPINESS_LENGTH);
		keys.add(Constants.UTILITY_LENGTH);
		keys.add(Constants.DATE_NEEDED);
		return keys;
	}
	
	protected class Validator {
		public String getValidValue(String str) {
			return str;
		}
	}
	
	protected class PriceValidator extends Validator {
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
	}
	
	protected class HappinessUtilityValueValidator extends Validator {
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
	}

	protected class DateNeededValueValidator extends Validator {
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
	}
}
