package com.haxwell.disposableIncomeScheduler.report.commands;

import com.haxwell.disposableIncomeScheduler.report.CommandList;

/**
 * A Command object used to modify the command list as it is being processed.
 * 
 * When this command is executed, it will add commands to the overall command list,
 * which will then be processed as if they had been there from the beginning. Only
 * works for proceeding commands; the command list cannot be modified to execute 
 * commands that occur before this FunctionCommand's position in the commandList.
 * 
 * @author jjames
 *
 */
public class FunctionCommand extends Command {
	
	public FunctionCommand() {

	}

	public void func(CommandList cl, int index) {

	}
}
