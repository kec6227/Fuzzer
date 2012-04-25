package com.fuzzer.links;

import java.util.HashSet;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;


public class SiteReader {

	private static final boolean PAGE_DISCOVERY = false;
	private static final boolean PAGE_GUESSING = false;
	private static final boolean COMPLETE_PARSING = true; //Random page/input when false
	private static final boolean GUESS_PASSWORDS = false;
	
	private static final String SITE = "http://localhost:8080/bodgeit";
	private HashSet<String> totalPages = new HashSet<String>();
	
	public static void main(String[] args){
		LinkFinder finder = new LinkFinder(SITE);
		List<HtmlAnchor> links = finder.findAllLinks();
		
		getLinks(links);
		
	}
	
	public static void getLinks(List<HtmlAnchor> links){
		System.out.println("Finding links on site: " + SITE);
		for(HtmlAnchor link: links){
			System.out.println(link.getHrefAttribute());
		}
	}
}
