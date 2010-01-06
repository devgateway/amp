package org.dgfoundation.ecs.core;

import org.dgfoundation.ecs.exceptions.ECSException;

public class ECSClientManager {
	private static HttpClient client = null;
	private static boolean startedOk = false;

	public static boolean isStartedOk() {
		return startedOk;
	}

	public static void setStartedOk(boolean startedOk) {
		ECSClientManager.startedOk = startedOk;
	}

	public static synchronized HttpClient getClient() throws ECSException {
		/*
		if (!startedOk){
			throw new ECSException(new Exception("ecs not started yet"));
		}
		*/
		return getClientBypassStarted();
	}
	public static HttpClient getClientBypassStarted() throws ECSException {
		if (client == null){
			try {
				client = new HttpClient();
			} catch (ECSException e) {
				client = null;
				throw e;
			}
		}
		return client;
	}
}
