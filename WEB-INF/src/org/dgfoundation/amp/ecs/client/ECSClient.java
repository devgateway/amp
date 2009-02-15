package org.dgfoundation.amp.ecs.client;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.common.ECSConstants;
import org.dgfoundation.amp.ecs.common.ECSCustom;
import org.dgfoundation.amp.ecs.common.ECSParameters;
import org.dgfoundation.amp.ecs.common.ECSServer;
import org.dgfoundation.amp.ecs.common.ErrorKeeperItem;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Error Centralization System Client
 * 
 * @author Arty
 *
 */
public class ECSClient {
	private static final String MODULE_TAG = "ecs";
	private static String ECS_SERVER_ADDRESS = "82.77.15.251";
	private static String THIS_SERVER_NAME = "myAMPServer";
	private static Logger logger = Logger.getLogger(ECSClient.class);

	private ECSServer server;
		
	public ECSClient() throws AMPException {
		//sets timeout for rmi sockets to 5 seconds
		try {
			if (RMISocketFactory.getSocketFactory() == null)
				RMISocketFactory.setSocketFactory(new TimeoutFactory(5));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		//DO init system properties
		String actualPolicy = System.getProperty("java.security.policy");
		//System.getProperties().setProperty("java.security.policy", "/home/alexandru/Lucru/jboss-amp-4.2.3/bin/ecsClient.policy");
		System.getProperties().setProperty("java.rmi.server.codebase", "http://"+ECS_SERVER_ADDRESS+"/ecs/");
		//System.getProperties().setProperty("java.rmi.server.hostname", "192.168.1.128");
		//System.getenv();
		//
		
		server = null;
		try {
			System.setSecurityManager(new RMISecurityManager());
			//server = (ECSServer) Naming.lookup("rmi://"+ECS_SERVER_ADDRESS+"/" + ECSConstants.SERVER_NAME);
			Registry reg = LocateRegistry.getRegistry(ECS_SERVER_ADDRESS);
			server = (ECSServer)reg.lookup(ECSConstants.SERVER_NAME);
		} catch (Exception e) {
			logger.error("Unable to contact server registry!", e);
			server = null;
			//TODO: set quartz to retry ... or launch thread ... should be done elsewhere
			AMPException ae = new AMPException("Unable to contact server registry");
			ae.addTag(MODULE_TAG);
			throw ae;
		}
	}
	
	private String getCurrentAMPServerName(){
		String s = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_SERVER_NAME);
		return s;
	}
	
	public boolean sendError(ErrorKeeperItem eki) throws AMPException{
		boolean result = false;
		try{
			String name = getCurrentAMPServerName();
			result = server.sendError(name, eki);
			//server.getParameters(name);
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}

	public boolean sendErrorList(Queue<ErrorKeeperItem> errList) throws AMPException{
		boolean result = false;
		try{
			String name = getCurrentAMPServerName();
			result = server.sendErrorList(name, errList);
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}
	
	public ECSParameters getParameters(String report) throws AMPException{
		ECSParameters result = null;
		try{
			String name = getCurrentAMPServerName();
			result = server.getParameters(name, report);
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}

	public ECSCustom runCustom() throws AMPException{
		ECSCustom result = null;
		try{
			String name = getCurrentAMPServerName();
			result = server.runCustom(name);
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}
	
	
}
