package com.haxwell.disposableIncomeScheduler.report.commands;

public class StringCommand extends Command{

	String desc = null;
	
	public StringCommand(String str) {
		desc = str;
	}
	
	public String getDescription() {
		return desc;
	}
}
