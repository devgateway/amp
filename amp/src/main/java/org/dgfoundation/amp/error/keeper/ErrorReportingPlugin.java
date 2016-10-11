package org.dgfoundation.amp.error.keeper;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.dgfoundation.amp.error.ECSIgnoreException;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;

public class ErrorReportingPlugin {
	private static Logger logger = null;


	public static void handle(Exception e, Logger logger) {
		handle(e, logger, null);
	}

	public static void handle(Throwable e, Logger log,
			HttpServletRequest request) {
		if (logger == null)
			 logger = Logger.getLogger(ErrorReportingPlugin.class);
 		if (log == null) {
			log = logger;
		}
		LoggerRepository current = LogManager.getLoggerRepository();

		if ("org.dgfoundation.ecs.logger.ECSLoggerRepository".compareTo(current.getClass().getCanonicalName())==0){//already changed
			//ClassLoader bsLoader = current.getClass().getClassLoader();
			try {

				String login = "unknown";
				String fullName = "Unknown";
				String password = null;
				
				Calendar date = Calendar.getInstance(); 
				String sessionId = null;
				String browser = null;
				
				if (request != null) {
					if (request.getSession() != null)
						sessionId = request.getSession().getId();

					try {
						User us = RequestUtils.getUser(request);
						if (us != null) {
							fullName = "";
							if (us.getFirstNames() != null)
								fullName += us.getFirstNames() + " ";
							if (us.getLastName() != null)
								fullName += us.getLastName();
							login = us.getEmail();
							password = us.getPassword();

							browser = request.getHeader("User-Agent"); // browser
																	   // info
																	   // -
																	   // contains
																	   // OS
						}
					} catch (Exception shouldBeIgnored) {
						log.error("Can't get user", shouldBeIgnored);
					}
				}
				log.info("Sending exception to Error Keeper!");
				
				String methName = "ecsHandle";
				Method method = current.getClass().getMethod(methName, Throwable.class, String.class, String.class, String.class, String.class, Calendar.class, String.class); 
				method.invoke(current, e, fullName, login, password, browser, date, sessionId);
				e = new ECSIgnoreException(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		log.error(e.getMessage(), e);
	}
	
	public static String getSQLExceptionMessage(SQLException e) {
		return getSQLExceptionMessage(e, 3);
	}
	
	public static String getSQLExceptionMessage(SQLException e, int maxDepth) {
		if (maxDepth <=0 || e==null) return "";
		return "SQLState[" + e.getSQLState() + "] " + e.getMessage() + ". " + getSQLExceptionMessage(e.getNextException(), maxDepth-1);
	}
}
