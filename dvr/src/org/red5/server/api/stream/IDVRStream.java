package org.red5.server.api.stream;

import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.stream.data.DVRInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;

public interface IDVRStream {

	public void publish(IConnection conn);
	
	public void unpublish(IConnection client);
	
	public boolean isInUse();
	
	public void shutdown();
	
	public void getDefaultStreamInfo(DVRInfo info);
	
	public DVRInfo getStreamInfo();
	
	public void setStreamInfo(ExtendedDVRStreamInfo streamInfo);
	
	public void onStopRecord();
	
	public void onStartRecord();	
	
	public String getName();
	
	public void setName(String name);
	
	public boolean isRecording();
	
	public void setRecording(boolean isRecording);
}
