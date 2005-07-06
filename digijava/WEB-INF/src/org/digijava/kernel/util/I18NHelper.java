/*
 *   I18NHelper.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Aug 17, 2003
 * 	 CVS-ID: $Id: I18NHelper.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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


package org.digijava.kernel.util;

import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * This class can be used to retrieve the Resource Bundle
 * pertaining to the specified locale.
 */

public class I18NHelper {

    public static String KERNEL_LOG_MESSAGES = "org.digijava.kernel.util.Resource";

    /**
     * Gets the Module message bundle corresponding to
     * the System locale. Returns <code> null </code>
     * if the file cannot be located.
     * @param moduleName Name of the module
     *
     * @return The Module Resource Bundle
     */

    public static ResourceBundle getModuleBundle(String moduleName) {
        return getKernelBundle();
    }

    /**
     * Gets the Kernel message bundle corresponding to
     * the System locale. Returns <code> null </code>
     * if the file cannot be located.
     *
     * @return The Kernel Resource Bundle
     */
    public static ResourceBundle getKernelBundle() {
        try {
            return ResourceBundle.getBundle(KERNEL_LOG_MESSAGES);
        }
        catch (Exception e) {
            // This exception cannot be logged
            System.err.println(
                "Exception while loading kernel file for default "
                + "System Locale "
                + Locale.getDefault());
        }

        return null;
    }

    /**
     * Gets the Module message bundle corresponding to
     * the specified locale. Returns the default bundle
     * if the locale-specific bundle cannot be located.
     * Returns <code> null </code>
     * if even the default file  cannot be located.
     * @param moduleName Name of the module
     * @param locale The Locale object for which the bundle is
     * to be loaded.
     * @return The Module Resource Bundle
     */
    public static ResourceBundle getModuleBundle(String moduleName,
                                                 Locale locale) {
        return getKernelBundle(locale);
    }

    /**
     * Gets the Core message bundle corresponding to
     * the specified locale. Returns the default bundle
     * if the locale-specific bundle cannot be located.
     * Returns <code> null </code>
     * if even the default file  cannot be located.
     * @param locale The Locale object for which the bundle is
     * to be loaded.
     * @return The Core Resource Bundle
     */
    public static ResourceBundle getKernelBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(KERNEL_LOG_MESSAGES,
                                            locale);
        }
        catch (Exception e) {
            // This exception cannot be logged
            System.err.println(
                "Exception while loading kernel log file for locale " + locale);
        }

        return null;

    }

    public static Logger getKernelLogger(Class clazz) {
        Logger logger = Logger.getLogger(clazz);
        logger.setResourceBundle(getKernelBundle());

        return logger;
    }

    public static Logger getKernelLogger(Class clazz, Locale locale) {
        Logger logger = Logger.getLogger(clazz);
        logger.setResourceBundle(getKernelBundle(locale));

        return logger;
    }

    public static Logger getModuleLogger(String moduleName, Class clazz) {
        Logger logger = Logger.getLogger(clazz);
        logger.setResourceBundle(getModuleBundle(moduleName));

        return logger;
    }

    public static Logger getModuleLogger(String moduleName, Class clazz, Locale locale) {
        Logger logger = Logger.getLogger(clazz);
        logger.setResourceBundle(getModuleBundle(moduleName, locale));

        return logger;
    }

}