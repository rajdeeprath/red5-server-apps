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

import java.util.ArrayList;
import java.util.Arrays;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
//import org.slf4j.Logger;



import org.red5.server.api.stream.IBroadcastStream;

import com.flashvisions.server.rtmp.transcoder.decorator.RTMPTranscoderResource;
import com.flashvisions.server.rtmp.transcoder.exception.InvalidTranscoderResourceException;
import com.flashvisions.server.rtmp.transcoder.exception.TranscoderException;
import com.flashvisions.server.rtmp.transcoder.facade.TranscoderFacade;
import com.flashvisions.server.rtmp.transcoder.interfaces.IProperty;
import com.flashvisions.server.rtmp.transcoder.interfaces.ITranscoderFacade;
import com.flashvisions.server.rtmp.transcoder.pojo.Property;
import com.flashvisions.server.rtmp.transcoder.pojo.io.StreamMedia;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter {
	
	//private static Logger log = Red5LoggerFactory.getLogger(Application.class);

	ITranscoderFacade facade;


	@Override
	public boolean appStart(IScope arg0) {
		// TODO Auto-generated method stub
		
		facade = TranscoderFacade.getInstance();
		facade.setFFmpegPath("C:\\ffmpeg\\bin\\ffmpeg.exe");
		facade.setHomeDirectory("C:\\red5-1.0.2");
		facade.setWorkingDirectory("C:\\red5-1.0.2\\transcoder\\");
		facade.setTemplateDirectory("C:\\red5-1.0.2\\transcoder\\templates");
		facade.setOperatingMediaServer("red5");
		try {
			facade.init();
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return super.appStart(arg0);
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
		
		IConnection conn = Red5.getConnectionLocal();
		
		if(!conn.hasAttribute("publishing"))
		{
			try {
			ArrayList<IProperty> inputflags = new ArrayList<IProperty>(Arrays.asList(new Property("-re")));
			facade.doTranscode(new RTMPTranscoderResource(new StreamMedia("rtmp://localhost/transcoderlive/stream"),inputflags), "sample-rtmp-template.xml");
			} catch (TranscoderException | InvalidTranscoderResourceException e) {
			System.out.print(e.getMessage());
			}finally{
			conn.setAttribute("publishing", true);
			}
		}
	}


	@Override
	public void streamBroadcastClose(IBroadcastStream arg0) {
		// TODO Auto-generated method stub
		super.streamBroadcastClose(arg0);
		
		IConnection conn = Red5.getConnectionLocal();
		conn.removeAttribute("publishing");
	}

	
	
	

}
