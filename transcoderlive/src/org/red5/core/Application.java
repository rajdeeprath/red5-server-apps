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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
//import org.slf4j.Logger;
import org.red5.server.api.stream.IBroadcastStream;

import com.flashvisions.server.rtmp.transcoder.context.TranscodeRequest;
import com.flashvisions.server.rtmp.transcoder.decorator.RTMPTranscoderResource;
import com.flashvisions.server.rtmp.transcoder.exception.InvalidTranscoderResourceException;
import com.flashvisions.server.rtmp.transcoder.exception.TranscoderException;
import com.flashvisions.server.rtmp.transcoder.facade.Red5TranscoderFacade;
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
	
	
	private static ITranscoderFacade facade;
	
	private String workingDirectory;
	private String ffmpegPath;
	private String templateDirectory;
	private String homeDirectory;
	private String transcoderTemplate;
	

	/**
	 * @return the workingDirectory
	 */
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	/**
	 * @return the ffmpegPath
	 */
	public String getFfmpegPath() {
		return ffmpegPath;
	}

	/**
	 * @param ffmpegPath the ffmpegPath to set
	 */
	public void setFfmpegPath(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}

	/**
	 * @return the templateDirectory
	 */
	public String getTemplateDirectory() {
		return templateDirectory;
	}

	/**
	 * @param templateDirectory the templateDirectory to set
	 */
	public void setTemplateDirectory(String templateDirectory) {
		this.templateDirectory = templateDirectory;
	}

	/**
	 * @return the homeDirectory
	 */
	public String getHomeDirectory() {
		return homeDirectory;
	}

	/**
	 * @param homeDirectory the homeDirectory to set
	 */
	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	/**
	 * @return the transcoderTemplate
	 */
	public String getTranscoderTemplate() {
		return transcoderTemplate;
	}

	/**
	 * @param transcoderTemplate the transcoderTemplate to set
	 */
	public void setTranscoderTemplate(String transcoderTemplate) {
		this.transcoderTemplate = transcoderTemplate;
	}

	/* (non-Javadoc)
	 * @see org.red5.server.adapter.MultiThreadedApplicationAdapter#appStart(org.red5.server.api.scope.IScope)
	 */
	@Override
	public boolean appStart(IScope arg0) {
		// TODO Auto-generated method stub
		
		/* Boot strap */
		ITranscoderFacade facade = Red5TranscoderFacade.getInstance();
		facade.setFFmpegPath(ffmpegPath);
		facade.setHomeDirectory(homeDirectory);
		facade.setWorkingDirectory(workingDirectory);
		facade.setTemplateDirectory(templateDirectory);
		facade.setOperatingMediaServer("red5"); // to be removed in future
		
		try {
			facade.init();
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.appStart(arg0);
	}

	/* (non-Javadoc)
	 * @see org.red5.server.adapter.MultiThreadedApplicationAdapter#appStop(org.red5.server.api.scope.IScope)
	 */
	@Override
	public void appStop(IScope arg0) {
		// TODO Auto-generated method stub
		super.appStop(arg0);
	}

	/* (non-Javadoc)
	 * @see org.red5.server.adapter.MultiThreadedApplicationAdapter#streamBroadcastClose(org.red5.server.api.stream.IBroadcastStream)
	 */
	@Override
	public void streamBroadcastClose(IBroadcastStream arg0) {
		// TODO Auto-generated method stub
		super.streamBroadcastClose(arg0);
		
		/* fire request */
		try 
		{
			ITranscoderFacade facade = Red5TranscoderFacade.getInstance();
			facade.abortTranscode();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	/* (non-Javadoc)
	 * @see org.red5.server.adapter.MultiThreadedApplicationAdapter#streamBroadcastStart(org.red5.server.api.stream.IBroadcastStream)
	 */
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamBroadcastStart(stream);
		
		
		/* Transcode request Object (overrides global objects)*/
		TranscodeRequest request = new TranscodeRequest();
		request.setWorkingDirectory(this.getHomeDirectory() + File.separator + "webapps" + File.separator + getScope().getName() +  File.separator + "streams" + File.separator + "hls");
		request.setTemplateFileName(transcoderTemplate);
		request.setCleanUpSegmentsOnExit(true);
		
		
		/* fire request */
		try {
			ArrayList<IProperty> inputflags = new ArrayList<IProperty>(Arrays.asList(new Property("-re")));
			
			IScope scope = this.getScope();
			IConnection conn = Red5.getConnectionLocal();
			
			ITranscoderFacade facade = Red5TranscoderFacade.getInstance();
			facade.doTranscode(new RTMPTranscoderResource(new StreamMedia("rtmp://" + conn.getHost() + getScope().getContextPath() + "/" + stream.getPublishedName()),inputflags), request);
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTranscoderResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.red5.server.adapter.MultiThreadedApplicationAdapter#streamPublishStart(org.red5.server.api.stream.IBroadcastStream)
	 */
	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		// TODO Auto-generated method stub
		super.streamPublishStart(stream);
	}

}
