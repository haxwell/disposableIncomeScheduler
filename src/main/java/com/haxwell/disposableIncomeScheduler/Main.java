package com.haxwell.disposableIncomeScheduler;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class Main {
	
	private static int count = 0;

	public static void main(String[] args) {
	
		
		/**
		 * WHERE I LEFT OFF.. i think we'll need to create a MVC type structure, where the model is the JSON, along with some 
		 * other state.. the Controller would then take the model, and determine, given the state, which MenuHandlers to return
		 * as a collection to the View, which will then display those, and accept an input. Need to think about and create about
		 * that idea more.
		 */

		
		/**
		 * WILO.. the controller has been created, processor now calls it in a loop.. need to create more handlerProviders which
		 * check the state and the data to see if they can present their menu option, and fill out/create more handlers which 
		 * actually modify the data.
		 */
		
		// WILO : Spring taking a long time to load, and this shouldn't need an internet connection. get those files, and store
		//  them locally. reference them locally. .. Other than that, menuitemutils is being built out, can get subgroups, and 
		//  goals of groups now.. better understanding how the data model is working, and will be accessed.
		
		JSONObject state = new JSONObject();
		JSONObject obj = DataFileManager.read(args[0]); // arg[0] = path to json data file
		
		MenuItemUtils.initializeState(state);
		
		boolean changes = Processor.process(obj, state);
		
		if (changes) {
			System.out.println();
			System.out.println("You made changes. Type 'yes' to save them.");
			String val = System.console().readLine();
			
			if (val.toLowerCase().equals("yes")) {
				DataFileManager.write(args[0], obj);
			}
		}

		boolean rtn = ("true".endsWith("dsfad"));
	}
}
