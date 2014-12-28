package com.haxwell.disposableIncomeScheduler.report.commands;

public class SubtractCommand extends Command {
	
	protected long amt = -1;
	protected String grp = null;
	protected String desc = null;
	private String bufferString;
	
	public SubtractCommand(long amount, String desc) {
		this.amt = amount;
		this.desc = desc; 
	}

	public SubtractCommand(double amount, String desc) {
		this.amt = (long)Math.round(amount);
		this.desc = desc;
	}
	
	public SubtractCommand(long amount, String desc, String group) {
		this.amt = amount;
		this.grp = group;
		this.desc = desc;
	}
	
	public SubtractCommand(double amount, String desc, String group) {
		this.amt = (long)Math.round(amount);
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
	
	public void setBufferString(String str) {
		this.bufferString = str;
	}
	
	public String getBufferString() {
		return this.bufferString == null ? "" : this.bufferString;
	}
}
