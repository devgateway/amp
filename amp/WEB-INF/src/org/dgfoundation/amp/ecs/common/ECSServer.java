package org.dgfoundation.amp.ecs.common;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Queue;


/**
 * Error Centralization System Server
 * @author Arty
 *
 */
public interface ECSServer extends Remote{
	public boolean sendError(String serverName, ErrorKeeperItem eki) throws RemoteException;
	public boolean sendErrorList(String serverName, Queue<ErrorKeeperItem> errList) throws RemoteException;
	public ECSParameters getParameters(String serverName, String report) throws RemoteException;
	public ECSCustom runCustom(String serverName) throws RemoteException;
}
