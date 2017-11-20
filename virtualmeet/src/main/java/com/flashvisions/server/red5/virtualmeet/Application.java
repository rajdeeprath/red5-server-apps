package com.flashvisions.server.red5.virtualmeet;


import java.util.ArrayList;
import java.util.List;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.stream.IBroadcastStream;
//import org.slf4j.Logger;
import org.red5.server.api.stream.ISubscriberStream;
import org.red5.server.exception.ClientRejectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application extends MultiThreadedApplicationAdapter {

	private static Logger log = LoggerFactory.getLogger(Application.class);
	
	private IScope appScope;
	
	
	@Override
	public boolean appStart(IScope arg0) {
		this.appScope = arg0;
		return super.appStart(arg0);
	}
	
	
	
	

	
	@Override
	public boolean appConnect(IConnection conn, Object[] arg1) {
		
		String username;
		String roomname;
		
		try
		{
			if(arg1.length < 2){
				throw new Exception("Missing parameters");
			}
			
			username = String.valueOf(arg1[0]);
			roomname = String.valueOf(arg1[1]);
			
			if(username == null || roomname == null){
				throw new Exception("Invalid value or one or more parameters!");
			}
			
			
			if(this.hasUser(username, roomname))
			{
				throw new Exception("User by name " + username + " exists in the room defined by name " + roomname);
			}
			else
			{
				boolean added = addUser(username, roomname);
				if(!added) {
					throw new Exception("Unable to add user to room!");
				}
			}
			
			conn.setAttribute("username", username);
			conn.setAttribute("roomname", roomname);
		}
		catch(Exception e)
		{
			throw new ClientRejectedException(e.getMessage());
		}	
		
		
		return super.appConnect(conn, arg1);
	}
	
	
	
	
	
	/**
	 * 
	 * @param scope
	 * @param name
	 * @param persistent
	 * @return
	 * @throws Exception
	 */
	private ISharedObject getSharedDataStore(IScope scope, String name, boolean persistent) throws Exception
	{
		List<String> users;
		ISharedObject so; 
		
		so = this.getSharedObject(scope, name, persistent);
		if(so == null)
		{
			boolean created = this.createSharedObject(scope, name, persistent);
			if(!created){
				throw new Exception("Unable to create SharedObject by name " + name);
			}
			so = this.getSharedObject(scope, name, persistent);
		}
		
		
		// initialize the list
		if(!so.hasAttribute("users"))
		{
			users = new ArrayList<String>();
			so.setAttribute("users", users);
		}
		else
		{
			users = (List<String>) so.getListAttribute("users");
			if(users == null){
				users = new ArrayList<String>();
			}
		}
		
		
		return so;
	}

	
	
	
	
	
	
	/**
	 * 
	 * @param username
	 * @param roomname
	 * @return
	 * @throws Exception
	 */
	private boolean hasUser(String username, String roomname) throws Exception
	{
		ISharedObject so = getSharedDataStore(appScope, roomname, false);
		List<String> users = (List<String>) so.getListAttribute("users");
		
		if(users != null){
			return users.contains(username);
		}
		
		return false;
	}

	
	
	
	
	/**
	 * 
	 * @param username
	 * @param roomname
	 * @return
	 * @throws Exception
	 */
	private boolean addUser(String username, String roomname) throws Exception
	{
		ISharedObject so = getSharedDataStore(appScope, roomname, false);
		List<String> users = (List<String>) so.getListAttribute("users");
		
		if(users.contains(username)){
			return false;
		}
		
		users.add(username);
		so.setAttribute("users", users);
		so.setDirty(true);
		return true;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param username
	 * @param roomname
	 * @return
	 * @throws Exception
	 */
	private boolean removeUser(String username, String roomname) throws Exception{
		
		ISharedObject so = getSharedDataStore(appScope, roomname, false);
		List<String> users = (List<String>) so.getListAttribute("users");
		
		if(users.contains(username)){
			users.remove(username);
			so.setAttribute("users", users);
			so.setDirty(true);
			return true;
		}
		
		return false;
	}
	
	
	
	

	@Override
	public void appDisconnect(IConnection conn) 
	{
		String username;
		String roomname;
		
		try
		{
			if(conn.hasAttribute("username")){
				username = conn.getStringAttribute("username");
				roomname = conn.getStringAttribute("roomname");
				this.removeUser(username, roomname);
			}
		}
		catch(Exception e)
		{
			log.error("Error removing user : ", e);
		}
		
		super.appDisconnect(conn);
	}



	@Override
	public void appStop(IScope arg0) {
		// TODO Auto-generated method stub
		super.appStop(arg0);
	}
	
	
	
	
	

	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		log.info("streamBroadcastClose {} ", stream);
		super.streamBroadcastClose(stream);
	}

	
	
	
	
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		log.info("streamBroadcastStart {} ", stream);
		super.streamBroadcastStart(stream);
	}

	
	
	
	
	@Override
	public void streamRecordStart(IBroadcastStream stream) {
		log.info("streamRecordStart {} ", stream);
		super.streamRecordStart(stream);
	}

	
	
	
	
	@Override
	public void streamSubscriberClose(ISubscriberStream stream) {
		log.info("streamSubscriberClose {} ", stream);
		super.streamSubscriberClose(stream);
	}

	
	
	
	
	@Override
	public void streamSubscriberStart(ISubscriberStream stream) {
		log.info("streamSubscriberStart {} ", stream);
		super.streamSubscriberStart(stream);
	}

}
