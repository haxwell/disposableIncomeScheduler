package com.haxwell.disposableIncomeScheduler;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;

import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class Main {
	
	public static void main(String[] args) {

		JSONObject state = new JSONObject();
		JSONObject obj = null;
		
		try {
			obj = DataFileManager.read(args[0]); // arg[0] = path to json data file
		}
		catch (ParseException pe) {
			pe.printStackTrace();
			return;
		}
		
		MenuItemUtils.initializeState(state);
		
		DataAndStateSingleton dass = DataAndStateSingleton.getInstance();
		dass.setData(obj);
		dass.setState(state);
		
		boolean changes = new Processor().process();
		
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
