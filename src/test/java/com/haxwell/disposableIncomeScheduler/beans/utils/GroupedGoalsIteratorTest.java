package com.haxwell.disposableIncomeScheduler.beans.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.haxwell.disposableIncomeScheduler.Constants;
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
	public void testGroupedGoalsIterator() {
		JSONArray grpArr = MenuItemUtils.getLongTermGoals(data);
		
		GroupedGoalsIterator sut = new GroupedGoalsIterator(grpArr);
		
		List<JSONObject> list = new ArrayList<>();
		
		while (sut.hasNext())
			list.add(sut.next());
		
		assertTrue(list.size() == 11);
		
		String expectedElements = "johnathans goals, 12880, bathroom, sink, shower, outside, garage door, kitchen, trip to France, airfare, lodging";
		StringTokenizer tokenizer = new StringTokenizer(expectedElements, ",");
		int index = 0;
		
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken().trim();
			JSONObject obj = list.get(index++);
			
			if (obj.containsKey(Constants.DESCRIPTION_JSON)) {
				assertTrue(obj.get(Constants.DESCRIPTION_JSON).equals(token));	
			}
			else {
				Set<String> keySet = obj.keySet();
				
				assertTrue(keySet.size() == 1);
				
				for (String str : keySet) {
					assertTrue(str.equals(token));
				}
			}
			
		}
	}
	
	@Test
	public void testGroupedGoalsIterator_objIsALongTermGoal_method() {
		JSONArray grpArr = MenuItemUtils.getLongTermGoals(data);
		
		GroupedGoalsIterator sut = new GroupedGoalsIterator(grpArr);
		
		assertTrue(sut.hasNext());
		
		JSONObject obj = sut.next(); // get johnathans_goals
		
		assertFalse(sut.objIsALongTermGoal(obj));
		
		assertTrue(sut.hasNext());
		obj = sut.next(); // get 12880
		assertFalse(sut.objIsALongTermGoal(obj));
		
		assertTrue(sut.hasNext());
		obj = sut.next(); // get bathroom
		assertFalse(sut.objIsALongTermGoal(obj));
		
		assertTrue(sut.hasNext());
		obj = sut.next(); // get sink
		assertTrue(sut.objIsALongTermGoal(obj));
	}

	@Test
	public void testGroupedGoalsIterator_objIsASubgroup_method() {
		JSONArray grpArr = MenuItemUtils.getLongTermGoals(data);
		
		GroupedGoalsIterator sut = new GroupedGoalsIterator(grpArr);
		
		assertTrue(sut.hasNext());
		
		JSONObject obj = sut.next(); // get johnathans_goals
		
		assertTrue(sut.objIsASubgroup(obj));
		
		assertTrue(sut.hasNext());
		obj = sut.next(); // get 12880
		assertTrue(sut.objIsASubgroup(obj));
		
		assertTrue(sut.hasNext());
		obj = sut.next(); // get bathroom
		assertTrue(sut.objIsASubgroup(obj));
		
		assertTrue(sut.hasNext());
		obj = sut.next(); // get sink
		assertFalse(sut.objIsASubgroup(obj));
	}

	@Test
	public void testGroupedGoalsIterator_getPath() {
		JSONArray grpArr = MenuItemUtils.getLongTermGoals(data);
		
		GroupedGoalsIterator sut = new GroupedGoalsIterator(grpArr);
		
		assertTrue(sut.hasNext());

		String expectedElements = "johnathans goals, 12880, bathroom, sink, shower, outside, garage door, kitchen, trip to France, airfare, lodging";
		StringTokenizer tokenizer = new StringTokenizer(expectedElements, ",");
		
		while (sut.hasNext()) {
			sut.next();
			
			assertTrue(tokenizer.hasMoreTokens());
			String token = tokenizer.nextToken().trim();
			
			assertTrue(sut.getPath().endsWith(token));
		}
	}
}
