package com.fuzzer.util;

import com.fuzzer.links.URLTarget;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class FuzzerUtil {

	public static boolean checkForEcho(HtmlPage page, URLTarget original, String exploit) {
		return page.getWebResponse().getContentAsString().contains(exploit)
				&& !original.originalContent.contains(exploit);
	}
}
