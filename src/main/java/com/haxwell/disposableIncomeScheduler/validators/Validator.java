package com.haxwell.disposableIncomeScheduler.validators;

public class Validator {
	public Validator() { /* default constructor */ }
	
	public String getValidValue(String str) {
		return str;
	}
	
	public boolean isValidValue(String str) {
		return str != null && str.length() > 0;
	}
}
