package com.fuzzer.links;

import java.io.IOException;
import java.net.MalformedURLException;

import com.fuzzer.config.Config;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class PasswordAuth {

	
	
	public static void login(WebClient wc) {
		if (Config.LOGIN_URL == null) {
			return;
		}
		if (!Config.LOGIN_URL.startsWith(Config.TARGET)) {
			return;
		}
		System.out.println("Logging Into Site...");
		HtmlPage page = null;
		try {
			page = wc.getPage(Config.LOGIN_URL);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		for (HtmlForm form : page.getForms()) {
			if (Config.LOGIN_USER_FIELD != null) {
				try {
					final HtmlTextInput userField;
					userField = form.getInputByName(Config.LOGIN_USER_FIELD);
					userField.setValueAttribute(Config.LOGIN_USER);
				} catch (Exception e) {
					continue;
				}
			}
			
			final HtmlPasswordInput passField;
			try {
				passField = form.getInputByName(Config.LOGIN_PASS_FIELD);
				passField.setValueAttribute(Config.LOGIN_PASS);
			} catch (Exception e) {
				continue;
			}

			try {
				HtmlSubmitInput submit = (HtmlSubmitInput) form.getFirstByXPath("//input[@type='submit']");
				submit.click();
				System.out.println("Logged In");
			} catch (IOException e) {
				System.out.println("Could Not Login To Site");
			}
		}
	}
}
