package com.haxwell.disposableIncomeScheduler.beans;

import java.util.List;

import net.minidev.json.JSONObject;

import com.haxwell.disposableIncomeScheduler.InputGetter;
import com.haxwell.disposableIncomeScheduler.Println;

public class MenuItemHandlerBean {

	String menuText;
	List<MenuItemHandlerBean> children;
	
	private InputGetter inputGetter;
	private Println println;
	
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
	
	public InputGetter getInputGetter() {
		InputGetter rtn = inputGetter;
		
		if (rtn == null)
			rtn = new InputGetter();
		
		return rtn;
	}
	
	public void setInputGetter(InputGetter ig) {
		inputGetter = ig;
	}
	
	public Println getPrintlner() {
		Println rtn = println;
		
		if (rtn == null)
			rtn = new Println();
		
		return rtn;
	}
	
	public void setPrintlner(Println p) {
		println = p; 
	}
}
