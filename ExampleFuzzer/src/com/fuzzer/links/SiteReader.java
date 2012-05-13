package com.fuzzer.links;

import java.util.List;

import org.apache.commons.logging.LogFactory;


import com.fuzzer.config.Config;
import com.fuzzer.exploits.PasswordExploit;
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
		
		if (!Config.LOGIN_GUESS_PASSWORDS) {
			PasswordAuth.login(client);
		} else {
			PasswordExploit.crack(client);
		}
		
		URLFinder finder = new URLFinder(client);
		List<URLTarget> targets = Config.PAGE_DISCOVERY ? finder.findTargetsFrom(Config.TARGET) : finder.getTargetsOn(Config.TARGET);
		printLinks(targets);
		
		if (Config.PAGE_GUESSING) {
			System.out.println("Searching for Pages: ");
			List<String> guessed = PageGuesser.guessPaths(client, Config.TARGET);
			for (String page : guessed) {
				System.out.println("Found Page: " + page);
			}
		}
		
		for (URLTarget target : targets) {
			if (Config.shouldUse()) {
				runExploitsOnPage(target);
			}
		}
	}
	
	public static void runExploitsOnPage(URLTarget target){
		System.out.println("\nTesting Exploits On: " + target.page);
		XSSExploit.exploitForTarget(client, target);
		SQLInjectionExploit.exploitForTarget(client, target);
	}
	
	private static void printLinks(List<URLTarget> targets){
		System.out.println("\nAll Links:");
		for (URLTarget target : targets) {
			target.print();
		}
		System.out.println("Total Targets: " + targets.size());
	}
}
