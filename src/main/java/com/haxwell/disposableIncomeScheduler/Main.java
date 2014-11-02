package com.haxwell.disposableIncomeScheduler;

import net.minidev.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public class Main {

	// haven't added the ability to create a file yet.. so if its ever needed the format is:
	// {"periodLength":"14","items":[{"price":"10000","utilityImmediacy":"5","description":"Trip to France","happinessImmediacy":"25","happinessLength":"13","previousSavedAmount":"100","dateNeededBy":"06\/21\/2016","utilityLength":"10"},{"price":"7000","utilityImmediacy":"5","description":"Kitchen remodel","happinessImmediacy":"25","happinessLength":"13","previousSavedAmount":"100","dateNeededBy":"09\/15\/2015","utilityLength":"25"},{"price":"6000","utilityImmediacy":"25","description":"Furnace","happinessImmediacy":"25","happinessLength":"13","previousSavedAmount":"100","dateNeededBy":"09\/15\/2016","utilityLength":"25"}],"amountSavedPerPeriod":"500","totalInThePot":"1000"}
	
	public static void main(String[] args) {
		
		JSONObject obj = DataFileManager.read(args[0]); // arg[0] = path to json data file

		ApplicationContext ctx = new ClassPathXmlApplicationContext("/application-context.xml");
		
		boolean changes = Processor.process((MenuItemHandlerBean)ctx.getBean("MenuItemHandlers"), obj);
		
		if (changes) {
			System.out.println();
			System.out.println("You made changes. Type 'yes' to save them.");
			String val = System.console().readLine();
			
			if (val.toLowerCase().equals("yes")) {
				DataFileManager.write(args[0], obj);
			}
		}
	}
}
