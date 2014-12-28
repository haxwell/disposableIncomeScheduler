package com.haxwell.disposableIncomeScheduler.report.commands;

public class AddCommand extends Command {
	
	protected long amt = -1;
	protected String grp = null;
	protected String desc = null;
	
	public AddCommand(long amount, String desc) {
		this.amt = amount;
		this.desc = desc; 
	}

	public AddCommand(long amount, String desc, String group) {
		this.amt = amount;
		this.grp = group;
		this.desc = desc;
	}
	
	public long getAmount() {
		return amt;
	}
	
	public String getGroup() {
		return grp;
	}
	
	public String getDescription() {
		return desc;
	}
}
