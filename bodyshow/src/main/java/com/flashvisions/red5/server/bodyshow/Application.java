package com.flashvisions.red5.server.bodyshow;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Copyright (c) 2006-2008 by respective authors (see below). All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later 
 * version. 
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IClientBroadcastStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flashvisions.red5.server.tv.bodyshow.model.LiveStream;




public class Application extends MultiThreadedApplicationAdapter {

	private Logger logger = LoggerFactory.getLogger(Application.class);
	private IScope appScope;
	
	private String accessToken;


	
	
	@Override
	public boolean appStart(IScope arg0) {
		logger.info("application started!");
		this.appScope = arg0;
		return super.appStart(arg0);
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public List<LiveStream> getLiveStreams()
	{
		List<LiveStream> streams = new ArrayList<LiveStream>();
		Set<String> names = this.getBroadcastStreamNames(appScope); 
		
		for (String name : names) 
		{
		    IClientBroadcastStream stream =  (IClientBroadcastStream) this.getBroadcastStream(appScope, name);
		    LiveStream info = new LiveStream(name);
		    info.setStartTime(getUTCTime(stream.getCreationTime()));
		    streams.add(info);
		}
		
		return streams;
	}
	
	 
	
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	private String getUTCTime(long time)
	{
		SimpleDateFormat sdf = new SimpleDateFormat();
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return sdf.format(new Date(time));
	}




	public String getAccessToken() {
		return accessToken;
	}




	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
