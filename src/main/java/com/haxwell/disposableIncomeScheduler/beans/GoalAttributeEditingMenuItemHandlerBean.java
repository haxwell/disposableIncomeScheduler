package com.haxwell.disposableIncomeScheduler.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.validators.DateNeededValueValidator;
import com.haxwell.disposableIncomeScheduler.validators.HappinessUtilityValueValidator;
import com.haxwell.disposableIncomeScheduler.validators.PositiveIntegerValidator;
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
		validatorMap.put(Constants.PREVIOUS_SAVED_AMT, new PositiveIntegerValidator());
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

	// NOTE: Gave myself a pass this time, but future self, I done sold you down
	// the river! If the need arises
	// to add another map of the KEYS to some attribute, future self, you gotta
	// think of a better solution, better
	// than adding another method with another map. You're welcome. - You
	// 3/12/2016 self.

	/**
	 * Returns a map of all the keys that exist that cannot otherwise be
	 * divined/calculated/overlooked.
	 * 
	 * @return
	 */
	protected Map<String, Boolean> getIsRequiredKeys() {
		Map<String, Boolean> rtn = new HashMap<String, Boolean>();

		rtn.put(Constants.DESCRIPTION, true);
		rtn.put(Constants.PRICE, true);

		return rtn;
	}
}
