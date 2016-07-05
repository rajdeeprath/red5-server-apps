package org.red5.demos.rtmpwebsocketcomm.listeners;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.red5.demos.rtmpwebsocketcomm.router.impl.CommLinkRouter;
import org.red5.demos.rtmpwebsocketcomm.utils.CommlinkUtils;
import org.red5.logging.Red5LoggerFactory;
import org.red5.net.websocket.WebSocketConnection;
import org.red5.net.websocket.listener.WebSocketDataListener;
import org.red5.net.websocket.model.*;
import org.slf4j.Logger;

public class WebSocketCommLinkListener extends WebSocketDataListener {


	private static final Logger logger = Red5LoggerFactory.getLogger(WebSocketCommLinkListener.class);
	private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();
	private CommLinkRouter router;
	
	
	@Override
	public void onWSMessage(WSMessage message) {
		// TODO Auto-generated method stub

		logger.info("onWSMessage - Message received");
		
		switch(message.getMessageType())
		{
			case PING:
			break;
			
			case PONG:
			break;
				
			case CLOSE:
			break;
				
			case TEXT:
			break;
		}
        
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
		logger.info("Requesting connection for scope " + CommlinkUtils.getScopePath(conn));
		
		connections.add(conn);
	}

	
	
	
	@Override
	public void onWSDisconnect(WebSocketConnection conn) {
		// TODO Auto-generated method stub
		logger.info("onWSDisconnect");
		
		if(connections.contains(conn))
		connections.remove(conn);
	}
	
	
	
	
	/**
    * Send message to all connected connections.
    * 
    * @param path
    * @param msg
    */
   public void sendToAll(String path, String message) {
       for (WebSocketConnection conn : connections) {
           if (path.equals(conn.getPath())) 
           {
               try 
               {
                   conn.send(message);
               } 
               catch (UnsupportedEncodingException e) 
               {
            	   logger.warn("Error sending messages" + e.getMessage());
               }
           } 
           else 
           {
               logger.warn("Path did not match for message {} != {}", path, conn.getPath());
           }
       }
   }




	public CommLinkRouter getRouter() {
		return router;
	}




	public void setRouter(CommLinkRouter router) {
		this.router = router;
		this.router.setWsListener(this);
	}

}
