package com.haxwell.disposableIncomeScheduler;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class CalculatorTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void getDollarAmountsToBeAppliedToShortTermGoals_PreApplyingFundsForThisPeriod() {
		Map<String, Double> expectedValues = new HashMap<String, Double>();
		
		expectedValues.put("clothing", 75.0);
		expectedValues.put("gas", 120.0);
		expectedValues.put("groceries", 150.0);
		
		Map<String, Double> map = Calculator.getDollarAmountsToBeAppliedToShortTermGoals(data, state);
		
		assertTrue(map.size() == 3);
		
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		for (int i = 0; i < stgs.size(); i++) {
			JSONObject obj = (JSONObject) stgs.get(i);
			
			String desc = obj.get(Constants.DESCRIPTION_JSON)+"";
			assertTrue(map.containsKey(desc));
			
			Double d = map.get(desc);
			assertTrue(expectedValues.get(desc).equals(d));
		}
	}

	@Test
	public void getDollarAmountsToBeAppliedToShortTermGoals_PreApplyingFundsForThisPeriod_WithPreviousAmtSaved() {
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		JSONObject stg = getJSONObjectByDescriptionFromJSONArray(stgs, "clothing");
		stg.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "75");

		Map<String, Double> expectedValues = new HashMap<String, Double>();
		
		expectedValues.put("clothing", 150.0);
		expectedValues.put("gas", 120.0);
		expectedValues.put("groceries", 150.0);
		
		Map<String, Double> map = Calculator.getDollarAmountsToBeAppliedToShortTermGoals(data, state);
		
		assertTrue(map.size() == 3);
		
		for (int i = 0; i < stgs.size(); i++) {
			JSONObject obj = (JSONObject) stgs.get(i);
			
			String desc = obj.get(Constants.DESCRIPTION_JSON)+"";
			assertTrue(map.containsKey(desc));
			
			Double d = map.get(desc);
			assertTrue(expectedValues.get(desc).equals(d));
		}
	}

	@Test
	public void getDollarAmountsToBeAppliedToShortTermGoals_PostApplyingFundsForThisPeriod() {
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);
		JSONObject stg = getJSONObjectByDescriptionFromJSONArray(stgs, "clothing");
		stg.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "75");

		Map<String, Double> expectedValues = new HashMap<String, Double>();
		
		expectedValues.put("clothing", 75.0);
		expectedValues.put("gas", 120.0);
		expectedValues.put("groceries", 150.0);
		
		state.put(Constants.PERIODIC_AMT_HAS_BEEN_APPLIED, "feezlelydoo");
		
		Map<String, Double> map = Calculator.getDollarAmountsToBeAppliedToShortTermGoals(data, state);
		
		assertTrue(map.size() == 3);
		
		for (int i = 0; i < stgs.size(); i++) {
			JSONObject obj = (JSONObject) stgs.get(i);
			
			String desc = obj.get(Constants.DESCRIPTION_JSON)+"";
			assertTrue(map.containsKey(desc));
			
			Double d = map.get(desc);
			assertTrue(expectedValues.get(desc).equals(d));
		}
	}

	@Test
	public void testApplyMoneyToLongTermGoals() {
		data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, "2000");
		
		long totalDollarAmount = 2400;
		// apply the money once.. with the default values in 'data', the 'Garage Door' goal should have more than it needs saved.
		Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);
		
		initializeState_Outside();
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<JSONObject> goals = MenuItemUtils.getGoalsOfAGroup(arr);
		long price = -1;
		long prevSavedAmt = -1;
		
		for (JSONObject obj : goals) {
			if (obj.containsKey(strOutside_garage)) {
				price = Long.parseLong(obj.get(Constants.PRICE_JSON)+"");
				prevSavedAmt = Long.parseLong(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON)+"");
				
				assertTrue(prevSavedAmt > price);
			}
		}
		
		// apply the money again.. the 'Garage Door' goal should have the same amount as before.. no money should be applied to it
		//  once a moneyApplication has put it over the top.
		Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);
		
		arr = MenuItemUtils.getSelectedGroup(data, state);
		goals = MenuItemUtils.getGoalsOfAGroup(arr);
		
		for (JSONObject obj : goals) {
			if (obj.containsKey(strOutside_garage)) {
				assertTrue(prevSavedAmt == Long.parseLong(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON)+""));
			}
		}
	}
	
}
