package org.dgfoundation.ecs.keeper;

import java.util.Calendar;
import java.util.LinkedList;

import org.dgfoundation.ecs.core.ECS;
import org.dgfoundation.ecs.keeper.ErrorScene;
import org.dgfoundation.ecs.keeper.ErrorUser;

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
	
	public String toJson(String id){
		String result = 
			ECS.q(id) + ":{" +
			ECS.t("count", String.valueOf(count)) +
			ECS.t("stackTrace", stackTrace) +
			ECS.q("users") + ":[";
		
		for (int i = 0; i < users.size(); i++){
			ErrorUser user = users.get(i);
			result += user.toJson();
			if (i != users.size() - 1)
				result += ",";
		}
		result += "]," +
			ECS.q("userScenes") + ":[";

		for (int i = 0; i < userScenes.size(); i++){
			ErrorScene[] scenes = userScenes.get(i);
			result += "[";
			
			for (int j = 0; j < scenes.length; j++){
				ErrorScene scene = scenes[j];
				result += scene.toJson();
				if (j != scenes.length - 1)
					result += ",";
			}
			
			result += "]";
			if (i != userScenes.size() - 1)
				result += ",";
		}
		result += "]";
		result += "}";
		
		return result;
	}
	

}
