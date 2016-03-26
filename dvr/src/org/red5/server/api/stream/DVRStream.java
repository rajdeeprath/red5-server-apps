package org.red5.server.api.stream;

import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.stream.data.DVRStreamInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;

@SuppressWarnings("unused")
public class DVRStream implements IDVRStream {

	private Object publisher = null;
	
	private String name = null;
	
	private DVRStreamInfo streamInfo = null;
	
	private boolean isRecording = false;
	
	
	
	public DVRStream(String name) {
		this.name = name;
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
		return false;
	}

	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getDefaultStreamInfo(ExtendedDVRStreamInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExtendedDVRStreamInfo getStreamInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStreamInfo(DVRStreamInfo streamInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopRecord() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartRecord() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRecording() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRecording(boolean isRecording) {
		// TODO Auto-generated method stub
		
	}

}
