package com.fuzzer.links;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fuzzer.config.Config;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class URLFinder {

	private WebClient client;
	
	public URLFinder(WebClient client){
		this.client = client;
	}
	
	public ArrayList<URLTarget> findTargetsFrom(String url) {
		ArrayList<URLTarget> targets = new ArrayList<URLTarget>();
		Queue<String> queue = new ConcurrentLinkedQueue<String>();
		HashSet<String> visited = new HashSet<String>();
		
		targets.add(new URLTarget(url));
		queue.add(url);
		
		while (!queue.isEmpty()) {
			String next = queue.poll();
			if (visited.contains(next)) {
				continue;
			}
			visited.add(next);
			ArrayList<URLTarget> found = getTargetsOn(next);
			targets.addAll(found);
			for (URLTarget sample : found) {
				queue.addAll(sample.samplePages);
			}
		}
		
		if (Config.PAGE_INPUT_MERGE) {
			targets = mergeTargets(targets);
		}
		
		return targets;
	}
	
	
	public ArrayList<URLTarget> getTargetsOn(String url) {
		System.out.println("Finding ALL Links On: " + url);
		HtmlPage page = null;
		
		try {
			page = client.getPage(url);
		} catch (FailingHttpStatusCodeException e) {
			System.err.println("Bad status code: " + e.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		
		ArrayList<URLTarget> targets = new ArrayList<URLTarget>();
		if (page == null) {
			return targets;
		}
		
		targets.addAll(parseAnchors(page));
		targets.addAll(parseForms(page));
		
		if (Config.PAGE_INPUT_MERGE) {
			targets = mergeTargets(targets);
		}
		
		try {
			Thread.sleep(Config.TIME_GAP_MS);
		} catch (InterruptedException e) {
		}
		
		return targets;
	}
	
	public static ArrayList<URLTarget> mergeTargets(List<URLTarget> targets) {
		HashMap<String, URLTarget> targetMap = new HashMap<String, URLTarget>();
		for (URLTarget target : targets) {
			if (targetMap.containsKey(target.page)) {
				URLTarget existing = targetMap.get(target.page);
				existing.samplePages.addAll(target.samplePages);
				existing.getArgs.addAll(target.getArgs);
				existing.postArgs.addAll(target.postArgs);
				existing.sampleValues.putAll(target.sampleValues);
				if (existing.originalContent.isEmpty() && !target.originalContent.isEmpty()) {
					existing.originalContent = target.originalContent;
				}
			} else {
				targetMap.put(target.page, target);
			}
		}
		return new ArrayList<URLTarget>(targetMap.values());
	}
	
	public ArrayList<URLTarget> parseAnchors(HtmlPage page) {
		List<HtmlAnchor> links = page.getAnchors();
		ArrayList<URLTarget> targets = new ArrayList<URLTarget>();
		for (HtmlAnchor link : links) {
			URLTarget target = parseURL(page, link.getHrefAttribute());
			if (target != null) {
				targets.add(target);
			}
		}
		return targets;
	}
	
	
	public ArrayList<URLTarget> parseForms(HtmlPage page) {
		List<HtmlForm> forms = page.getForms();
		ArrayList<URLTarget> targets = new ArrayList<URLTarget>();
		for (HtmlForm form : forms) {
			String action = form.getActionAttribute();
			if (action.isEmpty()) {
				action = page.getUrl().toExternalForm();
			}
			boolean methodIsPost = form.getMethodAttribute().equalsIgnoreCase("post");
			URLTarget target = parseURL(page, action);
			if (target != null) {
				targets.add(target);
				ArrayList<String> args = new ArrayList<String>();
				List<HtmlElement> elements = new ArrayList<HtmlElement>();
				elements.addAll(form.getElementsByTagName("input"));
				elements.addAll(form.getElementsByTagName("select"));
				elements.addAll(form.getElementsByTagName("textarea"));
				for (HtmlElement e : elements) {
					String name = e.getAttribute("name");
					if (!name.isEmpty()) {
						args.add(name);
						target.sampleValues.put(name, e.getAttribute("value"));
					}
				}
				if (methodIsPost) {
					target.postArgs.addAll(args);
				} else {
					target.getArgs.addAll(args);
				}
			}
		}
		
		return targets;
	}
	
	
	public URLTarget parseURL(HtmlPage page, String url) {
		if (url.isEmpty()) {
			return null;
		}
		URL fqUrl = null;
		try {
			fqUrl = page.getFullyQualifiedUrl(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		// Only handle http(s)
		if (!fqUrl.getProtocol().startsWith("http")) {
			return null;
		}
		
		// Stay on target site
		URL targetSite = null;
		try {
			targetSite = new URL(Config.TARGET);
		} catch (MalformedURLException e) {
			return null;
		}
		if (!fqUrl.getHost().equalsIgnoreCase(targetSite.getHost())) {
			System.out.println("Ignoring: " + fqUrl);
			return null;
		}
		
		// Create the target
		URLTarget target = new URLTarget(fqUrl.toExternalForm().split("\\?|\\#")[0]);
		target.samplePages.add(fqUrl.toExternalForm());
		if (target.originalContent.isEmpty()) {
			target.originalContent = page.getWebResponse().getContentAsString();
		}
		if (fqUrl.getQuery() != null) {
			for (String param : fqUrl.getQuery().split("&")) {
	            String pair[] = param.split("=");
	            try {
					String key = URLDecoder.decode(pair[0], "UTF-8");
					target.getArgs.add(key);
					if (pair.length > 1) {
						String value = URLDecoder.decode(pair[1], "UTF-8");
						target.sampleValues.put(key, value);
					}
				} catch (UnsupportedEncodingException e) {
					target.getArgs.add(pair[0]);
					target.sampleValues.put(pair[0], pair[1]);
				}
	        }
		}
		return target;
	}
}
