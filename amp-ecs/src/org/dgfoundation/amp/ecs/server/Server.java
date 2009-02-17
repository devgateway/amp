package org.dgfoundation.amp.ecs.server;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.common.ECSConstants;
import org.dgfoundation.amp.ecs.common.ECSCustom;
import org.dgfoundation.amp.ecs.common.ECSParameters;
import org.dgfoundation.amp.ecs.common.ECSServer;
import org.dgfoundation.amp.ecs.common.ErrorKeeperItem;

public class Server extends UnicastRemoteObject implements ECSServer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private BufferedWriter out;
	private Logger log;
	public static Object lock = new Object();

	protected Server(Logger l) throws RemoteException {
		super();
		this.log = l;
	}

	@Override
	public ECSParameters getParameters(String serverName, String report)
			throws RemoteException {
		log.info("Receiving from: " + serverName + "\n");
		log.info(report);
		ECSParameters ep = new ECSParameters();
		ep.setRunOnceCustom(false);
		ep.setSynchronizeDelay(-1); //no update
		return ep;
	}

	@Override
	public ECSCustom runCustom(String serverName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendError(String serverName, ErrorKeeperItem eki)
			throws RemoteException {
		log.info("Receiving error from: " + serverName + "\n");
		log.info("Exception: " + eki.getException().getMessage());
		log.info("   count = " + eki.getCount());
		log.info("	 users = " + eki.getUsers().keySet());
		
		return false;
	}

	@Override
	public boolean sendErrorList(String serverName,
			Queue<ErrorKeeperItem> errList) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		Logger log = Logging.startLogger("", "Server");
		
		//String command = args[0].toLowerCase();
		System.setSecurityManager(new RMISecurityManager());
		
		Registry r = null;
		try { //locate registry
			log.info("Starting registry ...");
			r = LocateRegistry.createRegistry (1099);
			log.info("Done!");
			//r = LocateRegistry.getRegistry();
		} catch (Exception e) {
			log.error("Unable to start registry", e);
			System.exit(-1);
		}
		
		log.info("Trying to bind...");
		try { //bind server
			Server serv = new Server(log);
			log.info("Server Instance started...");
			r.bind(ECSConstants.SERVER_NAME, serv);
			//Naming.rebind(ECSConstants.SERVER_NAME, serv);
			log.info("Done");
		} catch (Exception e) {
			log.error("Unable to bind server to local registry", e);
			System.exit(-1);
		}
		log.info("Server registered!");

		while (true){ //rulam pana cand ne opreste userul
			System.out.println("Hit ENTER to stop server!");
			int ch = -99;
			try {
				ch = System.in.read();
			} catch (IOException e) {
				break;
			}
			if ((ch == 10) || (ch == 27) || (ch == -1)) //CR or EOS
				break;
		}

		log.info("Trying to unbind...");
		try { //unbind
			Naming.unbind(ECSConstants.SERVER_NAME);
			log.info("Done");
		} catch (Exception e) {
			log.error("Unable to bind server to local registry", e);
			System.exit(-1);
		}
		log.info("Server unregistered!");

		System.exit(0);
	}
}
