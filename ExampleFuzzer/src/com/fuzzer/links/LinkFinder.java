package com.fuzzer.links;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LinkFinder {

	private HtmlPage currentPage;
	WebClient client;
	
	public LinkFinder(String page, WebClient client){
		System.out.println("Finding ALL links on page: " + page);
		this.client = client;
		try {
			currentPage = client.getPage(page);
		} catch (FailingHttpStatusCodeException e) {
			System.err.println("Bad status code: " + e.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
	
	public List<HtmlAnchor> findAllLinks(){
		if(currentPage == null){
			return null; // This happens if the page returned a 404 in the constructor.
		}
		return currentPage.getAnchors();
	}
	
}
