package com.haxwell.disposableIncomeScheduler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.beans.utils.PaycheckUtils;

public class PaycheckUtilsTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void testFoo() {
		PaycheckUtils.getPaycheckNumberArray(data);
	}
}
