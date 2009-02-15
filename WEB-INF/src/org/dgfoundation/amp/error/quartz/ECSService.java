package org.dgfoundation.amp.error.quartz;

import org.apache.log4j.Logger;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Arty
 * 
 */

public class ECSService extends AbstractServiceImpl {
	ECSRunner crummer = null;

	protected void processInitEvent(ServiceContext serviceContext) {
		/*String cleanerEnabled = FeaturesUtil
				.getGlobalSettingValue("Automatic Audit Logger Cleanup");
		if (cleanerEnabled != null) {
			if (!("-1").equalsIgnoreCase(cleanerEnabled)) {*/
		/*	}
		}*/
		String ecsEnabled = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED);
		if ("true".equalsIgnoreCase(ecsEnabled))
			startService();
		else
			stopService();
	}

	public void startService() {
		Logger log = Logger.getLogger(ECSService.class);
		
		log.info("Starting ECS Service ...");
		try {
			ECS adc = ECS.getInstance();
			adc.start();
		} catch (Exception e) {
			log.error("Failed starting ECS Service", e);
		}
	}

	public void stopService() {
		Logger log = Logger.getLogger(ECSService.class);
		
		log.info("Stopping ECS Service ...");
		try {
			ECS adc = ECS.getInstance();
			adc.stop();
		} catch (Exception e) {
			log.error("Failed starting ECS Service", e);
		}
	}
}
