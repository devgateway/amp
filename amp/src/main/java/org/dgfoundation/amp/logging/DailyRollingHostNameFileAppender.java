/**
 * 
 */
package org.dgfoundation.amp.logging;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;

/**
 * @author mihai
 * 
 */
public class DailyRollingHostNameFileAppender extends DailyRollingFileAppender {

	protected String hostName="amp";
	public static final String JNDI_HOSTNAME="java:comp/env/hostname";

	/**
	 * The default constructor does nothing.
	 * 
	 * @throws NamingException
	 */
	public DailyRollingHostNameFileAppender() throws NamingException {		
		Context initialContext = new InitialContext();
		hostName = (String) initialContext.lookup(JNDI_HOSTNAME);
	}

	public DailyRollingHostNameFileAppender(Layout layout, String filename,
			String datePattern) throws IOException, NamingException {
		super(layout, filename, datePattern);
		Context initialContext = new InitialContext();
		hostName = (String) initialContext.lookup(JNDI_HOSTNAME);
	}
	
	@Override
	public void setFile(String file) {
		// TODO Auto-generated method stub
		super.setFile(file.replace("[hostname]", hostName));
	}
}
