/*
 *   SiteCallback.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Jun 25, 2004
     * 	 CVS-ID: $Id: SiteCallback.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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

package org.digijava.module.admin.util;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.util.TranslationCallback;

/**
 * SiteCallback instance, which implements TranslationCallback Callback interface to be passed to TrnUtil.sortByTranslation() method
 */

public class SiteCallback
    implements TranslationCallback {

    public String getSiteId(Object o) {
        if (o instanceof Site) {
            Site site = (Site) o;
            return site.getSiteId();
        }

        throw new ClassCastException(
            "Site object must be passed to getTranslationKey(), not " +
            o.getClass().getName());
    }

    public String getTranslationKey(Object o) {

        if (o instanceof Site) {
            Site site = (Site) o;
            return "site:" + site.getSiteId();
        }
        throw new ClassCastException(
            "Site object must be passed to getTranslationKey(), not " +
            o.getClass().getName());
    }

    public String getDefaultTranslation(Object o) {
        if (o instanceof Site) {
            Site site = (Site) o;
            return site.getName();
        }
        throw new ClassCastException(
            "Site object must be passed to getTranslationKey(), not " +
            o.getClass().getName());
    }

}