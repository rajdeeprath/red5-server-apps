package org.red5.server.api.stream.data;

public class StatusCode {

	public String code;
	
	public String description;
	
	
	public StatusCode(){
		
	}
	
	
	public StatusCode(String code, String description){
		this.code = code;
		this.description = description;
	}
}
