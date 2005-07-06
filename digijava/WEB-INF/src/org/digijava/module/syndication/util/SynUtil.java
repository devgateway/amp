/*
 *   SynUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: SynUtil.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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
package org.digijava.module.syndication.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SynUtil {


    /**
     *
     * @param request
     * @return
     * @throws DgException
     */
    public static List getLanguages(HttpServletRequest request) throws DgException {

        // set language list
        Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
        //
        HashMap translations = new HashMap();
        Iterator iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnLocale item = (TrnLocale)iterator.next();
            translations.put(item.getCode(), item);
        }

        List sortedLanguages = new ArrayList();
        iterator = languages.iterator();
        while (iterator.hasNext()) {
            Locale item = (Locale)iterator.next();
            sortedLanguages.add(translations.get(item.getCode()));
        }
        Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

        return sortedLanguages;

    }

    /**
     *
     * @param request
     * @return
     * @throws DgException
     */
    public static List getCountries(HttpServletRequest request) throws DgException {

        Collection countries = TrnUtil.getCountries(RequestUtils.getNavigationLanguage(request).getCode());
        ArrayList sortedCountries = new ArrayList(countries);
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);

        return sortedCountries;
    }

}
