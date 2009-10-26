package org.dgfoundation.amp.ecs.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;


public class ErrorKeeperItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Throwable exception;
	private int count;
	private HashMap<ErrorUser, LinkedList<ErrorScene>> users;
	
	public ErrorKeeperItem() {
		super();
		users = new HashMap<ErrorUser, LinkedList<ErrorScene>>();
	}
	public ErrorKeeperItem(Throwable ex, ErrorUser user, ErrorScene scene){
		this();
		this.exception = ex;
		count = 1;
		LinkedList<ErrorScene> list = new LinkedList<ErrorScene>();
		list.add(scene);
		
		users.put(user, list);
	}
	
	/**
	 * Thread safe
	 */
	public synchronized void increment(ErrorUser user, ErrorScene scene){
		count++;
		LinkedList<ErrorScene> esList = users.get(user);
		
		if (esList == null){
			esList = new LinkedList<ErrorScene>();
			esList.add(scene);
			users.put(user, esList);
		}
		else
			esList.add(scene);
	}

	/**
	 * Thread safe
	 */
	public synchronized void update(ErrorKeeperItem eki){
		count += eki.getCount();
		for (ErrorUser user: eki.getUsers().keySet()){
			LinkedList<ErrorScene> oList = eki.getUsers().get(user);
			LinkedList<ErrorScene> esList = users.get(user);
			if (esList == null){
				esList = oList;
				users.put(user, esList);
			}
			else
				esList.addAll(oList);
		}
	}

	public Throwable getException() {
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
	public HashMap<ErrorUser, LinkedList<ErrorScene>> getUsers() {
		return users;
	}
	
	
}
