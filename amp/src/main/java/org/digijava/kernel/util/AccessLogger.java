/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.util;

import org.apache.log4j.Logger;
import org.digijava.commons.asyncdispatcher.AsyncBuffer;
import org.digijava.commons.asyncdispatcher.AsyncHandler;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.entity.AccessLog;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.searchfriendly.ActionInformation;
import org.digijava.kernel.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

public class AccessLogger
    implements AsyncHandler {
    private static Logger logger = Logger.getLogger(AccessLogger.class);

    /**
     * Size of the buffer used.
     */
    private static final int BUFFER_SIZE = 512;
    /**
     * Number of object to accumulate in the buffer before logger thread
     * begins inserting into the database.
     */
    private static final int POOL_SIZE = 100;

    private static AsyncBuffer asyncBuffer = null;

    /**
     * Log HTTP request
     * @param req HttpServletRequest object
     */
    public static void log(HttpServletRequest req) {
        logger.debug("log(HttpServletRequest req) was called");
        AccessLog logEntry = assembleLogEntry(req);
        appendToBuffer(logEntry);
    }

    /**
     * Log AccessLog entity
     * @param logEntry AccessLog entity
     */
    public static void log(AccessLog logEntry) {
        logger.debug("log() was called for entry " + logEntry);
        appendToBuffer(logEntry);
    }

    public static void shutdown() {
        logger.debug("shutdown() called");
        if (asyncBuffer != null) {
            asyncBuffer.close();
        }
        logger.debug("shutdown() complete");
    }

    /**
     * Callback function, which does actual logging
     * @param parm1 Object to log
     * @throws java.lang.ClassCastException if unsupported type of object was
     * passed
     */
    public void handleSingleItem(Object parm1) throws java.lang.
        ClassCastException {
        AccessLog logEntry = null;

        logger.debug("Processing log entry");
        if (parm1 == null) {
            logger.error("Can not log null values");
            return;
        }

        logEntry = (AccessLog) parm1;
        String itemIdentity = DgUtil.fillPattern(logEntry.getIdentityPattern(), logEntry.getParameters(), false);
        if (itemIdentity == null || itemIdentity.trim().length() == 0) {
            logger.debug("Identity is empty. Skipping");
            return;
        }
        String userIp = RequestUtils.getForwarderAddress(logEntry.getForwardedAddr(), false);
        if (userIp == null) {
            userIp = logEntry.getOriginalIp();
        }

        try {
            logEntry = (AccessLog) logEntry.clone();
        }
        catch (CloneNotSupportedException ex1) {
            logger.error("Unable to clone object", ex1);
            return;
        }
        logEntry.setItemIdentity(itemIdentity);
        logEntry.setUserIp(userIp);


        logger.debug("Saving log entry " + logEntry + " to database");
        PersistenceManager.getSession().save(logEntry);
        logger.debug("Save complete");
    }

    private static AccessLog assembleLogEntry(HttpServletRequest request) {
        AccessLog entry = new AccessLog();
        entry.setLogDate(new Date());

        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);
        if (moduleInstance != null) {
            entry.setInstanceName(moduleInstance.getInstanceName());
            entry.setModuleName(moduleInstance.getModuleName());

            if (moduleInstance.getRealInstance() != null) {
                entry.setRealSiteId(moduleInstance.getRealInstance().getSite().
                                    getSiteId());
                entry.setRealInstanceName(moduleInstance.getRealInstance().
                                          getInstanceName());
            }
        }

        Site currentSite = RequestUtils.getSite(request);
        if (currentSite != null) {
            entry.setSiteId(currentSite.getSiteId());
        }

        User currentUser = RequestUtils.getUser(request);
        if (currentUser != null) {
            entry.setUserId(currentUser.getId());
        }

        entry.setOriginalIp(request.getRemoteAddr());
        entry.setForwardedAddr(RequestUtils.getForwardAddresses(request));
        entry.setUrl(DgUtil.getFullURL(request));

        Locale currentLocale = RequestUtils.getNavigationLanguage(request);
        if (currentLocale != null) {
            entry.setLanguageCode(currentLocale.getCode());
        }

        Action action = getActionConfiguration(request);
        if (action != null) {
            entry.setIdentityType(action.getIdentityType());
            entry.setIdentityPattern(action.getIdentityPattern());
            //entry.setParameters(new HashMap(request.getParameterMap()));

            HashMap params = new HashMap<>();
            Enumeration<String> enumParamNames = request.getParameterNames();
            while (enumParamNames.hasMoreElements()) {
                String paramName = enumParamNames.nextElement();
                String paramValue = request.getParameter(paramName);

                params.put(paramName, paramValue);
            }
            entry.setParameters(params);
        }

        return entry;
    }

    private static Action getActionConfiguration(HttpServletRequest request) {
        ActionInformation actionInfo = (ActionInformation) request.getAttribute(
            Constants.ACTION_INFORMATION);
        if (actionInfo == null) {
            return null;
        }
        ModuleConfig moduleConfig = DigiConfigManager.getModuleConfig(
            actionInfo.getModule());
        if (moduleConfig != null) {
            return moduleConfig.getAction(actionInfo.getAction());
        }
        return null;
    }

    private static void appendToBuffer(AccessLog logEntry) {
        // If logging is enabled in DiGi config, log entries which have
        // identity pattern defined
        if ((asyncBuffer != null) && (logEntry.getIdentityPattern() != null) &&
            (logEntry.getIdentityPattern().trim().length() != 0)) {
            asyncBuffer.put(logEntry);
        }
    }

    static {
        DigiConfig config = DigiConfigManager.getConfig();
        if (config.isEnableLogging()) {
            int buffSize = config.getAccessLogBuffSize() == null ? BUFFER_SIZE :
                    config.getAccessLogBuffSize();

            int poolSize = config.getAccessLogPoolSize() == null ? POOL_SIZE :
                    config.getAccessLogPoolSize();

            logger.info("Creating access logger. Buffer size: " + buffSize + ", pool size: " + poolSize);
            asyncBuffer = new AsyncBuffer(
                new AccessLogger(),
                buffSize,
                poolSize
                );
        } else {
            logger.info("Access logger is turned off");
        }
    }
}
