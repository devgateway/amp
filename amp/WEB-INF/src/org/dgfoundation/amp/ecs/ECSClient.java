package org.dgfoundation.amp.ecs;

import java.util.Calendar;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.common.ECSService;
import org.dgfoundation.amp.ecs.common.ECSServiceLocator;
import org.dgfoundation.amp.ecs.common.ECS_PortType;
import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.keeper.ErrorKeeperItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Error Centralization System Client
 * 
 * @author Arty
 *
 */
public class ECSClient {
	private static final String MODULE_TAG = "ecs";
	private static Logger logger = Logger.getLogger(ECSClient.class);
	
	private ECS_PortType ecs = null;
		
	public ECSClient() throws AMPException {
		
		ECSService es = new ECSServiceLocator();
		try{
			ecs = es.getECS();
		} catch (Exception e){
			//TODO: set quartz to retry ... or launch thread ... should be done elsewhere
			logger.error("Unable to contact ECS server !", e);
			
			ecs = null;
			
			e = new Exception("Unable to contact ECS server", e);
			AMPException ae = new AMPException(e);
			ae.addTag(MODULE_TAG);
	
			throw ae;
		}
	}		
	
	public String getCurrentAMPServerName(){
		String s = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_SERVER_NAME);
		return s;
	}
	
	public boolean sendError(ErrorKeeperItem eki) throws AMPException{
		boolean result = false;
		try{
			String name = getCurrentAMPServerName();
			
			if (eki.getUsers().size() > 0){
				ErrorUser[] tmp = new ErrorUser[eki.getUsers().size()];
				for (int i = 0; i < eki.getUsers().size(); i++){
					tmp[i] = eki.getUsers().get(i);
				}
				ErrorScene[][] tmp2 = new ErrorScene[eki.getUserScenes().size()][];
				for (int i = 0; i < eki.getUserScenes().size(); i++){
					tmp2[i] = eki.getUserScenes().get(i);
				}
				
				result = ecs.sendError(name, eki.getCount(), eki.getStackTrace(), tmp, tmp2);
			}
//			ErrorUser[] us1 = new ErrorUser[2];
//			us1[0] = new ErrorUser("a1", "a2", "a3");
//			us1[1] = new ErrorUser("b1", "b2", "b3");
//			ErrorScene[][] es = new ErrorScene[2][];
//			es[0] = new ErrorScene[2]; es[0][0] = new ErrorScene("sdfasf", Calendar.getInstance()); es[0][1] = new ErrorScene("aaaaaf", Calendar.getInstance());
//			es[1] = new ErrorScene[3]; es[1][0] = new ErrorScene("sdfasf", Calendar.getInstance()); es[1][1] = new ErrorScene("sdfasf", Calendar.getInstance()); es[1][2] = new ErrorScene("sdfasf", Calendar.getInstance());
//			ecs.test(name, us1, es);
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}

	public boolean sendErrorList(Queue<ErrorKeeperItem> errList) throws AMPException{
		boolean result = false;
		try{
			String name = getCurrentAMPServerName();
			for (ErrorKeeperItem i: errList){
				sendError(i);
			}
			//result = ecs.sendErrorList(name, errList.toArray());
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}
	
	public String[] getParameters(String report) throws AMPException{
		String[] result = null;
		try{
			String name = getCurrentAMPServerName();
			result = ecs.getParameters(name, report);
		}catch (Exception e) {
			AMPException ae = new AMPException(Constants.AMP_ERROR_LEVEL_ERROR, true, e);
			ae.addTag(MODULE_TAG);
			throw ae;
		}
		return result;
	}
}
