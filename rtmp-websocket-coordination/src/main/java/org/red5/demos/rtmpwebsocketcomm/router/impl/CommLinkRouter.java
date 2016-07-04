package org.red5.demos.rtmpwebsocketcomm.router.impl;


import org.red5.demos.rtmpwebsocketcomm.Application;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.scope.IScope;
import org.slf4j.Logger;


public class CommLinkRouter
{
    private static Logger log = Red5LoggerFactory.getLogger(Application.class);
    
	
	private Application app;
	
	

	public Application getApp() {
		return app;
	}



	public void setApp(Application app) {
		this.app = app;
	}



	
	public void route(String path, String message) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	public void route(IScope scope, String message) {
		// TODO Auto-generated method stub
		
	}

}
