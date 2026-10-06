package org.dgfoundation.amp.error.keeper;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

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
