/*
 *   LOBManager.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Mar 24, 2004
 * 	 CVS-ID: $Id: LOBManager.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.kernel.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import net.sf.hibernate.HibernateException;


public class LOBManager {

    private static final Logger logger = Logger.getLogger(LOBManager.class);
    private static final String CONFIG_FILE_NAME = "hibernate.extra.properties";

    private static Properties properties;

    static {
        properties = new Properties();
        InputStream is = LOBManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if (is != null) {
            try {
                properties.load(is);
            }
            catch (IOException ex) {
                logger.fatal("Unable to parse extra properties for hibernate", ex);
                throw new RuntimeException(ex);
            }
        } else {
            logger.debug("File hibernate.extra.properties not found in CLASSPATH");
        }
    }

    /**
     * Gets instance of NativeJdbcExtractor, defined for application server. If
     * null, it means that wrapped connections are not used
     * @return instance of NativeJdbcExtractor
     * @throws HibernateException if NativeJdbcExtractor can not be instantiated
     */
    public static NativeJdbcExtractor getJdbcExtractor() throws HibernateException {
        String className = properties.getProperty("jdbcExtractorClass");
        if (className == null) {
            logger.debug("jdbcExtractorClass parameter is not set");
            return null;
        }

        Class clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            logger.error("Unable to find class " + className);
            throw new HibernateException(ex);
        }

        NativeJdbcExtractor extractor = null;
        try {
            extractor = (NativeJdbcExtractor) clazz.newInstance();
        }
        catch (Exception ex1) {
            logger.error("Unable to instantiate class " + className);
            throw new HibernateException(ex1);
        }

        return extractor;
    }

    /**
     * Checks PreparedStatement, passed by parameter. If it is Oracle's
     * PreparedStatement, returns true. If any other - false. <b>attention</b>
     * if Oracle's PreparedStatement is wrapped by application server, this
     * method will return false.
     * @param ps PreparedStatement
     * @return true If it is Oracle's PreparedStatement. false - if not
     */
    public static boolean isOraclePreparedStatement(PreparedStatement ps) {
        Class oraclePsClass;

        try {
            oraclePsClass = ps.getClass().getClassLoader().loadClass(
                "oracle.jdbc.driver.OraclePreparedStatement");
        }
        catch (ClassNotFoundException ex) {
            return false;
        }

        if (oraclePsClass.isAssignableFrom(ps.getClass())) {
            logger.debug("This is oracle connection: " + ps.getClass().getName());
            return true;
        }  else {
            logger.debug("This is NOT oracle connection" + ps.getClass().getName());
            return false;
        }
    }

    /**
     * Returns LobCreator instance for PreparedStatement, passed by parameter.
     * It's caller's responsibility to close LobCreator after work is done.
     * @param st PreparedStatement
     * @return LobCreator instance
     * @throws HibernateException If error occurs
     * @throws SQLException If error occurs
     */
    public static LobCreator getLobCreator(PreparedStatement st) throws HibernateException, SQLException {
        NativeJdbcExtractor jdbcExtractor = LOBManager.getJdbcExtractor();

        PreparedStatement realPs;
        if (jdbcExtractor == null) {
            logger.debug("JDBC Extractor is not set");
            realPs = st;
        }
        else {
            logger.debug("JDBC Extractor class is " +
                         jdbcExtractor.getClass().getName());
            realPs = jdbcExtractor.getNativePreparedStatement(st);
        }

        LobHandler lobHandler;
        boolean isOracle = LOBManager.isOraclePreparedStatement(realPs);
        try {
            if (isOracle) {
                logger.debug("Processing ORACLE statement");
                OracleLobHandler handler = new OracleLobHandler();
                handler.setNativeJdbcExtractor(jdbcExtractor);
                handler.setCache(false);
                lobHandler = handler;
            }
            else {
                logger.debug("Processing NON-ORACLE statement");
                lobHandler = new DefaultLobHandler();
            }
        }
        catch (Exception ex1) {
            throw new HibernateException(ex1);
        }

        return lobHandler.getLobCreator();

    }

}