package com.haxwell.disposableIncomeScheduler;

import java.util.HashMap;	
import java.util.Map;
import java.util.List;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class Processor {

	public static boolean process() {
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		JSONObject state = DataAndStateSingleton.getInstance().getState();
		
		Map<Integer, MenuItemHandlerBean> map = new HashMap<>();
		boolean changesMade = false;
		
		MenuItemHandlerBean selection;
		
		do {
			List<MenuItemHandlerBean> list = Controller.getInstance().getMenuItemHandlers();
			
			map.clear();
			
			int index = 0;
			for (MenuItemHandlerBean child : list) {
				map.put(++index, child);
			}

			displayMenu(map);
			
			System.out.println("\nCurrent Group: " + MenuItemUtils.getSelectedGroupName(state) + "\n");
			
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
		String input = "-";
		
		while (rtn == null && !input.equals("quit")) {
			try {
				input = System.console().readLine();
				int i = Integer.parseInt(input);
				rtn = map.get(i);
				
				if (rtn == null)
					System.out.println("\nInvalid selection.");
			}
			catch (NumberFormatException nfe) {
				if (!input.equals("quit"))
					System.out.println("\nNot a number. Try again. Type 'quit' to exit.\n");
			}
		}
		
		return rtn;
	}
}
