package org.dgfoundation.amp.ecs.server;

import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.TTCCLayout;

/**
 * Clasa pentru setarea loggerelor
 * @author alexandru
 *
 */
public class Logging {

	static int debug = 0;
	
	public static Logger startLogger(String filePrefix, String clientName){
		Logger logger = Logger.getLogger("ecs."+clientName);
		logger.setLevel((Level)Level.DEBUG);
		try {
			File f = new File("logs" , filePrefix + clientName + ".log");
			FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
			Layout l = new PatternLayout("%-5p %d{HH:mm:ss,SSS} [%c{5}] [%t]: %m%n");
			fapp.setLayout(l);
			logger.addAppender(fapp);
			//comment this no to write to console
			logger.addAppender(new ConsoleAppender(l));
		}
		catch (Exception e) { /* catch any exceptions here */ }
		return logger;
	}
	
	public static Logger getLogger(String clientName){
		return Logger.getLogger("lpd."+clientName);
	}

}
