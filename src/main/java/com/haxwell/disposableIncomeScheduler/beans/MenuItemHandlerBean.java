package com.haxwell.disposableIncomeScheduler.beans;

import java.util.List;

import net.minidev.json.JSONObject;

public class MenuItemHandlerBean {

	String menuText;
	List<MenuItemHandlerBean> children;
	
	/**
	 * 
	 * @param data
	 * @return true if changes were made to the data
	 */
	public boolean doIt(JSONObject data, JSONObject state) {
		return false;
	}
	
	public String getMenuText() {
		return null;
	}

	public List<MenuItemHandlerBean> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItemHandlerBean> children) {
		this.children = children;
	}
}
