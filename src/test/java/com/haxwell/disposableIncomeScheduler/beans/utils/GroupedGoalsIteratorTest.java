package com.haxwell.disposableIncomeScheduler.beans.utils;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

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
		
		String str = "";
	}
}
