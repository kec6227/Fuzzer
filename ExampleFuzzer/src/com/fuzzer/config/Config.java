package com.fuzzer.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class Config {
	public static final boolean TARGET_ONLY = true;
	//public static final String TARGET = "http://vm549-03b.se.rit.edu/";
	public static final String TARGET = "http://localhost:8080/bodgeit";
	//public static final String TARGET = "http://www.alexanderzlatin.com/";
	
	public static final String LOGIN_URL = null;
	public static final String LOGIN_USER = null;
	public static final String LOGIN_USER_FIELD = null;
	public static final String LOGIN_PASS = null;
	public static final String LOGIN_PASS_FIELD = null;

	
	public static final long TIME_GAP_MS = 50;
	public static final boolean PAGE_INPUT_MERGE = true;
	
	public static final boolean PAGE_DISCOVERY = true;
	public static final boolean PAGE_GUESSING = false;
	public static final boolean COMPLETE_PARSING = true; //Random page/input when false
	public static final boolean GUESS_PASSWORDS = false;
	
	public static final List<String> IGNORE_INPUTS = Arrays.asList(new String[] {"access_token"});
	
	
	public static List<String> getSensitiveDataList() {
		return getListFromFile("");
	}
	
	public static List<String> getSQLInjectionList() {
		return getListFromFile("");
	}
	
	public static List<String> getXSSList() {
		return getListFromFile("");
	}
	
	public static List<String> getListFromFile(String filename) {
		List<String> lines = new ArrayList<String>();
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = fileReader.readLine()) != null){
				if (!line.isEmpty()) {
					lines.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
}
