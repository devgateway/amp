/*
 *   SwitchLanguage.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Jan 14, 2004
 * 	 CVS-ID: $Id: SwitchLanguage.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.module.translation.form.TranslationForm;
import org.digijava.kernel.util.DgUtil;
import java.util.StringTokenizer;
import java.net.URLDecoder;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SwitchLanguage
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        TranslationForm formBean = (TranslationForm) form;
        String localeKey = null;
        String referrerUrl = request.getParameter("rfr");
        localeKey = request.getParameter("code");
        if( referrerUrl != null )
            referrerUrl = URLDecoder.decode(referrerUrl, "UTF-8");
        else
            referrerUrl = "";

        //String localeKey=(String)request.getParameter("lang");
        Locale locale = new Locale();
        locale.setCode(localeKey);
        DgUtil.switchLanguage(locale, request, response);

        SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
        String sitePath = currentDomain.getSitePath();
        if (sitePath != null) {
            referrerUrl = sitePath + referrerUrl;
        }

        return new ActionForward(referrerUrl, true);
    }
}