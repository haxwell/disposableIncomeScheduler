package com.haxwell.disposableIncomeScheduler.beans;

import com.haxwell.disposableIncomeScheduler.report.CommandList;
import com.haxwell.disposableIncomeScheduler.report.ReportGenerator;
import com.haxwell.disposableIncomeScheduler.report.ReportScreenPrinter;

import net.minidev.json.JSONObject;

public class DisplayTheReportMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

//	private JSONObject data;
	
	public String getMenuText() {
		return "Display the Report";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		ReportGenerator rg = new ReportGenerator();
		
		CommandList cl = rg.getReportData(data, state);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		Println println = new Println();
		println.println("");
		
		rsp.print(println);
		
		return false;
	}
}
