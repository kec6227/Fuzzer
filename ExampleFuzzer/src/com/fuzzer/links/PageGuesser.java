package com.fuzzer.links;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.fuzzer.config.Config;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

public class PageGuesser {

	private static List<String> DATA = Config.getSensitiveDataList();
	
	public static List<String> guessPaths(WebClient wc, String base) {
		List<String> found = new ArrayList<String>();
		
		for (String path : DATA) {
			String url = base + path;
			try {
				wc.getPage(url);
				found.add(url);
			} catch (FailingHttpStatusCodeException e) {
				if (e.getStatusCode() != 404) {
					found.add(url);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e){
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(Config.TIME_GAP_MS);
		} catch (InterruptedException e) {
		}
		
		return found;
	}
}
