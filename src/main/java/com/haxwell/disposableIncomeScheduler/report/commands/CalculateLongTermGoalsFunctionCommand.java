package com.haxwell.disposableIncomeScheduler.report.commands;

import java.util.Set;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Constants;
import com.haxwell.disposableIncomeScheduler.beans.utils.GroupedGoalsIterator;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.report.CommandList;

public class CalculateLongTermGoalsFunctionCommand extends FunctionCommand {

	JSONObject data = null;
	
	public CalculateLongTermGoalsFunctionCommand(JSONObject data) {
		this.data = data;
	}
	
	public void func(CommandList cl, int index) {
		GroupedGoalsIterator ggi = new GroupedGoalsIterator(MenuItemUtils.getLongTermGoals(data));
		
		int indexOffset = 1;
		cl.add(index + indexOffset++, new StringCommand(""));
		
		while (ggi.hasNext()) {
			JSONObject obj = ggi.next();
			
			String levelString = "";
			
			for (int i=0; i < ggi.getLevel(); i++)
				levelString += "  ";
		
			if (ggi.objIsALongTermGoal(obj)) {
				long prevSavedAmt = Long.parseLong(obj.get(Constants.PREVIOUS_SAVED_AMT_JSON)+"");
				long price = Long.parseLong(obj.get(Constants.PRICE_JSON)+"");
				
				String desc = obj.get(Constants.DESCRIPTION_JSON)+"";
				
				if (prevSavedAmt >= price)
					desc += " *";
				
				SubtractCommand element = new SubtractCommand(prevSavedAmt, desc);
				element.setBufferString(levelString);
				cl.add(index + indexOffset++, element);
			} else {
				Set<String> keySet = obj.keySet();
				String objName = keySet.iterator().next();
				cl.add(index + indexOffset++, new StringCommand(levelString + objName));
			}
		}
	}
}
