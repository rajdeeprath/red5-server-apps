package org.red5.server.api.stream;

import org.red5.server.api.IClient;
import org.red5.server.api.stream.data.DVRStreamInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;

public interface IDVRStream {

	public void publish(IClient client);
	
	public void unpublish(IClient client);
	
	public boolean isInUse();
	
	public boolean shutdown();
	
	public void getDefaultStreamInfo(ExtendedDVRStreamInfo info);
	
	public ExtendedDVRStreamInfo getStreamInfo();
	
	public void setStreamInfo(DVRStreamInfo streamInfo);
	
	public void onStopRecord();
	
	public void onStartRecord();	
	
	public String getName();
	
	public void setName(String name);
	
	public boolean isRecording();
	
	public void setRecording(boolean isRecording);
}
