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

import java.util.Locale;

/**
 * This class can be used to retrieve the Resource Bundle
 * pertaining to the specified locale.
 */

public class I18NHelper {

    public static Logger getKernelLogger(Class clazz) {
        Logger logger = Logger.getLogger(clazz);
        return logger;
    }

    public static Logger getKernelLogger(Class clazz, Locale locale) {
        Logger logger = Logger.getLogger(clazz);
        return logger;
    }

    public static Logger getModuleLogger(String moduleName, Class clazz) {
        Logger logger = Logger.getLogger(clazz);
        return logger;
    }

    public static Logger getModuleLogger(String moduleName, Class clazz, Locale locale) {
        Logger logger = Logger.getLogger(clazz);
        return logger;
    }
}
