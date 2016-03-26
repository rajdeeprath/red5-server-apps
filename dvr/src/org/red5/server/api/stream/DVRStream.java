package org.red5.server.api.stream;

import java.util.Date;

import org.red5.core.Application;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.data.DVRInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public class DVRStream implements IDVRStream {

	private static Logger log = Red5LoggerFactory.getLogger(DVRStream.class);
	
	private  MultiThreadedApplicationAdapter adapter;
	
	private IScope scope;
	
	private Object publisher = null;
	
	private String name = null;
	
	private ExtendedDVRStreamInfo streamInfo = null;
	
	private boolean isRecording = false;
	
	private double streamLen = 0;
	
	
	
	public DVRStream(String name, IScope scope, MultiThreadedApplicationAdapter adapter) {
		this.name = name;
		this.adapter = adapter;
		this.scope = scope;
	}
	
	

	@Override
	public void publish(IConnection conn) {
		// TODO Auto-generated method stub
		this.publisher = conn;
	}

	@Override
	public void unpublish(IConnection client) {
		// TODO Auto-generated method stub
		this.publisher = null;
	}

	@Override
	public boolean isInUse() {
		// TODO Auto-generated method stub
		return (this.publisher != null)?true:false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		// NO OP
	}

	@Override
	public void getDefaultStreamInfo(DVRInfo info) {
		// TODO Auto-generated method stub
		
		streamLen = adapter.getStreamLength(this.name);
		if(streamLen > 0 && this.publisher != null)
		{
			info.code = "NetStream.DVRStreamInfo.Success";
			
			this.streamInfo = new ExtendedDVRStreamInfo();
			this.streamInfo.callTime = new Date();
			this.streamInfo.startRec = new Date();
			this.streamInfo.stopRec = new Date();
			this.streamInfo.stopRec = new Date();
			this.streamInfo.maxLen = adapter.getStreamLength(this.name);
			this.streamInfo.begOffset = 0;
			this.streamInfo.endOffset = 0;
			this.streamInfo.append = false;
			this.streamInfo.offline = false;
			this.streamInfo.currLen = adapter.getStreamLength(this.name);
			this.streamInfo.offline = false;
			this.streamInfo.isRec = false;
			
			info.data = this.streamInfo;
		}
		else
		{
			info.code = "NetStream.DVRStreamInfo.Failed";
			info.data = null;
		}
	}

	
	
	
	
	@Override
	public DVRInfo getStreamInfo() {
		// TODO Auto-generated method stub
		log.debug("Inside DVRStream getStreamInfo");
		
		DVRInfo info = new DVRInfo();
		if(this.streamInfo == null)
		{
			this.getDefaultStreamInfo(info);
		}
		else if(this.streamInfo.offline)
		{
			info.code = "NetStream.DVRStreamInfo.Failed";
			info.data = null;
		}
		else
		{
			info.code = "NetStream.DVRStreamInfo.Success";
			info.data = this.streamInfo;
			info.data.isRec = this.isRecording;
			info.data.currLen = adapter.getStreamLength(this.name);
		}
		
		
		return info;
	}

	@Override
	public void setStreamInfo(ExtendedDVRStreamInfo streamInfo) {
		// TODO Auto-generated method stub
		log.debug("Inside DVRStream setStreamInfo");
		
		Date currentDate = new Date();
		long currTime = currentDate.getTime();
		
		this.streamInfo = streamInfo;
		this.streamInfo.lastUpdate = currentDate;
		
		long startRecTime = 0;
		long stopRecTime = 0;
		
		if((int) streamInfo.startRec == -1 && streamInfo.startRec == null)
		{
			startRecTime = -1000;
		}
		else if(streamInfo.startRec instanceof Date && streamInfo.startRec == null)
		{
			startRecTime = ((Date) streamInfo.startRec).getTime();
		}
		else
		{
			return;
		}
		
		
		if((int) streamInfo.stopRec == -1 || streamInfo.stopRec == null)
		{
			stopRecTime = -1000;
		}
		else if(streamInfo.stopRec instanceof Date && streamInfo.stopRec == null)
		{
			stopRecTime = ((Date) streamInfo.stopRec).getTime();
		}
		else
		{
			return;
		}
		
		
		if(startRecTime == -1000 && stopRecTime == -1000)
		{
			return;
		}
		
		
		if(stopRecTime != -1000)
		{
			if(currTime < stopRecTime)
			{
				long timeDiff = startRecTime - currTime;
				new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	onStopRecord();
				            }
				        }, 
				        timeDiff 
				);
			}
			else
			{
				this.onStopRecord();
			}
		}
		
		
		if(startRecTime != -1000)
		{
			if(currTime < startRecTime)
			{
				long timeDiff = startRecTime - currTime;
				new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	onStartRecord();
				            }
				        }, 
				        timeDiff 
				);
			}
			else
			{
				this.onStartRecord();
			}
		}
		
		
	}
	
	
	
	
	
	@Override
	public void onStopRecord() {
		// TODO Auto-generated method stub
		log.debug("Inside DVRStream onStopRecord");
		this.isRecording = false;
		
		IBroadcastStream  stream = adapter.getBroadcastStream(scope, this.name);
		
		try
		{
			stream.close();
		}
		catch(Exception e)
		{
			log.error("Unable to close stream. Cause : " + e.getMessage());
		}
		
	}

	
	
	
	
	
	@Override
	public void onStartRecord() 
	{
		log.debug("Inside DVRStream onStartRecord");
		this.isRecording = true; 
		
		IBroadcastStream  stream = adapter.getBroadcastStream(scope, this.name);
		
		try
		{
			if(this.streamInfo.append)
			{
				stream.saveAs(this.name, true);
			}
			else
			{
				stream.saveAs(this.name, false);
			}
		}
		catch(Exception e)
		{
			log.error("Unable to save stream. Cause : " + e.getMessage());
		}
	}
	
	
	

	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	
	
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	
	
	@Override
	public boolean isRecording() {
		// TODO Auto-generated method stub
		return this.isRecording;
	}

	
	
	@Override
	public void setRecording(boolean isRecording) {
		// TODO Auto-generated method stub
		this.isRecording = isRecording;
	}



	public MultiThreadedApplicationAdapter getAdapter() {
		return adapter;
	}



	public void setAdapter(MultiThreadedApplicationAdapter adapter) {
		this.adapter = adapter;
	}

}
