package org.dgfoundation.amp.ecs.webservice;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class ECS {
	
	public String[] getParameters(String serverName, String report){
		return null;
	}
	public boolean sendError(String serverName, int count, String stackTrace, ErrorUser[] users, ErrorScene[][] userScenes){
		return false;
	}
	
	 /*
	public boolean sendErrorList(String serverName,
			Queue<ErrorKeeperItem> errList){
		return false;
	}
	*/
	
	//////////////
	//Blank method so that some classes will have a serializer genearated
	public ErrorUser getUser(){
		return null;
	}
	//
	public ErrorScene getScene(){
		return null;
	}
	//
	public UserScenes getUserScenes(){
		return null;
	}
	//END
	//////////////
	
}
