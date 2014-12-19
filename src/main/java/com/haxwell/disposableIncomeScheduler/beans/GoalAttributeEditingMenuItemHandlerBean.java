package com.haxwell.disposableIncomeScheduler.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.validators.DateNeededValueValidator;
import com.haxwell.disposableIncomeScheduler.validators.HappinessUtilityValueValidator;
import com.haxwell.disposableIncomeScheduler.validators.PriceValidator;
import com.haxwell.disposableIncomeScheduler.validators.Validator;

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
}
