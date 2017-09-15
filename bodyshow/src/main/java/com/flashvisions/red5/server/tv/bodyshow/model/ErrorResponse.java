package com.flashvisions.red5.server.tv.bodyshow.model;

public class ErrorResponse  extends Response{
	
	private static String status = "error";
	
	private String errorMessage;
	
	
	public ErrorResponse(){
		super(status);
	}
	
	
	public ErrorResponse(int httpCode, String errorMessage){
		super(status, httpCode);
		this.errorMessage = errorMessage;
	}
	

	@Override
	public String getStatus() {
		return status;
	}



	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
