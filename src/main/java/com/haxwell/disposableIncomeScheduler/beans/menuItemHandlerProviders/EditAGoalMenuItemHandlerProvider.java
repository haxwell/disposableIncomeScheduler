package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.EditAGoalMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class EditAGoalMenuItemHandlerProvider extends
		AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		JSONObject state = DataAndStateSingleton.getInstance().getState();

		JSONArray arr = MenuItemUtils.getSelectedGroup(data, state);
		
		List<JSONObject> arrGoals = MenuItemUtils.getGoalsOfAGroup(arr);

		if (arrGoals.size() > 0 && MenuItemUtils.isMenuFocusedOn(state, Constants.LONG_TERM_GOALS_JSON))
			return new EditAGoalMenuItemHandler();
		
		return null;
	}
}
