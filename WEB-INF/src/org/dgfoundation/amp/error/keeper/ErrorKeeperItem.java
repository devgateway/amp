package org.dgfoundation.amp.error.keeper;

import java.util.LinkedList;

import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;

public class ErrorKeeperItem {
	private String stackTrace;
	private int count;
	private LinkedList<ErrorUser> users;
	private LinkedList<ErrorScene[]> userScenes;
	
	public ErrorKeeperItem() {
		users = new LinkedList<ErrorUser>();
		userScenes = new LinkedList<ErrorScene[]>();
	}
	
	public ErrorKeeperItem(String stackTrace, ErrorUser user, ErrorScene scene) {
		this();
		this.count = 1;
		this.stackTrace = stackTrace;
		this.users.add(user);
		ErrorScene[] sList = new ErrorScene[1];
		sList[0] = scene;
		this.userScenes.add(sList);
	}
	
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public LinkedList<ErrorUser> getUsers() {
		return users;
	}
	public void setUsers(LinkedList<ErrorUser> users) {
		this.users = users;
	}
	public LinkedList<ErrorScene[]> getUserScenes() {
		return userScenes;
	}
	public void setUserScenes(LinkedList<ErrorScene[]> userScenes) {
		this.userScenes = userScenes;
	}
	
	

}
