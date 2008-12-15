package org.dgfoundation.amp.test.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.StandaloneJndiAMPInitializer;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;

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
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();

			String repository = getProperties().getProperty("repository");
			String path = this.getClass().getResource("/").getPath().replaceAll("/WEB-INF/classes/", repository);

			DigiConfigManager.initialize(path);
			PersistenceManager.initialize(true, null);
		} catch (Exception e) {
			log.error(e);
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
