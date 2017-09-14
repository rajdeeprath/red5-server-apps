package com.flashvisions.red5.server.tv.bodyshow;

import org.red5.server.api.stream.IStreamPlaybackSecurity;
import org.red5.server.api.stream.IStreamPublishSecurity;


public class SecurityLoader extends SecurityBase {

	private IStreamPlaybackSecurity playbackSecurity;
	private IStreamPublishSecurity publishSecurity;
	
	public void setPlaybackSecurity(IStreamPlaybackSecurity playback)
	{
		playbackSecurity = playback;
	}
	
	public void setPublishSecurity(IStreamPublishSecurity publish)
	{
		publishSecurity = publish;
	}
	
	
	public void init()
	{
		application.registerStreamPlaybackSecurity(playbackSecurity);
		application.registerStreamPublishSecurity(publishSecurity);
	}

}
