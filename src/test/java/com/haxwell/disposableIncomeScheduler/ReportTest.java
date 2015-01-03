package com.haxwell.disposableIncomeScheduler;

import java.util.List;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.report.CommandList;
import com.haxwell.disposableIncomeScheduler.report.ReportGenerator;
import com.haxwell.disposableIncomeScheduler.report.ReportScreenPrinter;

public class ReportTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}
	
	@Test
	public void testReport1() {
		ReportGenerator rg = new ReportGenerator();
		
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
		
		CommandList cl = rg.getReportData(data, state);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		Println println = new Println();
		
		rsp.print(println);
	}

	@Test
	public void testReport2() {
		ReportGenerator rg = new ReportGenerator();
		
		CommandList cl = rg.getReportData(data, state);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		Println println = new Println();
		
		rsp.print(println);
	}
}
