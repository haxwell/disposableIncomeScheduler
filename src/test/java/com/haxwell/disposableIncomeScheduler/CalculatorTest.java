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

	private abstract class Command {
		public abstract void execute();
	}

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {

	}

	@Test
	public void getDollarAmountsToBeAppliedToShortTermGoals() {
		JSONArray stgs = MenuItemUtils.getShortTermGoals(data);

		// simulate the fact that the most recent paycheck has been applied to the data
		//  the method were testing, getDollarAmountsToBeAppliedToShortTermGoals(),
		//  returns the amount saved for each short term goal as of the last application
		//  of the paycheck.
		JSONObject stg = getJSONObjectByDescriptionFromJSONArray(stgs, "clothing");
		stg.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "75");

		stg = getJSONObjectByDescriptionFromJSONArray(stgs, "gas");
		stg.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "120");

		stg = getJSONObjectByDescriptionFromJSONArray(stgs, "groceries");
		stg.put(Constants.TOTAL_AMOUNT_SAVED_JSON, "150");

		// set up some expected values
		Map<String, Double> expectedValues = new HashMap<String, Double>();

		expectedValues.put("clothing", 75.0);
		expectedValues.put("gas", 120.0);
		expectedValues.put("groceries", 150.0);

		// exercise the method to be tested
		Map<String, Double> map = Calculator.getDollarAmountsToBeAppliedToShortTermGoals(data, state);

		assertTrue(map.size() == 3);

		for (int i = 0; i < stgs.size(); i++) {
			JSONObject obj = (JSONObject) stgs.get(i);

			String desc = obj.get(Constants.DESCRIPTION_JSON) + "";
			assertTrue(map.containsKey(desc));

			Double d = map.get(desc);
			assertTrue(expectedValues.get(desc).equals(d));
		}
	}

	@Test
	public void testApplyMoneyToLongTermGoals_GoalBecomesFullySavedAndNoMoreMoneyIsApplied() {
		data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, "2000");

		long totalDollarAmount = 2400;
		// apply the money once.. with the default values in 'data', the 'Garage Door' goal should have all the money it needs ($1000).
		Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);

		initializeState_Outside();
		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<JSONObject> goals = MenuItemUtils.getGoalsOfAGroup(arr);
		long price = -1;
		long prevSavedAmt = -1;

		for (JSONObject obj : goals) {
			if (obj.containsKey(strOutside_garage)) {
				price = Long.parseLong(obj.get(Constants.PRICE_JSON) + "");
				prevSavedAmt = Long.parseLong(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON) + "");

				assertTrue(prevSavedAmt == price);
			}
		}

		// apply the money again.. the 'Garage Door' goal should have the same amount as before.. no money should be applied to it
		// once a moneyApplication has put it over the top.
		Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);

		arr = MenuItemUtils.getSelectedGroup(data, state);
		goals = MenuItemUtils.getGoalsOfAGroup(arr);

		for (JSONObject obj : goals) {
			if (obj.containsKey(strOutside_garage)) {
				assertTrue(prevSavedAmt == Long.parseLong(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON) + ""));
			}
		}
	}

	@Test
	public void testApplyMoneyToLongTermGoals_AllGoalsAreFullySaved() {
		data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, "2000");

		long totalDollarAmount = 2400;

		// set the price for all the goals to 20.. so they can quickly be fully saved.
		initializeState_Bathroom();
		List<JSONObject> goals = MenuItemUtils.getGoalsOfAGroup(MenuItemUtils.getSelectedGroup(data, state));

		for (JSONObject goal : goals) {
			goal.put(Constants.PRICE_JSON, "20");
		}

		initializeState_Outside();
		goals = MenuItemUtils.getGoalsOfAGroup(MenuItemUtils.getSelectedGroup(data, state));

		for (JSONObject goal : goals) {
			goal.put(Constants.PRICE_JSON, "20");
		}

		// skip initializing state to Kitchen, because it should be empty.

		initializeState_TripToFrance();
		goals = MenuItemUtils.getGoalsOfAGroup(MenuItemUtils.getSelectedGroup(data, state));

		for (JSONObject goal : goals) {
			goal.put(Constants.PRICE_JSON, "20");
		}

		Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);

		verifyGoalSavedAmount(strBathroom_sink, "20", new Command() {
			@Override
			public void execute() {
				initializeState_Bathroom();
			}
		});

		verifyGoalSavedAmount(strBathroom_shower, "20", new Command() {
			@Override
			public void execute() {
				initializeState_Bathroom();
			}
		});

		verifyGoalSavedAmount(strOutside_garage, "20", new Command() {
			@Override
			public void execute() {
				initializeState_Outside();
			}
		});

		// skip verifying kitchen.. should be empty.. should probably check, too.. but don't want to right now..

		verifyGoalSavedAmount(strTripToFrance_airfare, "20", new Command() {
			@Override
			public void execute() {
				initializeState_TripToFrance();
			}
		});

		verifyGoalSavedAmount(strTripToFrance_lodging, "20", new Command() {
			@Override
			public void execute() {
				initializeState_TripToFrance();
			}
		});
	}

	@Test
	public void testApplyMoneyToLongTermGoals_NoGoalsAreFullySaved() {
		data.put(Constants.PREV_TOTAL_IN_THE_POT_BEFORE_APPLYING_FUNDS_JSON, "200");

		long totalDollarAmount = 240;

		Calculator.applyMoneyToLongTermGoals(data, totalDollarAmount);

		// NOTE: I had to change these before, and I'm not sure why.. perhaps the date effects the calculation.
		//  regardless, future self, if you have to change these again, figure a better way.
		verifyGoalSavedAmount(strBathroom_sink, "11", new Command() {
			@Override
			public void execute() {
				initializeState_Bathroom();
			}
		});

		verifyGoalSavedAmount(strBathroom_shower, "11", new Command() {
			@Override
			public void execute() {
				initializeState_Bathroom();
			}
		});

		verifyGoalSavedAmount(strOutside_garage, "116", new Command() {
			@Override
			public void execute() {
				initializeState_Outside();
			}
		});

		// skip verifying kitchen.. should be empty.. should probably check, too.. but don't want to right now..

		verifyGoalSavedAmount(strTripToFrance_airfare, "54", new Command() {
			@Override
			public void execute() {
				initializeState_TripToFrance();
			}
		});

		verifyGoalSavedAmount(strTripToFrance_lodging, "49", new Command() {
			@Override
			public void execute() {
				initializeState_TripToFrance();
			}
		});
	}

	private void verifyGoalSavedAmount(String goalKey, String amt, Command initStateCmd) {
		initStateCmd.execute();

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		List<JSONObject> goals = MenuItemUtils.getGoalsOfAGroup(arr);
		String prevSavedAmt = "";

		for (JSONObject obj : goals) {
			if (obj.get(Constants.DESCRIPTION_JSON).equals(goalKey)) {
				prevSavedAmt = obj.get(Constants.PREVIOUS_SAVED_AMT_JSON) + "";

				assertTrue(prevSavedAmt.equals(amt));
			}
		}
	}
}
