package org.digijava.module.dataExchange.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

public class CreateSourceUtil {
	public static List<String> getFieldNames( AmpColumnEntry entry, List<String> container) {
		if (container == null) {
			container = new ArrayList<String>();
		}
		if ( entry != null ) {
			if ( entry.canExport() )
				container.add( entry.getPath() );
			if ( entry.getElements() != null ) {
				for (AmpColumnEntry tempEntry: entry.getElements()) {
					getFieldNames(tempEntry, container);
				}
			}
			
		}
		return container;
	}
	
	public static DELogPerExecution createTestLogObj() {
		Random random			= new Random(System.currentTimeMillis());
		int rInt				= random.nextInt();
		DELogPerExecution log	= new DELogPerExecution();
		log.setDescription("Some description" +rInt);
		log.setExternalTimestamp("externalTimestamp" + rInt);
		log.setExecutionTime( new Timestamp(System.currentTimeMillis()));
		
		return log;
	}
}
