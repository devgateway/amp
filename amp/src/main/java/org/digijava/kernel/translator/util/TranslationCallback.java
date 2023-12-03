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

package org.digijava.kernel.translator.util;

/**
 * Callback interface which must be passed to TrnUtil.sortByTranslation() method
 */
public interface TranslationCallback {
    /**
     * Returns "string" identity of the site (returned by Site.getSiteId()
     * method) for which translation must be found.
     * For global translations, this method must return '0'
     * @param o Object for which callback is called
     * @return Return "string" identity of the site for which translation must
     * be found
     */
    public String getSiteId(Object o);

    /**
     * Returns translation key for the given object. For sites it will be
     * "site:" + siteId (for example: "site:dglogin")
     * @param o Object for which callback is called
     * @returnReturns translation key for the given object
     */
    public String getTranslationKey(Object o);

    /**
     * Returns the default translation for the given object. This value is
     * important if the object does not have translation for the target locale.
     * @param o Object for which callback is called
     * @return the default translation for the given object
     */
    public String getDefaultTranslation(Object o);

}
