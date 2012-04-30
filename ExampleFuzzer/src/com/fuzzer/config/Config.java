package com.fuzzer.config;

import java.util.List;
import java.util.Arrays;

import org.apache.commons.logging.LogFactory;

public class Config {
	public static final boolean TARGET_ONLY = true;
	//public static final String TARGET = "http://vm549-03b.se.rit.edu/";
	public static final String TARGET = "http://localhost:8080/bodgeit";
	//public static final String TARGET = "http://www.alexanderzlatin.com/";
	
	public static final long TIME_GAP_MS = 50;
	public static final boolean PAGE_INPUT_MERGE = true;
	
	public static final boolean PAGE_DISCOVERY = true;
	public static final boolean PAGE_GUESSING = false;
	public static final boolean COMPLETE_PARSING = true; //Random page/input when false
	public static final boolean GUESS_PASSWORDS = false;
	
	public static final List<String> IGNORE_INPUTS = Arrays.asList(new String[] {"access_token"});
}
