/*
 *   TranslationCallback.java
 * 	 @Author George Kvizhinadze gio@digijava.org
 * 	 Created: Mar 17, 2004
 * 	 CVS-ID: $Id: TranslationCallback.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

package org.digijava.kernel.translator.util;

/**
 * Callback interface which must be passwd to TrnUtil.sortByTranslation() method
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