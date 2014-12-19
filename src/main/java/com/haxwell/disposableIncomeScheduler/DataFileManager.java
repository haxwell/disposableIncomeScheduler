package com.haxwell.disposableIncomeScheduler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

public class DataFileManager {

	// reads JSON from a text file, returns a JSONObject or collection..
	public static JSONObject read(String filepath) throws ParseException {
		JSONObject rtn = new JSONObject();

		File file = new File(filepath);
		BufferedReader br;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
			   sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rtn = (JSONObject)JSONValue.parseWithException(sb.toString());
		
		return rtn;
	}
	
	public static void write(String filepath, JSONObject obj) {
		File file = new File(filepath);
		file.delete();
		
		BufferedWriter bw;
		
		try {
			bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(obj.toJSONString());
			
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
