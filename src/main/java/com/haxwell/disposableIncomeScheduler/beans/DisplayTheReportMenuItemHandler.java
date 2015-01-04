package com.haxwell.disposableIncomeScheduler.beans;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.report.CommandList;
import com.haxwell.disposableIncomeScheduler.report.ReportGenerator;
import com.haxwell.disposableIncomeScheduler.report.ReportScreenPrinter;

public class DisplayTheReportMenuItemHandler extends GoalAttributeEditingMenuItemHandlerBean {

	public String getMenuText() {
		return "Display the Report";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		
		ReportGenerator rg = new ReportGenerator();
		
		CommandList cl = rg.getReportData(data, state);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		getPrintlner().println("");
		
		rsp.print(getPrintlner());
		
		getPrintlner().println("Press Enter To Continue...");
		getInputGetter().readInput();
		
		return false;
	}
}
