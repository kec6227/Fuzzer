package com.fuzzer.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Config {
	// The target to fuzz
	//public static final String TARGET = "http://vm549-03b.se.rit.edu/";
	public static final String TARGET = "http://hugecase-beta.student.rit.edu:8080/bodgeit/";

	// Fuzz only pages on the same host as the target
	public static final boolean TARGET_ONLY = true;

	// Test all possible combinations
	public static final boolean COMPLETE_PARSING = true;
	// Percent of values that should randomly be used if COMPLETE_PARSING = false
	public static final double RANDOM_USE = 0.5;
	
	// Use these settings to login before fuzzing
	public static final String LOGIN_URL = "http://hugecase-beta.student.rit.edu:8080/bodgeit/login.jsp";
	public static final String LOGIN_USER = "' or ''='";
	public static final String LOGIN_USER_FIELD = "username";
	public static final String LOGIN_PASS = "' or ''='";
	public static final String LOGIN_PASS_FIELD = "password";
	
	// Set this to true to brute force LOGIN_PASS value
	public static final boolean LOGIN_GUESS_PASSWORDS = false;

	// Time to wait between page requests
	public static final long TIME_GAP_MS = 50;
	
	// If a page accepts different combinations of inputs, should they be merged?
	public static final boolean PAGE_INPUT_MERGE = true;
	
	// Should we attempt to discover all pages or just those linked from TARGET?
	public static final boolean PAGE_DISCOVERY = true;
	
	// Should we attempt to find hidden pages?
	public static final boolean PAGE_GUESSING = true;
	
	// List of POSt and GET params that should not be fuzzed.
	public static final List<String> IGNORE_INPUTS = Arrays.asList(new String[] {"access_token", "anticsrf"});
	
	///////////////////////////////////////////////////////////////////////////
	// END OF CONFIG
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean shouldUse() {
		return COMPLETE_PARSING || Math.random() <= Config.RANDOM_USE;
	}
	
	
	public static List<String> getSensitiveDataList() {
		return getListFromFile("data/sensitive.txt");
	}
	
	public static List<String> getSQLInjectionList() {
		return getListFromFile("data/sql.txt");
	}
	
	public static List<String> getXSSList() {
		return getListFromFile("data/xss.txt");
	}
	
	public static List<String> getPasswordList() {
		return getListFromFile("data/passwords.txt");
	}
	
	public static List<String> getPageList() {
		return getListFromFile("data/pages.txt");
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
