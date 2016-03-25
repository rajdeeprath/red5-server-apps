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

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
//import org.slf4j.Logger;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.ISubscriberStream;
import org.red5.server.api.stream.data.DVRStreamInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;
import org.slf4j.Logger;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter implements IDVRApplication{

	private static Logger log = Red5LoggerFactory.getLogger(Application.class);
	
	
	
	@Override
	public void FCPublish(String streamName) {
		// TODO Auto-generated method stub
		super.FCPublish(streamName);
	}

	@Override
	public void FCSubscribe(String streamName) {
		// TODO Auto-generated method stub
		super.FCSubscribe(streamName);
	}

	@Override
	public void FCUnpublish() {
		// TODO Auto-generated method stub
		super.FCUnpublish();
	}

	@Override
	public void FCUnpublish(String streamName) {
		// TODO Auto-generated method stub
		super.FCUnpublish(streamName);
	}

	@Override
	public boolean appConnect(IConnection arg0, Object[] arg1) {
		// TODO Auto-generated method stub
		return super.appConnect(arg0, arg1);
	}

	@Override
	public void appDisconnect(IConnection arg0) {
		// TODO Auto-generated method stub
		super.appDisconnect(arg0);
	}

	@Override
	public boolean appStart(IScope arg0) {
		// TODO Auto-generated method stub
		return super.appStart(arg0);
	}

	@Override
	public void appStop(IScope arg0) {
		// TODO Auto-generated method stub
		super.appStop(arg0);
	}

	@Override
	public void streamBroadcastClose(IBroadcastStream arg0) {
		// TODO Auto-generated method stub
		super.streamBroadcastClose(arg0);
	}

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamBroadcastStart(stream);
	}

	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamPublishStart(stream);
	}

	@Override
	public void streamRecordStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamRecordStart(stream);
	}

	@Override
	public void streamRecordStop(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamRecordStop(stream);
	}

	@Override
	public void streamSubscriberClose(ISubscriberStream stream) {
		// TODO Auto-generated method stub
		super.streamSubscriberClose(stream);
	}

	@Override
	public void streamSubscriberStart(ISubscriberStream stream) {
		// TODO Auto-generated method stub
		super.streamSubscriberStart(stream);
	}

	@Override
	public void releaseStream(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getStreamLength(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void DVRSubscribe(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DVRUnSubscribe(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DVRSetStreamInfo(DVRStreamInfo name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExtendedDVRStreamInfo DVRGetStreamInfo(String streamName) {
		// TODO Auto-generated method stub
		return null;
	}

}
