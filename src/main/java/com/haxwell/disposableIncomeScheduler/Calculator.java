package com.haxwell.disposableIncomeScheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class Calculator {
	
	protected static final String DESCRIPTION_JSON = "description";
	protected static final String HAPPINESS_IMMEDIACY_JSON = "happinessImmediacy";
	protected static final String UTILITY_IMMEDIACY_JSON = "utilityImmediacy";
	protected static final String HAPPINESS_LENGTH_JSON = "happinessLength";
	protected static final String UTILITY_LENGTH_JSON = "utilityLength";
	protected static final String DATE_NEEDED_JSON = "dateNeededBy";

	public static Map<String, Double> getWeights(JSONObject data) {
		Map<String, Double> rtn = new HashMap<String, Double>();
		JSONArray items = (JSONArray)data.get("items");
		
		int arr[] = getArrayToCalculateRelativeWeightOfDates(data);
		int itemSums[] = new int[items.size()];
		
		int totalOfHappinessAndUtilityScores = 0;
		
		int count = 0;
		for (; count < items.size(); count++) {
			JSONObject obj = (JSONObject)items.get(count);
			
			int hi = Integer.parseInt(obj.get(HAPPINESS_IMMEDIACY_JSON)+"");
			int ui = Integer.parseInt(obj.get(UTILITY_IMMEDIACY_JSON)+"");
			int hl = Integer.parseInt(obj.get(HAPPINESS_LENGTH_JSON)+"");
			int ul = Integer.parseInt(obj.get(UTILITY_LENGTH_JSON)+"");
			
			int hsn = 0;
			if (!obj.get(DATE_NEEDED_JSON).toString().equals("") && arr.length > 0) {
				int days = getNumberOfDaysFromToday(obj);
				hsn = arr[days - 1];
			}

			itemSums[count] = (hi + ui + hl + ul + hsn);
			
			totalOfHappinessAndUtilityScores += itemSums[count];
		}
		
		count = 0;
		for (; count < items.size(); count++) {
			JSONObject obj = (JSONObject)items.get(count);
			
			rtn.put(obj.get(DESCRIPTION_JSON).toString(), ((1.0 * itemSums[count]) / (1.0*totalOfHappinessAndUtilityScores)));
		}
		
		return rtn;
	}
	
	public static int getNumberOfDaysFromToday(JSONObject obj) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		int rtn = -1;
		
		try {
			cal.setTime(sdf.parse(obj.get(DATE_NEEDED_JSON).toString()));
			rtn = (int)(((((cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000 ) / 60) / 60) / 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
	}
	
	public static int[] getArrayToCalculateRelativeWeightOfDates(JSONObject data) {
		JSONArray items = (JSONArray)data.get("items");
		
		if (items.size() <= 1)
			return new int[0];
		
		Date furthestDate = getFurthestNeededByDate(items);
		
		int rangeInDays = (int)(((((furthestDate.getTime() - Calendar.getInstance().getTimeInMillis()) / 1000 ) / 60) / 60) / 24);
		
//		System.out.println(furthestDate.getTime() + ", " + Calendar.getInstance().getTimeInMillis());
//		System.out.println("rangeInDays = " + rangeInDays);
		
		int scale = 25;
		int maxSegmentSize = rangeInDays / scale;
		int currSegmentSize = 0;
		int segmentIndex = rangeInDays / maxSegmentSize;
		
		int[] arr = new int[rangeInDays];
		
		for (int i = 0; i < rangeInDays; i++) {
			
			if (currSegmentSize < maxSegmentSize)
				arr[i] = segmentIndex;
			else {
				
				segmentIndex--;
				arr[i] = segmentIndex;
				
				currSegmentSize = -1;
			}
			
			currSegmentSize++;
		}
		
		return arr;
	}

	private static Date getFurthestNeededByDate(JSONArray items) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar furthest = Calendar.getInstance();
		
		for (int i=0; i<items.size(); i++) {
			String dateStr = ((JSONObject)items.get(i)).get(DATE_NEEDED_JSON).toString();
			
			try {
				Date date = sdf.parse(dateStr);
				
				if (date.after(furthest.getTime())) {
					furthest.setTime(date);
				}
				
			} catch (ParseException e) {

			}
		}
		
		return furthest.getTime();
	}
}
