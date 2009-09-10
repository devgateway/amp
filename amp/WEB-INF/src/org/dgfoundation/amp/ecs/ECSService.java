package org.dgfoundation.amp.ecs;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.webservice.ECSConstants;
import org.dgfoundation.amp.error.AMPException;
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

	protected void processInitEvent(ServiceContext serviceContext) {
		//ECS Checks
		logger.info("Checking ECS Setup ...");
		if ((FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED) != null)&&(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED).equals("true"))){
			logger.info("    ECS is enabled");
			boolean start = true;
			
			String s = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_SERVER_NAME);
			logger.info("    Server Name: " + s);
			if (s.compareTo("defaultName") == 0){
				logger.error("###############################################");
				logger.error("#  Server name was not set, ECS not starting  #");
				logger.error("###############################################");
				start = false;
			}
			
			if (start)
				startService();
		}
		
	}

	public void startService() {
		logger.info("Starting ECS Service ...");
		try {
			ECS adc = ECS.getInstance();
			adc.start();
			logger.info("    Succes!");
		} catch (Exception e) {
			logger.error("    Failed starting ECS Service", e);
		}
	}

	public void stopService() {
		logger.info("Stopping ECS Service ...");
		try {
			ECSClient cl = new ECSClient();
			String[] res = cl.getParameters(ECSConstants.SERVER_SHUTDOWN);
		} catch (AMPException e) {
			logger.error("Can't contact ECS Server", e);
		}
		
		try {
			ECS adc = ECS.getInstance();
			adc.stop();
		} catch (Exception e) {
			logger.error("Failed stopping ECS Service", e);
		}
	}
}
