package org.dgfoundation.amp.ecs.webservice;

import java.io.Serializable;
import java.util.LinkedList;


public class ErrorKeeperItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String exception;
	private int count;
	private LinkedList<UserScenes> users;
	
	public ErrorKeeperItem() {
		super();
		users = new LinkedList<UserScenes>();
	}
	public ErrorKeeperItem(String ex, ErrorUser user, ErrorScene scene){
		this();
		this.exception = ex;
		count = 1;
		LinkedList<ErrorScene> list = new LinkedList<ErrorScene>();
		list.add(scene);
		UserScenes us = new UserScenes();
		us.setUser(user);
		us.setScenes(list);
		users.add(us);
	}
	
	public String getException() {
		return exception;
	}
	public int getCount() {
		return count;
	}
	
	/**
	 * WARNING: should not be used to alter in any way the collection.
	 * 
	 * Should be used only by the ECS Server.
	 * @return
	 */
	public void setException(String exception) {
		this.exception = exception;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public LinkedList<UserScenes> getUsers() {
		return users;
	}
	public void setUsers(LinkedList<UserScenes> users) {
		this.users = users;
	}


	
}
