package com.flashvisions.red5.server.tv.bodyshow;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;

public class SecurityBase {

	protected MultiThreadedApplicationAdapter application;
	
	public void setApplication(MultiThreadedApplicationAdapter app)
	{
		application = app;
	}
}


