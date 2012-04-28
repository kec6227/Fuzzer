package com.fuzzer.links;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.fuzzer.config.Config;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class URLTarget {
	
	public final String page;
	public HashSet<String> samplePages;
	
	public HashSet<String> getArgs;
	public HashSet<String> postArgs;
	
	public HashMap<String,String> sampleValues;
	
	public URLTarget(String page) {
		this.page = page;
		samplePages = new HashSet<String>();
		getArgs = new HashSet<String>();
		postArgs = new HashSet<String>();
		sampleValues = new HashMap<String, String>();
	}
	
	public String getURLStringWithGetValue(String value) {
		StringBuilder targetPage = new StringBuilder(page);
		
        Map<String,Object> map = new HashMap<String,Object>();
		for (String param : getArgs) {
			if (Config.IGNORE_INPUTS.contains(param)) {
				map.put(param, sampleValues.get(param));
			} else {
				map.put(param, value);
			}
		}
		if (!getArgs.isEmpty()) {
			targetPage.append("?");
			targetPage.append(MapQuery.urlEncodeUTF8(map));
		}
		
		return targetPage.toString();
	}
	
	public ArrayList<NameValuePair> createPostParams(String value) {
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String param : postArgs) {
			if (Config.IGNORE_INPUTS.contains(param)) {
				System.out.println(param + " = " + sampleValues.get(param));
				list.add(new NameValuePair(param, sampleValues.get(param)));
			} else {
				list.add(new NameValuePair(param, value));
			}
		}
		return list;
	}
	
	public void print() {
		System.out.println(page);
		if (!getArgs.isEmpty()) {
			System.out.println("	GET: " + Arrays.toString(getArgs.toArray(new String[]{})));
		}
		if (!postArgs.isEmpty()) {
			System.out.println("	POST: " + Arrays.toString(postArgs.toArray(new String[]{})));
		}
	}

}

class MapQuery {
    static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                urlEncodeUTF8(entry.getKey().toString()),
                urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();       
    }
    
    public static Map<String, String> getQueryMap(String url) {
    	String query = "";
		try {
			URL qurl = new URL(url);
			query = qurl.getQuery();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
