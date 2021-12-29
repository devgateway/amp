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


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserAccountForm;
import org.apache.log4j.Level;
import org.digijava.kernel.util.RequestUtils;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowAccountAction
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowAccountAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest
                                 request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserAccountForm accountForm = (UserAccountForm)form;

        // get user object from session
        User user = RequestUtils.getUser(request);

        // get success action forward
        ActionForward forward = mapping.findForward("forward");

        // check if user loged in
        // if is true fill account form
        if( user != null ) {

            // Set first name
            accountForm.setFirstName(user.getFirstNames());

            // set last name
            accountForm.setLastName(user.getLastName());

            // set email
            accountForm.setEmail(user.getEmail());

            // set organization name
            accountForm.setOrganizationName(user.getOrganizationName());

            // set organization type if any
            if( user.getOrganizationType() != null )
                accountForm.setOrganizationType(user.getOrganizationType().getType());

            // set country of residence if any
            if( user.getCountry() != null )
                accountForm.setCountryOfResidence(user.getCountry().getCountryName());

            // set URL (Web site)
            accountForm.setUrl(user.getUrl());

            // set address
            accountForm.setMailingAddress(user.getAddress());

            // set receive newsletter
            accountForm.setReceiveNewsletter(user.getUserPreference().isReceiveAlerts());

            // set display my profile
            accountForm.setDisplayMyProfile(user.getUserPreference().isPublicProfile());

            // set navigation language
            accountForm.setNavigationLanguage(user.getUserLangPreferences().getAlertsLanguage());

            // set content language
            accountForm.setContentLanguages(user.getUserLangPreferences().getContentLanguages());
        }
        else {
            // if user object can't found in session then
            // forward to home page
            forward = mapping.findForward("welcome");

            if (logger.isDebugEnabled()) {
                logger.error("User object not found in session");
            }
        }

        return forward;
    }
}
