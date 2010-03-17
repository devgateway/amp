package org.dgfoundation.amp.test.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.StandaloneJndiAMPInitializer;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * Hibernate and Digi Configuration initializer tool
 * 
 * @author Sebas
 * 
 */
public class Configuration {
	private static Logger log = Logger.getLogger(Configuration.class);
	private static boolean started = false;

	private Configuration() {
		try {
			// Initialize a JNDI reference to the real datasource deployed by
			// jboss-web.xml
			// You need to have a JBoss instance running on localhost!
			ResourceStreamHandlerFactory.installIfNeeded();
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
	
			DigiConfigManager.initialize("repository");
			PersistenceManager.initialize(false);


		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		catch (Error er) {
			er.printStackTrace();
		}
	}

	/**
	 * Initialize the current configuration, remember that JBoss should be
	 * started to get the JNDI reference of the data source
	 */
	public static void initConfig() {
		if (!started) {
			new Configuration();
			started = true;
		}
	}

	/**
	 * Get TEST properties attributes
	 * 
	 * @return
	 */
	public static Properties getProperties() {
		Properties properties = new Properties();
		try {
			properties.load(Configuration.class.getResourceAsStream("amp-test.properties"));
		} catch (IOException e) {
			log.error(e);
		}
		return properties;
	}
}
