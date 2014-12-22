package com.haxwell.disposableIncomeScheduler.report;

import java.util.HashMap;
import java.util.Map;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.beans.Println;
import com.haxwell.disposableIncomeScheduler.report.commands.AddCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.Command;
import com.haxwell.disposableIncomeScheduler.report.commands.FunctionCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.StringCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.SubtotalCommand;
import com.haxwell.disposableIncomeScheduler.report.commands.SubtractCommand;

/**
 * Takes a CommandList, and executes each one of them, displaying the results on the screen.
 * 
 */
public class ReportScreenPrinter {

	protected CommandList cl;
	
	public ReportScreenPrinter(CommandList cl) {
		this.cl = cl;
	}
	
	public void print(Println println) {
		
		int total = 0;
		Map<String, Integer> groupMap = new HashMap<String, Integer>();
		
		for (int index = 0; index < this.cl.size(); index++) {
			Command cmd = this.cl.get(index);
			
			if (cmd instanceof FunctionCommand) {
				FunctionCommand fcmd = (FunctionCommand)cmd;
				
				fcmd.func(this.cl, index);
			}
			
			if (cmd instanceof AddCommand) {
				AddCommand acmd = (AddCommand)cmd;
				
				int amt = acmd.getAmount();
				
				String grp = acmd.getGroup();
				if (grp != null) {
					Integer i = groupMap.get(grp);
					
					if (i == null)
						i = new Integer(amt);
					else
						i += amt;
					
					groupMap.put(grp, i);
				}

				total += amt;
				
				println.println(amt + " " + acmd.getDescription());
			}
			
			if (cmd instanceof SubtractCommand) {
				SubtractCommand scmd = (SubtractCommand)cmd;
				
				int amt = scmd.getAmount();
				
				String grp = scmd.getGroup();
				if (grp != null) {
					Integer i = groupMap.get(grp);
					
					if (i == null)
						i = new Integer(amt);
					else
						i += amt;
					
					groupMap.put(grp, i);
				}
				
				total -= amt;
				
				println.println(amt + " " + scmd.getDescription());
			}
			
			if (cmd instanceof SubtotalCommand) {
				SubtotalCommand scmd = (SubtotalCommand)cmd;
				
				int amt = -1;
				String grp = scmd.getGroup();
				if (grp == null) {
					amt = total;
					println.println("=======");
				}
				else {
					amt = groupMap.get(grp);
					groupMap.remove(grp);
					
					println.println("-------");
				}
				
				println.println(amt + " " + scmd.getDescription());
				
				if (grp == null)
					println.println("");
				
				JSONObject data = scmd.getData();
				if (data != null) {
					data.put(scmd.getDataAttribute(), amt);
				}
			}
			
			if (cmd instanceof StringCommand) {
				StringCommand scmd = (StringCommand)cmd;
				
				println.println(scmd.getDescription());
			}
		}
	}
	
	
}
