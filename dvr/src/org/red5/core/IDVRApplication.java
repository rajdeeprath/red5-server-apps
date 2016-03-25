package org.red5.core;

import org.red5.server.api.stream.data.DVRStreamInfo;
import org.red5.server.api.stream.data.ExtendedDVRStreamInfo;

public interface IDVRApplication {

	public void releaseStream(String name);
	
	public void DVRSubscribe(String name);
	
	public void DVRUnSubscribe(String name);
	
	public void DVRSetStreamInfo(DVRStreamInfo name);
	
	public ExtendedDVRStreamInfo DVRGetStreamInfo(String streamName);
}
