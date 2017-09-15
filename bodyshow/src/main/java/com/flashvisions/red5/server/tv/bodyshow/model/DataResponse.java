package com.flashvisions.red5.server.tv.bodyshow.model;

public class DataResponse extends Response {
	
	private Object data;
	
	private static String status = "data";
	
	
	public DataResponse(){
		super(status);
	}
	
	
	public DataResponse(int httpCode, Object data){
		super(status, httpCode);
		this.data = data;
	}
	

	

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
