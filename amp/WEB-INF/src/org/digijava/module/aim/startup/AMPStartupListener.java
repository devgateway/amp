/*
 * @Author Priyajith C
 */
package org.digijava.module.aim.startup;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumnsOrder;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettings;
import org.digijava.module.aim.util.FeaturesUtil;



public class AMPStartupListener extends HttpServlet
	implements ServletContextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5724776790911414323L;

	private static final String PATCH_METHOD_KEY = "patchAMP";

    private static Logger logger = Logger.getLogger(
            AMPStartupListener.class);
    
    protected Session session;

    public Session createSession() throws HibernateException, SQLException {
    	if(session!=null) return session;
    	session = PersistenceManager.getSession();
    	return session;
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
    public void contextInitialized(ServletContextEvent sce) {
    	ServletContext ampContext = null;

    	try {
        	ampContext = sce.getServletContext();

       /* 	Iterator itr = FeaturesUtil.
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
        		} else if (f.getCode().equalsIgnoreCase(Constants.DC_FEATURE)) {
        			ampContext.setAttribute(Constants.DC_FEATURE,new Boolean(true));
        			//logger.debug("MS Feature");
        		}
        		else if (f.getCode().equalsIgnoreCase(Constants.SC_FEATURE)) {
        			ampContext.setAttribute(Constants.SC_FEATURE,new Boolean(true));
        			//logger.debug("MS Feature");
        		}
        		
        		else if (f.getCode().equalsIgnoreCase(Constants.MS_FEATURE)) {
        			ampContext.setAttribute(Constants.MS_FEATURE,new Boolean(true));
        			//logger.debug("MS Feature");
        		}
        		
        		else if (f.getCode().equalsIgnoreCase(Constants.AC_FEATURE)) {
        			ampContext.setAttribute(Constants.AC_FEATURE,new Boolean(true));
        			//logger.debug("MS Feature");
        		}
        		
        		else if (f.getCode().equalsIgnoreCase(Constants.LB_FEATURE)) {
        			ampContext.setAttribute(Constants.LB_FEATURE,new Boolean(true));
        			//logger.debug("MS Feature");
        		}
        		
        		else if (f.getCode().equalsIgnoreCase(Constants.SA_FEATURE)) {
        			ampContext.setAttribute(Constants.SA_FEATURE,new Boolean(true));
        			//logger.debug("MS Feature");
        		}
        		

        	}*/
        	
        		ampContext.setAttribute(Constants.ME_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.AA_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.PI_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.CL_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.DC_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.SC_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.MS_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.AC_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.LB_FEATURE,new Boolean(true));
    			ampContext.setAttribute(Constants.SA_FEATURE,new Boolean(true));
        	
        	
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
                                Method m = c.getMethod(methodName, (Class[])null);
                                m.invoke(obj, (Object[])null);
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
        	
        	AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
        	//get the default amp template!!!
        	Session session=this.createSession();
        	AmpTemplatesVisibility currentTemplate=FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"),session);
        	ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
        	ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
        	Collection ampColumns=FeaturesUtil.getAMPColumnsOrder();
        	ampContext.setAttribute("ampColumnsOrder",ampColumns);
        	
        	GlobalSettings globalSettings = new GlobalSettings();
        	globalSettings.setPerspectiveEnabled(FeaturesUtil.isPerspectiveEnabled());
        	
        	ampContext.setAttribute(Constants.GLOBAL_SETTINGS, globalSettings);
        	
    	} catch (Exception e) {
    		logger.error("Exception while initialising AMP :" + e.getMessage());
    		e.printStackTrace(System.out);
    	}
    }
}
