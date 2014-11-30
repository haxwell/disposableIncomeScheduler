package com.haxwell.disposableIncomeScheduler.beans;

import java.util.LinkedList;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;

public class EditCurrentGroupMenuItemHandler extends MenuItemHandlerBean {

	public String getMenuText() {
		return "Edit Selected Group";
	}
	
	public boolean doIt(JSONObject data, JSONObject state) {
		boolean rtn = false;

		String choice = "";
		
		LinkedList<MenuItemHandlerBean> menuOptions = getMenuOptions();
		
		do {
			// list each item
			for (int count = 0; count < menuOptions.size(); count++) {
				String option = menuOptions.get(count).getMenuText();
				System.out.println(count+1 + ". " + option);
			}
			
			choice = getInputGetter().readInput();
			
			if (choice != null && !choice.equals("")) {
				MenuItemHandlerBean mi = menuOptions.get(Integer.parseInt(choice) - 1);
				mi.doIt(data, state);
			}

		} while (choice != null && !choice.equals(""));
		
		return rtn;
	}

	private LinkedList<MenuItemHandlerBean> getMenuOptions() {
		LinkedList<MenuItemHandlerBean> linkedList = new LinkedList<MenuItemHandlerBean>();
		
		linkedList.add(new ChangeSelectedGroupNameMenuItemHandler());
		linkedList.add(new SetCurrentGroupOverridePercentageMenuItemHandler());
		
		return linkedList;
	}
}
