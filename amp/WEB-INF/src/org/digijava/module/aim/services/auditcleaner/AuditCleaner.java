package org.digijava.module.aim.services.auditcleaner;

import org.apache.log4j.Logger;
import org.digijava.module.currencyrates.DailyCurrencyRateSingleton;

public final class AuditCleaner {
	private static Logger logger = Logger.getLogger(AuditCleaner.class);
	private static volatile AuditCleaner instance;
	private boolean status = false;
	private AuditCleanerRunner auditclenarrunner;
	
	public static synchronized AuditCleaner getInstance() {
		if (instance == null)
			synchronized (DailyCurrencyRateSingleton.class) {
				if (instance == null)
					instance = new AuditCleaner();
			}
		return instance;
	}
	
	public void start(){
		auditclenarrunner = new AuditCleanerRunner();
		auditclenarrunner.launch();
		status = true;
	}
	
	public void stop(){
		auditclenarrunner.stop();
		status = false;
	}
	public boolean isRunning(){
		return status;
	}
}
