package org.digijava.module.aim.services.auditcleaner;

import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 * 
 */

public class AuditCleanerService extends AbstractServiceImpl {
	AuditCleanerRunner crummer = null;

	protected void processInitEvent(ServiceContext serviceContext) {
		String cleanerEnabled = FeaturesUtil
				.getGlobalSettingValue("Automatic Audit Logger Cleanup");
		if (cleanerEnabled != null) {
			if (!("-1").equalsIgnoreCase(cleanerEnabled)) {
				startCleaner(Integer.parseInt(cleanerEnabled));
			}
		}
	}

	public void startCleaner(int days) {
		AuditCleaner adc = AuditCleaner.getInstance();
		adc.start();
	}
}
