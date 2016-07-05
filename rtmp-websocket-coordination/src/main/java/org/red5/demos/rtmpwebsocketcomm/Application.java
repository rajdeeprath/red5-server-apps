package org.red5.demos.rtmpwebsocketcomm;

import org.red5.demos.rtmpwebsocketcomm.utils.CommlinkUtils;
import org.red5.logging.Red5LoggerFactory;
import org.red5.net.websocket.WebSocketPlugin;
import org.red5.net.websocket.WebSocketScopeManager;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
import org.red5.server.plugin.PluginRegistry;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class Application extends MultiThreadedApplicationAdapter implements ApplicationContextAware {

    private static Logger log = Red5LoggerFactory.getLogger(Application.class);

    @SuppressWarnings("unused")
    private ApplicationContext applicationContext;
    
    
    private Mode mode;


	
	
	

	public Mode getMode() {
		return mode;
	}






	public void setMode(Mode mode) {
		this.mode = mode;
	}






	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    
	
	

    
    @Override
	public boolean appConnect(IConnection conn, Object[] arg1) {
		// TODO Auto-generated method stub
    	
    	log.info("Requesting connection for scope " + CommlinkUtils.getScopePath(conn));
    	return super.appConnect(conn, arg1);
	}







	@Override
    public boolean appStart(IScope scope) {
        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin("WebSocketPlugin")).getManager();
        manager.addApplication(scope);
        return super.appStart(scope);
    }

    
	
	
	
	
    @Override
    public void appStop(IScope scope) {
        WebSocketScopeManager manager = ((WebSocketPlugin) PluginRegistry.getPlugin("WebSocketPlugin")).getManager();
        manager.removeApplication(scope);
        super.appStop(scope);
    }

}
