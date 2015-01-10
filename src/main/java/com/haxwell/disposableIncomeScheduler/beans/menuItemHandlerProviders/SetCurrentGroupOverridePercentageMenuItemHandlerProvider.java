package com.haxwell.disposableIncomeScheduler.beans.menuItemHandlerProviders;

import java.util.List;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.MenuItemHandlerBean;
import com.haxwell.disposableIncomeScheduler.beans.SetCurrentGroupOverridePercentageMenuItemHandler;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.utils.DataAndStateSingleton;

public class SetCurrentGroupOverridePercentageMenuItemHandlerProvider extends
		AbstractMenuItemHandlerProvider {

	@Override
	public MenuItemHandlerBean getMenuItemHandler() {
		JSONObject data = DataAndStateSingleton.getInstance().getData();
		JSONObject state = DataAndStateSingleton.getInstance().getState();

		String selGroupName = MenuItemUtils.getSelectedGroupName(state);

		if (selGroupName != null && !selGroupName.equals(MenuItemUtils.getRootGroupName())) {
			List<JSONObject> siblingsOfSelectedGroup = MenuItemUtils.getSiblingsOfSelectedGroup(data, state);
			
			// check that the group has siblings, otherwise its weigh is 1.0, and makes no sense being overridden
			if (siblingsOfSelectedGroup.size() > 0 && MenuItemUtils.isMenuFocusedOn(state, Constants.LONG_TERM_GOALS_JSON))
				return new SetCurrentGroupOverridePercentageMenuItemHandler();
		}
		
		return null;
	}
}
