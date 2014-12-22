package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class GroupedGoalsIterator implements Iterator {

	Stack<InnerDataObj> stack;
	
	public GroupedGoalsIterator(JSONArray arr) {
		stack = new Stack<InnerDataObj>();
		
		InnerDataObj ido = new InnerDataObj(arr);
		
		stack.push(ido);
	}
	
	@Override
	public boolean hasNext() {
		Boolean rtn = null;
		
		InnerDataObj ido = stack.peek();
		
		do {
			// check if the ido has a prev returned string
			if (ido.lastObjReturned != null && ido.lastNameReturned != null) {
				// if so, check are there children of the prev returned string
				if (!ido.groupsWhoseChildrenHaveBeenProcessed.contains(ido.lastNameReturned) && thereAreChildren(ido)) {
					//	if so, create a new ido based on the collection of children, push the ido and restart this loop
					JSONArray arr = (JSONArray)ido.lastObjReturned.get(ido.lastNameReturned);
					
					InnerDataObj newIDO = new InnerDataObj(arr);
					stack.push(newIDO);
					
					ido = newIDO;					
				} else {
					//  if not, then check, can we display another goal sibling?
					if (ido.canAdvance()) {
						//		if yes, storing a ref to the string and object returned from the iterator
						rtn = true;
					} else {
						//		if not, (no more children) then we're done here.. pop the ido, peek at the top, and restart the loop					
						stack.pop();
						ido = null;
						
						if (!stack.isEmpty()) {
							ido = stack.peek();
							ido.groupsWhoseChildrenHaveBeenProcessed.add(ido.lastNameReturned);
						}
					}
				}
			} else {
				// if not, next call to next() will advance the ido, storing a ref to the string and object returned from the iterator
				rtn = ido.canAdvance();
			}
		} while (rtn == null && ido != null);
		
		if (rtn == null)
			rtn = false;
		
		return rtn;
	}
	
	private boolean thereAreChildren(InnerDataObj ido) {
		JSONArray arr = (JSONArray)ido.lastObjReturned.get(ido.lastNameReturned);
		return arr != null && arr.size() > 0;
	}

	@Override
	public Object next() {
		String rtn = null;
		
		InnerDataObj ido = stack.peek();
		
		rtn = ido.advance();
		
		return rtn;
	}

	private class InnerDataObj {
		String lastNameReturned;
		JSONObject lastObjReturned;
		List<String> sgNameList;
		List<String> goalsNameList;
		List<JSONObject> sgList;
		List<JSONObject> goalsList;
		Iterator<String> nameIterator;
		Iterator<JSONObject> sgIterator;
		Iterator<JSONObject> goalsIterator;
		List<String> groupsWhoseChildrenHaveBeenProcessed;
		
		public InnerDataObj(JSONArray arr) {
			sgNameList = MenuItemUtils.getSubgroupNamesOfAGroup(arr);
			
			if (sgNameList.size() > 0) {
				sgList = MenuItemUtils.getSubgroups(arr);
				nameIterator = sgNameList.iterator();
				sgIterator = sgList.iterator();
			} else {
				sgNameList = null;
				
				goalsNameList = MenuItemUtils.getGoalNamesOfAGroup(arr);
				goalsList = MenuItemUtils.getGoalsOfAGroup(arr);
				nameIterator = goalsNameList.iterator();
				goalsIterator = goalsList.iterator();
			}
			
			groupsWhoseChildrenHaveBeenProcessed = new ArrayList<>();
		}
		
		public boolean canAdvance() {
			if (subgroups())
				return sgIterator.hasNext();
			else
				return goalsIterator.hasNext();
		}
		
		public String advance() {
			if (subgroups()) {
				lastObjReturned = sgIterator.next();
			} else {
				lastObjReturned = goalsIterator.next();
			}
			
			lastNameReturned = nameIterator.next();
			
			return lastNameReturned;
		}
		
		public boolean subgroups() {
			return sgList != null;
		}
	}
}
