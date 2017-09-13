package com.flashvisions.red5.server.tv.bodyshow.model;

public class LiveStream {
	
	String name;
	
	String startTime;
	
	
	public LiveStream(){
		
	}
	
	
	public LiveStream(String name){
		
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	
	
}
