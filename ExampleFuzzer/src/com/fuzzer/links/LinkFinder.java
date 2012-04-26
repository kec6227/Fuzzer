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
		this.client = client;
		try {
			currentPage = client.getPage(page);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<HtmlAnchor> findAllLinks(){
		return currentPage.getAnchors();
	}
	
}
