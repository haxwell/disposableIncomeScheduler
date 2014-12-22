package com.haxwell.disposableIncomeScheduler.report.commands;

public class SubtractCommand extends Command {
	
	protected int amt = -1;
	protected String grp = null;
	protected String desc = null;
	
	public SubtractCommand(int amount, String desc) {
		this.amt = amount;
		this.desc = desc; 
	}

	public SubtractCommand(double amount, String desc) {
		this.amt = (int)Math.round(amount);
		this.desc = desc;
	}
	
	public SubtractCommand(int amount, String desc, String group) {
		this.amt = amount;
		this.grp = group;
		this.desc = desc;
	}
	
	public SubtractCommand(double amount, String desc, String group) {
		this.amt = (int)Math.round(amount);
		this.grp = group;
		this.desc = desc;
	}

	public int getAmount() {
		return amt;
	}
	
	public String getGroup() {
		return grp;
	}
	
	public String getDescription() {
		return desc;
	}
}