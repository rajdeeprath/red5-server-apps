package com.flashvisions.red5.server.tv.bodyshow;

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
import org.red5.server.api.stream.IBroadcastStream;
//import org.slf4j.Logger;
import org.red5.server.api.stream.ISubscriberStream;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter {

	//private static Logger log = Red5LoggerFactory.getLogger(Application.class);

	
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
	public void streamBroadcastClose(IBroadcastStream stream) {
		log.info("streamBroadcastClose {} ", stream);
		super.streamBroadcastClose(stream);
	}

	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		log.info("streamBroadcastStart {} ", stream);
		super.streamBroadcastStart(stream);
	}

	@Override
	public void streamRecordStart(IBroadcastStream stream) {
		log.info("streamRecordStart {} ", stream);
		super.streamRecordStart(stream);
	}

	@Override
	public void streamSubscriberClose(ISubscriberStream stream) {
		log.info("streamSubscriberClose {} ", stream);
		super.streamSubscriberClose(stream);
	}

	@Override
	public void streamSubscriberStart(ISubscriberStream stream) {
		log.info("streamSubscriberStart {} ", stream);
		super.streamSubscriberStart(stream);
	}

}
