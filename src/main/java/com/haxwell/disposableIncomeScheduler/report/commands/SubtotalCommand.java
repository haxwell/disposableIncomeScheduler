package com.haxwell.disposableIncomeScheduler.report.commands;

import net.minidev.json.JSONObject;

public class SubtotalCommand extends Command {

	protected String grp = null;
	protected String desc = null;
	
	protected JSONObject data = null;
	protected String dataAttribute = null;
	
	public SubtotalCommand(String description) {
		this.desc = description;
	}

	public SubtotalCommand(String description, String group) {
		this.desc = description;
		this.grp = group;
	}
	
	public SubtotalCommand(String description, JSONObject data, String dataAttribute) {
		this.desc = description;
		this.data = data;
		this.dataAttribute = dataAttribute;
	}
	
	public String getGroup() {
		return grp;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public JSONObject getData() {
		return data;
	}
	
	public String getDataAttribute() {
		return dataAttribute;
	}
}
