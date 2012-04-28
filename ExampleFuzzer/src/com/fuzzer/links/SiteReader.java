package com.fuzzer.links;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.fuzzer.config.Config;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;


public class SiteReader {

	private static WebClient client;
	
	public static void main(String[] args){
		client = new WebClient();
		client.setJavaScriptEnabled(true);
		
		URLFinder finder = new URLFinder(client);
		List<URLTarget> targets = Config.PAGE_DISCOVERY ? finder.getTargetsOn(Config.TARGET) : finder.findTargetsFrom(Config.TARGET);
		printLinks(targets);
		
		for (URLTarget target : targets) {
			runExploitsOnPage(target);
		}
	}
	
	public static void runExploitsOnPage(URLTarget target){
		
	}
	
	private static void printLinks(List<URLTarget> targets){
		System.out.println("\nAll links:\n\n");
		for (URLTarget target : targets) {
			System.out.println(target.page);
			if (!target.getArgs.isEmpty()) {
				System.out.println("	GET: " + Arrays.toString(target.getArgs.toArray(new String[]{})));
			}
			if (!target.postArgs.isEmpty()) {
				System.out.println("	POST: " + Arrays.toString(target.postArgs.toArray(new String[]{})));
			}
		}
		System.out.println("Total Targets: " + targets.size());
	}
}
