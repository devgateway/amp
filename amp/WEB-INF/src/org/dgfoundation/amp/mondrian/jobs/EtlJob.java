package org.dgfoundation.amp.mondrian.jobs;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;

/**
 * defines an abstract Mondrian ETL subjob which might or might not make changes to the DB. If a job signals that it made changes to the DB, the Mondrian cache should be reset
 * @author Constantin Dolghier
 *
 */
public abstract class EtlJob {
	public final static Logger logger = Logger.getLogger(EtlJob.class);
	
	/**
	 * only defined while running a job, e.g. anytime :D
	 */
	protected Connection postgrsConn;
	
	/**
	 * only defined while running a job, e.g. anytime :D
	 */
	protected MonetConnection monetConn;
	
	public boolean runJob(Connection postgrsConn, MonetConnection monetConn) throws SQLException {
		this.postgrsConn = postgrsConn;
		this.monetConn = monetConn;
		boolean res = runJobInternal();
		return res;
	}
	
	@Override public abstract String toString();
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		return this.toString().equals(oth.toString()); 
	}
	
	/**
	 * checks if conditions apply for making changes to the DB. If yes, applies them and returns true. else, returns false
	 * @return IFF the mondrian cache should be reset
	 * @throws SQLException
	 */
	protected abstract boolean runJobInternal() throws SQLException;
}
