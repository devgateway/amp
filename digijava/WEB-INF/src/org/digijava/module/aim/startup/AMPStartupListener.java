/*
 * @Author Priyajith C
 */
package org.digijava.module.aim.startup;

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
        			logger.info("ME Feature..");
        		} else if (f.getCode().equalsIgnoreCase(Constants.AA_FEATURE)) {
        			ampContext.setAttribute(Constants.AA_FEATURE,new Boolean(true));
        			logger.info("AA Feature..");        			
        		} else if (f.getCode().equalsIgnoreCase(Constants.PI_FEATURE)) {
        			ampContext.setAttribute(Constants.PI_FEATURE,new Boolean(true));
        			logger.info("PI Feature..");
        		} else if (f.getCode().equalsIgnoreCase(Constants.CL_FEATURE)) {
        			ampContext.setAttribute(Constants.CL_FEATURE,new Boolean(true));
        			logger.info("CL Feature..");
        		}
        	}
        	
    	} catch (Exception e) {
    		logger.error("Exception while initialising AMP :" + e.getMessage());
    		e.printStackTrace(System.out);
    	}
    }
}