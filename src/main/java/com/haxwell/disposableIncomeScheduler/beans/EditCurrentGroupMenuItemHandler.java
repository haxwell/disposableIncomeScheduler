package com.haxwell.disposableIncomeScheduler.beans;

import java.util.LinkedList;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders.ChangeSelectedGroupNameMenuItemHandlerProvider;
import com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders.SetCurrentGroupOverridePercentageMenuItemHandlerProvider;

public class EditCurrentGroupMenuItemHandler extends MenuItemHandlerBean {

	public String getMenuText() {
		return "Edit Selected Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		String choice = "";
		
		LinkedList<MenuItemHandlerBean> menuOptions = getMenuOptions(data, state);
		
//		do {
			// list each item
			for (int count = 0; count < menuOptions.size(); count++) {
				String option = menuOptions.get(count).getMenuText();
				System.out.println(count+1 + ". " + option);
			}
			
			choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				try {
					int choiceInt = Integer.parseInt(choice);
					
					if (choiceInt > 0 && choiceInt <= menuOptions.size()){
						MenuItemHandlerBean mi = menuOptions.get(choiceInt - 1);
						rtn = mi.doIt(data, state);
					} else {
						choice = null;
						System.out.println("Exception: " + choiceInt + " " + menuOptions.size());
					}
					
				} catch (NumberFormatException nfe) {
					choice = null;
					System.out.println("NFE");
				}
			}

//		} while (choice != null && !choice.equals(""));
		
		return rtn;
	}

	private LinkedList<MenuItemHandlerBean> getMenuOptions(JSONObject data, JSONObject state) {
		LinkedList<MenuItemHandlerBean> linkedList = new LinkedList<MenuItemHandlerBean>();
		
		MenuItemHandlerBean menuItemHandler = new ChangeSelectedGroupNameMenuItemHandlerProvider().getMenuItemHandler(data, state);
		if (menuItemHandler != null)
			linkedList.add(menuItemHandler);
		
		menuItemHandler = new SetCurrentGroupOverridePercentageMenuItemHandlerProvider().getMenuItemHandler(data, state); 
		if (menuItemHandler != null)
			linkedList.add(menuItemHandler);
		
		return linkedList;
	}
}
