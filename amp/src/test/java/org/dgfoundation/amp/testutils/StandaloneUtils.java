package org.dgfoundation.amp.testutils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.dgfoundation.amp.*;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 * @author mihai, copy-pasted and modified from DigiSchemaExport by Constantin
 * This is a standalone initializer for digi/hibernate, that can be use to patch/inspect/query
 * the amp database without jboss/tomcat running.
 */
public class StandaloneUtils {
	private static Logger logger = Logger.getLogger(StandaloneUtils.class);
	
	
	  public static AmpActivity loadActivity(Long id, Session session) throws DgException {
		AmpActivity result = null;

		try {
//session.flush();
			result = (AmpActivity) session.get(AmpActivity.class, id);
			session.evict(result);
			result = (AmpActivity) session.get(AmpActivity.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivity with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Canno load AmpActivity with id" + id, e);
		}
		return result;
	}
	
	  public static void runWithinAmpContext(MyRunnable runnable)
	  {
		  try
		  {
			  runWithinAmpContext_internal(runnable);
		  }
		  catch(Exception ex)
		  {
			  throw new RuntimeException(ex);
		  }
	  }
	
	/**
	 * @param args
	 */
	private static void runWithinAmpContext_internal(MyRunnable runnable) throws Exception
	{
   	//StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
    	
    	
    	/**
    	 * Change hibernate configuration file
    	 */
    	HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
    	
        ResourceStreamHandlerFactory.installIfNeeded();

 //       Map commandLineParams = new HashMap(); //getCommandLineParameters(args);
 //       logger.info("Command-line parameters: " + commandLineParams);
 //       String moduleName = (String) commandLineParams.get("-m");

//        if (moduleName != null) {
//            logger.info("Working for the module " + moduleName);
//        }
//        else {
//            logger.info("Working for the whole database");
//        }

        DigiConfigManager.initialize("./repository");
        PersistenceManager.initialize(false, null);
        try {
            try {
            	Configuration cfg = HibernateClassLoader.getConfiguration();
            }
            catch (Exception ex1) {
                logger.error("Error creating hibernate configuration", ex1);
                throw ex1;
            }

            try {
            	runnable.run();
            }
            catch (Exception ex) {
            	logger.error("Error running client code ", ex);
            	throw ex;
            }
        }
        finally {
            PersistenceManager.cleanup();
        }
	}

}
