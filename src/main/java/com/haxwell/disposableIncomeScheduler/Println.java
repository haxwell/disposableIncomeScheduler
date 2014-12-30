package com.haxwell.disposableIncomeScheduler;

public class Println {

	public void println(String str) {
		System.out.println(str);
	}
	
	public void println() {
		System.out.println("");
	}
	
	public void print(String str) {
		System.out.print(str);
	}
	
	public void format(String format, Object ... args) {
		System.out.format(format, args);
	}
}
