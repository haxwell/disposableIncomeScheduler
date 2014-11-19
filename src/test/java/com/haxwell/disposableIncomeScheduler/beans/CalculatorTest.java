package com.haxwell.disposableIncomeScheduler.beans;

import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Calculator;

public class CalculatorTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testCalculateWeights() {
		JSONObject weights = Calculator.getWeights(data);
		
		assertTrue(weights.size() > 0);
	}
}
