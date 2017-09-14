package com.flashvisions.red5.server.bodyshow.security;

import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IStreamPublishSecurity;

import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;


public class PublishSecurity extends SecurityBase implements IStreamPublishSecurity {
	
	private Boolean enablePublish = true;
	private Boolean PUBLISHNameAuth = true;
	private Boolean HTMLDomainsAuth = true;
	private Boolean SWFDomainsAuth = true;
	private String[] allowedPublishNames;
	private String[] allowedHTMLDomains;
	private String[] allowedSWFDomains;
	private String htmlDomains = "allowedHTMLdomains.txt";
	private String swfDomains = "allowedSWFdomains.txt";
	private String publishNames = "allowedSWFdomains.txt";
    
  
	public PublishSecurity()
    {
    }
	
	public void setPublishNames(String value)
	{
		publishNames = value;
	}
	
	public void setHtmlDomains(String value)
	{
		htmlDomains = value;
	}
	
	public void setSwfDomains(String value)
	{
		swfDomains = value;
	}
	
	public void setEnablePublish(Boolean value)
	{
		enablePublish = value;
	}
    
	public void init()
	{
		this.allowedHTMLDomains = this.readValidDomains(htmlDomains,"HTMLDomains");
		this.allowedSWFDomains = this.readValidDomains(swfDomains, "SWFDomains");
		this.allowedPublishNames = this.readValidPublishes(publishNames);
	}
	
	@Override
	public boolean isPublishAllowed(IScope scope, String name, String mode) {
		
		if (enablePublish)
		{
			IConnection conn = Red5.getConnectionLocal();
			
			try {
				String pageUrl = conn.getConnectParams().get("pageUrl").toString();
				String swfUrl = conn.getConnectParams().get("swfUrl").toString();
				String ip = conn.getRemoteAddress();
				
				if (( ip != "127.0.0.1") && HTMLDomainsAuth &&  !this.validate( pageUrl, this.allowedHTMLDomains ) )
				{
					return false;
				}
		
				if ((ip != "127.0.0.1") && SWFDomainsAuth &&  !this.validate(swfUrl, this.allowedSWFDomains ) )
				{
					return false;
				}
				
				/* Check for publish security flag */
				
				if(PUBLISHNameAuth && !this.validateStream(name, this.allowedPublishNames)) 
				{
					return false;
				}
				
			} catch (Exception e) {
				if (HTMLDomainsAuth || SWFDomainsAuth) return false;
				return true;
			}
			return true;
		}
		
		return false;
    }
	
	/********* Validate publish name **********/
	
	private Boolean validateStream(String name, String[] patterns)
	{
		// Convert to lower case
		name = name.toLowerCase();
		if (ArrayUtils.contains(patterns, name)) return true;
		return false;
	}
	
	
	/********* Validate Domain name **********/
	
	private Boolean validate(String url, String[] patterns)
	{
		// Convert to lower case
		url = url.toLowerCase();
		int domainStartPos = 0; // domain start position in the URL
		int domainEndPos = 0; // domain end position in the URL
		
		switch (url.indexOf( "://" ))
		{
			case 4:
				if(url.indexOf( "http://" ) ==0)
					domainStartPos = 7;
				break;
			case 5:
				if(url.indexOf( "https://" ) ==0)
					domainStartPos = 8;
				break;
		}
		if(domainStartPos == 0)
		{
			// URL must be HTTP or HTTPS protocol based
			return false;
		}
		domainEndPos = url.indexOf("/", domainStartPos);
		if(domainEndPos>0)
		{
			int colonPos = url.indexOf(":", domainStartPos); 
			if( (colonPos>0) && (domainEndPos > colonPos))
			{
				// probably URL contains a port number
				domainEndPos = colonPos; // truncate the port number in the URL
			}
		}
		
		url = url.substring(domainStartPos, domainEndPos);
		
		// if has www in begining thne remove it
		
		if(url.indexOf("www.")== 0)
			url = url.replace("www.", "");
		
		if (ArrayUtils.contains(patterns, url)) return true; 
		
		return false;
	}

	
	private String[] readValidDomains(String fileName , String domainsType )
	{
		String[] domainsArray = new String[100];
		
		try {
			DataInputStream in = new DataInputStream(application.getResource(fileName).getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			
			int index = 0;
			@SuppressWarnings("unused")
			int lineCount = 0;
			
			String strLine = "";
		   
		    while ((strLine = br.readLine()) != null)   {
		    	if( strLine.equals("")  || strLine.indexOf("#") == 0)
				{
					continue;
				}
		    	
		    	if(strLine.indexOf(" ") < 0)
				{
		
		    		index++;
		    		domainsArray[index] =  strLine.toLowerCase();
					
					if(strLine.trim().equals("*"))
					{
						if (domainsType.equals("HTMLDomains"))
						{
							HTMLDomainsAuth = false;
						} else if (domainsType.equals("SWFDomains")) {
							SWFDomainsAuth = false;	
						}
						
					}
				}
		    }
	    
		    in.close();
		} catch (Exception e) {

			e.printStackTrace();
			if (domainsType.equals("HTMLDomains"))
			{	
				HTMLDomainsAuth = false;
			} else if (domainsType.equals("HTMLDomains")) {
				SWFDomainsAuth = false;	
			}
		}

		return domainsArray;
	}
	
//-------------- Read in  valid publish names ----------------------
	
	private String[] readValidPublishes(String fileName)
	{
		String[] publishArray = new String[100];
		
		try {
			DataInputStream in = new DataInputStream(application.getResource(fileName).getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			
			int index = 0;
			@SuppressWarnings("unused")
			int lineCount = 0;
			
			String strLine = "";
		   
		    while ((strLine = br.readLine()) != null)   {
		    	if( strLine.equals("")  || strLine.indexOf("#") == 0)
				{
					continue;
				}
		    	
		    	if(strLine.indexOf(" ") < 0)
				{
		
		    		index++;
		    		publishArray[index] =  strLine.toLowerCase();
					
		    		// disable publish security
		    		
					if(strLine.trim().equals("*"))
					{
						PUBLISHNameAuth = false;
					}
				}
		    }
	    
		    in.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			enablePublish = false;
		}

		return publishArray;
	}
    
}