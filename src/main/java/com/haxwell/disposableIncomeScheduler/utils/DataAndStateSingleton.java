package com.haxwell.disposableIncomeScheduler.utils;

import net.minidev.json.JSONObject;

public class DataAndStateSingleton {

	private static DataAndStateSingleton instance;
	
	private JSONObject data;
	private JSONObject state;
	
	private DataAndStateSingleton() {
		
	}
	
	public static DataAndStateSingleton getInstance() {
		if (instance == null)
			instance = new DataAndStateSingleton();
		
		return instance;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public JSONObject getState() {
		return state;
	}

	public void setState(JSONObject state) {
		this.state = state;
	}
}
