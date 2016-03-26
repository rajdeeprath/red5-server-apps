package org.red5.core;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.red5.core.utils.DVRUtils;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.stream.DVRStream;
//import org.slf4j.Logger;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IDVRStream;
import org.red5.server.api.stream.ISubscriberStream;
import org.red5.server.api.stream.data.DVRStreamInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;
import org.red5.server.api.stream.data.StatusCode;
import org.slf4j.Logger;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter implements IDVRApplication{

	private static Logger log = Red5LoggerFactory.getLogger(Application.class);
	
	private static HashMap<String, IDVRStream> streams;
	private static Double VERSION = 1.0;
	


	@Override
	public void appDisconnect(IConnection arg0) {
		// TODO Auto-generated method stub
		
		Iterator<Map.Entry<String, IDVRStream>> iterator = streams.entrySet().iterator();
		while(iterator.hasNext()){
		   Map.Entry<String, IDVRStream> entry = iterator.next();
		   String key = entry.getKey();
		   IDVRStream stream = entry.getValue();
		   removeSubscriber(stream, key);
		}


		super.appDisconnect(arg0);
	}
	
	

	@Override
	public boolean appStart(IScope arg0) {
		// TODO Auto-generated method stub
		streams = new HashMap<String, IDVRStream>();
		return super.appStart(arg0);
	}
	
	
	
	
	
	public void removeSubscriber(IDVRStream stream, String streamFullName)
	{
		if(!stream.isInUse() && streams.containsKey(streamFullName))
		{
			stream.shutdown();
			streams.remove(streamFullName);
		}
	}
	
	
	
	
	
	public IDVRStream getStream(String streamFullName, boolean create)
	{
		if(streams.containsKey(streamFullName) && create)
		{
			log.debug("Creating new dvr stream reference");
			streams.put(streamFullName, new DVRStream(streamFullName));
		}
		
		IDVRStream stream = (streams.containsKey(streamFullName))?streams.get(streamFullName):null;
		return stream;
	}
	
	
	
	
	
	
	@Override
	public void FCPublish(String streamName) {
		// TODO Auto-generated method stub
		
		log.debug("Inside FCPublish - stream name: " + streamName);
		
		IConnection conn = 	Red5.getConnectionLocal();
		((IServiceCapableConnection) conn).invoke("onFCPublish", new Object[]{new StatusCode("NetStream.Publish.Start", streamName)});  
		
		super.FCPublish(streamName);
	}
	
	
	
	
	
	
	
	
	@Override
	public void FCUnpublish(String streamName) {
		
		log.debug("Inside FCPublish - stream name: " + streamName);
		
		IConnection conn = 	Red5.getConnectionLocal();
		((IServiceCapableConnection) conn).invoke("onFCUnpublish", new Object[]{new StatusCode("NetStream.Unpublish.Success", streamName)});
		
		super.FCUnpublish(streamName);
	}

	
	
	
	
	
	
	@Override
	public void FCSubscribe(String streamName) {
		
		log.debug("Inside FCSubscribe - stream name: " + streamName);
		
		IConnection conn = 	Red5.getConnectionLocal();
		((IServiceCapableConnection) conn).invoke("onFCSubscribe", new Object[]{new StatusCode("NetStream.Play.Start", streamName)});
	
		super.FCSubscribe(streamName);
	}
	
	
	
	
	
	
	public void FCUnSubscribe(String streamName) {
		
		log.debug("Inside FCUnSubscribe - stream name: " + streamName);
		
		IConnection conn = 	Red5.getConnectionLocal();
		((IServiceCapableConnection) conn).invoke("FCUnSubscribe", new Object[]{new StatusCode("NetStream.Play.Stop", streamName)});
	}


	
	
	
	
	@Override
	public void releaseStream(String name) {
		// TODO Auto-generated method stub
		log.debug("Inside releaseStream - stream name: " + name);
	}

	
	
	
	
	@Override
	public double getStreamLength(String name) {
		// TODO Auto-generated method stub
		log.debug("Inside getStreamLength - stream name: " + name);
		return super.getStreamLength(name);
	}

	
	
	
	
	
	@Override
	public void DVRSubscribe(String name) {
		// TODO Auto-generated method stub
		
		log.debug("Inside DVRSubscribe - stream name: " + name);
		
		String streamFullName = DVRUtils.getStreamFullName(name);
		IDVRStream stream = this.getStream(streamFullName, true);
		
		IConnection conn = 	Red5.getConnectionLocal();
		((IServiceCapableConnection) conn).invoke("onDVRSubscribe", new Object[]{new StatusCode("NetStream.Play.Start", name)});
	}

	
	
	
	
	
	@Override
	public void DVRUnSubscribe(String name) {
		// TODO Auto-generated method stub
		
		log.debug("Inside DVRUnSubscribe - stream name: " + name);
		
		String streamFullName = DVRUtils.getStreamFullName(name);
		IDVRStream stream = this.getStream(streamFullName, true);
	
		IConnection conn = 	Red5.getConnectionLocal();
		((IServiceCapableConnection) conn).invoke("onDVRUnSubscribe", new Object[]{new StatusCode("NetStream.Play.Stop", name)});
	}

	
	
	
	
	@Override
	public void DVRSetStreamInfo(DVRStreamInfo info) {
		// TODO Auto-generated method stub
		log.debug("Inside DVRSetStreamInfo");
		
		String streamFullName = DVRUtils.getStreamFullName(info.streamName);
		IDVRStream stream = this.getStream(streamFullName, true);
		stream.setStreamInfo(info);
	}

	
	
	
	@Override
	public ExtendedDVRStreamInfo DVRGetStreamInfo(String streamName) {
		// TODO Auto-generated method stub
		log.debug("Inside ExtendedDVRStreamInfo");
		
		String streamFullName = DVRUtils.getStreamFullName(streamName);
		IDVRStream stream = this.getStream(streamFullName, true);
		
		return stream.getStreamInfo();
	}


	
	
	
	

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamBroadcastStart");
		
		super.streamBroadcastStart(stream);
	}


	
	

	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamPublishStart");
		
		String streamFullName = DVRUtils.getStreamFullName(stream.getPublishedName());
		IDVRStream dvrStream = this.getStream(streamFullName, true);
		
		IConnection conn = Red5.getConnectionLocal();
		dvrStream.publish(conn);
		
		super.streamPublishStart(stream);
	}
	
	
	
	
	
	
	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamBroadcastClose");
		
		String streamFullName = DVRUtils.getStreamFullName(stream.getPublishedName());
		IDVRStream dvrStream = this.getStream(streamFullName, false);
		
		if(dvrStream != null)
		{
			IConnection conn = Red5.getConnectionLocal();
			dvrStream.unpublish(conn);
		}
		
		super.streamBroadcastClose(stream);
	}
	
	
	



	@Override
	public void streamRecordStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamRecordStart");
		
		super.streamRecordStart(stream);
	}



	@Override
	public void streamRecordStop(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamRecordStop");
		
		super.streamRecordStop(stream);
	}



	@Override
	public void streamSubscriberClose(ISubscriberStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamSubscriberClose");
		
		super.streamSubscriberClose(stream);
	}



	@Override
	public void streamSubscriberStart(ISubscriberStream stream) {
		// TODO Auto-generated method stub
		log.debug("Inside streamSubscriberStart");
		
		super.streamSubscriberStart(stream);
	}
	
	

}
