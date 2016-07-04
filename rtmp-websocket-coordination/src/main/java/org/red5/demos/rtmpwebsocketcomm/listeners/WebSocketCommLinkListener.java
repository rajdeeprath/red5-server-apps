package org.red5.demos.rtmpwebsocketcomm.listeners;

import java.util.HashSet;
import java.util.Set;

import org.red5.demos.rtmpwebsocketcomm.router.impl.ArduinoCommLinkRouter;
import org.red5.logging.Red5LoggerFactory;
import org.red5.net.websocket.WebSocketConnection;
import org.red5.net.websocket.listener.WebSocketDataListener;
import org.red5.net.websocket.model.WSMessage;
import org.slf4j.Logger;

public class WebSocketCommLinkListener extends WebSocketDataListener {


	private static final Logger logger = Red5LoggerFactory.getLogger(WebSocketCommLinkListener.class);
	private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();
	private ArduinoCommLinkRouter router;
	
	
	@Override
	public void onWSMessage(WSMessage message) {
		// TODO Auto-generated method stub

		logger.info("onWSMessage - Message received");
        
	}

	
	
	
	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return super.getProtocol();
	}








	@Override
	public void setProtocol(String protocol) {
		// TODO Auto-generated method stub
		super.setProtocol(protocol);
	}








	@Override
	public void onWSConnect(WebSocketConnection conn) {
		// TODO Auto-generated method stub

		logger.info("onWSConnect");
		logger.debug("WebSocket connection path: {}", conn.getPath());
		
		connections.add(conn);
	}

	
	
	
	@Override
	public void onWSDisconnect(WebSocketConnection conn) {
		// TODO Auto-generated method stub
		logger.info("onWSDisconnect");
		
		if(connections.contains(conn))
		connections.remove(conn);
	}




	public ArduinoCommLinkRouter getRouter() {
		return router;
	}




	public void setRouter(ArduinoCommLinkRouter router) {
		this.router = router;
	}

}
