package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * the AMP-schema-specific scratchpad <br />
 * please see {@link NiReportsEngine#schemaSpecificScratchpad} 
 * @author Dolghier Constantin
 *
 */
public class AmpReportsScratchpad implements SchemaSpecificScratchpad {
	 
	public final Map<PropertyDescription, ColumnValuesCacher> columnCachers = new HashMap<>();
	public final Connection connection;

	public AmpReportsScratchpad(NiReportsEngine engine) {
		try {this.connection = PersistenceManager.getJdbcConnection();}
		catch(Exception e) {throw AlgoUtils.translateException(e);}
	}
		
	public static AmpReportsScratchpad get(NiReportsEngine engine) {
		return (AmpReportsScratchpad) engine.schemaSpecificScratchpad;
	}
	
	@Override
	public void close() throws Exception {
		this.connection.close();
	}
}
