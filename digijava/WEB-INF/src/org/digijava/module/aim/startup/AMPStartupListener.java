/*
 * @Author Priyajith C
 */
package org.digijava.module.aim.startup;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;


public class AMPStartupListener extends HttpServlet 
	implements ServletContextListener {
    
	private static final String PATCH_METHOD_KEY = "patchAMP";
	
    private static Logger logger = Logger.getLogger(
            AMPStartupListener.class);
    
    public void contextDestroyed(ServletContextEvent sce) {

    }
    public void contextInitialized(ServletContextEvent sce) {
    	ServletContext ampContext = null;
    	
    	try {
        	ampContext = sce.getServletContext();
        	
        	Iterator itr = FeaturesUtil.
        		getActiveFeatures().iterator();
        	while (itr.hasNext()) {
        		AmpFeature f = (AmpFeature) itr.next();
        		if (f.getCode().equalsIgnoreCase(Constants.ME_FEATURE)) {
        			ampContext.setAttribute(Constants.ME_FEATURE,new Boolean(true));
        			//logger.debug("ME Feature..");
        		} else if (f.getCode().equalsIgnoreCase(Constants.AA_FEATURE)) {
        			ampContext.setAttribute(Constants.AA_FEATURE,new Boolean(true));
        			//logger.debug("AA Feature..");        			
        		} else if (f.getCode().equalsIgnoreCase(Constants.PI_FEATURE)) {
        			ampContext.setAttribute(Constants.PI_FEATURE,new Boolean(true));
        			//logger.debug("PI Feature..");
        		} else if (f.getCode().equalsIgnoreCase(Constants.CL_FEATURE)) {
        			ampContext.setAttribute(Constants.CL_FEATURE,new Boolean(true));
        			//logger.debug("CL Feature..");
        		}
        	}
        	boolean defFlagExist = FeaturesUtil.defaultFlagExist();
        	ampContext.setAttribute(Constants.DEF_FLAG_EXIST,new Boolean(defFlagExist));
        	
        	Enumeration params = ampContext.getInitParameterNames();
        	if (params != null) {
        		logger.info("params is not null");
        		while (params.hasMoreElements()) {
        			String paramName = (String) params.nextElement();
        			logger.info("Param Name :" + paramName);
        			if (paramName.startsWith(PATCH_METHOD_KEY)) {
        	        	String patchMethod = ampContext.getInitParameter(PATCH_METHOD_KEY);
        	        	int index = patchMethod.indexOf(':');
        	        	if (index > -1) {
        	        		String className = patchMethod.substring(0,index);
        	        		String methodName = patchMethod.substring(index+1,patchMethod.length());
        	        		logger.info("ClassName :" + className);
        	        		logger.info("MethodName :" + methodName);
        	        		try {
        	        			Class c = Class.forName(className);
        	        			Object obj = c.newInstance();
        	        			Method m = c.getMethod(methodName,null);
        	        			m.invoke(obj,null);
        	        		} catch (ClassNotFoundException cnfe) {
        	        			logger.error("Cannot find the patching class:" + className);
        	        		}
        	        	} else {
        	        		logger.error("Wrong paramater values specified for patchMethod");
        	        	}        				
        			}
        		}
        	} else {
        		logger.info("params is null");
        	}
    	} catch (Exception e) {
    		logger.error("Exception while initialising AMP :" + e.getMessage());
    		e.printStackTrace(System.out);
    	}
    }
}