package com.haxwell.disposableIncomeScheduler;

import java.io.File;

import com.haxwell.disposableIncomeScheduler.beans.CreateNewFileMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;

public class Main {

	public static void main(String[] args) {

		JSONObject state = new JSONObject();
		JSONObject data = null;
		
		boolean brandNewFile = true;

		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();

		MenuItemUtils.initializeState(state);
		dass.setState(state);
		
		String filename = "";

		if (args.length > 0) {
			
			filename = args[0];

			File f = new File(filename);
			if (f.exists()) {
				try {
					data = DataFileManager.read(filename); 	// filename = path to json
															// data file
					dass.setData(data);
					
				} catch (ParseException pe) {
					pe.printStackTrace();
					return;
				}
				
				brandNewFile = false;
			} 
		}

		if (brandNewFile) {
			new CreateNewFileMenuItemHandler().doIt(null, null);
			data = dass.getData();
			filename = "foo.txt";
		}

		boolean changes = new Processor().process();

		if (changes || brandNewFile) {
			System.out.println();
			System.out.println("You made changes. Type 'yes' to save them.");
			String val = System.console().readLine();

			if (val.toLowerCase().equals("yes")) {
				DataFileManager.write(filename, data);
			}
		}
	}
}
