package org.dgfoundation.amp.ecs.webservice;

import java.util.LinkedList;

public class UserScenes {
	private ErrorUser user;
	private LinkedList<ErrorScene> scenes;
	
	public ErrorUser getUser() {
		return user;
	}
	public void setUser(ErrorUser user) {
		this.user = user;
	}
	public LinkedList<ErrorScene> getScenes() {
		return scenes;
	}
	public void setScenes(LinkedList<ErrorScene> scenes) {
		this.scenes = scenes;
	}
	
	
}
