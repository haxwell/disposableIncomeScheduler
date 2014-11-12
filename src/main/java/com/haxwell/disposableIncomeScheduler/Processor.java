package com.haxwell.disposableIncomeScheduler;

import java.util.HashMap;	
import java.util.Map;
import java.util.List;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;

public class Processor {

	public static boolean process(JSONObject data, JSONObject state) {
		Map<Integer, MenuItemHandlerBean> map = new HashMap<>();
		boolean changesMade = false;
		
		MenuItemHandlerBean selection;
		
		do {
			List<MenuItemHandlerBean> list = Controller.getInstance().getMenuItemHandlers(data, state);
			
			map.clear();
			
			int index = 0;
			for (MenuItemHandlerBean child : list) {
				map.put(++index, child);
			}

			displayMenu(map);
			selection = getMenuSelection(map);
			
			if (selection != null) {
				if (selection.doIt(data, state))
					changesMade = true;
			}
		
		} while (selection != null);
		
		return changesMade;		
	}
	
	private static void displayMenu(Map<Integer, MenuItemHandlerBean> map) {
		int index = 0;

		System.out.println();
		
		while (map.containsKey(++index)) {
			System.out.println(index + ". " + ((MenuItemHandlerBean)map.get(index)).getMenuText());
		}
	}
	
	private static MenuItemHandlerBean getMenuSelection(Map<Integer, MenuItemHandlerBean> map) {
		MenuItemHandlerBean rtn = null;
		boolean NaN = true;
		String input = "-";
		
		while (NaN && !input.equals("")) {
			try {
				input = System.console().readLine();
				int i = Integer.parseInt(input);
				rtn = map.get(i);
				NaN = false;
			}
			catch (NumberFormatException nfe) {
				if (!input.equals(""))
					System.out.println("\nNot a number. Try again. Just hit ENTER to exit.\n");
			}
		}
		
		return rtn;
	}
}
