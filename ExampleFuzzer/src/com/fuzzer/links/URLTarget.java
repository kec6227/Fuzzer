package com.fuzzer.links;

import java.util.Arrays;
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
	
	public void print() {
		System.out.println(page);
		if (!getArgs.isEmpty()) {
			System.out.println("	GET: " + Arrays.toString(getArgs.toArray(new String[]{})));
		}
		if (!postArgs.isEmpty()) {
			System.out.println("	POST: " + Arrays.toString(postArgs.toArray(new String[]{})));
		}
	}

}
