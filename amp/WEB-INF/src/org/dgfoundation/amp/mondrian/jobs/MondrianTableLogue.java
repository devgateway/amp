package org.dgfoundation.amp.mondrian.jobs;

import java.sql.Connection;
import java.sql.SQLException;

import org.dgfoundation.amp.mondrian.EtlConfiguration;

/**
 * a callback to be called as an prologue / epilogue to processing a Mondrian table
 * @author Constantin Dolghier
 *
 */
public interface MondrianTableLogue {
	public void run(EtlConfiguration etlConfiguration, Connection conn) throws SQLException;
}
