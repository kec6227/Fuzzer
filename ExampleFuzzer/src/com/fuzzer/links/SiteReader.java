package com.fuzzer.links;

import java.util.List;

import org.apache.commons.logging.LogFactory;


import com.fuzzer.config.Config;
import com.fuzzer.exploits.SQLInjectionExploit;
import com.fuzzer.exploits.XSSExploit;
import com.gargoylesoftware.htmlunit.WebClient;


public class SiteReader {

	static {
		// This disables the HTMLUnit apache logging
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}

	private static WebClient client;
	
	public static void main(String[] args){
		client = new WebClient();
		client.setJavaScriptEnabled(true);
		
		PasswordAuth.login(client);
		
		URLFinder finder = new URLFinder(client);
		List<URLTarget> targets = Config.PAGE_DISCOVERY ? finder.findTargetsFrom(Config.TARGET) : finder.getTargetsOn(Config.TARGET);
		printLinks(targets);
		
		for (URLTarget target : targets) {
			runExploitsOnPage(target);
		}
	}
	
	public static void runExploitsOnPage(URLTarget target){
		XSSExploit.exploitForTarget(client, target);
		SQLInjectionExploit.exploitForTarget(client, target);
	}
	
	private static void printLinks(List<URLTarget> targets){
		System.out.println("\nAll links:");
		for (URLTarget target : targets) {
			target.print();
		}
		System.out.println("Total Targets: " + targets.size());
	}
}
