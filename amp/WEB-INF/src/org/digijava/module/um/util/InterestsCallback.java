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

package org.digijava.module.um.util;

import org.digijava.kernel.entity.Interests;
import org.digijava.kernel.translator.util.TranslationCallback;

/**
 * InterestsCallback instance, which implements TranslationCallback Callback interface to be passed to TrnUtil.sortByTranslation() method
 */


public class InterestsCallback
    implements TranslationCallback {

    public String getSiteId(Object o) {
        if (o instanceof Interests) {
            Interests interest = (Interests) o;
            return interest.getSite().getSiteId();
        }

        throw new ClassCastException(
            "Interests object must be passed to getTranslationKey(), not " +
            o.getClass().getName());
    }

    public String getTranslationKey(Object o) {

        if (o instanceof Interests) {
            Interests interest = (Interests) o;
            return "site:" + interest.getSite().getSiteId();
        }
        throw new ClassCastException(
            "Interests object must be passed to getTranslationKey(), not " +
            o.getClass().getName());
    }

    public String getDefaultTranslation(Object o) {
        if (o instanceof Interests) {
            Interests interest = (Interests) o;
            return interest.getSite().getName();
        }
        throw new ClassCastException(
            "Interests object must be passed to getTranslationKey(), not " +
            o.getClass().getName());
    }

}
