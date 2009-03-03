package org.dgfoundation.amp.error.quartz;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Arty
 * 
 */

public class ECSService extends AbstractServiceImpl {
	private static Logger logger = Logger.getLogger(ECSService.class);
	private static ECSRunner crummer = new ECSRunner();

	protected void processInitEvent(ServiceContext serviceContext) {
		/*String cleanerEnabled = FeaturesUtil
				.getGlobalSettingValue("Automatic Audit Logger Cleanup");
		if (cleanerEnabled != null) {
			if (!("-1").equalsIgnoreCase(cleanerEnabled)) {*/
		/*	}
		}*/

		//ECS Checks
		logger.info("Checking ECS Setup ...");
		if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED).equals("true")){
			logger.info("   - ECS is enabled");
			String policy = System.getProperties().getProperty("java.security.policy");
			if (policy.indexOf("ecsClient.policy") < 0){
				logger.info("   - policy NOT set!");
				try{
					Iterator iterator = FeaturesUtil.getGlobalSettingsCache().iterator();
					while (iterator.hasNext()) {
						AmpGlobalSettings ags = (AmpGlobalSettings) iterator.next();
						if (ags.getGlobalSettingsName().equals(GlobalSettingsConstants.ECS_ENABLED)){
							ags.setGlobalSettingsValue("false");
							logger.info("ECS was Turned off!");
							break;
						}
					}
				} catch (Exception ignored) {logger.error(ignored);}
				logger.error("#####################################################################");
				logger.error("#                   Warning ECS policy not set!                     #");
				logger.error("#                      ECS System turned OFF                        #");
				logger.error("#                                                                   #");
				logger.error("# To enable the system set the java security policy as instructed!  #");
				logger.error("#                                                                   #");
				logger.error("# Add this parameter to the jboss startup parameters:               #");
				logger.error("#    -Djava.security.policy=\"<<Path to>>/ecsClient.policy\"          #");
				logger.error("# <<Path to>> must be the absoulte path to ecsClient.policy file.   #");
				logger.error("#                                                                   #");
				logger.error("#####################################################################");
			}
		}
		//

		
		String ecsEnabled = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED);
		if ("true".equalsIgnoreCase(ecsEnabled))
			startService();
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
