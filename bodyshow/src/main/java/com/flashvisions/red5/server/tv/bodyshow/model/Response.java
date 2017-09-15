package com.flashvisions.red5.server.tv.bodyshow.model;

abstract class Response {
	
	private String status;
	
	private int httpCode;
	
	
	
	public Response(){
		
	}
	
	
	public Response(String status){
		this.status = status;
	}
	
	
	public Response(String status, int httpCode){
		this.status = status;
		this.httpCode = httpCode;
	}

	
	public Response(int httpCode){
		this.httpCode = httpCode;
	}
	

	public String getStatus() {
		return status;
	}



	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}
	
	
	

}
