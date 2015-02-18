package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.digijava.kernel.persistence.PersistenceManager;

/**
 * a class which encapsulates a ResultSet and its generated statement and which closes them both
 * @author Dolghier Constantin
 *
 */
public class RsInfo implements AutoCloseable {
	public final ResultSet rs;
	public final Statement statement;
	
	public RsInfo(ResultSet rs, Statement statement) {
		this.rs = rs;
		this.statement = statement;
	}
	
	@Override public void close() throws SQLException {
		PersistenceManager.closeQuietly(this.statement);
		PersistenceManager.closeQuietly(this.rs);
	}
}
