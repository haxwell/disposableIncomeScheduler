package com.haxwell.disposableIncomeScheduler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.beans.Println;
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
	public void testReport() {
		ReportGenerator rg = new ReportGenerator();
		
		CommandList cl = rg.getReportData(data);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		Println println = new Println();
		
		rsp.print(println);
	}
}
