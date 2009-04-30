package org.dgfoundation.amp.ecs.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

public class TimeoutFactory extends RMISocketFactory {
	private int timeout;
	public TimeoutFactory(int timeout) {
		this.timeout = timeout;
	}

	public Socket createSocket(String host, int port) throws IOException {
		Socket ret = getDefaultSocketFactory().createSocket(host, port);
		ret.setSoTimeout(timeout * 1000);
		return ret;
	}
	public ServerSocket createServerSocket(int port) throws IOException {
		return getDefaultSocketFactory().createServerSocket(port);
	}
}

