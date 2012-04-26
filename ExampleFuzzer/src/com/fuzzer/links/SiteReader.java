package com.fuzzer.links;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;


public class SiteReader {

	static {
		// This disables the HTMLUnit apache logging
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}
	
	private static final boolean PAGE_DISCOVERY = true;
	private static final boolean PAGE_GUESSING = false;
	private static final boolean COMPLETE_PARSING = true; //Random page/input when false
	private static final boolean GUESS_PASSWORDS = false;
	
	private static final String SITE = "http://localhost:8080/bodgeit/";
	
	// Toggle on when fuzzing the other class' site
	private static final boolean FUZZING_WEB_CLASS = false;
	
	//For the Web Engineering class
	private static final String SITE_ROOT = "http://vm549-03b.se.rit.edu";
	private static HashSet<String> totalPages = new HashSet<String>();
	
	private static HashSet<String> visitedPages = new HashSet<String>();
	
	private static WebClient client;
	
	public static void main(String[] args){
		client = new WebClient();
		client.setJavaScriptEnabled(true);
		
		LinkFinder finder = new LinkFinder(SITE, client);
		List<HtmlAnchor> links = finder.findAllLinks();
		
		getLinks(links);
		
		if(PAGE_DISCOVERY){
			printLinks();
		}
	}
	
	public static void getLinks(List<HtmlAnchor> links){
		if(links==null){
			return;
		}
		for(HtmlAnchor link: links){
//			System.out.println(link.getHrefAttribute());
			if(PAGE_DISCOVERY){
				totalPages.add(link.getHrefAttribute().split("\\?")[0]);
			}
			// Get before the question mark to not count each argument
			// as a separate link.
			if(visitedPages.contains(link.getHrefAttribute().split("\\?")[0])){
//				System.out.println("Already read link : " + link.getHrefAttribute().split("\\?")[0]);
				continue;
			}
			visitedPages.add(link.getHrefAttribute().split("\\?")[0]);
			LinkFinder finder;
			if(FUZZING_WEB_CLASS){
				finder = new LinkFinder(SITE_ROOT + link.getHrefAttribute(), client);
			}else{
				finder = new LinkFinder(SITE + "/" + link.getHrefAttribute(), client);
			}
			getLinks(finder.findAllLinks());
		}
	}
	
	public static void runExploitsOnPage(String pageToExploit){
		
	}
	
	private static void printLinks(){
		System.out.println("\nAll links:\n\n");
		for(String s : totalPages){
			System.out.println(s);
		}
		System.out.println("Total pages are : " + totalPages.size());
	}
}
