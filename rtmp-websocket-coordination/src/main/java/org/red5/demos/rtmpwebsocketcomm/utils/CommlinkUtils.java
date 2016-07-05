package org.red5.demos.rtmpwebsocketcomm.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.red5.demos.rtmpwebsocketcomm.Mode;
import org.red5.logging.Red5LoggerFactory;
import org.red5.net.websocket.WebSocketConnection;
import org.red5.server.api.IConnection;
import org.slf4j.Logger;

public class CommlinkUtils {
	
	private static final Logger logger = Red5LoggerFactory.getLogger(CommlinkUtils.class);
	
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
	
	
	
	
	public static Mode getMode(Object conn)
	{
		try
		{
			if(conn instanceof IConnection)
			{
				IConnection connection = (IConnection) conn;
				String queryString = (String) connection.getConnectParams().get("queryString");
				if(queryString != null && queryString.length()>1)
				return Mode.QUERYSTRING;
				else
				return Mode.PATH;
			}
			else if(conn instanceof WebSocketConnection)
			{
				WebSocketConnection connection = (WebSocketConnection) conn;
				Map<String, ?> queryStringMap = connection.getQuerystringParameters();
				if(queryStringMap != null && queryStringMap.size()>1)
				return Mode.QUERYSTRING;
				else
				return Mode.PATH;
			}
		}
		catch(Exception e)
		{
			logger.error("Unable to determine mode " + e.getMessage());
		}
		
		return Mode.UNKNOWN;
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
