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