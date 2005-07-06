/*
 *   UserUpdateMarketForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: UserUpdateMarketForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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

package org.digijava.module.um.form;
import java.util.Collection;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * @author philipp
 *
 */
public class UserUpdateMarketForm extends UserUpdateForm
{
	private Collection alertLanguages;
	private String selectedAlertLanguage;

    /**
     * @return
     */
    public Collection getAlertLanguages()
    {
        return alertLanguages;
    }

    /**
     * @param collection
     */
    public void setAlertLanguages(Collection collection)
    {
        alertLanguages = collection;
    }

    /**
     * @return
     */
    public String getSelectedAlertLanguage()
    {
        return selectedAlertLanguage;
    }

    /**
     * @param string
     */
    public void setSelectedAlertLanguage(String string)
    {
        selectedAlertLanguage = string;
    }

    /**
     *
     * @param actionMapping
     * @param httpServletRequest
     * @return
     */
    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = super.validate(actionMapping, httpServletRequest);

        return errors;
    }

}
