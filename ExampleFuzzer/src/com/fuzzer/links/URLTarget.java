package com.fuzzer.links;

import java.util.ArrayList;
import java.util.HashSet;

public class URLTarget {
	
	public final String page;
	public HashSet<String> samplePages;
	
	public HashSet<String> getArgs;
	public HashSet<String> postArgs;
	
	public URLTarget(String page) {
		this.page = page;
		samplePages = new HashSet<String>();
		getArgs = new HashSet<String>();
		postArgs = new HashSet<String>();
	}

}
