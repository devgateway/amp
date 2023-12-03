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

package org.digijava.module.um.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;

import java.util.*;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowUserRegisterBlank extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserRegisterForm registerForm = (UserRegisterForm) form;



        // set country resident data
        List countries = DbUtil.getCountries();
        HashMap countriesMap = new HashMap();
        Iterator iterator = TrnUtil.getCountries(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnCountry item = (TrnCountry)iterator.next();
            countriesMap.put(item.getIso(), item);
        }
       //sort countries
        List sortedCountries = new ArrayList();
        iterator = countries.iterator();
        while (iterator.hasNext()) {
            Country item = (Country)iterator.next();
            sortedCountries.add(countriesMap.get(item.getIso()));
        }
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);

        if( sortedCountries != null ) {
//            sortedCountries.add(0, new Country( new Long( 0 ),null,"Select a country",null,null,null,null ) );
            sortedCountries.add(0, new TrnCountry( "-1","Select a country") );
        }
        registerForm.setCountryResidence(sortedCountries);
        // --------------------------

        // set default web site
        registerForm.setWebSite("http://");

        // set Navigation languages
        Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));

        HashMap translations = new HashMap();
        iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnLocale item = (TrnLocale)iterator.next();
            translations.put(item.getCode(), item);
        }
       //sort languages
        List sortedLanguages = new ArrayList();
        iterator = languages.iterator();
        while (iterator.hasNext()) {
            Locale item = (Locale)iterator.next();
            sortedLanguages.add(translations.get(item.getCode()));
        }
        Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

        registerForm.setNavigationLanguages(sortedLanguages);

        return null;
    }
}
