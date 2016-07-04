package org.red5.demos.rtmpwebsocketcomm.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.red5.net.websocket.WebSocketConnection;
import org.red5.server.api.IConnection;

public class CommlinkUtils {
	
	
	
	public static String getScopePath(IConnection conn)
	{
		try
		{
			Map<String, String> params = splitQuery((String) conn.getConnectParams().get("queryString"));
			return params.get("scope");
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	
	
	
	public static String getScopePath(WebSocketConnection conn)
	{
		try
		{
			Map<String, ?> params = getSanitizedMap(conn.getQuerystringParameters());
			return (String) params.get("scope");
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	

	
	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    
	    // remove ?
	    if(query.startsWith("?"))
	    query = query.replace("?", "");
	    	
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}
	
	
	
	private static Map<String, ?> getSanitizedMap(Map<String, ?> queryStringMap)
	{
		Map<String, Object> sanitized = new HashMap<String, Object>();
		
		for (Map.Entry<String, ?> entry : queryStringMap.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		    if(entry.getKey().startsWith("?"))
		    {
		    	String key = entry.getKey().substring(1);
		    	Object value = entry.getValue();
		    	sanitized.put(key, value);
		    }
		}
		
		return sanitized;
	}
}
