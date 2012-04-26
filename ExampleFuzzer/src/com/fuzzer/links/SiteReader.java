package com.fuzzer.links;

import java.util.HashSet;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;


public class SiteReader {

	private static final boolean PAGE_DISCOVERY = false;
	private static final boolean PAGE_GUESSING = false;
	private static final boolean COMPLETE_PARSING = true; //Random page/input when false
	private static final boolean GUESS_PASSWORDS = false;
	
	private static final String SITE = "http://vm549-03b.se.rit.edu/Home?access_token=AAAEsT22mZAJcBAHdIXUTAoZAGjSizKZA0ZCkkJX6eQnht8c7616n3WVAkBbmLHfOms55l3KLbyFdnR92Sbmh6BFPlSbim9CDoToiNbLzIwZDZD#_=_";
	
	//For the Web Engineering class
	private static final String SITE_ROOT = "http://vm549-03b.se.rit.edu";
	private static HashSet<String> totalPages = new HashSet<String>();
	
	private static WebClient client;
	
	public static void main(String[] args){
		client = new WebClient();
		client.setJavaScriptEnabled(true);
		
		LinkFinder finder = new LinkFinder(SITE, client);
		List<HtmlAnchor> links = finder.findAllLinks();
		
		getLinks(links);
		
	}
	
	public static void getLinks(List<HtmlAnchor> links){
		System.out.println("Finding links on site: " + SITE);
		for(HtmlAnchor link: links){
			System.out.println(link.getHrefAttribute());
			if(PAGE_DISCOVERY){
				totalPages.add(link.getHrefAttribute());
			}
		}
	}
	
	public static void runExploitsOnPage(String pageToExploit){
		
	}
}
