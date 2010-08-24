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
			//StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
			HibernateClassLoader.HIBERNATE_CFG_XML = "/hibernate-test.xml";
			
			String repository = getProperties().getProperty("repository");
			String path = this.getClass().getResource("/").getPath().replaceAll("/WEB-INF/classes/", repository);
			
			InitialContext ctx = new InitialContext();
			DataSource src = (DataSource) ctx.lookup("java:comp/env/ampDS");
			String databaseName = src.getConnection().getCatalog();

			
		
			log.info("preparing hibernate configuration file");

			String line;
			StringBuffer sb = new StringBuffer();
			//I had to make a direct connection due to remote exceptions after changing the connection pool manager
			FileInputStream fis = new FileInputStream(this.getClass().getResource(HibernateClassLoader.HIBERNATE_CFG_XML).getFile());
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("_DATABASE_NAME_", src.getConnection().getCatalog());
				sb.append(line + "\n");
			}
			reader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(this.getClass().getResource(HibernateClassLoader.HIBERNATE_CFG_XML).getFile()));
			out.write(sb.toString());
			out.close();
           
			DigiConfigManager.initialize(path);
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
