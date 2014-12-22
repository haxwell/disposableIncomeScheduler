package com.haxwell.disposableIncomeScheduler.beans.utils;

import static org.junit.Assert.assertTrue;	
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.minidev.json.JSONArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.JSONDataBasedTest;

public class GroupedGoalsIteratorTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}
	
	@Test
	public void testGroupGoalsIterator() {
		JSONArray grpArr = (JSONArray)data.get(MenuItemUtils.getRootGroupName());
		
		GroupedGoalsIterator sut = new GroupedGoalsIterator(grpArr);
		
		List<String> list = new ArrayList<>();
		
		while (sut.hasNext())
			list.add(sut.next()+"");
		
		assertTrue(list.size() == 11);
		
		String expectedElements = "goals_johnathans goals, goals_12880, goals_bathroom, sink, shower, goals_outside, garage door, goals_kitchen, goals_trip to France, airfare, lodging";
		StringTokenizer tokenizer = new StringTokenizer(expectedElements, ",");
		int index = 0;
		
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken().trim();
			assertTrue(list.get(index++).equals(token));
		}
	}
}
