package org.digijava.module.dataExchange.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
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
		
		log.setLogItems(new ArrayList<DELogPerItem>() );
		for (int i=0; i<100; i++) {
			DELogPerItem iLog	= new DELogPerItem();
			int riInt			= random.nextInt();
			if ( i%7 == 0 )
				iLog.setLogType(DELogPerItem.LOG_TYPE_ERROR);
			else
				iLog.setLogType(DELogPerItem.LOG_TYPE_INFO);
			iLog.setName("Activity saved" + i + "_" + riInt);
			iLog.setDescription("ILog description" + i + "_" + riInt);
			iLog.setItemType( DELogPerItem.ITEM_TYPE_ACTIVITY );
			iLog.setExecutionTime(new Timestamp(System.currentTimeMillis() ));
			iLog.setDeLogPerExecution(log);
			
			log.getLogItems().add(iLog);
		}
		
		return log;
	}
}
