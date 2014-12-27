package com.haxwell.disposableIncomeScheduler.report.commands;

import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.Calculator;
import com.haxwell.disposableIncomeScheduler.beans.utils.GroupedGoalsIterator;
import com.haxwell.disposableIncomeScheduler.beans.utils.MenuItemUtils;
import com.haxwell.disposableIncomeScheduler.report.CommandList;

public class CalculateLongTermGoalsFunctionCommand extends FunctionCommand {

	JSONObject data = null;
	
	public CalculateLongTermGoalsFunctionCommand(JSONObject data) {
		this.data = data;
	}
	
	public void func(CommandList cl, int index) {
		GroupedGoalsIterator ggi = new GroupedGoalsIterator((JSONArray)data.get(MenuItemUtils.getRootGroupName()));
		Map<String, Long> dapg = Calculator.getDollarAmountsToBeAppliedPerGroup(data);
		
		int indexOffset = 1;
		cl.add(index + indexOffset++, new StringCommand(""));
		
		while (ggi.hasNext()) {
			String goal = ggi.next();
			
			String levelString = "";
			
			for (int i=0; i < ggi.getLevel(); i++)
				levelString += "  ";
		
			if (dapg.containsKey(goal)) {
				SubtractCommand element = new SubtractCommand(dapg.get(goal), goal);
				element.setBufferString(levelString);
				cl.add(index + indexOffset++, element);
			} else {
				cl.add(index + indexOffset++, new StringCommand(levelString + goal));
			}
		}
	}
}
