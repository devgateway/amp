/*
 *   InterestsCallback.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Mar 26, 2004
 * 	 CVS-ID: $Id: InterestsCallback.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

package org.digijava.module.um.util;

import org.digijava.kernel.translator.util.TranslationCallback;
import org.digijava.kernel.entity.Interests;

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