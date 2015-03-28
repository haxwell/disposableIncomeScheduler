package com.haxwell.disposableIncomeScheduler.beans;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;
import com.haxwell.disposableIncomeScheduler.Println;
import com.haxwell.disposableIncomeScheduler.report.CommandList;
import com.haxwell.disposableIncomeScheduler.report.ReportGenerator;
import com.haxwell.disposableIncomeScheduler.report.ReportScreenPrinter;

public class ApplyPaycheckToExpensesAndGoalsMenuItemHandlerTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}

	@Test
	public void foo() {
		createDataAndStateObjects("/home/jjames/haxSVN/trunk/projects/disposableIncomeScheduler/./src/main/resources/testData.json");
		
		ApplyPaycheckToExpensesAndGoalsMenuItemHandler aph = new ApplyPaycheckToExpensesAndGoalsMenuItemHandler();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("yes", "","","","","","","","","");
		
		aph.setInputGetter(mockedInputGetter);

		aph.doIt(data, state);
		
		/// 
		ReportGenerator rg = new ReportGenerator();
		
		CommandList cl = rg.getReportData(data, state);
		
		ReportScreenPrinter rsp = new ReportScreenPrinter(cl);
		
		Println println = new Println();
		
		rsp.print(println);
	}
}
